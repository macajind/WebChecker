package com.company;

public class Main {

    public static void main(String[] args) {
	    // Download file
        HtmlFile htmlFile = new HtmlFile(DataDownloader.downloadTextFile("http://www.csfd.cz/film/2294-vykoupeni-z-veznice-shawshank/"));
        // Write out file
        writeOutStringArray(htmlFile.getFile());
        System.out.println("\n\n\n\n\n");

        // Get content of inline element <a href="... /a>
        String[] test1 = htmlFile.getContentOfElement("a", "href=\"/zebricky/nejlepsi-filmy/?show=complete#highlight-chart-2294\"");
        // Get content of element <div class="... /div>
        String[] test2 = htmlFile.getContentOfElement("div", "class=\"ct-related similar\"");
        writeOutStringArray(test1);
        System.out.println("\n");
        writeOutStringArray(test2);
    }

    /**
     * Write out content of array
     * Just for testing
     * @param array String array
     */
    private static void writeOutStringArray(String[] array){
        for(String s : array) System.out.println(s);
    }
}
