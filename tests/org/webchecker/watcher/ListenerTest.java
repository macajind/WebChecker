package org.webchecker.watcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ListenerTest {

    @Test
    public void testActionFilling() throws Exception {
        BiConsumer<Element, Element> action = (e1, e2) -> System.out.println(); // random BiConsumer
        Listener l = Listener
                .listener()
                .action(action);
        assertEquals(action, l.action());
    }

    @Test
    public void testChangeFilling() throws Exception {
        BiPredicate<Element, Element> changed = (e1, e2) -> true; // random BiPredicate
        Listener l = Listener
                .listener()
                .changed(changed);
        assertEquals(changed, l.changed());
    }

    @Test
    public void testElementSupplierFilling() throws Exception {
        Function<Document, Element> supplyElement = d -> d; // random Function
        Listener l = Listener
                .listener()
                .element(supplyElement);
        assertEquals(supplyElement, l.element());
    }

    @Test
    public void testConfigFilling() throws Exception {
        ListenerConfig c = ListenerConfig.defaults();
        Listener l = Listener
                .listener()
                .config(c);
        assertEquals(c, l.config());
    }

    @Test(expected = Listener.BadFilledException.class)
    public void testBadFilledChecking() throws Exception {
        Listener
                .listener()
                .element(Utils.randomElementFunction())
                .changed(Utils.randomChangedFunction())
                .register(Utils.randomListenerGroup());
    }
}