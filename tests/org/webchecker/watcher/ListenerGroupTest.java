package org.webchecker.watcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ListenerGroupTest {


    @Test
    public void testCreatingGroup() throws Exception {
        ListenerGroup g = ListenerGroup.newGroup(Utils.randomDocumentToWork());
        assertArrayEquals("There already are some listeners", g.listeners().toArray(), new Listener[0]);
        assertEquals("ListenerGroup is created and destroyed in no time", g.isDestroyed(), false);
    }

    @Test
    public void testAddingListener() throws Exception {
        ListenerGroup g = ListenerGroup.newGroup(Utils.randomDocumentToWork());
        Listener l1 = Utils.randomListener();
        Listener l2 = Utils.randomListener();
        Listener l3 = Utils.randomListener();
        g.addAllListeners(l1, l2, l3);
        assertArrayEquals(g.listeners().toArray(), new Listener[] {l1, l2, l3});
    }

    @Test(expected = ListenerGroup.AlreadyDestroyedException.class)
    public void testDestroy() throws Exception {
        ListenerGroup g = ListenerGroup.newGroup(Utils.randomDocumentToWork());
        g.destroy();
        g.addListener(Utils.randomListener());
    }
}