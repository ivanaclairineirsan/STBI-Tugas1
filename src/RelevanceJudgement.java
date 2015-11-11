import java.util.HashSet;
import java.util.Set;

/**
 * Created by christangga on 05-Oct-15.
 */
public class RelevanceJudgement {

    public int no;
    public Set<Integer> relevantDocs;

    public RelevanceJudgement(int no, int relevantDoc) {
        relevantDocs = new HashSet<>();

        this.no = no;
        relevantDocs.add(relevantDoc);
    }

    public RelevanceJudgement(int no, Set<Integer> relevantDocs) {
        this.no = no;
        this.relevantDocs = relevantDocs;
    }

    public void add(int relevantDoc) {
        relevantDocs.add(relevantDoc);
    }

}
