package org.webchecker.watcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link Listener}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ListenerTest {

    @Test
    public void testActionFilling() throws Exception {
        BiConsumer<Element, Element> action = (e1, e2) -> System.out.println(); // random BiConsumer
        Listener l = Listener.listener().setAction(action);
        assertEquals(action, l.getAction());
    }

    @Test
    public void testChangeFilling() throws Exception {
        BiPredicate<Element, Element> changed = (e1, e2) -> true; // random BiPredicate
        Listener l = Listener.listener().setChanged(changed);
        assertEquals(changed, l.getChanged());
    }

    @Test
    public void testElementSupplierFilling() throws Exception {
        Function<Document, Element> supplyElement = d -> d; // random Function
        Listener l = Listener.listener().setSupplyElement(supplyElement);
        assertEquals(supplyElement, l.getSupplyElement());
    }

    @Test
    public void testConfigFilling() throws Exception {
        ListenerConfig c = ListenerConfig.getDefaults();
        Listener l = Listener.listener().setConfig(c);
        assertEquals(c, l.getConfig());
    }

    @Test(expected = Listener.BadFilledException.class)
    public void testBadFilledChecking() throws Exception {
        Listener.listener().setSupplyElement(Utils.randomElementFunction()).setChanged(Utils.randomChangedFunction()).register(Utils.randomListenerGroup());
    }
}
