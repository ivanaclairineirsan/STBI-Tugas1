import java.util.*;

/**
 * Created by rikysamuel on 10/24/2015.
 */
public class RetrievedDocument {
    /* the query number */
    public int queryNo;
    /* query weight */
    public double queryWeight;
    /* the document */
    public Set<Document> documents;
    /* the normalization */
    public int normalization;
    /* the query chunk*/
    public Map<String, Double> weightedTerms;
    /* the invertedFile docs */
    public Map<String, Map<Integer, Double>> invertedTerms;
    /* the ranked document and the Recall and Precision */
    public Map<Integer, String[]> rankedDocuments;
    /* the size of the relevant judgement */
    public Set<Integer> relevantJudgement;
    /* the average recall-precision */
    public double[] recallPrecision;
    /* the non-interpolated average precision */
    public double NIAP;


    public RetrievedDocument(int queryNo, Map<String, Map<Integer, Double>> invertedTerms, Set<Integer> relevantJudgement,
                             Set<Document> documents, double queryWeight, Map<String, Double> weightedTerms,
                             int normalization) {
        this.queryNo = queryNo;
        this.invertedTerms = invertedTerms;
        this.relevantJudgement = relevantJudgement;
        this.queryWeight = queryWeight;
        this.documents = documents;
        this.weightedTerms = weightedTerms;
        this.NIAP = 0;
        this.normalization = normalization;

        this.rankedDocuments = new HashMap<>();

        System.out.println("computing similarity");
        computeSimilarity();
        System.out.println("computing accuracy");
        computeAccuracy();
    }

    /**
     * the similarity procedure, to determine the rank of each document
      */
    public void computeSimilarity() {
        double documentWeight = 0;
        String[] temp;
        Map<Integer, Double> invTemp;
        Map<Integer, String[]> documentElement;

        for (Document document : documents) {
            for (Map.Entry<String, Double> weightedTerm : weightedTerms.entrySet()) {
                if (invertedTerms.containsKey(weightedTerm.getKey())) {
                    invTemp = invertedTerms.get(weightedTerm.getKey());
                    if (invTemp.containsKey(document.no)) {
                        documentWeight += invTemp.get(document.no);
                    }
                }
            }

            if (normalization == 1) {
                Documents d = new Documents();
//                d.invertedTerms = new ArrayList<>()invertedTerms;
                documentWeight = documentWeight / d.longDocument(document.no);
            }

            temp = new String[4];
            temp[0] = String.valueOf(queryWeight * documentWeight);
            temp[1] = String.valueOf(document.title);
            // temp[2] for recall
            // temp[3] for precision

            if (!temp[0].equals("0.0")) {
                rankedDocuments.put(document.no, temp);
            }
            documentWeight = 0;
        }
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

        for (Map.Entry<Integer, String[]> rankedDocument : rankedDocuments.entrySet()) {
            if (isRelevant(rankedDocument.getKey())) {
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

            String[] currentDocument = rankedDocument.getValue();
            currentDocument[2] = String.valueOf(temp[0]);
            currentDocument[3] = String.valueOf(temp[1]);
            rankedDocument.setValue(currentDocument);

            counter++;
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
            for (Map.Entry<Integer, String[]> rankedDocument : rankedDocuments.entrySet()) {
                System.out.print(rankedDocument.getKey() + ", similarity: ");
                System.out.print(rankedDocument.getValue()[0] + ", title: ");
                System.out.println(rankedDocument.getValue()[1]);
            }
        }
    }
}
