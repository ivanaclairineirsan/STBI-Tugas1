package com.researchengine.model;

import java.util.Map;
import java.util.Set;

public class Query {

    /* query content */
    public String queryContent;
    /* the relevant judgments of the query */
    public Set<Integer> rj;
    /* query terms */
    public Map<String, Double> terms;

    public Query(String queryContent, Set<Integer> rj) {
        this.queryContent = queryContent.toLowerCase();
        this.rj = rj;
    }

}
