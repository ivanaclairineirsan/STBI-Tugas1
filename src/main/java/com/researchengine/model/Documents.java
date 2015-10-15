package com.researchengine.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by christangga on 05-Oct-15.
 */
public class Documents {

    public ArrayList<Document> docList;

    public Documents(String docLocation, int tf, int idf, int normalization, int stemming, String swLocation, String ifLocation) {
        loadDocuments(docLocation);

        removeStopWord(swLocation);

        doStemming(stemming);

        ArrayList<InvertedTerm> itList = calculateTermWeight(tf, idf, normalization);

        saveToFile(itList, ifLocation);
    }

    public void loadDocuments(String docLocation) {
        docList = new ArrayList<Document>();

        try {
            //load document
            Scanner filein =  new Scanner(new FileInputStream(docLocation));
            String temp = filein.nextLine();
            while(filein.hasNextLine())
            {
                //parsing di sini
                if (temp.substring(0, 1) == ".I") {
                    Document tempDoc = new Document();
                    tempDoc.no = Integer.parseInt(temp.substring (3,temp.length()-1));

                    //ambil judul
                    String contenttemp = null;
                    temp = filein.nextLine(); //pasti ".T"

                    temp = filein.nextLine(); //title baris pertama

                    while (temp.substring(0,1) != ".A") {
                        tempDoc.title += temp;
                        temp = filein.nextLine();
                    }
                    //keluar dari loop, judul sudah terambil semua, isi temp sekarang adalah ".A"

                    temp = filein.nextLine(); //author pertama

                    while (temp != ".W") {
                        tempDoc.author += temp + ';';
                        temp = filein.nextLine();
                    }
                    //keluar dari loop, author sudah terisi, isi temp sekarang adalah ".W"

                    temp = filein.nextLine(); //baris pertama deskripsi dokumen

                    while (filein.hasNextLine() && temp.substring(0,1) != ".I")
                    {
                        tempDoc.description += temp;
                        temp = filein.nextLine();
                    }
                    if(!filein.hasNextLine())
                        tempDoc.description += temp;

                    docList.add(tempDoc); //masukkan dokumen ke dalam array
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeStopWord(String swLocation) {
        // remove stop word here
    }

    public void doStemming(int stemming) {
        if (stemming > 0) {
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
