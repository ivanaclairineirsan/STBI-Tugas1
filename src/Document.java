import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    public Map<String, Double> terms;

    public Document(int no, String title, String author, String description) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Document() {
        no = 0;
        title = "";
        author = "";
        description = "";
    }
}




