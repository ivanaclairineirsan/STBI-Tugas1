package com.lala.model;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Document {

    public int no;
    public String title;
    public String author;
    public String description;

    public Document(int no, String title, String author, String description) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.description = description;
    }

}
