package org.webchecker.watcher;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Test case for support testing class {@link Utils}.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class UtilsTest {
    @Test
    public void testDocumentUpdate() throws Exception {
        Document d = Utils.testTestDocument();
        String firstValue = d.toString();
        d.appendText("hi!");
        Utils.updateTestDocument(d);
        String secondValue = Utils.testTestDocument().toString();
        assertFalse(firstValue.equals(secondValue));
    }
}
