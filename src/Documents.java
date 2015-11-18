import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Documents {

    public Map<Integer, Document> docList;
    public Map<String, Double> idfTerms;
    Set<String> stopWords;

    public Documents() {
        docList = new HashMap<>();
        idfTerms = new HashMap<>();
        stopWords = new HashSet<>();
    }

    public Documents(String docLocation, String swLocation, String ifLocation, String idfFileName,
                     int tf, int idf, int stemming) {
        docList = new HashMap<>();
        idfTerms = new HashMap<>();
        stopWords = new HashSet<>();

        loadDocuments(docLocation);
        loadStopWords(swLocation);

        for (Integer i : docList.keySet()) {
            splitSentences(docList.get(i), tf, stemming);
        }

        calculateIDF(idf);

        saveIdfToFile(idfFileName);
        saveTermsToFile(ifLocation);
    }

    public void loadDocuments(String docLocation) {
        try {
            Scanner scanner = new Scanner(new FileInputStream(new File(docLocation)));
            String line = scanner.nextLine();
            while (scanner.hasNextLine()) {
                if (line.substring(0, 2).equals(".I")) {
                    int no = Integer.parseInt(line.substring(3, line.length()));
                    String title = "";
                    String author = "";
                    String description = "";

                    line = scanner.nextLine();
                    if (line.substring(0, 2).equals(".T")) {
                        line = scanner.nextLine();
                        while (!line.substring(0, 2).equals(".A") && !line.substring(0, 2).equals(".W")) {
                            title += line;
                            title += ' ';

                            line = scanner.nextLine();
                        } // line.substring(0, 2).equals(".A") || line.substring(0, 2).equals(".W")
                    }

                    if (line.substring(0, 2).equals(".A")) {
                        line = scanner.nextLine();
                        while (!line.equals(".W")) {
                            author += line + ';';
                            line = scanner.nextLine();
                        }
                    }

                    if (line.equals(".W")) {
                        line = scanner.nextLine();
                        while (scanner.hasNextLine() && line.length() < 2 || !line.substring(0, 2).equals(".X") && !line.substring(0, 2).equals(".I")) {
                            description += line;
                            description += ' ';

                            line = scanner.nextLine();
                        }
                    }

                    Document d = new Document(title, author, description);
                    docList.put(no, d);
                }

                line = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadStopWords(String swLocation) {
        try {
            Scanner input = new Scanner(new FileReader(swLocation));
            while (input.hasNextLine()){
                stopWords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void splitSentences(Document document, int tfType, int stemType) {
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(document.title + document.description);

        double maxTf = 1.0;

        while (m.find()) {
            String key = m.group();

            // stop words removal
            if (stopWords.contains(key)) {
                key = "";
            }

            // stem
            if (!key.equals("") && stemType == 1) {
                key = stem(key);
            }

            // count the occurrences of a word
            if (!key.equals("")) {
                if (document.terms.containsKey(key)) {
                    document.terms.put(key, document.terms.get(key) + 1);
                    if (Double.compare(maxTf, document.terms.get(key)) < 0) {
                        maxTf = document.terms.get(key);
                    }
                } else {
                    document.terms.put(key, 1.0);
                }
            }
        }

        // apply TF
        switch(tfType) {
            case 0: // none
                break;
            case 1: // raw
                break;
            case 2: // binary
                for (String key : document.terms.keySet()) {
                    document.terms.put(key, 1.0);
                }
                break;
            case 3: // augmented
                for (String key : document.terms.keySet()) {
                    document.terms.put(key, 0.5 + 0.5 * document.terms.get(key) / maxTf);
                }
                break;
            case 4: // logarithmic
                for (String key : document.terms.keySet()) {
                    document.terms.put(key, 1 + Math.log10(document.terms.get(key)));
                }
                break;
        }
    }

    public String stem(String word) {
        PorterStemmer stem = new PorterStemmer();
        stem.add(word.toCharArray(), word.length());
        stem.stem();

        return stem.toString();
    }

    public void calculateIDF(int idfType){
        HashMap<String, Double> idfList = new HashMap<>();

        for (Integer i : docList.keySet()) {
            for (String key : docList.get(i).terms.keySet()) {
                Double occurrence = 1.0;
                if (idfList.containsKey(key)) {
                    occurrence = idfList.get(key) + 1.0;
                }
                idfList.put(key, occurrence);
            }
        }

        for (String key : idfList.keySet()) {
            idfTerms.put(key, Math.log10(docList.size() / idfList.get(key)));
        }

        if (idfType == 1) {
            for (Integer i : docList.keySet()) {
                for (String key : docList.get(i).terms.keySet()) {
                    Double idf = idfTerms.get(key);
                    docList.get(i).terms.put(key, idf * docList.get(i).terms.get(key));
                }
            }
        }
    }

    public void saveTermsToFile(String ifLocation) {
        try {
            Writer output = new BufferedWriter(new FileWriter(new File(ifLocation)));

            Map<Integer, Document> sortedDocuments = new TreeMap<>(docList);
            for (Integer i : sortedDocuments.keySet()) {
                Map<String, Double> invertedTerms = new TreeMap<>(sortedDocuments.get(i).terms);

                for (String key : invertedTerms.keySet()) {
                    output.write(key + '\t' + i + '\t' + invertedTerms.get(key) + "\n");
                }
            }

            output.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void saveIdfToFile(String ifLocation) {
        try {
            Map<String, Double> sortedIdf = new TreeMap<>(idfTerms);
            Writer writer = new BufferedWriter(new FileWriter(new File(ifLocation)));

            for (String key : sortedIdf.keySet()) {
                writer.write(key + '\t' + sortedIdf.get(key).toString() + "\n");
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Documents d = new Documents("data/CISI/cisi.all", "data/stopword.txt", "data/iFile.txt", "data/log.txt", 1, 0, 1);
//        Documents d = new Documents(); d.loadDocuments("data/CISI/cisi.all");
        System.out.println("load documents:" + (System.currentTimeMillis() - time) + " ms");
//        for (Integer i : d.docList.keySet()) {
//            System.out.println(i + "\n" + d.docList.get(i).title + "\n" + d.docList.get(i).author + "\n" + d.docList.get(i).description);
//        }
    }
}
