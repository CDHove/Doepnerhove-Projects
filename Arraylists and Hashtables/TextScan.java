// TextScan.java
// Opens text file supplied on the command line
// Usage:  java TextScan filename
// Counts all tokens found (a token is anything delimited by a space character)
// Displays each token found to the screen
// Code may be used in part for Project 5 with proper citing. 

import java.util.Scanner;
import java.io.*;

//TEXT SCAN USES ARRAYLIST, NGEN, AND LIST, ALL FILES MUST BE PRESENT FOR IT TO WORK
public class TextScan {


    //convert takes in a file name and converts every word in that file and adds it to an array
    //      print statements are to give feedback on if the file loaded or not
    public String[] convert(String file) {
        Scanner readFile = null;
        String s;
        ArrayList<String> outArr = new ArrayList<String>();
        int count = 0;


        System.out.println();
        System.out.println("Attempting to read from file: " + file);
        try {
            readFile = new Scanner(new File(file));
        }
        catch (FileNotFoundException e) {
            System.out.println("File: " + file + " not found");
            System.exit(1);
        }


        System.out.println("Connection to file: " + file + " successful");
        System.out.println();
        while (readFile.hasNext()) {
            s = readFile.next();
            outArr.add(s);
            System.out.println("Token found: " + s);
            count++;
        }

        System.out.println();
        System.out.println(count + " Tokens found");
        System.out.println();
        String[] hold = outArr.toArray();
        return hold;
    }


    public static void main(String[] args) {//Main used for testing only
        TextScan test = new TextScan();
        String[] holdTest = test.convert("canterbury.txt");
        for (int i=0; i<holdTest.length; i++) {
            System.out.println(holdTest[i]);
        }
    }  // main
}  // TextScan

//modified by halvo561 and doepn008