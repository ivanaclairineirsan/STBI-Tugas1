package com.researchengine.model.form;

import org.springframework.web.multipart.MultipartFile;

public class IndexForm {
    private MultipartFile docFile;
    private MultipartFile queryFile;
    private MultipartFile rjFile;
    private MultipartFile swFile;

    private String docTF;
    private String docIDF;
    private String docNormalization;
    private String docStemming;

    private String queryTF;
    private String queryIDF;
    private String queryStemming;

    private String topS;
    private String topN;
    private String rfMethod;
    private String queryExpansion;
    private String secondRetrieval;

    public IndexForm() {
    }

    public MultipartFile getDocFile() {
        return docFile;
    }

    public void setDocFile(MultipartFile docFile) {
        this.docFile = docFile;
    }

    public MultipartFile getQueryFile() {
        return queryFile;
    }

    public void setQueryFile(MultipartFile queryFile) {
        this.queryFile = queryFile;
    }

    public MultipartFile getRjFile() {
        return rjFile;
    }

    public void setRjFile(MultipartFile rjFile) {
        this.rjFile = rjFile;
    }

    public MultipartFile getSwFile() {
        return swFile;
    }

    public void setSwFile(MultipartFile swFile) {
        this.swFile = swFile;
    }

    public String getDocTF() {
        return docTF;
    }

    public int getDocTFInt() {
        switch (docTF) {
            case "raw":
                return 1;
            case "binary":
                return 2;
            case "augmented":
                return 3;
            case "logarithmic":
                return 4;
            case "none":
                return 0;
        }

        return 0;
    }

    public void setDocTF(String docTF) {
        this.docTF = docTF;
    }

    public String getDocIDF() {
        return docIDF;
    }

    public int getDocIDFInt() {
        if (docIDF.equals("use")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setDocIDF(String docIDF) {
        this.docIDF = docIDF;
    }

    public String getDocStemming() {
        return docStemming;
    }

    public int getDocStemmingInt() {
        if (docStemming.equals("use")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setDocStemming(String docStemming) {
        this.docStemming = docStemming;
    }

    public String getDocNormalization() {
        return docNormalization;
    }

    public int getDocNormalizationInt() {
        if (docNormalization.equals("use")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setDocNormalization(String docNormalization) {
        this.docNormalization = docNormalization;
    }

    public String getQueryTF() {
        return queryTF;
    }

    public int getQueryTFInt() {
        switch (queryTF) {
            case "raw":
                return 1;
            case "binary":
                return 2;
            case "augmented":
                return 3;
            case "logarithmic":
                return 4;
            case "none":
                return 0;
        }

        return 0;
    }

    public void setQueryTF(String queryTF) {
        this.queryTF = queryTF;
    }

    public String getQueryIDF() {
        return queryIDF;
    }

    public int getQueryIDFInt() {
        if (queryIDF.equals("use")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setQueryIDF(String queryIDF) {
        this.queryIDF = queryIDF;
    }

    public String getQueryStemming() {
        return queryStemming;
    }

    public int getQueryStemmingInt() {
        if (queryStemming.equals("use")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setQueryStemming(String queryStemming) {
        this.queryStemming = queryStemming;
    }

    public String getTopS() {
        return topS;
    }

    public int getTopSInt() {
        return Integer.parseInt(topS);
    }

    public void setTopS(String topS) {
        this.topS = topS;
    }

    public String getTopN() {
        return topN;
    }

    public int getTopNInt() {
        return Integer.parseInt(topN);
    }

    public void setTopN(String topN) {
        this.topN = topN;
    }

    public String getRfMethod() {
        return rfMethod;
    }

    public int getRfMethodInt() {
        switch (rfMethod) {
            case "roccio":
                return 0;
            case "regular":
                return 1;
            case "dec-hi":
                return 2;
            case "pseudo":
                return 3;
        }

        return 0;
    }

    public void setRfMethod(String rfMethod) {
        this.rfMethod = rfMethod;
    }

    public String getQueryExpansion() {
        return queryExpansion;
    }

    public int getQueryExpansionInt() {
        if (queryExpansion.equals("yes")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setQueryExpansion(String queryExpansion) {
        this.queryExpansion = queryExpansion;
    }

    public String getSecondRetrieval() {
        return secondRetrieval;
    }

    public int getSecondRetrievalInt() {
        if (secondRetrieval.equals("different")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setSecondRetrieval(String secondRetrieval) {
        this.secondRetrieval = secondRetrieval;
    }

}
