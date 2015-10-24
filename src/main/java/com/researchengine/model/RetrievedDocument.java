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
    /* the retrieved docs */
    public ArrayList<InvertedTerm> relevantTerm;
    /* the recall and precision of each sorted related document. 0 for recall, 1 for precision */
    public ArrayList<Double[]> RecallPrecision;
    /* the size of the relevant judgement */
    public RelevanceJudgement relevantJudgement;
    /* the non-interpolated average precision */
    public double NIAP;

    public RetrievedDocument(int queryNo, ArrayList<InvertedTerm> relevantTerm, RelevanceJudgement relevantJudgement) {
        this.queryNo = queryNo;
        this.relevantTerm = relevantTerm;
        this.relevantJudgement = relevantJudgement;
        this.NIAP = 0;

        if (relevantTerm != null) {
            RecallPrecision = new ArrayList<>();
            sortRelevantTerm();
            computeAccuracy();
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
        Collections.sort(relevantTerm, new Comparator<InvertedTerm>() {
            @Override
            public int compare(InvertedTerm it1, InvertedTerm it2) {
                if (it2.weight > it1.weight) {
                    return 1;
                }
                return -1;
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

        for (int i = 0; i < relevantTerm.size(); i++) {
            if (isRelevant(relevantTerm.get(i).documentNo)) {
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

            RecallPrecision.add(temp);
        }

        computeNonInterpolatedAvgPrecision(lastRelevant);
    }

    /**
     * count the non interpolated average precision of all the query
     * @return non interpolated average precision
     */
    private void computeNonInterpolatedAvgPrecision(int numOfRelevantDocs) {
        NIAP = (numOfRelevantDocs > 0) ? (NIAP / (double) numOfRelevantDocs) : 0;
    }
}
