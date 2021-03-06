package com.researchengine.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Queries {
    /* list of relevance judgement */
    public Map<Integer, Set<Integer>> rjList; // relevance judgement
    /* list of query + relevance judgement */
    public Map<Integer, Query> queryList;
    /* the docList */
    public Map<Integer, Document> documents;

    /* the stopword list */
    Set<String> stopwords;
    /* inverted terms */
    private Map<String, Map<Integer, Double>> invertedTerms;

    public Queries() {

    }

    public Queries(Map<Integer, Document> documents) {
        this.documents = new HashMap<>(documents);
    }

    /**
     * create a single query
     * @param query the query
     */
    public void createQuery(String query) {
        queryList = new HashMap<>();
        queryList.put(1, new Query(query, null));
    }

    /**
     * load inverted file to the invertedTerms
     * @param ifLocation the location of the inverted file
     */
    public void loadInvertedFile(String ifLocation) {
        Scanner input;
        String[] temp;
        invertedTerms = new HashMap<>();
        Map<Integer, Double> invTermTemp;
        try {
            input = new Scanner(new FileReader(ifLocation));
            while (input.hasNextLine()){
                temp = input.nextLine().split("\t");
                if (invertedTerms.containsKey(temp[0])) {
                    invTermTemp = invertedTerms.get(temp[0]);
                    invTermTemp.put(Integer.valueOf(temp[1]), Double.valueOf(temp[2]));
                    invertedTerms.put(temp[0], invTermTemp);
                } else {
                    invTermTemp = new HashMap<>();
                    invTermTemp.put(Integer.valueOf(temp[1]), Double.valueOf(temp[2]));
                    invertedTerms.put(temp[0], invTermTemp);
                }
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
                temp = input.nextLine().split("\t");
                idf.put(temp[0], Double.valueOf(temp[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return idf;
    }

    /**
     * load relevance judgement to the rjList
     * @param rjFile the location of the relevance judgement
     */
    public void loadRelevanceJudgement(MultipartFile rjFile) {
        Scanner input;
        String[] temp;
        Set<Integer> docTemp;
        rjList = new HashMap<>();

        try {
            input = new Scanner(rjFile.getInputStream());
            // first line
            temp = input.nextLine().split("\\s+");
            docTemp = new HashSet<>();
            docTemp.add(Integer.valueOf(temp[1]));
            rjList.put(Integer.valueOf(temp[0]), docTemp);

            // iterate to the rest
            while (input.hasNextLine()){
                temp = input.nextLine().split("\\s+");

                // check for the existence of the key
                if (rjList.containsKey(Integer.valueOf(temp[0]))) {
                    docTemp = new HashSet<>(rjList.get(Integer.valueOf(temp[0])));
                    docTemp.add(Integer.valueOf(temp[1]));
                    rjList.put(Integer.valueOf(temp[0]), docTemp);
                } else {
                    docTemp = new HashSet<>();
                    docTemp.add(Integer.valueOf(temp[1]));
                    rjList.put(Integer.valueOf(temp[0]), docTemp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * find the related relevance judgement to the query
     * @param queryNo the number of the query
     * @return the Relevance Judgement if found, null if not
     */
    private Set<Integer> findRelevanceJudgement(int queryNo) {
        return rjList != null ? rjList.get(queryNo) : null;
    }

    /**
     * load query to the queryList
     * @param queryFile the location of the query
     */
    public void loadQueries(MultipartFile queryFile) {
        queryList = new HashMap<>();
        boolean contentMode = false, initialize = false;

        Scanner input;
        String[] temp;
        int queryNo = 0;
        StringBuffer queryContent = new StringBuffer("");
        try {
            input = new Scanner(queryFile.getInputStream());

            while (input.hasNextLine()){
                String line = input.nextLine();
                if (line.contains(".I ")) {
                    if (!queryContent.toString().equals("")) {
                        queryList.put(queryNo, new Query(queryContent.toString(), findRelevanceJudgement(queryNo)));
                    }
                    initialize = false;
                    contentMode = false;
                    temp = line.split(" ");
                    queryNo = Integer.valueOf(temp[1]);
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
                    queryList.put(queryNo, new Query(queryContent.toString(), findRelevanceJudgement(queryNo)));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the stopWords
     * @param swFile the stopWords location
     */
    public void loadStopWord(MultipartFile swFile) {
        Scanner input;
        stopwords = new HashSet<>();

        try {
            input = new Scanner(swFile.getInputStream());
            while (input.hasNextLine()){
                stopwords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * stem the word
     * @param word the word to be stem
     */
    public String doStemming(String word) {
        PorterStemmer stem;
        stem = new PorterStemmer();
        stem.add(word.toCharArray(), word.length());
        stem.stem();

        return stem.toString();
    }

    /**
     * Tokenize the sentence into the chunk of words
     * @param query the content of the query to tokenize
     */
    public void splitSentences(Query query, int TFType, int Stemming, MultipartFile swFile) {
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(query.queryContent.toLowerCase());

        query.terms = new HashMap<>();
        double maxFreq = 1;

        while(m.find()) {
            String key = m.group();

            // apply stopword
            loadStopWord(swFile);
            if (stopwords.contains(key)) {
                key = "";
            }

            // apply stemming
            if (Stemming == 1) {
                key = doStemming(key);
            }

            // count the occurrences of a word
            if (!key.equals("")) {
                if (query.terms.containsKey(key)) {
                    query.terms.put(key, query.terms.get(key) + 1);
                    if (Double.compare(maxFreq, query.terms.get(key)) < 0) {
                        maxFreq = query.terms.get(key);
                    }
                } else {
                    query.terms.put(key, 1.0);
                }
            }
        }

        // apply TF
        switch(TFType) {
            case 0: // none
                break;
            case 1: // raw
                break;
            case 2: // binary
                for (String termKey : query.terms.keySet()) {
                    query.terms.put(termKey, 1.0);
                }
                break;
            case 3: // augmented
                for (String termKey : query.terms.keySet()) {
                    query.terms.put(termKey, 0.5 + 0.5 * query.terms.get(termKey)/maxFreq);
                }
                break;
            case 4: // logarithmic
                for (String termKey : query.terms.keySet()) {
                    query.terms.put(termKey, 1 + Math.log10(query.terms.get(termKey)));
                }
                break;
            default:
                break;
        }
    }

    public ArrayList<RetrievedDocument> searchAll(int tf, int idf, int isNormalize, int stemming,
                                                  MultipartFile swFile, String idfLocation, int method, int topS,
                                                  int isExpansion, int isPseudo, int topN, int useDifferentCollection) {
        Map<String, Double> idfScore = loadIDF(idfLocation);
        ArrayList<RetrievedDocument> result = new ArrayList<>();

        for (Map.Entry<Integer, Query> aQuery : queryList.entrySet()) {
            Query query = aQuery.getValue();
            RetrievedDocument rd = search(aQuery.getKey(), query, tf, idf, isNormalize, stemming, idfScore, swFile);

            result.add(rd);
        }

        return result;
    }

    public void secondRetrieval(int isPseudo, int topS, int topN, RetrievedDocument rd, int isExpansion, int method) {
        if (isPseudo > 0) {
            rd.topNRelevant(topS, topN);

            if (isExpansion > 0) {
                rd.updateQueryWithExpansion(topS, 0);
            } else {
                rd.updateQuery(0);
            }
        } else {
            rd.findRelevance(topS);
            if (isExpansion > 0) {
                rd.updateQueryWithExpansion(topS, method);
            } else {
                rd.updateQuery(method);
            }
        }
    }

    /**
     * Retrieve the related docList from a query
     * @param tf tf methods
     * @param idf idf methods
     * @param isNormalize whether want to normalize or not
     * @param swFile stopword location
     * @return list of RetrievedDocument
     */
    public RetrievedDocument search(Integer queryNo, Query query, int tf, int idf, int isNormalize, int stemming,
                                    Map<String, Double> idfScore, MultipartFile swFile) {
            splitSentences(query, tf, stemming, swFile);
            return new RetrievedDocument(queryNo, invertedTerms, query.rj, idf, isNormalize, idfScore, documents, query.terms);
    }
}
