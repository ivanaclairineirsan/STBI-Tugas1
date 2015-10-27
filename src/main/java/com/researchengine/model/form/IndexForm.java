package com.researchengine.model.form;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    //    private String docNormalization;
    private String docStemming;

    private String queryTF;
    private String queryIDF;
    private String queryNormalization;
    private String queryStemming;

    private String filePath;

    public IndexForm() {}

    public IndexForm(String filePath) {
        this.filePath = filePath;
    }

    public MultipartFile getDocLocation() {
        return docLocation;
    }

    public String getDocString() {
        try {
            if (!docLocation.isEmpty()) {
                byte[] bytes = docLocation.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + docLocation.getOriginalFilename())));
                stream.write(bytes);
                stream.close();

                return docLocation.getOriginalFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setDocLocation(MultipartFile docLocation) {
        this.docLocation = docLocation;
    }

    public MultipartFile getQueryLocation() {
        return queryLocation;
    }

    public String getQueryString() {
        try {
            if (!queryLocation.isEmpty()) {
                byte[] bytes = queryLocation.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + queryLocation.getOriginalFilename())));
                stream.write(bytes);
                stream.close();

                return queryLocation.getOriginalFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setQueryLocation(MultipartFile queryLocation) {
        this.queryLocation = queryLocation;
    }

    public MultipartFile getRjLocation() {
        return rjLocation;
    }

    public String getRjString() {
        try {
            if (!rjLocation.isEmpty()) {
                byte[] bytes = rjLocation.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + rjLocation.getOriginalFilename())));
                stream.write(bytes);
                stream.close();

                return rjLocation.getOriginalFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public void setRjLocation(MultipartFile rjLocation) {
        this.rjLocation = rjLocation;
    }

    public MultipartFile getSwLocation() {
        return swLocation;
    }

    public String getSwString() {
        try {
            if (!swLocation.isEmpty()) {
                byte[] bytes = swLocation.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + swLocation.getOriginalFilename())));
                stream.write(bytes);
                stream.close();

                return rjLocation.getOriginalFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
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

//    public int getDocNormalization() {
//        if (docNormalization.equals("use")) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }
//
//    public void setDocNormalization(String docNormalization) {
//        this.docNormalization = docNormalization;
//    }

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
