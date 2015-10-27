package com.researchengine.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by rikysamuel on 10/24/2015.
 */
public class RetrievedDocument {
    /* the query number */
    public int queryNo;
    /* query weight */
    public double queryWeight;
    /* the document */
    public ArrayList<Document> documents;
    /* the normalization */
    public String normalization;
    /* the query chunk*/
    public ArrayList<String[]> weightedTerms;
    /* the invertedFile docs */
    public ArrayList<InvertedTerm> invertedTerms;

    public ArrayList<String[]> getRankedDocuments() {
        return rankedDocuments;
    }

    public void setRankedDocuments(ArrayList<String[]> rankedDocuments) {
        this.rankedDocuments = rankedDocuments;
    }

    /* the ranked document and the Recall and Precision*/
    public ArrayList<String[]> rankedDocuments;
    /* the size of the relevant judgement */
    public RelevanceJudgement relevantJudgement;

    public double[] getRecallPrecision() {
        return recallPrecision;
    }

    public void setRecallPrecision(double[] recallPrecision) {
        this.recallPrecision = recallPrecision;
    }

    /* the average recall-precision */
    public double[] recallPrecision;

    public double getNIAP() {
        return NIAP;
    }

    public void setNIAP(double NIAP) {
        this.NIAP = NIAP;
    }

    /* the non-interpolated average precision */
    public double NIAP;

    public RetrievedDocument(int queryNo, ArrayList<InvertedTerm> relevantTerm, RelevanceJudgement relevantJudgement,
                             ArrayList<Document> documents, double queryWeight, ArrayList<String[]> weightedTerms,
                             String normalization) {
        this.queryNo = queryNo;
        this.invertedTerms = relevantTerm;
        this.relevantJudgement = relevantJudgement;
        this.queryWeight = queryWeight;
        this.documents = documents;
        this.weightedTerms = weightedTerms;
        this.NIAP = 0;
        this.rankedDocuments = new ArrayList<>();
        this.normalization = normalization;

        computeSimilarity();
        sortRelevantTerm();
        computeAccuracy();
    }

    /**
     * the similarity procedure, to determine the rank of each document
     */
    public void computeSimilarity() {
        double documentWeight = 0;
        String[] temp;
        for (Document document : documents) {
            for (String[] weightedTerm : weightedTerms) {
                for (InvertedTerm invertedTerm : invertedTerms) {
                    if (invertedTerm.term.equals(weightedTerm[0])) {
                        if (invertedTerm.documentNo == document.no) {
                            documentWeight += invertedTerm.weight;
                        }
                    }
                }
            }

            if (normalization.equals("use")) {
                Documents d = new Documents();
                d.invertedTerms = invertedTerms;
                documentWeight = documentWeight / d.longDocument(document.no);
            }

            temp = new String[5];
            temp[0] = String.valueOf(document.no);
            temp[1] = String.valueOf(queryWeight * documentWeight);
            temp[4] = String.valueOf(document.title);
            rankedDocuments.add(temp);
            documentWeight = 0;
        }
    }

    /**
     * check whether a docs is relevant or not. Used to compute current recall-precision
     */
    private boolean isRelevant(int docNo) {
        if (relevantJudgement != null) {
            for (int currentRelevantDoc : relevantJudgement.relevantDocs) {
                if (currentRelevantDoc == docNo) {
                    return true;
                }
            }
        }

        return  false;
    }

    /**
     * sort the result by its weight
     */
    private void sortRelevantTerm() {
        Collections.sort(rankedDocuments, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Double.compare(Double.valueOf(o2[1]), Double.valueOf(o1[1]));
            }
        });
    }

    /**
     *  compute the recall-precision-nonInterpolatedAveragePrecision of each retrieved docs
     */
    private void computeAccuracy() {
        Double[] temp;
        int lastRelevant = 0;
        boolean addNIAP = false;

        for (int i = 0; i < rankedDocuments.size(); i++) {
            if (isRelevant(Integer.valueOf(rankedDocuments.get(i)[0]))) {
                lastRelevant += 1;
                addNIAP = true;
            }
            temp = new Double[2];
            if (relevantJudgement != null) {
                // recall
                temp[0] = (relevantJudgement.relevantDocs.size() > 0) ? ((double) lastRelevant / relevantJudgement.relevantDocs.size()) : 0;
                // precision
                temp[1] = (double) lastRelevant / (i+1);

                if (addNIAP) {
                    NIAP += temp[1];
                    addNIAP = false;
                }

            } else  {
                temp[0] = 0.0;
                temp[1] = 0.0;
            }

            String[] currentDocument = rankedDocuments.get(i);
            currentDocument[2] = String.valueOf(temp[0]);
            currentDocument[3] = String.valueOf(temp[1]);
            rankedDocuments.set(i, currentDocument);
        }

        computeNonInterpolatedAvgPrecision();
        computeAvgRecallPrecision(lastRelevant);
    }

    /**
     * count the non interpolated average precision of all the query
     */
    private void computeNonInterpolatedAvgPrecision() {
        NIAP = (relevantJudgement != null) ? (NIAP / (double) relevantJudgement.relevantDocs.size()) : 0;
    }

    /**
     * the avg recall-precision of the related query
     * @param numOfRelevantDocs the relevant document
     */
    private void computeAvgRecallPrecision(int numOfRelevantDocs) {
        recallPrecision = new double[2]; // [0]:recall [1]:precision
        recallPrecision[0] = (relevantJudgement != null) ? (numOfRelevantDocs / (double) relevantJudgement.relevantDocs.size()) : 0;
        recallPrecision[1] = (documents.size() > 0) ? (numOfRelevantDocs / (double) documents.size()) : 0;
    }
}
