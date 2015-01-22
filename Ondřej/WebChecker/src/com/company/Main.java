package com.company;

import javax.lang.model.util.ElementFilter;
import java.io.Console;
import java.util.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //String file = DataDownloader.downloadTextFile("http://localhost/", true);
        //System.out.println(file.split("<footer>")[1].split("</footer>")[0]);
        //System.out.println();
        //System.out.println(DataDownloader.downloadTextFile("http://localhost/", false, 3,3));
        HtmlFile htmlFile = new HtmlFile(DataDownloader.downloadTextFile("http://www.csfd.cz/film/2294-vykoupeni-z-veznice-shawshank/"));
        for (String line : htmlFile.getFile()){
            System.out.println(line);
        }
      //  System.out.println(htmlFile.calculateNesting("\t\t<a>"));

    }
}
