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
    /* the relevant judgments of the query */
    public RelevanceJudgement rj;

    public Query(int no, String description, RelevanceJudgement rj) {
        this.no = no;
        this.description = description;
        this.rj = rj;
    }

}
