import java.util.*;

/**
 * Created by rikysamuel on 10/24/2015.
 */
public class RetrievedDocument {
    /* the query number */
    public int queryNo;
    /* the document */
    public Map<Integer, Document> documents;
    /* the normalization */
    public int normalization;
    /* the query chunk */
    public Map<String, Double> weightedTerms;
    /* the invertedFile docs */
    public Map<String, Map<Integer, Double>> invertedTerms;
    /* setter for idf */
    public int useIDF;
    /* the IDF value */
    public Map<String, Double> idfScore;
    /* the ranked document and the Recall and Precision */
    public SortedMap<Double, Set<String[]>> rankedDocuments;
    /* the size of the relevant judgement */
    public Set<Integer> relevantJudgement;
    /* the average recall-precision */
    public double[] recallPrecision;
    /* the non-interpolated average precision */
    public double NIAP;
    Map<Integer, Document> relevantDocs;
    Map<Integer, Document> irrelevantDocs;

    public RetrievedDocument(int queryNo, Map<String, Map<Integer, Double>> invertedTerms, Set<Integer> relevantJudgement,
                             int useIDF, int normalization, Map<String, Double> idfScore, Map<Integer, Document> documents,
                             Map<String, Double> weightedTerms) {
        this.queryNo = queryNo;
        this.invertedTerms = invertedTerms;
        this.relevantJudgement = relevantJudgement;
        this.useIDF = useIDF;
        this.idfScore = idfScore;
        this.documents = documents;
        this.weightedTerms = weightedTerms;
        this.normalization = normalization;
        relevantDocs = new HashMap<>();
        irrelevantDocs = new HashMap<>();

        computeSimilarity();
        computeAccuracy();
    }

    /**
     * the similarity procedure, to determine the rank of each document
      */
    public void computeSimilarity() {
        double queryWeight;
        double totalWeight = 0;
        String[] temp;
        Map<Integer, Double> invTemp;
        Set<String[]> tempDoc;

        this.rankedDocuments = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Integer, Document> document : documents.entrySet()) {
            for (Map.Entry<String, Double> weightedTerm : weightedTerms.entrySet()) {
                if (invertedTerms.containsKey(weightedTerm.getKey())) {
                    invTemp = invertedTerms.get(weightedTerm.getKey());
                    if (invTemp.containsKey(document.getKey())) {
                        queryWeight = weightedTerm.getValue();

                        if (useIDF == 1) {
//                            System.out.println(weightedTerm.getKey());
                            queryWeight *= idfScore.get(weightedTerm.getKey());
                        }
                        totalWeight += queryWeight*invTemp.get(document.getKey());
                    }
                }
            }

            if (normalization == 1) {
                Documents d = new Documents();
//                d.invertedTerms = new ArrayList<>(invertedTerms);
//                totalWeight = totalWeight / d.longDocument(document.no);
                totalWeight = totalWeight / queryLength(weightedTerms);
            }

            temp = new String[6];
            temp[0] = String.valueOf(document.getValue().title);
            temp[1] = String.valueOf(document.getValue().description);
            // temp[2] for recall
            // temp[3] for precision
            temp[4] = String.valueOf(document.getKey());
            temp[5] = String.valueOf(totalWeight);

            if (Double.compare(totalWeight, 0.0) > 0) {
                if (!rankedDocuments.containsKey(totalWeight)) {
                    tempDoc = new HashSet<>();
                    tempDoc.add(temp);
                    rankedDocuments.put(totalWeight, tempDoc);
                } else {
                    tempDoc = rankedDocuments.get(totalWeight);
                    tempDoc.add(temp);
                    rankedDocuments.put(totalWeight, tempDoc);
                }
            }
            totalWeight = 0;
        }
    }

    public void findRelevance(int topS, int useOldCollection) {
        int counter = 0;
        int noDocs;

        for (Map.Entry<Double, Set<String[]>> rankedDocument : rankedDocuments.entrySet()) {
            for (String[] docsEl : rankedDocument.getValue()) {
                if (counter == topS) {
                    break;
                }

                noDocs = Integer.valueOf(docsEl[4]);
                if (isRelevant(noDocs)) {
                    relevantDocs.put(noDocs, documents.get(noDocs));
                } else {
                    irrelevantDocs.put(noDocs, documents.get(noDocs));
                }

                counter++;
            }
        }

        
    }

    public void addToRelevant(int noDoc) {
        relevantDocs.put(noDoc, documents.get(noDoc));
    }

    public void addToIrrelevant(int noDoc) {
        irrelevantDocs.put(noDoc, documents.get(noDoc));
    }

    public void updateQuery(int method) {
        Map<String, Double> roccioWeight = new HashMap<>();

        double weightRelevant;
        double weightIrrelevant;

        /*System.out.print("Relevan: ");
        for(Map.Entry<Integer, Document> doc : relevantDocs.entrySet()) {
            System.out.print(doc.getKey() + " ");
        }
        System.out.println();
        System.out.print("Irrelevan: ");
        for(Map.Entry<Integer, Document> doc : irrelevantDocs.entrySet()) {
            System.out.print(doc.getKey() + " ");
        }*/

        for (Map.Entry<String, Double> term : weightedTerms.entrySet()) {
            weightRelevant = 0;
            weightIrrelevant = 0;

            for (Map.Entry<Integer, Document> doc : relevantDocs.entrySet()) {
                if (invertedTerms.containsKey(term.getKey())) {
                    if (invertedTerms.get(term.getKey()).containsKey(doc.getKey())) {
                        /*if (term.getKey().equals("titl")) {
                            System.out.println("this: " + invertedTerms.get(term.getKey()).get(doc.getKey()));
                        }*/
                        weightRelevant += invertedTerms.get(term.getKey()).get(doc.getKey());
                    }
                }
            }
//            System.out.println("total: " + weightRelevant);

            for (Map.Entry<Integer, Document> doc : irrelevantDocs.entrySet()) {
                if (invertedTerms.containsKey(term.getKey())) {
                    if (invertedTerms.get(term.getKey()).containsKey(doc.getKey())) {
                        if (method == 2) { // if method = ide-dechi
                            weightIrrelevant = invertedTerms.get(term.getKey()).get(doc.getKey());
                            break;
                        } else {
                            weightIrrelevant += invertedTerms.get(term.getKey()).get(doc.getKey());
                        }
                    }
                }
            }

            if (method == 0) { // roccio
                roccioWeight.put(term.getKey(), term.getValue() + (weightRelevant/(double)relevantDocs.size()) - (weightIrrelevant/(double)irrelevantDocs.size()));
            } else {
                if (method == 1) { // ide-reguler
                    roccioWeight.put(term.getKey(), term.getValue() + weightRelevant - weightIrrelevant);
                } else { //dec-hi
//                    System.out.println("old: " + term.getKey() + " " + term.getValue() + " " + (weightRelevant));
                    roccioWeight.put(term.getKey(), term.getValue() + weightRelevant - weightIrrelevant);
                }
            }
        }

        /*System.out.println("old query:");
        for (Map.Entry<String, Double> term : weightedTerms.entrySet()) {
            System.out.print(term.getKey() + " ");
        }*/
        roccioWeight = new HashMap<>(editQuery(roccioWeight));
        weightedTerms = new HashMap<>(roccioWeight);


        computeSimilarity();
        computeAccuracy();
        /*System.out.println("new query: ");
        for (Map.Entry<String, Double> term : roccioWeight.entrySet()) {
            System.out.println(term.getKey() + " " + term.getValue());
        }*/
//        System.out.println();
    }

    public Map<String, Double> editQuery(Map<String, Double> termWeight) {
        Map<String, Double> temp = new HashMap<>();
        for (Map.Entry<String, Double> weight : termWeight.entrySet()) {
//            System.out.println("term: " + weight.getKey() + " " + weight.getValue() + " " + weightedTerms.get(weight.getKey()));
            if (Double.compare(weight.getValue(), 0.0) > 0) {
                temp.put(weight.getKey(), weight.getValue());
            }
        }

        return temp;
    }

    public void updateQueryWithExpansion(int topS, int method) {
        Map<Integer, Document> relevantDocs = new HashMap<>();
        Map<Integer, Document> irrelevantDocs = new HashMap<>();
        Map<String, Double> roccioWeight = new HashMap<>();

        int counter = 0;
        int noDocs;

        double weightRelevant = 0;
        double weightIrrelevant = 0;

        for (Map.Entry<Double, Set<String[]>> rankedDocument : rankedDocuments.entrySet()) {
            for (String[] docsEl : rankedDocument.getValue()) {
                if (counter == topS) {
                    break;
                }

                noDocs = Integer.valueOf(docsEl[4]);
                if (isRelevant(noDocs)) {
                    relevantDocs.put(noDocs, documents.get(noDocs));
                } else {
                    irrelevantDocs.put(noDocs, documents.get(noDocs));
                }

                counter++;
            }
        }

        for (Map.Entry<String, Double> term : weightedTerms.entrySet()) {
            for (Map.Entry<Integer, Document> doc : relevantDocs.entrySet()) {
                if (invertedTerms.containsKey(term.getKey())) {
                    if (invertedTerms.get(term.getKey()).containsKey(doc.getKey())) {
                        weightRelevant += invertedTerms.get(term.getKey()).get(doc.getKey());
                    }
                }
            }

            for (Map.Entry<Integer, Document> doc : irrelevantDocs.entrySet()) {
                if (invertedTerms.containsKey(term.getKey())) {
                    if (invertedTerms.get(term.getKey()).containsKey(doc.getKey())) {
                        if (method == 2) { // if method = ide-dechi
                            weightIrrelevant = invertedTerms.get(term.getKey()).get(doc.getKey());
                            break;
                        } else {
                            weightIrrelevant += invertedTerms.get(term.getKey()).get(doc.getKey());
                        }
                    }
                }
            }

            if (method == 0) { // roccio
                roccioWeight.put(term.getKey(), term.getValue() + (weightRelevant/(double)relevantDocs.size()) - (weightIrrelevant/(double)irrelevantDocs.size()));
            } else {
                if (method == 1) { // ide-reguler
                    roccioWeight.put(term.getKey(), term.getValue() + weightRelevant - weightIrrelevant);
                } else { //dec-hi
                    roccioWeight.put(term.getKey(), term.getValue() + weightRelevant - weightIrrelevant);
                }
            }
        }

        weightRelevant = 0;
        weightIrrelevant = 0;

        for (Map.Entry<Integer, Document> doc : relevantDocs.entrySet()) {
            for (Map.Entry<String, Double> docTerms : doc.getValue().terms.entrySet()) {

                if (!roccioWeight.containsKey(docTerms.getKey())) {
                    for (Map.Entry<Integer, Document> docRelev : relevantDocs.entrySet()) {
                        if (invertedTerms.containsKey(docTerms.getKey())) {
                            if (invertedTerms.get(docTerms.getKey()).containsKey(docRelev.getKey())) {
                                if (method == 2) { // if method = ide-dechi
                                    weightIrrelevant = invertedTerms.get(docTerms.getKey()).get(docRelev.getKey());
                                    break;
                                } else {
                                    weightRelevant += invertedTerms.get(docTerms.getKey()).get(docRelev.getKey());
                                }
                            }
                        }
                    }

                    for (Map.Entry<Integer, Document> docIrrelev : irrelevantDocs.entrySet()) {
                        if (invertedTerms.containsKey(docTerms.getKey())) {
                            if (invertedTerms.get(docTerms.getKey()).containsKey(docIrrelev.getKey())) {
                                weightIrrelevant += invertedTerms.get(docTerms.getKey()).get(docIrrelev.getKey());
                            }
                        }
                    }
                }

                if (method == 0) { // roccio
                    roccioWeight.put(docTerms.getKey(), (weightRelevant/(double)relevantDocs.size()) - (weightIrrelevant/(double)irrelevantDocs.size()));
                } else {
                    if (method == 1) { // ide-reguler
                        roccioWeight.put(docTerms.getKey(), weightRelevant - weightIrrelevant);
                    } else { //dec-hi
                        roccioWeight.put(docTerms.getKey(), weightRelevant - weightIrrelevant);
                    }
                }
            }
        }

        roccioWeight = editQuery(roccioWeight);
        weightedTerms = roccioWeight;
        computeSimilarity();
        computeAccuracy();
    }

    /**
     * the query length
     * @return the length of the query
     */
    public double queryLength(Map<String, Double> terms) {
        Double result = 0.0;

        for (Map.Entry keyValue : terms.entrySet()) {
            result += Math.pow((double) keyValue.getValue(), 2);
        }

        return  result;
    }

    /**
     * check whether a docs is relevant or not. Used to compute current recall-precision
     * @param docNo document id
     * @return TRUE if relevant, FALSE if not
     */
    private boolean isRelevant(int docNo) {
        return relevantJudgement != null && relevantJudgement.contains(docNo);

    }

    /**
     * compute the recall-precision-nonInterpolatedAveragePrecision of each retrieved docs
     */
    private void computeAccuracy() {
        Double[] temp;
        int numRelevantRetrieved = 0;
        boolean addNIAP = false;
        int counter = 1;
        NIAP = 0;

        for (Map.Entry<Double, Set<String[]>> rankedDocument : rankedDocuments.entrySet()) {
            for (String[] docNo : rankedDocument.getValue()) {
                if (isRelevant(Integer.valueOf(docNo[4]))) {
                    numRelevantRetrieved += 1;
                    addNIAP = true;
                }

                temp = new Double[2];
                if (relevantJudgement != null) {
                    // recall
                    temp[0] = (relevantJudgement.size() > 0) ? ((double) numRelevantRetrieved / relevantJudgement.size()) : 0;
                    // precision
                    temp[1] = (double) numRelevantRetrieved / counter;

                    if (addNIAP) {
                        NIAP += temp[1];
                        addNIAP = false;
                    }

                } else  {
                    temp[0] = 0.0;
                    temp[1] = 0.0;
                }

                docNo[2] = String.valueOf(temp[0]);
                docNo[3] = String.valueOf(temp[1]);

                Set<String[]> tempDoc =  rankedDocument.getValue();
                tempDoc.add(docNo);
                rankedDocument.setValue(tempDoc);

                counter++;
            }
        }

        computeNonInterpolatedAvgPrecision();
        computeAvgRecallPrecision(numRelevantRetrieved);
    }

    /**
     * count the non interpolated average precision of all the query
     */
    private void computeNonInterpolatedAvgPrecision() {
        NIAP = (relevantJudgement != null) ? (NIAP / (double) relevantJudgement.size()) : 0;
    }

    /**
     * the avg recall-precision of the related query
     * @param numOfRelevantDocs the relevant document
     */
    private void computeAvgRecallPrecision(int numOfRelevantDocs) {
        recallPrecision = new double[2]; // [0]:recall [1]:precision
        recallPrecision[0] = (relevantJudgement != null) ? (numOfRelevantDocs / (double) relevantJudgement.size()) : 0;
        recallPrecision[1] = (documents.size() > 0) ? (numOfRelevantDocs / (double) documents.size()) : 0;
    }

    public void printDocResult() {
        if (rankedDocuments!=null) {
            for (Map.Entry<Double, Set<String[]>> rankedDocument : rankedDocuments.entrySet()) {
                for (String[] docs : rankedDocument.getValue()) {
                    System.out.print(rankedDocument.getKey() + " - ");
                    System.out.print(docs[0]);
                    System.out.println(" (" + docs[4] + ")");
                }
            }
        }
    }
}
