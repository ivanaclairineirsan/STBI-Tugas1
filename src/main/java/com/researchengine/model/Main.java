package com.researchengine.model;

import java.util.ArrayList;

/**
 * Kelas buat catatan aja cara pake nya -- nanti kelas ini dihapus
 */
public class Main {
    public static void main(String[] args) {
        Documents d = new Documents();
        d.loadDocuments("data/CISI/cisi.all");
//        for (int i = 0; i < 500; i++) {
//            System.out.println(d.docList.get(i).no);
//            System.out.println(d.docList.get(i).title);
//            System.out.println(d.docList.get(i).author);
//            System.out.println(d.docList.get(i).description);
//            System.out.println();
//        }
        Queries q = new Queries(d.docList);
        q.loadRelevanceJudgement("data/CISI/qrels.text");
        q.loadQueries("data/CISI/query.text");

        q.loadInvertedFile("data/iFile.txt");

//        q.createQuery("What problems and concerns are there in making up descriptive titles? What difficulties are involved in automatically retrieving articles from approximate titles? What is the usual relevance of the content of articles to their titles?");

        ArrayList<RetrievedDocument> results = q.search(1, true, true, "data/stopword.txt", "data/IDF.txt");


        int idx = 0;
        // print one query
        System.out.print(q.queryList.get(idx).no + ": " + q.queryList.get(idx).description + ", ");

        // print relevance judgement related to the query above
        if (q.queryList.get(idx).rj!= null) {
            System.out.print(q.queryList.get(idx).rj.relevantDocs);
        } else {
            System.out.print("[No Relevance Judgement found!]");
        }

        // print the retrieved docs and its recall-precision
        System.out.println();
        double test = 0;
//        for (int i1 = 0; i1 < results.size(); i1++) {
        RetrievedDocument result = results.get(idx);
        System.out.print("[");
        ArrayList<String[]> output = result.rankedDocuments;
        for (int i = 0; i < 50; i++) {
//                System.out.println(output.get(i)[0] + " : " + output.get(i)[1]);
            System.out.println(output.get(i)[0] + " : " + output.get(i)[1] + "(" + output.get(i)[2] + "-" + output.get(i)[3] + ")");
        }
        System.out.println("]");
        System.out.println("Avg Recall-Precision: " + result.recallPrecision[0] + "-" + result.recallPrecision[1]);
        System.out.println("NIAP: " + result.NIAP);
        System.out.println(d.docList.size());

//        }

    }
}
