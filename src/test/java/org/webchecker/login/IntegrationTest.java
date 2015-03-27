package org.webchecker.login;

import org.junit.Test;
import org.webchecker.State;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * Integration test for logging with Login module represented by class {@link Login}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class IntegrationTest {

    @Test
    public void testSimpleLogin() throws Exception {
        State s = new State();
        Map<String, String> randomMap = new HashMap<>();
        FormMock form = FormMock.getSimpleFormMock();
        form.setNextResponseCookies(randomMap);
        Login login = Login.getInstance();
        login.prepare(form, s);
        login.login();

        assertEquals(login.logged(m -> m == randomMap), true);
    }

    @Test
    public void testAutoLogging() throws Exception {
        State s = new State();
        FormMock form = FormMock.getSimpleFormMock();
        Login l = Login.getInstance();
        l.prepare(form, s);

        Map<String, String> loggedCookies = new HashMap<>();
        Map<String, String> randomCookies = new HashMap<>();
        form.setNextResponseCookies(randomCookies);
        Predicate<Map<String, String>> isLogged = m -> m == loggedCookies;
        l.startLogging(1L, isLogged);
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        assertTrue(l.autoLogging()); // auto logger should not stop, while we are not logged

        form.setNextResponseCookies(loggedCookies);
        form.resetSendCount(); // yes, should be synchronized
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        assertFalse(form.getSendCount() == 1); // auto logger should stop, when we are successfully logged
    }
}
