package org.webchecker.forms;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
     * @param url
     */
    public void openDocument(URL url) {
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
     * Should select form element by identifier from currently opened page/document and return it.
     * What happens when document is null?
     * What happens when no element is found?
     * What happens if found element is not form?
     * What happens when it finds more then 1 element, which match the identifier?
     *
     * @param identifier
     * @return
     */
    public Form selectForm(String identifier) {
        return null;
    }

    /**
     * Support method for verification of form element.
     */
    private Boolean isElementForm(Element element) {
        return false;
    }
}
