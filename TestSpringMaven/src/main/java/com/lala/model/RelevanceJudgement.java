package com.lala.model;

import java.util.ArrayList;

/**
 * Created by christangga on 05-Oct-15.
 */
public class RelevanceJudgement {

    public int no;
    public ArrayList<Integer> relevantDocs;

    public RelevanceJudgement(int no, int relevantDoc) {
        relevantDocs = new ArrayList<Integer>();

        this.no = no;
        relevantDocs.add(relevantDoc);
    }

    public RelevanceJudgement(int no, ArrayList<Integer> relevantDocs) {
        this.no = no;
        this.relevantDocs = relevantDocs;
    }

    public void add(int relevantDoc) {
        relevantDocs.add(relevantDoc);
    }

}
