package com.researchengine.model;

/**
 * Created by Ivana Clairine on 10/26/2015.
 */
public class IDFClass {
    String term;
    Double idfNumber;

    public IDFClass() {
        term = "";
        idfNumber = 0.0;
    }

    public IDFClass(String term, double numb)
    {
        this.term = term;
        this.idfNumber = numb;
    }

}
