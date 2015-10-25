package com.lala.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Documents {

    public ArrayList<Document> docList;

    public Documents(){

    }

    public Documents(String docLocation, int tf, int idf, int normalization, int stemming, String swLocation, String ifLocation) throws FileNotFoundException {
        loadDocuments(docLocation);

        try {
            removeStopWord(swLocation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        doStemming(stemming);

        ArrayList<InvertedTerm> itList = calculateTermWeight(tf, idf, normalization);

        saveToFile(itList, ifLocation);
    }

    public void loadDocuments(String docLocation) {

        docList = new ArrayList<Document>();

        try {

            //load document
            File file = new File(docLocation);
            Scanner filein =  new Scanner(new FileInputStream(file));
            String temp = filein.nextLine();

            while(filein.hasNextLine())
            {

                //parsing di sini
                if (temp.substring(0,2).equals(".I")) {
                    System.out.println("temp: " + temp.substring(0,2));
                    Document tempDoc = new Document();

                    tempDoc.no = Integer.parseInt(temp.substring(3, temp.length()));

                    //ambil judul
                    String contenttemp = "";
                    temp = filein.nextLine(); //pasti ".T"

                    temp = filein.nextLine(); //title baris pertama

                    while (!temp.substring(0,2).equals(".A")) {
                        tempDoc.title += temp;
                        tempDoc.title += ' ';
                        temp = filein.nextLine();
                    }
                    //keluar dari loop, judul sudah terambil semua, isi temp sekarang adalah ".A"

                    temp = filein.nextLine(); //author pertama

                    while (!temp.equals(".W")) {
                        tempDoc.author += temp + ';';
                        temp = filein.nextLine();
                    }


                    //keluar dari loop, author sudah terisi, isi temp sekarang adalah ".W"
                    if(filein.hasNextLine()) //jaga-jaga kalo ada teks kosong
                    {
                        boolean flag = false;
                        temp = filein.nextLine(); //baris pertama deskripsi dokumen
                        //System.out.println("Selesai baca 1 dokumen, read more : " + temp);
                        while (filein.hasNextLine() && !flag)
                        {
                            if(temp.length() > 2)
                                if(temp.substring(0,2).equals(".I"))
                                    flag = true;
                            tempDoc.description += temp;
                            tempDoc.description += ' ';
                            temp = filein.nextLine();
                        }

                        if(!filein.hasNextLine())
                            tempDoc.description += temp;

                        docList.add(tempDoc); //masukkan dokumen ke dalam array
                    }

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeStopWord(String swLocation) throws FileNotFoundException {
        // remove stop word here
        Scanner filein =  new Scanner(new FileInputStream(swLocation));
        ArrayList<String> stopword = new ArrayList<String>();
        while (filein.hasNextLine()){
            String temp = filein.nextLine();
            stopword.add(temp);
        }

        for (int i = 0; i < docList.size(); i++)
        {
            int j = 0;
            while ( j < docList.get(i).description.length())
            {
                String temp = null;
                while (docList.get(i).description.charAt(j) != ' ')
                {
                    temp += docList.get(i).description.charAt(j);
                    j++;
                }
                j++;
                //keluar dari sini berarti ada ' '
                temp.replace(" ", "");

                for (int k = 0; k < stopword.size(); k++)
                {
                    if(equals(temp = stopword.get(k)))
                    {
                        docList.get(i).description.replaceAll(temp, "");
                    }
                }

            }
        }
    }

    public void doStemming(int stemming) {
        if (stemming > 0) {
            Porter porter = new Porter();
            for(int i = 0; i < docList.size(); i++)
            {
                int j = 0;
                while(j < docList.get(i).description.length())
                {
                    String temp = null;
                    while(docList.get(i).description.charAt(j)!=' ' && j < docList.get(i).description.length())
                    {
                        temp += docList.get(i).description.charAt(j);
                        j++;
                    }
                    docList.get(i).description.replaceAll(temp, porter.stripAffixes(temp));
                    j++;
                }
            }
            // do stemming here
        }
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

    public void saveToFile(ArrayList<InvertedTerm> itList, String ifLocation) {
        // save to file here
    }

}
