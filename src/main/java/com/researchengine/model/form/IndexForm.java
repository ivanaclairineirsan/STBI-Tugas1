package com.researchengine.model.form;

/**
 * Created by christangga on 14-Oct-15.
 */
public class IndexForm {
    private String docLocation;
    private String queryLocation;
    private String rjLocation;
    private String swLocation;

    private String docTF;
    private String docIDF;
    private String docNorm;

    private String queryTF;
    private String queryIDF;
    private String queryNorm;

    public String getDocLocation() {
        return docLocation;
    }

    public void setDocLocation(String docLocation) {
        this.docLocation = docLocation;
    }

    public String getQueryLocation() {
        return queryLocation;
    }

    public void setQueryLocation(String queryLocation) {
        this.queryLocation = queryLocation;
    }

    public String getRjLocation() {
        return rjLocation;
    }

    public void setRjLocation(String rjLocation) {
        this.rjLocation = rjLocation;
    }

    public String getSwLocation() {
        return swLocation;
    }

    public void setSwLocation(String swLocation) {
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

    public String getDocNorm() {
        return docNorm;
    }

    public void setDocNorm(String docNorm) {
        this.docNorm = docNorm;
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

    public String getQueryNorm() {
        return queryNorm;
    }

    public void setQueryNorm(String queryNorm) {
        this.queryNorm = queryNorm;
    }
}
