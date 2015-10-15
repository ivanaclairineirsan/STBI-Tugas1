package com.researchengine.model;

import java.util.ArrayList;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Queries {

    public ArrayList<RelevanceJudgement> rjList; // relevance judgement
    public ArrayList<Query> queryList;

    public Queries(String querylocation, int tf, int idf, int normalization, int stemming, String swLocation, String rjLocation, String ifLocation) {
        ArrayList<InvertedTerm> itList = loadFile(ifLocation);

        loadRelevanceJudgement(rjLocation);

        loadQueries(querylocation);

        removeStopWord(swLocation);

        doStemming(stemming);

        calculateTermWeight(tf, idf, normalization);

        search();
    }

    public ArrayList<InvertedTerm> loadFile(String ifLocation) {
        // load file here

        return new ArrayList<InvertedTerm>();
    }

    public void loadRelevanceJudgement(String rjlocation) {
        rjList = new ArrayList<RelevanceJudgement>();

        // load relevance judgement here
    }

    public void loadQueries(String querylocation) {
        queryList = new ArrayList<Query>();

        // load queries here
    }

    public void removeStopWord(String swLocation) {
        // remove stop word here
    }

    public void doStemming(int stemming) {
        if (stemming > 0) {
            // do stemming here
        }
    }

    public void calculateTermWeight(int tf, int idf, int normalization) {
        switch(tf) {
            case 0: // none
                break;
            case 1: // raw
                break;
            case 2: // binary
                break;
            case 3: // augmented
                break;
            case 4: // logarithmic
                break;
            default:
                break;
        }

        if (idf > 0) {

        }

        if (normalization > 0) {

        }

        // add queryList.query.docList here
    }

    public void search() {
        // search here, update query.relevantDocList here
    }

    public double precision(int relevantDoc, int retrievedDoc) {
        // calculate avg precision here

        return 0;
    }

    public double recall(int relevantDoc, int allRelevantDoc) {
        // calculate avg recall here

        return 0;
    }

    public double nonInterpolatedAvgPrecision() {
        // calculate avg NIAP here

        return 0;
    }

}
