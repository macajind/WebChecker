package org.webchecker.search;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ResultTest {
    @Test
    public void testResultCreating() throws Exception {
        Element e = Utils.randomElement();
        Result t = Result.result(e);

        final String key = "KEY";
        final String value = "VALUE";
        t.putVariable(key, value);

        assertEquals(t.varValue(key), value);
        assertEquals(t.varValue(key + "ahs"), null);
    }
}