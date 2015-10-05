package com.lala.model;

import java.util.ArrayList;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Documents {

    public ArrayList<Document> docList;

    public Documents(String docLocation, int tf, int idf, int normalization, int stemming, String swLocation, String ifLocation) {
        loadDocuments(docLocation);

        removeStopWord(swLocation);

        doStemming(stemming);

        ArrayList<InvertedTerm> itList = calculateTermWeight(tf, idf, normalization);

        saveToFile(itList, ifLocation);
    }

    public void loadDocuments(String docLocation) {
        docList = new ArrayList<Document>();

        // load docs here
    }

    public void removeStopWord(String swLocation) {
        // remove stop word here
    }

    public void doStemming(int stemming) {
        if (stemming > 0) {
            // do stemming here
        }
    }

    public ArrayList<InvertedTerm> calculateTermWeight(int tf, int idf, int normalization) {
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

        return new ArrayList<InvertedTerm>();
    }

    public void saveToFile(ArrayList<InvertedTerm> itList, String ifLocation) {
        // save to file here
    }

}
