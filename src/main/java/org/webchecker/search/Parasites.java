package org.webchecker.search;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;

/**
 * Static class providing implementation of well-known search engine parasites such as CSFD parasite.
 *
 * @author Matěj Kripner &lt;kripnermatej@gmail.com&gt;
 * @version 1.0
 */
public class Parasites {

    private Parasites() {
        //Exists only to defeat instantiation.
    }

    /**
     * Return new {@link CSFDParasite} object.
     *
     * @return new {@link CSFDParasite} object
     */
    public static Parasite getCSFDParasite() {
        return new CSFDParasite();
    }

    /**
     * Implementation of parasite on CSFD search engine.
     */
    private static class CSFDParasite implements Parasite {

        /**
         * {@inheritDoc}
         */
        @Override
        public String url(String query) {
            return "http://www.csfd.cz/hledat/?q=" + query;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Results extractResults(Document doc) {
            Element content = doc.select("div#search-films div.content").first();

            LinkedList<Result> results = new LinkedList<>();
            content.select("ul.ui-image-list > li").stream().forEach(e -> results.add(Result.result(e).addVariable("main", true)));
            content.select("ul.others > li").stream().forEach(e -> results.add(Result.result(e).addVariable("main", false)));
            return new Results(results);
        }
    }
}
