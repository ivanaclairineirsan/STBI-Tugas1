import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Documents {

    public ArrayList<Document> docList;
    public ArrayList<InvertedTerm> invertedTerms;
    public Map<String, Double> idfTerms;
    Set<String> stopwords;

    public Documents() {
    }


    public Documents(String docLocation, int tf, int idf, int normalization, int stemming, String swLocation,
                     String ifLocation, String idfFileName) {
        loadDocuments(docLocation);
        for(int i = 0; i <docList.size(); i++) {
            splitSentences(docList.get(i), tf, stemming, swLocation);
        }
        setInvertedTerms(idf);
        saveToFile(ifLocation);
        saveToFileIDF(idfFileName);
    }

    public Document getDocument(int number) {
        return docList.get(number);
    }

    public int longDocument (int no)
    {
        return docList.get(0).terms.size();
    }

    public void loadDocuments(String docLocation) {
        docList = new ArrayList<>();
        try {
            //load document
            File file = new File(docLocation);
            Scanner filein = new Scanner(new FileInputStream(file));
            String temp = filein.nextLine();
            while (filein.hasNextLine()) {
                //parsing di sini
                if (temp.substring(0, 2).equals(".I")) {
                    String tempAuthor = "";
                    String tempTitle = "";
                    int tempNo = 0;
                    String tempDescription = "";

                    tempNo = Integer.parseInt(temp.substring(3, temp.length()));
                    //ambil judul
                    temp = filein.nextLine(); //pasti ".T"
                    if (temp.substring(0, 2).equals(".T")) {
                        boolean flag = false;
                        temp = filein.nextLine(); //title baris pertama
                        while (flag == false) {
                            if (temp.substring(0, 2).equals(".A") || temp.substring(0, 2).equals(".W"))
                                flag = true;

                            if (!flag){
                                tempTitle += temp;
                                tempTitle += ' ';
                                temp = filein.nextLine();
                            }
                        }
                    }
                    //keluar dari loop, judul sudah terambil semua, isi temp sekarang adalah ".A atau .W"
                    if (temp.substring(0, 2).equals(".A")) {
                        temp = filein.nextLine(); //author pertama
                        while (!temp.equals(".W")) {
                            tempAuthor += temp + ';';
                            temp = filein.nextLine();
                        }
                    }
                    //keluar dari loop, author sudah terisi, isi temp sekarang adalah ".W"
                    if (temp.equals(".W")) {
                        //jaga-jaga kalo ada teks kosong
                        if (filein.hasNextLine()) {
                            boolean flag = false;
                            temp = filein.nextLine(); //baris pertama deskripsi dokumen
                            while (filein.hasNextLine() && !flag) {
                                if (temp.length() > 2)
                                    if (temp.substring(0, 2).equals(".I"))
                                        flag = true;

                                if (!flag){
                                    tempDescription += temp;
                                    tempDescription += ' ';
                                    temp = filein.nextLine();

                                }
                            }

                            if (!filein.hasNextLine() || temp.substring(0, 2).equals(".I")) {
                                tempDescription += temp;
                            }

                            Document docTemp = new Document(tempNo, tempTitle, tempAuthor, tempDescription);
                            docList.add(docTemp); //masukkan dokumen ke dalam array
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadStopWord(String swLocation) {
        Scanner input;
        stopwords = new HashSet<>();

        try {
            input = new Scanner(new FileReader(swLocation));
            while (input.hasNextLine()){
                stopwords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String doStemming(String word) {
        PorterStemmer stem;
        stem = new PorterStemmer();
        stem.add(word.toCharArray(), word.length());
        stem.stem();

        return stem.toString();
    }

    public void setInvertedTerms(int idf) {
        calculateIDF();

        if(idf == 1)
        {
            for(Document d: docList)
            {
                for(Object key : d.terms.keySet()) {
                    Double tempIDF = idfTerms.get(key);
                    d.terms.put(key.toString(), tempIDF*d.terms.get(key));

                }
            }
        }
    }


    public void calculateIDF(){
        idfTerms = new HashMap<>();
        HashMap<String, Double> mySet = new HashMap<>();

        for(int i = 0; i < docList.size(); i++)
        {
            for(String s: docList.get(i).terms.keySet())
            {
                Double tempOccurence = 1.0;
                if(mySet.containsKey(s))
                {
                    tempOccurence = mySet.get(s)+1.0;
                }
                mySet.put(s, tempOccurence);
            }
        }

        for(String s: mySet.keySet()){
            double tempLog = Math.log10(docList.size()/mySet.get(s));
            idfTerms.put(s, tempLog);
        }

    }


    public void saveToFile(String ifLocation) {
        try {
            Map<String, Double> invertedTerms;

            Writer output = null;
            File file = new File(ifLocation);
            output = new BufferedWriter(new FileWriter(file));

            for(Document d:docList) {
                invertedTerms = new TreeMap<>(d.terms);
                for (Map.Entry<String, Double> entry : invertedTerms.entrySet()) {
                    output.write( entry.getKey() +','+ d.no+','+ entry.getValue().toString() + "\n");
                }
            }

            output.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void saveToFileIDF(String ifLocation) {

        try {
            Writer output = null;
            File file = new File(ifLocation);
            Map<String, Double> sortedIDF = new TreeMap<String, Double>(idfTerms);
            output = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<String, Double> entry : sortedIDF.entrySet()) {
                output.write( entry.getKey() +','+ entry.getValue().toString() + "\n");
            }

            output.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public void splitSentences(Document query, int TFType, int Stemming, String swLocation) {
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(query.description);

        query.terms = new HashMap<>();
        double maxFreq = 1;

        while(m.find()) {
            String key = m.group();

            // apply stopword
            loadStopWord(swLocation);
            if (stopwords.contains(key)) {
                key = "";
            }

            // apply stemming
            if (Stemming == 1) {
                key = doStemming(key);
            }

            // count the occurrences of a word
            if (!key.equals("")) {
                if (query.terms.containsKey(key)) {
                    query.terms.put(key, query.terms.get(key) + 1);
                    if (Double.compare(maxFreq, query.terms.get(key)) < 0) {
                        maxFreq = query.terms.get(key);
                    }
                } else {
                    query.terms.put(key, 1.0);
                }
            }
        }

        // apply TF
        switch(TFType) {
            case 0: // none
                break;
            case 1: // raw
                break;
            case 2: // binary
                for (String termKey : query.terms.keySet()) {
                    query.terms.put(termKey, 1.0);
                }
                break;
            case 3: // augmented
                for (String termKey : query.terms.keySet()) {
                    query.terms.put(termKey, 0.5 + 0.5 * query.terms.get(termKey)/maxFreq);
                }
                break;
            case 4: // logarithmic
                for (String termKey : query.terms.keySet()) {
                    query.terms.put(termKey, 1 + Math.log10(query.terms.get(termKey)));
                }
                break;
            default:
                break;
        }
    }

}

