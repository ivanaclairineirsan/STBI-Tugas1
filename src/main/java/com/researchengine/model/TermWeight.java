package com.researchengine.model;

/**
 * Created by christangga on 05-Oct-15.
 */
public class TermWeight {

    public String term;
    public int documentNo;
    public double tf;
    public double idf;
    public double normalization;

    public TermWeight(String term, int documentNo) {
        this.term = term;
        this.documentNo = documentNo;
        this.tf = 0;
        this.idf = 0;
        this.normalization = 0;
    }

    @Override
    public String toString() {
        return term + "," + String.valueOf(documentNo) + "," + String.valueOf(tf) + "\n";
    }

}
