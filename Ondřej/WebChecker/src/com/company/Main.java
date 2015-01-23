package com.company;

import java.util.ArrayList;

/**
 *
 * @author Ondřej Štorc
 * @version 2.0
 */
class Main {

    private static final String URL = "http://www.csfd.cz/film/2292-zelena-mile/";
    private static final String META = "meta";
    private static final String CONTENT = "content";
    private static final String PARAGRAPH = "p";

    /**
     *
     * @param args
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
     *
     * @param array {@link String} array - very comprehensive
     */
    private static void writeOutStringArray(ArrayList<String> array) {
        array.forEach(System.out::println);
    }
}
