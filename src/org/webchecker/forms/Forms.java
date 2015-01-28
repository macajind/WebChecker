package org.webchecker.forms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Module for manipulating HTML forms from <a href="https://github.com/macajind/WebChecker" target="_blank">Web Checker</a> library.
 * It is designed as singleton, which can only have open one HTML page at time,
 * but it returns instances of {@link Form}s and you can change pages at your wish.
 * So in the end, you can have prepared any number of {@link Form}s you like,
 * even from different web pages, and manipulate them as you please.
 *
 * @author Tunik
 * @version 1.0
 */
public final class Forms {

    private static Forms instance = null;
    private Document document = null;

    private Forms() {
        //Exists only to defeat instantiation.
    }

    /**
     * Method for getting singleton instance of module {@link Forms}, which you need to work with this module.
     *
     * @return singleton instance of module {@link Forms}
     */
    public static Forms getInstance() {
        if (instance == null) {
            instance = new Forms();
        }
        return instance;
    }

    /**
     * Should create {@link Document} instance from given url parameter and open it.
     *
     * @param url of page which you want to open
     * @throws IOException in case of bad format url
     */
    public void openDocument(URL url) throws IOException {
        document = Jsoup.connect(url.toString()).get();
    }

    /**
     * Open {@link Document} in singleton instance of module {@link Forms}.
     * If {@link Document} parameter is {@code null}, its like no document was opened.
     *
     * @param document {@link Document} to be opened in singleton instance of module {@link Forms}
     */
    public void openDocument(Document document) {
        this.document = document;
    }

    /**
     * Close currently open {@link Document} in singleton instance of module {@link Forms}.
     */
    public void closeDocument() {
        this.document = null;
    }

    /**
     * Get current {@link Document}
     * @return Current {@link Document}
     */
    public Document getDocument() { return this.document; }

    /**
     * Should select form element by identifier from currently opened page/document and return it.
     * When document is null application will throw {@link java.lang.NullPointerException}
     * When application doesn't found any element, application will return {@literal null}
     * When found element is not form, application will try found another matching element, but if at page isn't any matching element application return {@link null}
     * When on the page is more then one elements with same identifier, application will select first matching form element
     *
     * @param identifier is CSS selector {@link String}. For more information's about CSS selectors visit this <a href='http://www.w3schools.com/cssref/css_selectors.asp'>page</a>
     * @return new instance of {@link Form}, which contains elements of found form and url page of {@link Document}
     */
    public Form selectForm(String identifier) throws MalformedURLException {
        for(Element element : document.select(identifier)) {
            if (isElementForm(element)) {
                return new Form(element, new URL(document.baseUri()));
            }
        }
        return null;
    }

    /**
     * Support method for verification of form element.
     */
    private Boolean isElementForm(Element element) {
        return element.tagName().equals("form");
    }
}
