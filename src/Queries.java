import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Queries {
    /* list of relevance judgement */
    public Map<Integer, Set<Integer>> rjList; // relevance judgement
    /* list of query + relevance judgement */
    public Map<Integer, Query> queryList;
    /* the documents */
    public Set<Document> documents;

    /* the stopword list */
    Set<String> stopwords;
    /* inverted terms */
    private Map<String, Map<Integer, Double>> invertedTerms;

    public Queries() {

    }

    public Queries(ArrayList<Document> documents) {
        this.documents = new HashSet<>(documents);
    }

    /**
     * create a single query
     * @param query the query
     */
    public void createQuery(String query) {
        queryList = new HashMap<>();
        queryList.put(0, new Query(query, null));
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
                temp = input.nextLine().split(",");
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
     * @param rjLocation the location of the relevance judgement
     */
    public void loadRelevanceJudgement(String rjLocation) {
        Scanner input;
        String[] temp;
        Set<Integer> docTemp;
        rjList = new HashMap<>();

        try {
            input = new Scanner(new FileReader(rjLocation));
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * find the related relevance judgement to the query
     * @param queryNo the number of the query
     * @return the RelevanceJudgement if found, null if not
     */
    private Set<Integer> findRelevanceJudgement(int queryNo) {
        return rjList != null ? rjList.get(queryNo) : null;
    }

    /**
     * load query to the queryList
     * @param querylocation the location of the query
     */
    public void loadQueries(String querylocation) {
        queryList = new HashMap<>();
        boolean contentMode = false, initialize = false;

        Scanner input;
        String[] temp;
        int queryNo = 0;
        StringBuffer queryContent = new StringBuffer("");
        try {
            input = new Scanner(new FileReader(querylocation));

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
        }
    }

    /**
     * Remove the stopwords
     * @param swLocation the stopwords location
     */
    public void loadStopWord(String swLocation) {
        Scanner input;
        stopwords = new HashSet<>();

        try {
            input = new Scanner(new FileReader(swLocation));
            while (input.hasNextLine()){
                stopwords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
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
    public void splitSentences(Query query, int TFType, int Stemming, String swLocation) {
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(query.queryContent);

        query.terms = new HashMap<>();
        double maxFreq = 1;

        while(m.find()) {
            String key = m.group();

            // apply stopword
            loadStopWord(swLocation);
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

    /**
     * Retrieve the related documents from the query
     * @param tf tf methods
     * @param idf idf methods
     * @param isNormalize whether want to normalize or not
     * @param swLocation stopword location
     * @return list of RetrievedDocument
     */
    public ArrayList<RetrievedDocument> search(int tf, int idf, int isNormalize, int stemming, String swLocation,
                                               String idfLocation) {
        HashMap<String, Double> idfScore = loadIDF(idfLocation);
        ArrayList<RetrievedDocument> result = new ArrayList<>();

        for (Map.Entry<Integer, Query> aQuery : queryList.entrySet()) {

            Query query = aQuery.getValue();
            splitSentences(query, tf, stemming, swLocation);

            RetrievedDocument rd = new RetrievedDocument(aQuery.getKey(), invertedTerms, query.rj, idf, isNormalize,
                    idfScore, documents, query.terms);

            // void(rd)
            result.add(rd);
        }

        return result;
    }

    void pseudoRetrieval(RetrievedDocument retrievedDocument, int N) {

    }
}
