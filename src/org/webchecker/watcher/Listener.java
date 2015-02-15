package org.webchecker.watcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Class represents a listener. In fact, it is only a container of basic listener's attributes and config object.
 * The rule is, that one listener is bind to one element, which get from {@link #supplyElement} function. The listener
 * define function for determine changes in document and function, which should be used when a change/changes occurs.
 * The class is designed to provide maximal comfort for a user.
 *
 * @author Matěj Kripner<kripnermatej@gmail.com>
 * @version 1.0
 */
public class Listener {

    /**
     * This function should be called when listener listen some changes in a document. The consumer consume two
     * Elements - the new old (before change) and the new one
     */
    private BiConsumer<Element, Element> action;
    /**
     * This predicate gets old and new element and determine if there is a change
     */
    private BiPredicate<Element, Element> changed;
    /**
     * The function gets a document and extract the element which change is this listener listening to.
     */
    private Function<Document, Element> supplyElement;
    /**
     * A configuration of this listener, in fact instance of ListenerConfig class
     *
     * @see ListenerConfig
     */
    private ListenerConfig config;
    /**
     * The state of document, which is this listener listening to, in time of last change listened.
     *
     * @see #changed()
     * @see #action()
     * @see #setLastChangeDocument(org.jsoup.nodes.Document)
     */
    private Document lastChangeDocument;

    /**
     * The basic constructor, that get this listener to default state
     */
    private Listener() {
        config(null);
    }

    /**
     * Factory method of Listener class
     *
     * @return New Listener is the default state
     */
    public static Listener listener() {
        return new Listener();
    }

    /**
     * Simple setter, that set the {@link #supplyElement} attribute to given value
     *
     * @param getElement New value of {@link #supplyElement} attribute
     * @return This
     */
    public Listener element(Function<Document, Element> getElement) {
        this.supplyElement = getElement;
        return this;
    }

    /**
     * Simple setter, that set the {@link #changed} attribute to given value
     *
     * @param changed New value of {@link #changed} attribute
     * @return This
     */
    public Listener changed(BiPredicate<Element, Element> changed) {
        this.changed = changed;
        return this;
    }

    /**
     * Simple setter, that set the {@link #action} attribute to given value
     *
     * @param action New value of {@link #action} attribute
     * @return This
     */
    public Listener action(BiConsumer<Element, Element> action) {
        this.action = action;
        return this;
    }

    /**
     * Simple setter, that set the {@link #config} attribute to given value or to default config when null given
     *
     * @param config New value of {@link #config} attribute, or default config when null
     * @return This
     */
    public Listener config(ListenerConfig config) {
        this.config = (config == null) ? ListenerConfig.defaults() : config;
        return this;
    }

    /**
     * Simple getter, that return the value of {@link #config} attribute
     *
     * @return The value of {@link #config} attribute
     */
    public ListenerConfig config() {
        return config;
    }

    /**
     * Simple getter, that return the value of {@link #supplyElement} attribute
     *
     * @return The value of {@link #supplyElement} attribute
     */
    public Function<Document, Element> element() {
        return supplyElement;
    }

    /**
     * Simple getter, that return the value of {@link #changed} attribute
     *
     * @return The value of {@link #changed} attribute
     */
    public BiPredicate<Element, Element> changed() {
        return changed;
    }

    /**
     * Simple getter, that return the value of {@link #action} attribute
     *
     * @return The value of {@link #action} attribute
     */
    public BiConsumer<Element, Element> action() {
        return action;
    }

    /**
     * If this listener is well filled, register it to given ListenerGroup. Otherwise throw an {@link Listener.BadFilledException}
     *
     * @param group The {@link ListenerGroup}, to which should be this listener registered
     */
    public void register(ListenerGroup group) {
        checkFilled();
        group.addListener(this);
    }

    /**
     * Check, if this listener is well filled and throw an {@link Listener.BadFilledException} when no.
     * In fact a listener is well filled, when:<br />
     * {@code
     * action() != null && changed() != null && element() != null
     * }
     */
    private void checkFilled() {
        if (action() == null || changed() == null || element() == null) {
            throw new BadFilledException("Action, changed and element in each listener has to be filled");
        }
    }

    /**
     * Simple getter, that return the value of {@link #lastChangeDocument} attribute
     *
     * @return The value of {@link #lastChangeDocument} attribute
     */
    Document getLastChangeDocument() {
        return lastChangeDocument;
    }

    /**
     * Simple setter, that set the {@link #lastChangeDocument} attribute to given value
     *
     * @param lastChangeDocument New value of {@link #lastChangeDocument} attribute
     */
    void setLastChangeDocument(Document lastChangeDocument) {
        this.lastChangeDocument = lastChangeDocument;
    }

    /**
     * Exception, that should be throw, when someone try to register a bad filled listener
     */
    public static class BadFilledException extends RuntimeException {

        public BadFilledException(String msg) {
            super(msg);
        }
    }
}
