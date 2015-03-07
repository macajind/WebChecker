package org.webchecker.search;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Parasites {
    private Parasites() {
        //Exists only to defeat instantiation.
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
        public Results extractResults(Document doc) {
            Element content = doc.select("div#search-films div.content").first();

            LinkedList<Result> results = new LinkedList<>();
            content
                    .select("ul.ui-image-list > li")
                    .stream()
                    .forEach(e -> results.add(Result.result(e).putVariable("main", true)));
            content
                    .select("ul.others > li")
                    .stream()
                    .forEach(e -> results.add(Result.result(e).putVariable("main", false)));
            return new Results(results);
        }
    }
}
