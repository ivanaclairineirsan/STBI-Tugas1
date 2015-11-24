package com.researchengine.model;

import java.util.HashMap;
import java.util.Map;


public class Document {

    public String title;
    public String author;
    public String description;
    public Map<String, Double> terms;

    public Document(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.terms = new HashMap<>();
    }

    public double documentLength(){
            double result = 0.0;
            for (Map.Entry keyValue : terms.entrySet()) {
                result += Math.pow((double) keyValue.getValue(), 2);
            }
            return  result;
    }
}



