package org.webchecker.search;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Parasites {
    private Parasites() { }
    public static Parasite getGoogleParasite() {
        throw new UnsupportedOperationException("Not supported yet");
    }
    public static Parasite getCSFDParasite() {
        return new CSFDParasite();
    }

    private static class CSFDParasite implements Parasite {

        @Override
        public String url(String query) {
            return "http://www.csfd.cz/hledat/?q=" + query;
        }

        @Override
        public List<Result> extractElements(Document doc) {
            Element content = doc.select("div#search-films div.content").first();

            LinkedList<Result> results = new LinkedList<>();
            content
                    .select("ul.ui-image-list > li")
                    .stream()
                    .forEach(r -> {
                        results.add(Result.result(r).putVariable("main", true));
                    });
            content
                    .select("ul.others > li")
                    .stream()
                    .forEach(r -> {
                        results.add(Result.result(r).putVariable("main", false));
                    });
            return results;
        }
    }
}
