package org.webchecker.watcher;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link ListenerGroup}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class ListenerGroupTest {

    private final ListenerGroup g = ListenerGroup.newGroup(Utils.randomDocumentToWork());

    @Test
    public void testCreatingGroup() throws Exception {
        Assert.assertArrayEquals("There already are some listeners", g.getListeners().toArray(), new Listener[0]);
    }

    @Test
    public void testDestroyingGroup() throws Exception {
        assertEquals("ListenerGroup is created and destroyed in no time", g.isDestroyed(), false);
    }

    @Test
    public void testAddingListener() throws Exception {
        Listener l1 = Utils.randomListener();
        Listener l2 = Utils.randomListener();
        Listener l3 = Utils.randomListener();
        g.addAllListeners(l1, l2, l3);
        assertArrayEquals(g.getListeners().toArray(), new Listener[]{l1, l2, l3});
    }

    @Test(expected = ListenerGroup.AlreadyDestroyedException.class)
    public void testDestroy() throws Exception {
        g.destroy();
        g.addListener(Utils.randomListener());
    }
}
