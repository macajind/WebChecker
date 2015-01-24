package com.company;


import java.util.ArrayList;

/**
 * This application pulls data from <a href="http://www.csfd.cz">ČSFD</a>. Download data then processes and prints them to the console.
 *
 * @author Ondřej Štorc
 * @version 2.0
 */
class Main {

    private static final String URL = "http://www.csfd.cz/film/227786-interstellar/";
    private static final String META = "meta";
    private static final String CONTENT = "content";
    private static final String PARAGRAPH = "p";

    /**
     * This method initializes an instance of the class {@link HtmlFile}. Then use {@link HtmlFile} class to selects content of elements or attributes and prints them to the console.
     *
     * @param args not used in this application
     */
    public static void main(String[] args) {
        HtmlFile htmlFile = new HtmlFile(DataDownloader.downloadTextFile(URL)); // Download file
        //Write out file
        System.out.println("Name: \t\t\t" + htmlFile.getContentOfAttribute(META, "property=\"og:title\"", CONTENT).split(" / ")[0]);
        System.out.print("Origin: \t\t");
        writeDownStringArray(htmlFile.getContentOfElement(PARAGRAPH, "class=\"origin\""));
        System.out.print("Genre: \t\t\t");
        writeDownStringArray(htmlFile.getContentOfElement(PARAGRAPH, "class=\"genre\""));
        System.out.println("Description:\n\t  " + htmlFile.getContentOfAttribute(META, "name=\"description\"", CONTENT).replace(".", ".\n\t"));
    }

    /**
     * Write out content of array.
     * When array is null, application will print {@link NullPointerException} stack trace
     *
     * @param array {@link String} array of string
     */
    private static void writeDownStringArray(ArrayList<String> array) {
        array.forEach(System.out::println);
    }
}
