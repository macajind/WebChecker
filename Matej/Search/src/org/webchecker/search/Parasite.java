package org.webchecker.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public interface Parasite {
    int DEFAULT_TIMEOUT = 2000;

    String url(String query);
    Results extractResults(Document doc);

    default public Parasite anotherURL(String url) {
        return new Parasite() {
            @Override
            public String url(String query) {
                return url;
            }

            @Override
            public Results extractResults(Document doc) {
                return Parasite.this.extractResults(doc);
            }
        };
    }

    default public Results search(Document docWithResults) {
        return extractResults(docWithResults);
    }
    default public Results search(String query, int timeout) throws IOException {
        URL url = new URL(url(query));
        return search(Jsoup.parse(url, timeout));
    }
    default public Results search(String query) throws IOException {
        return search(query, DEFAULT_TIMEOUT);
    }
}
