package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;


public class Site {

    private String url;
    private Document page;

    public Site(String url){

        this.url = url;

    }

    /**
     * Connection to the web
     *
     */
    public void openConnection() throws IOException {

        page = Jsoup.connect(url).userAgent("Mozilla").get();


    }

    public void savePage() throws IOException {

        Elements creators = page.getElementsByClass("creators");
        String director = "";
        Element creator = null;

        for (Element c : creators){
            creator = c;
        }

            for (int i = 1; i < creators.size(); i++) {
                if ((creator.select("h4:nth-child(" + i + ")").text()).equals("Réžie:")){
                    director = creator.select("h4 ~ span a").text();
                }
                else{
                    director = "Neznámy";
                }

        }

        System.out.println(creator);


    }
}
