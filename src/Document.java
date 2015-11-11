import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Document {

    public int no;
    public String title;
    public String author;
    public String description;
    public ArrayList<String> terms;

    public Document(int no, String title, String author, String description) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.description = description;
        this.terms = new ArrayList<String>();
        this.terms = splitSentences();
    }

    public Document() {
        no = 0;
        title = "";
        author = "";
        description = "";
        terms = new ArrayList<String>();
    }

    public ArrayList<String> splitSentences() {
        ArrayList<String> terms = new ArrayList<String>();
        String sentence = description.toLowerCase();
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(sentence);
        while(m.find()) {
            terms.add(m.group());
        }
        return terms;
    }

}
