import java.util.*;

/**
 * Created by rikysamuel on 10/24/2015.
 */
public class RetrievedDocument {
    /* the query number */
    public int queryNo;
    /* the document */
    public Set<Document> documents;
    /* the normalization */
    public int normalization;
    /* the query chunk*/
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


    public RetrievedDocument(int queryNo, Map<String, Map<Integer, Double>> invertedTerms, Set<Integer> relevantJudgement,
                             int useIDF, int normalization, Map<String, Double> idfScore, Set<Document> documents,
                             Map<String, Double> weightedTerms) {
        this.queryNo = queryNo;
        this.invertedTerms = invertedTerms;
        this.relevantJudgement = relevantJudgement;
        this.useIDF = useIDF;
        this.idfScore = idfScore;
        this.documents = documents;
        this.weightedTerms = weightedTerms;
        this.NIAP = 0;
        this.normalization = normalization;
        this.rankedDocuments = new TreeMap<>(Collections.reverseOrder());

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

        for (Document document : documents) {
            for (Map.Entry<String, Double> weightedTerm : weightedTerms.entrySet()) {
                if (invertedTerms.containsKey(weightedTerm.getKey())) {
                    invTemp = invertedTerms.get(weightedTerm.getKey());
                    if (invTemp.containsKey(document.no)) {
                        queryWeight = weightedTerm.getValue();

                        if (useIDF == 1) {
                            queryWeight *= idfScore.get(weightedTerm.getKey());
                        }
                        totalWeight += queryWeight*invTemp.get(document.no);
                    }
                }
            }

            if (normalization == 1) {
                Documents d = new Documents();
//                d.invertedTerms = new ArrayList<>(invertedTerms);
//                totalWeight = totalWeight / d.longDocument(document.no);
                totalWeight = totalWeight / queryLength(weightedTerms);
            }

            temp = new String[5];
            temp[0] = String.valueOf(document.title);
            temp[1] = String.valueOf(document.description);
            // temp[2] for recall
            // temp[3] for precision
            temp[4] = String.valueOf(document.no);

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
                    System.out.println(docs[0]);
                }
            }
        }
    }
}
