package cz.matejkripner.watch;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author Matěj Kripner
 * @version 1.0
 */
public class Listener {
    private BiConsumer<Element, Element> action;
    private BiPredicate<Element, Element> changed;
    private Function<Document, Element> supplyElement;

    private ListenerConfig config;

    private Document lastChangeDocument;

    private Listener() {
        config(null);
    }

    public static Listener listener() {
        return new Listener();
    }

    public Listener element(Function<Document, Element> getElement) {
        this.supplyElement = getElement;
        return this;
    }
    public Listener changed(BiPredicate<Element, Element> changed) {
        this.changed = changed;
        return this;
    }
    public Listener action(BiConsumer<Element, Element> action) {
        this.action = action;
        return this;
    }
    public Listener config(ListenerConfig config) {
        this.config = (config == null) ? ListenerConfig.defaults() : config;
        return this;
    }

    public ListenerConfig config() {
        return config;
    }
    public Function<Document, Element> supplyElement() {
        return supplyElement;
    }

    public BiPredicate<Element, Element> changed() {
        return changed;
    }

    public BiConsumer<Element, Element> action() {
        return action;
    }

    public void register(ListenerGroup group) {
        checkFilled();
        group.addListener(this);
    }
    private void checkFilled() {
        if(action() == null || changed() == null || supplyElement() == null) {
            throw new BadFilledException("Action, changed and element in each listener has to be filled");
        }
    }

    Document getLastChangeDocument() {
        return lastChangeDocument;
    }

    void setLastChangeDocument(Document lastChangeDocument) {
        this.lastChangeDocument = lastChangeDocument;
    }

    public static class BadFilledException extends RuntimeException {
        public BadFilledException(String msg) {
            super(msg);
        }
    }
}
