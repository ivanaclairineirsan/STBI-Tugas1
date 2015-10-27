package com.researchengine.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Kelas buat catatan aja cara pake nya -- nanti kelas ini dihapus
 */
public class Main {
    public static void main(String[] args) {
        Documents docs = new Documents();
        docs.loadDocuments("data/ADI/adi.all");

//        docs.removeStopWord("data/stopword.txt");
//        docs.doStemming(1);
//        docs.setInvertedTerms(1, 1);
//        docs.saveToFile("data/iFile2.txt");
//        docs.calculateIDF();
//        docs.saveToFileIDF("data/IDF2.txt");

        Queries queries = new Queries(docs.docList);
        queries.loadInvertedFile("data/iFile2.txt");
//        queries.loadRelevanceJudgement("data/ADI/qrels.text");
        queries.loadQueries("data/ADI/query.text");

//        queries.createQuery("What problems and concerns are there in making up descriptive titles? What difficulties are involved in automatically retrieving articles from approximate titles? What is the usual relevance of the content of articles to their titles?");

        ArrayList<RetrievedDocument> results = queries.search("raw", "use", "use", "use", "data/stopword.txt", "data/IDF2.txt");

        int idx = 0;
        // print one query
        System.out.print(queries.queryList.get(idx).no + ": " + queries.queryList.get(idx).description + ", ");

        // print relevance judgement related to the query above
        if (queries.queryList.get(idx).rj!= null) {
            System.out.print(queries.queryList.get(idx).rj.relevantDocs);
        } else {
            System.out.print("[No Relevance Judgement found!]");
        }

        // print the retrieved docs and its recall-precision
        System.out.println();
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
        System.out.println(docs.docList.size());

    }
}
