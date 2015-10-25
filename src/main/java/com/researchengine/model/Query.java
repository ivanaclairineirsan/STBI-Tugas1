package com.researchengine.model;

import java.util.ArrayList;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Query {

    /* query identifier */
    public int no;
    /* query content */
    public String description;
    /* the documents retrieved related to the query */
    public ArrayList<Document> relevantDocList;
    /* the relevant judgments of the query */
    public RelevanceJudgement rj;

    /* the recall */
    public double recall;
    /* the precision */
    public double precission;
    /* the non interpolated average precision */
    public double NIAP;

    /* the number of relevant docs retrieved */
    private int relevantNum;

    public Query(int no, String description, RelevanceJudgement rj) {
        this.no = no;
        this.description = description;
        this.relevantDocList = new ArrayList<>();
        this.rj = rj;
    }

    /**
     * add the relevant document to the query if not already exist yet
     * @param document related to the query
     * @return 1 if success, 0 if not
     */
    public int addRelevantDocument(Document document) {
        for (int i = 0; i < relevantDocList.size(); i++) {
            if (relevantDocList.get(i).no == document.no) { // if the document is already exist
                return 0;
            }
        }

        relevantDocList.add(document);
        return 1;
    }

    /**
     * find the number of relevant documents in relevant judgements
     */
    public void findRelevantNum() {
        if (relevantDocList.size() > 0) {
            if (rj != null) {
                for (Document aRelevantDocList : relevantDocList) {
                    for (int j = 0; j < rj.relevantDocs.size(); j++) {
                        if (aRelevantDocList.no == rj.relevantDocs.get(j)) {
                            relevantNum++;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * count recall of the query
     */
    public void countRecall() {
        relevantNum = 0;
        findRelevantNum();
        if (rj != null) {
            recall = (rj.relevantDocs.size() > 0) ? ((double) relevantNum/rj.relevantDocs.size()) : 0;
        } else {
            recall = 0;
        }
    }

    /**
     * count precision of the query
     */
    public void countPrecision() {
        relevantNum = 0;
        findRelevantNum();
        precission = (relevantDocList.size() > 0) ? ((double) relevantNum/relevantDocList.size()) : 0;
    }

    /**
     * count the non interpolated average precision of the query
     */
    public void countNonInterpolatedAvgPrecision() {
        NIAP = (rj != null) ? ( precission/rj.relevantDocs.size()) : 0;
    }

}
