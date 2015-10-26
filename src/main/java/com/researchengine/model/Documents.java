package com.researchengine.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Documents {

    public ArrayList<Document> docList;
    /* terms per document */
    public ArrayList<String> terms;

    public Documents(){

    }

    public void splitSentences(String sentence) {
        sentence = sentence.toLowerCase();
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(sentence);

        terms=new ArrayList<String>();
        while(m.find()) {
            terms.add(m.group());
        }
    }

    public int countWords(String sentence) {
        int counter = 0;
        sentence = sentence.toLowerCase();
        Pattern p = Pattern.compile("\\w");
        Matcher m = p.matcher(sentence);

        terms = new ArrayList<String>();
        while(m.find()) {
            terms.add(m.group());
        }
        return terms.size();
    }

    public Documents(String docLocation, int tf, int idf, int normalization, int stemming, String swLocation, String ifLocation) throws FileNotFoundException {
        loadDocuments(docLocation);

        ArrayList<InvertedTerm> itList = calculateTermWeight(tf, idf, normalization);

        saveToFile(itList, ifLocation, swLocation, stemming);
    }

    public Document getDocument(int number)
    {
        return docList.get(number);
    }

    public void loadDocuments(String docLocation) throws FileNotFoundException {
        docList = new ArrayList<Document>();
        try {
            //load document
            File file = new File(docLocation);
            Scanner filein =  new Scanner(new FileInputStream(file));
            String temp = filein.nextLine();
            while(filein.hasNextLine())
            {
                //parsing di sini
                if (temp.substring(0,2).equals(".I"))
                {
                    System.out.println("temp: " + temp.substring(0,2));
                    Document tempDoc = new Document();
                    tempDoc.no = Integer.parseInt(temp.substring (3,temp.length()));
                    //ambil judul
                    temp = filein.nextLine(); //pasti ".T"
                    if(temp.substring(0,2).equals (".T"))
                    {
                        boolean flag = false;
                        temp = filein.nextLine(); //title baris pertama
                        while (flag == false)
                        {
                            if(temp.substring(0,2).equals(".A") || temp.substring(0,2).equals(".W"))
                                flag = true;
                            tempDoc.title += temp;
                            tempDoc.title += ' ';
                            System.out.println("temp : " + temp);
                            if(!flag)
                                temp = filein.nextLine();
                        }

                    }
                    //keluar dari loop, judul sudah terambil semua, isi temp sekarang adalah ".A atau .W"
                    System.out.println("harusnya A, tapi : " + temp);
                    if(temp.substring(0,2).equals(".A"))
                    {
                        temp = filein.nextLine(); //author pertama
                        while (!temp.equals(".W")) {
                            tempDoc.author += temp + ';';
                            temp = filein.nextLine();
                        }
                    }
                    System.out.println("harusnya .W, tapi : " + temp);
                    //keluar dari loop, author sudah terisi, isi temp sekarang adalah ".W"
                    if(temp.equals(".W"))
                    {
                        if(filein.hasNextLine()) //jaga-jaga kalo ada teks kosong
                        {
                            boolean flag = false;
                            temp = filein.nextLine(); //baris pertama deskripsi dokumen
                            System.out.println("Baris pertama : " + temp);
                            while (filein.hasNextLine() && !flag)
                            {
                                if(temp.length() > 2)
                                    if(temp.substring(0,2).equals(".I"))
                                        flag = true;
                                tempDoc.description += temp;
                                tempDoc.description += ' ';
                                if(!flag)
                                    temp = filein.nextLine();
                            }

                            if(!filein.hasNextLine() || temp.substring(0,2).equals(".I"))
                                tempDoc.description += temp;

                            docList.add(tempDoc); //masukkan dokumen ke dalam array
                        }
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void removeStopWord(String swLocation) {
        Scanner input;
        ArrayList<String> stopwords = new ArrayList<String>();
        try {
            input = new Scanner(new FileReader(swLocation));
            while (input.hasNextLine()){
                stopwords.add(input.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < terms.size(); i++) {
            for (String stopword : stopwords) {
                if (terms.get(i).equals(stopword)) {
                    terms.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    public void doStemming(int stemming) {
        PorterStemmer stem;
        if (stemming == 1) {
            for (int i = 0; i < terms.size(); i++) {
                stem = new PorterStemmer();
                stem.add(terms.get(i).toCharArray(),terms.get(i).length());
                stem.stem();
                terms.set(i, stem.toString());
            }
        }
    }

    public ArrayList<String[]> calculateTermFrequency(int tf) {
        ArrayList<String[]> termFreq = new ArrayList<String[]>(); // list of weight per word
        String[] term; // [0]: term, [1]: frequency
        int counter;

        switch(tf) {
            case 0: // none
                while(terms.size() > 0) {
                    term = new String[2];
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = "1";
                    termFreq.add(term);
                }
                break;
            case 1: // raw
                while(terms.size() > 0) {
                    term = new String[2];
                    counter = 1;
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            counter++;
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf(counter);
                    termFreq.add(term);
                }
                break;
            case 2: // binary
                while(terms.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            terms.remove(i);
                            i--;
                        }
                    }
                    term[1] = String.valueOf(1);
                    termFreq.add(term);
                }
                break;
            case 3: // augmented
                int maxCount = -1;
                while(terms.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    counter = 1;
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            counter++;
                            terms.remove(i);
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
                    temp[1] = String.valueOf(0.5 + 0.5 * (Double.valueOf(temp[1])/maxCount));
                    termFreq.set(i,temp);
                }
                break;
            case 4: // logarithmic
                while(terms.size() > 0) {
                    term = new String[2]; // [0]: term, [1]: frequency
                    counter = 1;
                    term[0] = terms.get(0);
                    terms.remove(0);
                    for (int i = 0; i < terms.size(); i++) {
                        if (terms.get(i).equals(term[0])) {
                            counter++;
                            terms.remove(i);
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

    public ArrayList<InvertedTerm> calculateTermWeight(int tf, int idf, int normalization) {
        switch(tf) {
            case 0: // none
                break;
            case 1: // raw
                break;
            case 2: // binary
                break;
            case 3: // augmented
                break;
            case 4: // logarithmic
                break;
            default:
                break;
        }

        if (idf > 0) {

        }

        if (normalization > 0) {

        }

        return new ArrayList<InvertedTerm>();
    }

    public void saveToFile(ArrayList<InvertedTerm> itList, String ifLocation, String swLocation, int stemming) {
        // save to file here

        for(Document docs : docList)
        {
            splitSentences(docs.description);
            removeStopWord(swLocation);
            doStemming(stemming);
        }

    }

}
