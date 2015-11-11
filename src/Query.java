import java.util.Map;
import java.util.Set;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Query {

    /* query content */
    public String queryContent;
    /* the relevant judgments of the query */
    public Set<Integer> rj;
    /* query terms */
    public Map<String, Double> terms;

    public Query(String queryContent, Set<Integer> rj) {
        this.queryContent = queryContent.toLowerCase();
        this.rj = rj;
    }

}
