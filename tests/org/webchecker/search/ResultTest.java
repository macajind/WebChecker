package org.webchecker.search;

import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link Result}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class ResultTest {

    private Result t;
    private static final String key = "KEY";
    private static final String value = "VALUE";

    @Before
    public void setup() throws Exception {
        Element e = Utils.randomElement();
        t = Result.result(e);
        t.addVariable(key, value);
    }

    @Test
    public void testSuccessfulResultCreating() throws Exception {
        assertEquals(t.varValue(key), value);
    }

    @Test
    public void testUnsuccessfulResultCreating() throws Exception {
        assertEquals(t.varValue(key + "ahs"), null);
    }
}
