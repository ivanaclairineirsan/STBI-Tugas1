package com.researchengine.model;

import java.util.ArrayList;

/**
 * Kelas buat catatan aja cara pake nya -- nanti kelas ini dihapus
 */
public class Main {
    public static void main(String[] args) {
        Queries q = new Queries();
//        q.loadRelevanceJudgement("data/CISI/qrels.text");
//        q.loadQueries("data/CISI/query.text");
        q.loadInvertedFile("data/iFile.txt");

        q.createQuery("What problems and concerns are there in making up descriptive titles? What difficulties are involved in automatically retrieving articles from approximate titles? What is the usual relevance of the content of articles to their titles?");
        ArrayList<RetrievedDocument> results = q.search(0, true, true, "data/stopword.txt");


        int idx = 0;
        // print one query
        System.out.print(q.queryList.get(idx).no + ": " + q.queryList.get(idx).description + ", ");

        // print relevance judgement related to the query above
        if (q.queryList.get(idx).rj!= null) {
            System.out.print(q.queryList.get(idx).rj.relevantDocs);
        } else {
            System.out.print("[No Relevance Judgement found!]");
        }

        // print retrieved docs related to the query above
        if (q.queryList.get(idx).relevantDocList.size() != 0) {
            System.out.print(", [");
            for (int i = 0; i < q.queryList.get(idx).relevantDocList.size()-1; i++) {
                System.out.print(q.queryList.get(idx).relevantDocList.get(i).no + ", ");
            }
            System.out.print(q.queryList.get(idx).relevantDocList.get(q.queryList.get(idx).relevantDocList.size() - 1).no + "]");
        } else {
            System.out.print(", [No Relevant Documents found!]");
        }

        // print recall and precision related to the query above
        System.out.print(", recall: " + q.queryList.get(idx).recall + ", precision: " + q.queryList.get(idx).precission);

        // print the retrieved docs and its recall-precision
        System.out.println();
        for (RetrievedDocument result : results) {
            System.out.print(result.queryNo + ": [");
            ArrayList<InvertedTerm> relevantTerm = result.relevantTerm;
            for (int i = 0; i < relevantTerm.size(); i++) {
                InvertedTerm term = relevantTerm.get(i);
                Double[] score = result.RecallPrecision.get(i);
                System.out.print(term.documentNo + "(" + score[0] + "-" + score[1] + ") " + ", ");
//                System.out.print(term.documentNo + "(" + term.weight + "), ");
            }
            System.out.println("]" + result.NIAP);
        }

    }
}
