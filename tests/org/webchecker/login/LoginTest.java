package org.webchecker.login;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test case for class {@link Login}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class LoginTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testUnpreparedChecking() throws Exception {
        Login l = Login.getInstance();
        if (l.prepared()) l.unprepare();
        thrown.expect(Login.NotPreparedException.class);
        l.login();
    }
}
