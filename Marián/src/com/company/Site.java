package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 *
 * @author Marián
 * @version 2.0
 */
public class Site {

    private final String url;
    private Document page;
    private static final String CATEGORY = "Režie";
    private static final String USER_AGENT = "Mozilla";

    /**
     *
     * @param url
     */
    public Site(String url) {
        this.url = url;
    }

    /**
     * Connection to the web
     */
    public void openConnection() throws IOException {
        page = Jsoup.connect(url).userAgent(USER_AGENT).get();
    }

    /**
     *
     */
    public void savePage() {
        Boolean found = false;
        Elements creators = page.getElementsByClass("creators");
        for (Element creator : creators) {
            Elements categories = creator.getElementsByTag("div");
            categories.remove(0); //class Elements count itself to selections run above it, if it matches to it
            for (Element category : categories) {
                if (category.getElementsByTag("h4").get(0).text().contains(CATEGORY)) {
                    Elements directors = category.getElementsByTag("a");
                    for (Element director : directors) {
                        System.out.println(director.text());
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("Neznámý");
        }
    }
}
