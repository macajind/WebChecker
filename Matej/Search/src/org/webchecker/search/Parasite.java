package org.webchecker.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public interface Parasite {
    int DEFAULT_TIMEOUT = 2000;

    String url(String query);
    List<Result> extractElements(Document doc);

    default public Parasite anotherURL(String url) {
        return new Parasite() {
            @Override
            public String url(String query) {
                return url;
            }

            @Override
            public List<Result> extractElements(Document doc) {
                return Parasite.this.extractElements(doc);
            }
        };
    }

    default Results search(String query, int timeout) throws IOException {
        URL url = new URL(url(query));
        Document srcDoc = Jsoup.parse(url, timeout);
        return new Results(extractElements(srcDoc));
    }
    default Results search(String query) throws IOException {
        return search(query, DEFAULT_TIMEOUT);
    }
}
