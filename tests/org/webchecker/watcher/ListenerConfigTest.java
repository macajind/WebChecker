package org.webchecker.watcher;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link ListenerConfig}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ListenerConfigTest {

    private static final int BAD_AUTO_CHECKING_VALUE = ListenerConfig.MIN_AUTO_CHECKING - 1;
    private static final int GOOD_AUTO_CHECKING_VALUE = ListenerConfig.MIN_AUTO_CHECKING + 1;

    @Test
    public void testAutoCheckingOn() throws Exception {
        ListenerConfig c = ListenerConfig.defaults();
        int value = GOOD_AUTO_CHECKING_VALUE;
        c.autoCheckingOn(value);
        assertEquals("c.isAutoCheckingOn() does not work", c.isAutoCheckingOn(), true);
        assertEquals("c.getAutoChecking() does not work", c.getAutoChecking(), value);
    }

    @Test
    public void testAutoCheckingOff() throws Exception {
        ListenerConfig c = ListenerConfig.defaults();
        c.autoCheckingOff();
        assertEquals("c.isAutoCheckingOn() does not work", c.isAutoCheckingOn(), false);
        assertEquals("c.getAutoChecking() does not work", c.getAutoChecking(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAutoCheckingValidating() throws Exception {
        ListenerConfig c = ListenerConfig.defaults();
        c.autoCheckingOn(BAD_AUTO_CHECKING_VALUE);
    }

    @Test
    public void testOnAutoCheckingCalling() throws Exception {
        ListenerConfig c = ListenerConfig.defaults();
        LinkedList<Integer> forLambda = new LinkedList<>();
        c.setOnAutoCheckingChange((oldValue, newValue) -> {
            forLambda.add(oldValue);
            forLambda.add(newValue);
        });
        int oldValue = c.getAutoChecking();
        int newValue = GOOD_AUTO_CHECKING_VALUE;
        c.autoCheckingOn(newValue);

        assertEquals(forLambda.getFirst().intValue(), oldValue); // intValue() because of collision with assertEquals(Object, Object)
        assertEquals(forLambda.get(1).intValue(), newValue);
    }
}
