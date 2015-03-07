package org.webchecker.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * Class representing getting results from searching engine, we are parasitizing on. If we want to do this kind of
 * parasitizing, we have to get a source document and to extract results from it. This is why there are functions
 * {@link #url(String)} and {@link #extractResults(org.jsoup.nodes.Document)}.
 * We can say, that the {@link #url(String)} is a help function for generating URL from query. This can be helpful when
 * searching a lot
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public interface Parasite {
    /**
     * Default value of timeout. Use as timeout when there is no explicit defined timeout
     * (e.g. in {@link #search(String)} method)
     */
    int DEFAULT_TIMEOUT = 2000;

    /**
     * This method should return an URL from given query. We have to be able to download a web page with search results
     * from the URL returned by this method
     * @param query Query to be searched with the engine, this parasite is parasitizing on
     * @return URL from which can we download the web page with search results
     */
    String url(String query);
    /**
     * Extract search results from given document and create new {@link org.webchecker.search.Results} object with it
     * @param doc Document to extract results from
     * @return Results extracted from given document
     */
    Results extractResults(Document doc);

    /**
     * Returns new parasite with same {@link #extractResults(org.jsoup.nodes.Document)} but redefined {@link #url(String)}
     * method. The new {@link #url(String)} method will be returning given string
     * @param url New returning value, that should new {@link #url(String)} method return
     * @return New parasite with redefined {@link #url(String)} method
     */
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

    /**
     * Returns results from searching the given query with the engine
     * @param query The query to be searched with the engine. If null, the behavior will depend on {@link #url(String)}
     * method behavior
     * @param timeout Timeout for downloading the page with results
     * @return Results from the engine for given query
     * @throws IOException If problem with downloading occurs
     */
    default public Results search(String query, int timeout) throws IOException {
        URL url = new URL(url(query));
        return extractResults(Jsoup.parse(url, timeout));
    }

    /**
     * In fact do the same as the {@link #search(String, int)} method, but use {@link #DEFAULT_TIMEOUT} as a timeout
     * @param query The query to be searched with the engine. If null, the behavior will depend on {@link #url(String)}
     * method behavior
     * @return Results from the engine for given query
     * @throws IOException If problem with downloading occurs
     */
    default public Results search(String query) throws IOException {
        return search(query, DEFAULT_TIMEOUT);
    }
}
