package org.webchecker.watcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Class represents a listener. In fact, it is only a container of basic listener's attributes and config object.
 * <p>
 * The rule is, that one listener is bind to one element, which get from supply element function. The listener
 * define function for determine changes in document and function, which should be used when a change/changes occurs.
 * <p>
 * The class is designed to provide maximal comfort for a user.
 *
 * @author Matěj Kripner<kripnermatej@gmail.com>
 * @version 1.0
 */
public class Listener {

    /* This function should be called when listener listen some changes in a document. The consumer consume two
       Elements - the new old (before change) and the new one. */
    private BiConsumer<Element, Element> action;

    // This predicate gets old and new element and determine if there is a change.
    private BiPredicate<Element, Element> changed;

    // The function gets a document and extract the element which change is this listener listening to.
    private Function<Document, Element> supplyElement;

    // A configuration of this listener, in fact instance of ListenerConfig class.
    private ListenerConfig config;

    // The state of document, which is this listener listening to, in time of last change listened.
    private Document lastChangeDocument;

    // The basic constructor, that get this listener to default state.
    private Listener() {
        setConfig(null);
    }

    /**
     * Factory method of Listener class.
     *
     * @return new {@link Listener} is the default state
     */
    public static Listener listener() {
        return new Listener();
    }

    /**
     * Simple setter, that set the supply element attribute to given value.
     *
     * @param supplyElement new value of supply element attribute
     * @return this
     */
    public Listener setSupplyElement(Function<Document, Element> supplyElement) {
        this.supplyElement = supplyElement;
        return this;
    }

    /**
     * Simple setter, that set the "changed" attribute to given value.
     *
     * @param changed new value of "changed" attribute
     * @return this
     */
    public Listener setChanged(BiPredicate<Element, Element> changed) {
        this.changed = changed;
        return this;
    }

    /**
     * Simple setter, that set the action attribute to given value.
     *
     * @param action new value of action attribute
     * @return this
     */
    public Listener setAction(BiConsumer<Element, Element> action) {
        this.action = action;
        return this;
    }

    /**
     * Simple setter, that set the config attribute to given value or to default config when null given.
     *
     * @param config new value of config attribute, or default config when null
     * @return this
     */
    public Listener setConfig(ListenerConfig config) {
        this.config = (config == null) ? ListenerConfig.getDefaults() : config;
        return this;
    }

    /**
     * Simple getter, that return the value of config attribute.
     *
     * @return the value of config attribute
     */
    public ListenerConfig getConfig() {
        return config;
    }

    /**
     * Simple getter, that return the value of supply element attribute.
     *
     * @return the value of supply element attribute
     */
    public Function<Document, Element> getSupplyElement() {
        return supplyElement;
    }

    /**
     * Simple getter, that return the value of "changed" attribute.
     *
     * @return the value of "changed" attribute
     */
    public BiPredicate<Element, Element> getChanged() {
        return changed;
    }

    /**
     * Simple getter, that return the value of action attribute.
     *
     * @return the value of action attribute
     */
    public BiConsumer<Element, Element> getAction() {
        return action;
    }

    /**
     * If this {@link Listener} is well filled, register it to given {@link ListenerGroup},
     * otherwise throws an {@link BadFilledException}.
     *
     * @param group The {@link ListenerGroup}, to which should be this listener registered
     * @throws BadFilledException                      if {@link Listener} is not well filled
     * @throws ListenerGroup.AlreadyDestroyedException if {@link Listener} was already destroyed
     */
    public void register(ListenerGroup group) throws BadFilledException, ListenerGroup.AlreadyDestroyedException {
        checkFilled();
        group.addListener(this);
    }

    /**
     * Check, if this {@link Listener} is well filled and throw an {@link BadFilledException} if not.
     * <p>
     * In fact a listener is well filled, when:<br />
     * {@code
     * action() != null && changed() != null && setElement() != null
     * }
     *
     * @throws BadFilledException if this {@link Listener} is not well filled
     */
    private void checkFilled() throws BadFilledException {
        if (getAction() == null || getChanged() == null || getSupplyElement() == null) {
            throw new BadFilledException("Action, changed and element in each listener has to be filled");
        }
    }

    /**
     * Simple getter, that return the value of document last change attribute.
     *
     * @return the value of document last change attribute
     */
    public Document getLastChangeDocument() {
        return lastChangeDocument;
    }

    /**
     * Simple setter, that set the document last change attribute to given value.
     *
     * @param lastChangeDocument new value of document last change attribute
     */
    public void setLastChangeDocument(Document lastChangeDocument) {
        this.lastChangeDocument = lastChangeDocument;
    }

    /**
     * Exception, that should be throw, when someone try to register a bad filled {@link Listener}.
     */
    public static class BadFilledException extends RuntimeException {
        public BadFilledException(String msg) {
            super(msg);
        }
    }
}
