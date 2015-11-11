/**
 * Created by christangga on 05-Oct-15.
 */
public class InvertedTerm {

    public String term;
    public int documentNo;
    public double weight;

    public InvertedTerm(String term, int documentNo, double weight) {
        this.term = term;
        this.documentNo = documentNo;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return term + "," + String.valueOf(documentNo) + "," + String.valueOf(weight) + "\n";
    }

}
