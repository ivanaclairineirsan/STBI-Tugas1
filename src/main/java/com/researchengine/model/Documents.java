package com.researchengine.model;

import org.springframework.web.multipart.MultipartFile;

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

    public Documents(MultipartFile docLocation, MultipartFile swLocation, String ifLocation, String idfFileName,
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

    public void loadDocuments(MultipartFile docFile) {
        try {
            Scanner scanner = new Scanner(docFile.getInputStream());
            String line = scanner.nextLine().toLowerCase();
            while (scanner.hasNextLine()) {
                if (line.substring(0, 2).equalsIgnoreCase(".I")) {
                    int no = Integer.parseInt(line.substring(3, line.length()));
                    String title = "";
                    String author = "";
                    String description = "";

                    line = scanner.nextLine().toLowerCase();

                    while (scanner.hasNextLine() && !line.substring(0, 2).equalsIgnoreCase(".I") && !line.substring(0, 2).equalsIgnoreCase(".T") && !line.substring(0, 2).equalsIgnoreCase(".A") && !line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                    }

                    if (scanner.hasNextLine() && line.substring(0, 2).equalsIgnoreCase(".T")) {
                        line = scanner.nextLine().toLowerCase();
                        while (!line.substring(0, 2).equalsIgnoreCase(".A") && !line.substring(0, 2).equalsIgnoreCase(".W") && !line.substring(0, 2).equalsIgnoreCase(".B")) {
                            title += line;
                            title += ' ';

                            line = scanner.nextLine().toLowerCase();
                        }
                    }

                    while (scanner.hasNextLine() && !line.substring(0, 2).equalsIgnoreCase(".I") && !line.substring(0, 2).equalsIgnoreCase(".T") && !line.substring(0, 2).equalsIgnoreCase(".A") && !line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                    }

                    if (scanner.hasNextLine() && line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                        while (scanner.hasNextLine() && line.length() < 2 || !line.substring(0, 2).equalsIgnoreCase(".B") && !line.substring(0, 2).equalsIgnoreCase(".X") && !line.substring(0, 2).equalsIgnoreCase(".I")) {
                            description += line;
                            description += ' ';

                            if (scanner.hasNextLine()) {
                                line = scanner.nextLine().toLowerCase();
                            } else {
                                break;
                            }
                        }
                    }

                    while (scanner.hasNextLine() && !line.substring(0, 2).equalsIgnoreCase(".I") && !line.substring(0, 2).equalsIgnoreCase(".T") && !line.substring(0, 2).equalsIgnoreCase(".A") && !line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                    }

                    if (scanner.hasNextLine() && line.substring(0, 2).equalsIgnoreCase(".A")) {
                        line = scanner.nextLine().toLowerCase();
                        while (!line.substring(0, 2).equalsIgnoreCase(".W") && !line.substring(0, 2).equalsIgnoreCase(".B") && !line.substring(0, 2).equalsIgnoreCase(".N")) {
                            author += line + ';';

                            line = scanner.nextLine().toLowerCase();
                        }
                    }

                    while (scanner.hasNextLine() && !line.substring(0, 2).equalsIgnoreCase(".I") && !line.substring(0, 2).equalsIgnoreCase(".T") && !line.substring(0, 2).equalsIgnoreCase(".A") && !line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                    }

                    if (scanner.hasNextLine() && line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                        while (scanner.hasNextLine() && line.length() < 2 || !line.substring(0, 2).equalsIgnoreCase(".X") && !line.substring(0, 2).equalsIgnoreCase(".I")) {
                            description += line;
                            description += ' ';

                            if (scanner.hasNextLine()) {
                                line = scanner.nextLine().toLowerCase();
                            } else {
                                break;
                            }
                        }
                    }

                    while (scanner.hasNextLine() && !line.substring(0, 2).equalsIgnoreCase(".I") && !line.substring(0, 2).equalsIgnoreCase(".T") && !line.substring(0, 2).equalsIgnoreCase(".A") && !line.substring(0, 2).equalsIgnoreCase(".W")) {
                        line = scanner.nextLine().toLowerCase();
                    }

                    Document d = new Document(title, author, description);
                    docList.put(no, d);
                } else {
                    line = scanner.nextLine().toLowerCase();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStopWords(MultipartFile swFile) {
        try {
            Scanner input = new Scanner(swFile.getInputStream());
            while (input.hasNextLine()) {
                stopWords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        switch (tfType) {
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

    public void calculateIDF(int idfType) {
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
                    output.write(key.toLowerCase() + '\t' + i + '\t' + invertedTerms.get(key) + "\n");
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
                writer.write(key.toLowerCase() + '\t' + sortedIdf.get(key).toString() + "\n");
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        String tc;

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n > 0) {
            switch (n) {
                case 1:
                    tc = "ADI";
                    break;
                case 2:
                    tc = "CISI";
                    break;
                case 3:
                    tc = "CACM";
                    break;
                case 4:
                    tc = "CRAN";
                    break;
                case 5:
                    tc = "MED";
                    break;
                case 6:
                    tc = "NPL";
                    break;
                default:
                    tc = "ADI";
                    break;
            }

            long time = System.currentTimeMillis();

//        Documents d = new Documents("data/" + tc + "/" + tc + ".all", "data/stopword.txt", "data/iFile.txt", "data/log.txt", 1, 0, 1);
            Documents d = new Documents();
//            d.loadDocuments("data/" + tc + "/" + tc + ".all");

            time = System.currentTimeMillis() - time;

            for (int i = 1; i <= d.docList.size(); ++i) {
                System.out.println(i + "\n" + d.docList.get(i).title + "\n" + d.docList.get(i).author + "\n" + d.docList.get(i).description);
            }

            System.out.println("Load: " + tc);
            System.out.println("Total documents: " + d.docList.size());
            System.out.println("Loaded in: " + time + " ms");

            n = scanner.nextInt();
        }

    }
}
