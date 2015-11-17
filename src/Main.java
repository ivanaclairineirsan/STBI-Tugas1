import java.util.List;

/**
 * Kelas buat catatan aja cara pake nya -- nanti kelas ini dihapus
 */
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
//        Documents d = new Documents("data/ADI/adi.all", 1, 0, 0, 1, "data/stopword.txt", "data/iFile.txt", "data/log.txt");
        long time = System.currentTimeMillis();
        Documents d = new Documents();
        d.loadDocuments("data/ADI/adi.all");

//        Queries q = new Queries();
        Queries q = new Queries(d.docList);
        q.loadInvertedFile("data/iFile.txt");
        q.loadRelevanceJudgement("data/ADI/qrels.text");
        q.loadQueries("data/ADI/query.text");
//        q.createQuery("give methods for high speed publication, printing, and distribution of  scientific journals.");
//        q.createQuery("What problems and concerns are there in making up descriptive titles?  \n" +
//                "What difficulties are involved in automatically retrieving articles from \n" +
//                "approximate titles?  \n" +
//                "What is the usual relevance of the content of articles to their titles?");
        List<RetrievedDocument> results = q.searchAll(1,0,0,1,"data/stopword.txt","data/log.txt");
        System.out.println("search time:" + (System.currentTimeMillis() - time) + " ms");

        int idx = 14;
        System.out.println("Query: " + idx);
        System.out.println(idx + " -> Query : " + q.queryList.get(idx).queryContent);
        RetrievedDocument result = results.get(idx);
        result.printDocResult();

        System.out.println("Avg Recall : " + result.recallPrecision[0]);
        System.out.println("Precision : " + result.recallPrecision[1]);
        System.out.println("NIAP: " + result.NIAP);
    }
}
