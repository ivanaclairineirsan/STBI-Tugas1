package com.researchengine.model.form;

public class RetrievalAjax {

    private int[] relevant;
    private int[] notRelevant;

    public RetrievalAjax() {
    }

    public int[] getRelevant() {
        return relevant;
    }

    public void setRelevant(int[] relevant) {
        this.relevant = relevant;
    }

    public int[] getNotRelevant() {
        return notRelevant;
    }

    public void setNotRelevant(int[] notRelevant) {
        this.notRelevant = notRelevant;
    }
}
