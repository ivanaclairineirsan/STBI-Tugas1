package com.researchengine.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Kelas buat catatan aja cara pake nya -- nanti kelas ini dihapus
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //0 no, 1 raw, 2 binary, 3 augmented, 4 log
        int tf = 4;
        int idf = 0;
        int normalization = 1;
        int stemming = 1;
        boolean idfbool = false;
        boolean normbool = true;
        boolean stemmingbool = true;

        //docLocation, tf, idf, normalization, stemming, swLocation, ifLocation
        Documents d = new Documents("data/ADI/adi.all", tf, idf, normalization, stemming, "data/stopword.txt", "data/iFile2.txt");
        d.saveToFile("data/iFile2.txt");
        //d.calculateIDF();
        //d.saveToFileIDF("data/IDF2.txt");


        Queries q = new Queries(d.docList);
        q.loadInvertedFile("data/iFile2.txt");
        q.loadRelevanceJudgement("data/ADI/qrels.text");
        q.loadQueries("data/ADI/query.text");

//        q.createQuery("What problems and concerns are there in making up descriptive titles? What difficulties are involved in automatically retrieving articles from approximate titles? What is the usual relevance of the content of articles to their titles?");
//      tf, idf, isNormalize, stemming
        ArrayList<RetrievedDocument> results = q.search(0, idfbool, normbool, stemmingbool, "data/stopword.txt", "data/IDF2.txt");


        for (int idx = 0; idx < q.queryList.size(); idx++) {// print one query
            System.out.println("No Query : " + q.queryList.get(idx).no);

            // print relevance judgement related to the query above
//            if (q.queryList.get(idx).rj != null) {
//                System.out.println("Relevant Judgement : " + q.queryList.get(idx).rj.relevantDocs);
//            } else {
//                System.out.print("[No Relevance Judgement found!]");
//            }

            // print the retrieved docs and its recall-precision
            RetrievedDocument result = results.get(idx);
//            System.out.print("[");
//            ArrayList<String[]> output = result.rankedDocuments;
//            System.out.print("Rank Dokumen : ");
//            for (int i = 0; i < output.size()-1; i++) {
//                System.out.print( output.get(i)[0] + " , ");
//                // System.out.print(output.get(i)[0] + " : " + output.get(i)[1] + " , ");
//                //            System.out.println(output.get(i)[0] + " : " + output.get(i)[1] + "(" + output.get(i)[2] + "-" + output.get(i)[3] + ")");
//            }
//            //System.out.println(output.get(output.size() - 1)[0] + " : " + output.get(output.size() - 1)[1] + ']');
//            System.out.println(output.get(output.size() - 1)[0]+']');
            System.out.println("Avg Recall : " + result.recallPrecision[0]);
            System.out.println("Precision : " + result.recallPrecision[1]);
            System.out.println("NIAP: " + result.NIAP);
            System.out.println();
            //System.out.println(d.docList.size());

        }
    }
}
