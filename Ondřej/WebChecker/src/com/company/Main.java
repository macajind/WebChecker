package com.company;

import java.util.ArrayList;

/**
 * This application pulls data from http://www.csfd.cz. Download data then processes and prints them to the console.
 * @author Ondřej Štorc
 * @version 2.0
 */
class Main {
    /**
     * The url of the film on ČSFD
     */
    private static final String URL = "http://www.csfd.cz/film/2292-zelena-mile/";
    /**
     * Meta tag
     */
    private static final String META = "meta";
    /**
     * Content attribute
     */
    private static final String CONTENT = "content";
    /**
     * Paragraph tag
     */
    private static final String PARAGRAPH = "p";

    /**
     * This method initializes an instance of the class {@link HtmlFile}. Then use this class to selects content of elements or attributes and prints them to the console.
     * @param args not used in this application
     */
    public static void main(String[] args) {
        HtmlFile htmlFile = new HtmlFile(DataDownloader.downloadTextFile(URL)); // Download file
        //Write out file
        System.out.println("Name: \t\t\t" + htmlFile.getContentOfAttribute(META, "property=\"og:title\"", CONTENT));
        System.out.print("Origin: \t\t");
        writeOutStringArray(htmlFile.getContentOfElement(PARAGRAPH, "class=\"origin\""));
        System.out.print("Genre: \t\t\t");
        writeOutStringArray(htmlFile.getContentOfElement(PARAGRAPH, "class=\"genre\""));
        System.out.println("Description:\n\t  " + htmlFile.getContentOfAttribute(META, "name=\"description\"", CONTENT).replace(".", ".\n\t"));

    }

    /**
     * Write out content of array.
     * What happens, when array is null?
     *  Application will print NullPointerException stack trace {@link NullPointerException}
     * @param array {@link String} array of string
     */
    private static void writeOutStringArray(ArrayList<String> array) {
        array.forEach(System.out::println);
    }
}
