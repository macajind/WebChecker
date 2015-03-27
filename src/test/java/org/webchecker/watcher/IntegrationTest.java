package org.webchecker.watcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for Watcher Module.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class IntegrationTest {

    private ArrayList<TestLambdaResult> results;

    @Before
    public void initialize() {
        results = new ArrayList<>();
    }

    private static class TestLambdaResult {
        int ID;
        Object value;

        public TestLambdaResult(int ID, Object value) {
            this.ID = ID;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof TestLambdaResult && ((TestLambdaResult) o).ID == ID;
        }

        public static TestLambdaResult byID(int ID) {
            return new TestLambdaResult(ID, null);
        }
    }

    @Test
    public void testSimpleListening() throws Exception {
        ListenerGroup g = ListenerGroup.newGroup(Utils::testTestDocument);
        // id to recognize lambda result
        int ID = 0;
        Function<Document, Element> extractElement = d -> d.select("p").first();
        // new listener
        Listener.listener().setChanged((e1, e2) -> !e1.text().equals(e2.text())).setSupplyElement(extractElement).setAction((e1, e2) -> results.add(new TestLambdaResult(ID, e2))).register(g);
        // change doc
        Document d = Utils.testTestDocument();
        extractElement.apply(d).appendText("hi!");
        Utils.updateTestDocument(d);
        // try to apply listening
        g.refresh();
        g.check();

        checkResult(ID);
    }

    @Test
    public void testAutoRefreshing() throws Exception {
        results.clear();
        ListenerGroup g = ListenerGroup.newGroup(Utils::testTestDocument);
        // id to recognize lambda result
        int ID = 1;
        Function<Document, Element> extractElement = d -> d.select("p").first();
        // new listener
        Listener.listener().setChanged((e1, e2) -> !e1.text().equals(e2.text())).setSupplyElement(extractElement).setAction((e1, e2) -> results.add(new TestLambdaResult(ID, e2))).setConfig(ListenerConfig.getDefaults().autoCheckingOn(60)).register(g);
        for (int i = 0; i < 3; i++) {
            // change doc
            Document d = Utils.testTestDocument();
            extractElement.apply(d).appendText("hi!");
            Utils.updateTestDocument(d);

            Thread.sleep(200); // this is not correct solution, but it works
            assertEquals(results.size(), 1);
            checkResult(ID);
        }
    }

    private void checkResult(int ID) {
        assertTrue(results.contains(TestLambdaResult.byID(ID)));
        results.clear();
    }

    @AfterClass
    public static void clean() throws Exception {
        Function<Document, Element> extractElement = d -> d.select("p").first();
        Document d = Utils.testTestDocument();
        extractElement.apply(d).html("first <a href=\"https://www.google.cz/search?q=paragraph\">paragraph</a>");
        Utils.updateTestDocument(d);
    }
}
