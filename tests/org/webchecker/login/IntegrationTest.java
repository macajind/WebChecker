package org.webchecker.login;

import org.junit.Test;
import org.webchecker.State;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
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
}
