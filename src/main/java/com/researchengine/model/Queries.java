package com.researchengine.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Queries {
    /* list of relevance judgement */
    public ArrayList<RelevanceJudgement> rjList; // relevance judgement
    /* list of query + relevance judgement */
    public ArrayList<Query> queryList;
    /* the documents */
    public ArrayList<Document> documents;

    /* terms per query */
    private ArrayList<String> terms;
    /* inverted terms */
    private ArrayList<InvertedTerm> invertedTerms;

    public Queries(ArrayList<Document> documents) {
        this.documents = documents;
    }

    /**
     * create a single query
     * @param query the query
     */
    public void createQuery(String query) {
        queryList = new ArrayList<>();
        queryList.add(new Query(0, query, null));
    }

    /**
     * load inverted file to the invertedTerms
     * @param ifLocation the location of the inverted file
     */
    public void loadInvertedFile(String ifLocation) {
        Scanner input;
        String[] temp;
        InvertedTerm term;
        invertedTerms = new ArrayList<>();
        try {
            input = new Scanner(new FileReader(ifLocation));
            while (input.hasNextLine()){
                temp = input.nextLine().split(",");
                term = new InvertedTerm(temp[0],Integer.valueOf(temp[1]),Double.valueOf(temp[2]));
                invertedTerms.add(term);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * load inverted file to the invertedTerms
     * @param ifLocation the location of the inverted file
     */
    public HashMap<String, Double> loadIDF(String ifLocation) {
        Scanner input;
        String[] temp;
        HashMap<String, Double> idf = new HashMap<>();
        try {
            input = new Scanner(new FileReader(ifLocation));
            while (input.hasNextLine()){
                temp = input.nextLine().split(",");
                idf.put(temp[0], Double.valueOf(temp[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return idf;
    }

    /**
     * load relevance judgement to the rjList
     * @param rjlocation the location of the relevance judgement
     */
    public void loadRelevanceJudgement(String rjlocation) {
        rjList = new ArrayList<>();
        RelevanceJudgement relJudge = null;
        rjList.add(new RelevanceJudgement(-1,null));

        Scanner input;
        String[] temp;
        try {
            input = new Scanner(new FileReader(rjlocation));
            while (input.hasNextLine()){
                temp = input.nextLine().split("\\s+");
                if (relJudge == null) {
                    relJudge = new RelevanceJudgement(Integer.valueOf(temp[0]),Integer.valueOf(temp[1]));
                    if (!input.hasNextLine()) { // if the file contains 1 relevant judgement
                        rjList.add(relJudge);
                    }
                } else {
                    if (relJudge.no == Integer.valueOf(temp[0])) {
                        relJudge.add(Integer.valueOf(temp[1]));
                        if (!input.hasNextLine()) { // the last relevant judgement in the file
                            rjList.add(relJudge);
                        }
                    } else {
                        rjList.add(relJudge);
                        relJudge = new RelevanceJudgement(Integer.valueOf(temp[0]),Integer.valueOf(temp[1]));
                        if (!input.hasNextLine()) { // new query and the last relevant judgement in the file
                            rjList.add(relJudge);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * find the related relevance judgement to the query
     * @param queryNo the number of the query
     * @return the RelevanceJudgement if found, null if not
     */
    private RelevanceJudgement findRelevanceJudgement(int queryNo) {
        for (RelevanceJudgement aRjList : rjList) {
            if (aRjList.no == queryNo) {
                return aRjList;
            }
        }

        return null;
    }

    /**
     * load query to the queryList
     * @param querylocation the location of the query
     */
    public void loadQueries(String querylocation) {
        queryList = new ArrayList<>();
        Query query;
        boolean contentMode = false, initialize = false;

        Scanner input;
        String[] temp;
        int queryNo = 0;
        StringBuffer queryContent = new StringBuffer("");
        try {
            input = new Scanner(new FileReader(querylocation));
            while (input.hasNextLine()){
                String line = input.nextLine();
                if (line.length() > 2) {
                    if (line.substring(0,2).equals(".I")) {
                        if (!queryContent.toString().equals("")) {
                            query = new Query(queryNo, queryContent.toString(), findRelevanceJudgement(queryNo));
                            queryList.add(query);
                        }
                        initialize = false;
                        contentMode = false;
                        temp = line.split(" ");
                        queryNo = Integer.valueOf(temp[1]);
                    }
                }
                if (line.equals(".T")) {
                    if (!initialize) {
                        queryContent = new StringBuffer("");
                    }
                    initialize = true;
                    contentMode = true;
                    line = "";
                }
                if (line.equals(".A")) {
                    if (!initialize) {
                        queryContent = new StringBuffer("");
                    }
                    contentMode = true;
                    initialize = true;
                    line = "";
                }
                if (line.equals(".W")) {
                    contentMode = true;
                    if (!initialize) {
                        queryContent = new StringBuffer("");
                    }
                    line = "";
                }

                if (contentMode) {
                    queryContent = queryContent.append(line).append(" ");
                }

                if (!input.hasNextLine()) {
                    query = new Query(queryNo, queryContent.toString(), findRelevanceJudgement(queryNo));
                    queryList.add(query);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tokenize the sentence into the chunk of words
     * @param sentence the sentence to tokenize
     */
    public void splitSentences(String sentence) {
        sentence = sentence.toLowerCase();
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(sentence);

        terms=new ArrayList<>();
        while(m.find()) {
            terms.add(m.group());
        }
    }

    /**
     * Remove the stopwords
     * @param swLocation the stopwords location
     */
    public void removeStopWord(String swLocation) {
        Scanner input;
        ArrayList<String> stopwords = new ArrayList<>();
        try {
            input = new Scanner(new FileReader(swLocation));
            while (input.hasNextLine()){
                stopwords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < terms.size(); i++) {
            for (String stopword : stopwords) {
                if (terms.get(i).equals(stopword)) {
                    terms.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    /**
     * stem the word
     * @param stemming true if want to stem the word
     */
    public void doStemming(boolean stemming) {
        PorterStemmer stem;
        if (stemming) {
            for (int i = 0; i < terms.size(); i++) {
                stem = new PorterStemmer();
                stem.add(terms.get(i).toCharArray(),terms.get(i).length());
                stem.stem();
                terms.set(i, stem.toString());
            }
        }
    }

    /**
     * Calculate the tf, idf, and normalization of each words of the query
     * @param tf select the tf method (0: no-TF, 1: raw-TF, 2: binary-TF, 3: augmented-TF, 4: logarithmic-TF
     * @return list of the terms and the weights related to the used method
     */
    public ArrayList<String[]> calculateTermFrequency(int tf) {
        ArrayList<String[]> termFreq = new ArrayList<>(); // list of weight per word
        String[] term; // [0]: term, [1]: frequency
        int counter;

        switch(tf) {
            case 0: // none
                while(terms.size() > 0) {
                    term = new String[2];
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = "1";
                    termFreq.add(term);
                }
                break;
            case 1: // raw
                while(terms.size() > 0) {
                    term = new String[2];
                    counter = 1;
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            counter++;
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf(counter);
                    termFreq.add(term);
                }
                break;
            case 2: // binary
                while(terms.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf(1);
                    termFreq.add(term);
                }
                break;
            case 3: // augmented
                int maxCount = -1;
                while(terms.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    counter = 1;
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            counter++;
                            terms.remove(i);
                            i--;
                        }
                    }
                    if (counter > maxCount) {
                        maxCount = counter;
                    }
                    term[1] = String.valueOf(counter);
                    termFreq.add(term);
                }

                for (int i = 0; i < termFreq.size(); i++) {
                    String[] temp = termFreq.get(i);
                    temp[1] = String.valueOf(0.5 + 0.5 * (Double.valueOf(temp[1])/maxCount));
                    termFreq.set(i,temp);
                }
                break;
            case 4: // logarithmic
                while(terms.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    counter = 1;
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            counter++;
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf((double) 1 + Math.log10(counter));
                    termFreq.add(term);
                }
                break;
            default:
                break;
        }

        return termFreq;
    }

    /**
     * Retrieve the related documents from the query
     * @param tf tf methods
     * @param idf idf methods
     * @param isNormalize whether want to normalize or not
     * @param swLocation stopword location
     * @return list of RetrievedDocument
     */
    public ArrayList<RetrievedDocument> search(int tf, boolean idf, boolean isNormalize, boolean stemming,
                                               String swLocation, String idfLocation) {
        double queryWeight;
        HashMap<String, Double> idfScore;
        ArrayList<String[]> weightedTerms;
        ArrayList<RetrievedDocument> result = new ArrayList<>();

        for (Query aQueryList : queryList) {

            queryWeight = 0;
            splitSentences(aQueryList.description);
            if (swLocation != null) {
                removeStopWord(swLocation);
            }
            if (stemming) {
                doStemming(true);
            }
            weightedTerms = calculateTermFrequency(tf);

            for (String[] weightedTerm : weightedTerms) {
                if (idf) {
                    idfScore = loadIDF(idfLocation);
                    if (idfScore.containsKey(weightedTerm[0])) {
                        queryWeight += (Double.valueOf(weightedTerm[1]) * idfScore.get(weightedTerm[0]));
                    } else {
                        queryWeight += Double.valueOf(weightedTerm[1]);
                    }

                } else {
                    queryWeight += Double.valueOf(weightedTerm[1]);
                }
            }

            if (isNormalize) {
                queryWeight = queryWeight / queryLength(weightedTerms);
            }

            result.add(new RetrievedDocument(aQueryList.no, invertedTerms, aQueryList.rj, documents,
                    queryWeight, weightedTerms, isNormalize));
        }


        return result;
    }

    /**
     * the query length
     * @param queryTerms the terms of the query
     * @return the length of the query
     */
    public double queryLength(ArrayList<String[]> queryTerms) {
        double result = 0;
        for (String[] queryTerm : queryTerms) {
            result += Math.pow(Double.valueOf(queryTerm[1]), 2);
        }

        return  result;
    }
}
