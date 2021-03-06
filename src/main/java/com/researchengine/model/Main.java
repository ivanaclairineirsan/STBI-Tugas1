package com.researchengine.model;

public class Main {
    private static String commaSeparate(Iterable<?> items) {
        StringBuilder builder = new StringBuilder();
        for (Object item : items) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(item);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
//        Documents d = new Documents("data/CISI/cisi.all", "data/stopword.txt", "data/iFile.txt", "data/log.txt", 1, 1, 1);
//        Documents d = new Documents(); d.loadDocuments("data/CISI/cisi.all");
//        System.out.println("load documents: " + (System.currentTimeMillis() - time) + " ms");
//        for(Map.Entry<Integer, Document> doc: d.docList.entrySet())
//        {
//            System.out.print(" " + doc.getKey());
//        }
//        Queries q = new Queries();

//        Queries q = new Queries(d.docList);
//        q.loadInvertedFile("data/iFile.txt");
//        q.loadRelevanceJudgement("data/CISI/qrels.text");
//        q.loadQueries("data/CISI/query.text");
//        q.createQuery("information");

//        q.createQuery("give methods for high speed publication, printing, and distribution of  scientific journals.");
//        q.createQuery("What problems and concerns are there in making up descriptive titles?  \n" +
//                "What difficulties are involved in automatically retrieving articles from \n" +
//                "approximate titles?  \n" +
//                "What is the usual relevance of the content of articles to their titles?");
//        long time = System.currentTimeMillis();
//        List<RetrievedDocument> results = q.searchAll(1,1,0,1,"data/stopword.txt","data/log.txt", 0, 20, 0, 0, 18, 1);
//        System.out.println("search time:" + (System.currentTimeMillis() - time) + " ms");
//
//        int idx = 1;
//        System.out.println("Query: " + idx);
//        System.out.println(idx + " -> Query : " + q.queryList.get(idx).queryContent);
//        RetrievedDocument result = results.get(idx-1);
//        result.printDocResult();
//        System.out.println("Avg Recall : " + result.recallPrecision[0]);
//        System.out.println("Precision : " + result.recallPrecision[1]);
//        System.out.println("NIAP: " + result.NIAP);
    }
}
