package com.company;

public class Main {

    public static void main(String[] args) {
	    // Download file
        //HtmlFile htmlFile = new HtmlFile(DataDownloader.downloadTextFile("http://www.csfd.cz/film/2294-vykoupeni-z-veznice-shawshank/"));
        HtmlFile htmlFile = new HtmlFile(DataDownloader.downloadTextFile("http://www.csfd.cz/film/2292-zelena-mile/"));
        // Write out file
        System.out.println("Name: \t\t\t" + htmlFile.getContentOfAttribute("meta", "property=\"og:title\"", "content"));
        System.out.print("Origin: \t\t");
        writeOutStringArray(htmlFile.getContentOfElement("p", "class=\"origin\""));
        System.out.print("Genre: \t\t\t");
        writeOutStringArray(htmlFile.getContentOfElement("p", "class=\"genre\""));
        System.out.println("Description:\n\t  " + htmlFile.getContentOfAttribute("meta", "name=\"description\"", "content").replace(".", ".\n\t"));
        //region Method testing
        /*    // Get content of inline element <a href="... /a>
            String[] test1 = htmlFile.getContentOfElement("a", "href=\"/zebricky/nejlepsi-filmy/?show=complete#highlight-chart-2294\"");
            // Get content of element <div class="... /div>
            String[] test2 = htmlFile.getContentOfElement("div", "class=\"ct-related similar\"");
            writeOutStringArray(test1);
            System.out.println("\n");
            writeOutStringArray(test2);
            System.out.println("\n");
            // Get content of content attribute
            System.out.println(htmlFile.getContentOfAttribute("meta", "name=\"keywords\"", "content"));
            // Get content of class attribute
            System.out.println(htmlFile.getContentOfAttribute("a", "href=\"/film/289469-alcatraz/\"", "class"));*/
        //endregion
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
