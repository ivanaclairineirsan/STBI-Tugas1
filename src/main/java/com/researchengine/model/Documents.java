package com.researchengine.model;

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
    public ArrayList<IDFClass> idfTerms;

    public Documents() {
    }

    public int countWords(int number) {
        return docList.get(number).terms.size();
    }

    public Documents(String docLocation, int tf, int idf, int normalization, int stemming, String swLocation, String ifLocation) throws FileNotFoundException {
        loadDocuments(docLocation);
        removeStopWord(swLocation);
        doStemming(stemming);
        setInvertedTerms(tf, idf);
        //  saveToFile(itList, ifLocation, swLocation, stemming);
    }

    public Document getDocument(int number) {
        return docList.get(number);
    }

    public void loadDocuments(String docLocation) {
        docList = new ArrayList<Document>();
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
                            tempTitle += temp;
                            tempTitle += ' ';
                            if (!flag)
                                temp = filein.nextLine();
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
                                tempDescription += temp;
                                tempDescription += ' ';
                                if (!flag)
                                    temp = filein.nextLine();
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

    public void removeStopWord(String swLocation) {
        Scanner input;
        ArrayList<String> stopwords = new ArrayList<String>();
        try {
            input = new Scanner(new FileReader(swLocation));
            while (input.hasNextLine()) {
                stopwords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int number = 0; number < docList.size(); number++) {
            for (int i = 0; i < docList.get(number).terms.size(); i++) {
                for (String stopword : stopwords) {
                    if (docList.get(number).terms.get(i).equals(stopword)) {
                        docList.get(number).terms.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
    }

    public void doStemming(int stemming) {
        PorterStemmer stem;
        if (stemming == 1) {
            for (int number = 1; number < docList.size(); number++) {
                for (int i = 0; i < docList.get(number).terms.size(); i++) {
                    stem = new PorterStemmer();
                    stem.add(docList.get(number).terms.get(i).toCharArray(), docList.get(number).terms.get(i).length());
                    stem.stem();
                    docList.get(number).terms.set(i, stem.toString());
                }
            }
        }
    }

    public void setInvertedTerms(int tf, int idf) {
        invertedTerms = new ArrayList<InvertedTerm>();
        for (int i = 0; i < docList.size(); i++) {
            ArrayList<String[]> termfreq;
            termfreq = calculateTermFrequency(tf, i);


            if(idf == 0)
            for (int j = 0; j < termfreq.size(); j++) {
                invertedTerms.add(new InvertedTerm(termfreq.get(j)[0], docList.get(i).no, Double.valueOf(termfreq.get(j)[1])));
            }
            else if(idf == 1)
                {
                    calculateIDF();
                    double tempTFIDF = 0.0;
                    for (int j = 0; j < termfreq.size(); j++) {
                        int k = 0; boolean stop = false;
                        while(k<idfTerms.size() && !stop)
                        {
                            if(termfreq.get(j)[0].equals(idfTerms.get(k).term))
                            {
                                tempTFIDF = idfTerms.get(k).idfNumber * Double.valueOf(termfreq.get(j)[1]);
                                IDFClass temp1 = new IDFClass(idfTerms.get(k).term, tempTFIDF);
                                idfTerms.set(k, temp1);
                                stop = true;
                            }
                            k++;
                        }
                        invertedTerms.add(new InvertedTerm(termfreq.get(j)[0], docList.get(i).no, tempTFIDF));
                    }
                }
        }
    }

    public ArrayList<String> makeUnique (ArrayList<String> input)
    {
        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0; i < input.size(); i++)
        {
            if(!temp.contains(input.get(i)))
                temp.add(input.get(i));
        }
        return temp;
    }

    public void calculateIDF(){
        ArrayList<String> tempTerms = new ArrayList<String>();
        ArrayList<Double> tempFrequency;
        ArrayList<String> temp;

        for(int i = 0; i < docList.size(); i++)
        {
            temp = new ArrayList<String>(makeUnique(docList.get(i).terms));
            for(int j = 0; j < temp.size(); j++)
                tempTerms.add(temp.get(j));
        }

        Set<String> mySet = new HashSet<String>(tempTerms);
        idfTerms = new ArrayList<IDFClass>();
        for(String s: mySet){
            IDFClass tempIDF = new IDFClass(s, Collections.frequency(tempTerms, s));
            System.out.println("Pembilang : " + docList.size() + "Penyebut :" + tempIDF.idfNumber);
            double tempLog = Math.log10(docList.size()/tempIDF.idfNumber);
            tempIDF.idfNumber = tempLog;
            idfTerms.add(tempIDF);
        }

        for(int i = 0; i < idfTerms.size(); i++)
        {
            System.out.println(idfTerms.get(i).term + "  " + idfTerms.get(i).idfNumber);
        }

    }

    public ArrayList<String[]> calculateTermFrequency(int tf, int number) {
        ArrayList<String[]> termFreq = new ArrayList<String[]>(); // list of weight per word
        String[] term; // [0]: term, [1]: frequency
        int counter;
        ArrayList<String> termtemps = new ArrayList<String>(docList.get(number).terms);
        switch (tf) {
            case 0: // none
                while (termtemps.size() > 0) {
                    term = new String[2];
                    term[0] = termtemps.get(0);
                    termtemps.remove(0);
                    for (int i = 0; i < termtemps.size(); i++) {
                        if (termtemps.get(i).equals(term[0])) {
                            termtemps.remove(i);
                            i--;
                        }
                    }
                    term[1] = "1";
                    termFreq.add(term);
                }
                break;
            case 1: // raw
                while (termtemps.size() > 0) {
                    term = new String[2];
                    counter = 1;
                    term[0] = termtemps.get(0);
                    termtemps.remove(0);
                    for (int i = 0; i < termtemps.size(); i++) {
                        if (termtemps.get(i).equals(term[0])) {
                            counter++;
                            termtemps.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf(counter);
                    termFreq.add(term);
                }
                break;
            case 2: // binary
                while (termtemps.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    term[0] = termtemps.get(0);
                    termtemps.remove(0);
                    for (int i = 0; i < termtemps.size(); i++) {
                        if (termtemps.get(i).equals(term[0])) {
                            termtemps.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf(1);
                    termFreq.add(term);
                }
                break;
            case 3: // augmented
                int maxCount = -1;
                while (termtemps.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    counter = 1;
                    term[0] = termtemps.get(0);
                    termtemps.remove(0);
                    for (int i = 0; i < termtemps.size(); i++) {
                        if (termtemps.get(i).equals(term[0])) {
                            counter++;
                            termtemps.remove(i);
                            i--;
                        }
                    }
                    if (counter > maxCount) {
                        maxCount = counter;
                    }
                    term[1] = String.valueOf(counter);
                    termFreq.add(term);
                }

                for (int i = 0; i < termFreq.size(); i++) {
                    String[] temp = termFreq.get(i);
                    temp[1] = String.valueOf(0.5 + 0.5 * (Double.valueOf(temp[1]) / maxCount));
                    termFreq.set(i, temp);
                }
                break;
            case 4: // logarithmic
                while (termtemps.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    counter = 1;
                    term[0] = termtemps.get(0);
                    termtemps.remove(0);
                    for (int i = 0; i < termtemps.size(); i++) {
                        if (termtemps.get(i).equals(term[0])) {
                            counter++;
                            termtemps.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf((double) 1 + Math.log10(counter));
                    termFreq.add(term);
                }
                break;
            default:
                break;
        }

        return termFreq;
    }

    public void saveToFile(String ifLocation) {
        try {
            Writer output = null;
            File file = new File(ifLocation);
            output = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < invertedTerms.size(); i++) {
                output.write(invertedTerms.get(i).term + ',' + invertedTerms.get(i).documentNo + ',' + invertedTerms.get(i).weight + "\n");
            }

            output.close();
            System.out.println("File has been written");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void saveToFileIDF(String ifLocation) {

        try {
            Writer output = null;
            File file = new File(ifLocation);
            output = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < idfTerms.size(); i++) {
                //CODE TO FETCH RESULTS AND WRITE FILE
                output.write(idfTerms.get(i).term + ',' +idfTerms.get(i).idfNumber + "\n");
            }

            output.close();
            System.out.println("File has been written");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public double longDocument(int number){
        double temp = 0.0;
        double sumTemp = 0.0;

        ArrayList<InvertedTerm> tempInverted = new ArrayList<InvertedTerm>();
        for(int i = 0; i < invertedTerms.size(); i++)
        {
            if(invertedTerms.get(i).documentNo == number)
                tempInverted.add(invertedTerms.get(i));
        }

        for(int i = 0; i < tempInverted.size(); i++)
        {
            sumTemp += Math.pow(tempInverted.get(i).weight,2.0);
        }

        return Math.sqrt(sumTemp);
    }
}
