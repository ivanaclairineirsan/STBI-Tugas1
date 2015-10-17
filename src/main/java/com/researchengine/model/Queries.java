package com.researchengine.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
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

    /* terms per query */
    public ArrayList<String> terms;
    /* inverted terms */
    private ArrayList<InvertedTerm> invertedTerms;

    public Queries() {
    }

    public Queries(String queryLocation, int tf, boolean idf, boolean normalization, boolean stemming, String swLocation, String rjLocation, String ifLocation) {
        loadInvertedFile(ifLocation);

        loadRelevanceJudgement(rjLocation);

        loadQueries(queryLocation);

        removeStopWord(swLocation);

        doStemming(stemming);

        calculateTermWeight(tf, idf, normalization);

        search();
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
     * find the weight of the related term from the inverted file
     * @param term the term to find the weight
     * @return the weight of the term
     */
    public double docTermWeight(String term) {
        for (InvertedTerm invertedTerm: invertedTerms) {
            if (invertedTerm.term.equals(term)) {
                return invertedTerm.weight;
            }
        }
        return 0;
    }

    /**
     * Calculate the tf, idf, and normalization of each words of the query
     * @param tf select the tf method (0: no-TF, 1: raw-TF, 2: binary-TF, 3: augmented-TF, 4: logarithmic-TF
     * @param idf select the idf method (true: use IDF, false: don't use IDF)
     * @param normalization select the normalization method (true: use normalization, false: don't use normalization)
     * @return list of the terms and the weights related to the used method
     */
    public ArrayList<String[]> calculateTermWeight(int tf, boolean idf, boolean normalization) {
        ArrayList<String[]> termFreq = new ArrayList<>();
        String[] term;
        int counter;

        switch(tf) {
            case 0: // none
                break;
            case 1: // raw
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

        if (idf) {
            for (int i = 0; i < termFreq.size(); i++) {
                String[] temp = termFreq.get(i);
                temp[1] = String.valueOf(Double.valueOf(temp[1]) * docTermWeight(temp[0]));
                termFreq.set(i,temp);
            }
        }

        if (normalization) {
            for (int i = 0; i < termFreq.size(); i++) {
                String[] temp = termFreq.get(i);
                temp[1] = String.valueOf(Double.valueOf(temp[1])/termFreq.size());
                termFreq.set(i,temp);
            }
        }

        return termFreq;
    }

    public void search() {
        // search here, update query.relevantDocList here
    }

    /**
     * count the avg precision of all the query
     * @return the avg precission
     */
    public double precision() {
        double totalPrecission = 0;
        for (Query aQueryList : queryList) {
            aQueryList.countPrecision();
            totalPrecission += aQueryList.precission;
        }

        return totalPrecission/queryList.size();
    }

    /**
     * count the avg recall of the query
     * @return the avg precission
     */
    public double recall() {
        double totalRecall = 0;
        for (Query aQueryList : queryList) {
            aQueryList.countRecall();
            totalRecall += aQueryList.recall;
        }

        return totalRecall/queryList.size();
    }

    /**
     * count the non interpolated average precision of all the query
     * @return non interpolated average precision
     */
    public double nonInterpolatedAvgPrecision() {
        double totalNIAP = 0;
        for (Query aQueryList : queryList) {
            aQueryList.countNonInterpolatedAvgPrecision();
            totalNIAP += aQueryList.NIAP;
        }
        return totalNIAP;
    }

}