package com.researchengine.model.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by christangga on 14-Oct-15.
 */
public class IndexForm {
    private MultipartFile docLocation;
    private MultipartFile queryLocation;
    private MultipartFile rjLocation;
    private MultipartFile swLocation;

    private String docTF;
    private String docIDF;
    private String docNormalization;
    private String docStemming;

    private String queryTF;
    private String queryIDF;
    private String queryNormalization;
    private String queryStemming;

    public MultipartFile getDocLocation() {
        return docLocation;
    }

    public void setDocLocation(MultipartFile docLocation) {
        this.docLocation = docLocation;
    }

    public MultipartFile getQueryLocation() {
        return queryLocation;
    }

    public void setQueryLocation(MultipartFile queryLocation) {
        this.queryLocation = queryLocation;
    }

    public MultipartFile getRjLocation() {
        return rjLocation;
    }

    public void setRjLocation(MultipartFile rjLocation) {
        this.rjLocation = rjLocation;
    }

    public MultipartFile getSwLocation() {
        return swLocation;
    }

    public void setSwLocation(MultipartFile swLocation) {
        this.swLocation = swLocation;
    }

    public String getDocTF() {
        return docTF;
    }

    public void setDocTF(String docTF) {
        this.docTF = docTF;
    }

    public String getDocIDF() {
        return docIDF;
    }

    public void setDocIDF(String docIDF) {
        this.docIDF = docIDF;
    }

    public String getDocStemming() {
        return docStemming;
    }

    public void setDocStemming(String docStemming) {
        this.docStemming = docStemming;
    }

    public String getDocNormalization() {
        return docNormalization;
    }

    public void setDocNormalization(String docNormalization) {
        this.docNormalization = docNormalization;
    }

    public String getQueryTF() {
        return queryTF;
    }

    public void setQueryTF(String queryTF) {
        this.queryTF = queryTF;
    }

    public String getQueryIDF() {
        return queryIDF;
    }

    public void setQueryIDF(String queryIDF) {
        this.queryIDF = queryIDF;
    }

    public String getQueryNormalization() {
        return queryNormalization;
    }

    public void setQueryNormalization(String queryNormalization) {
        this.queryNormalization = queryNormalization;
    }

    public String getQueryStemming() {
        return queryStemming;
    }

    public void setQueryStemming(String queryStemming) {
        this.queryStemming = queryStemming;
    }

}
