package com.lala.model;

import java.util.ArrayList;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Query {

    public int no;
    public String description;
    public ArrayList<Document> relevantDocList;
    public RelevanceJudgement rj;

    public Query(int no, String description, RelevanceJudgement rj) {
        this.no = no;
        this.description = description;
        this.rj = rj;
    }

    public double precision(int relevantDoc, int retrievedDoc) {
        return (retrievedDoc > 0) ? relevantDoc/retrievedDoc : relevantDoc;
    }

    public double recall(int relevantDoc, int allRelevantDoc) {
        return (allRelevantDoc > 0) ? relevantDoc/allRelevantDoc : relevantDoc;
    }

    public double nonInterpolatedAvgPrecision() {
        // calculate NIAP here

        return 0;
    }

}
