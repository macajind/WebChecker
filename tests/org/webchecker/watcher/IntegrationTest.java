package org.webchecker.watcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Function;

/**
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
            if(!(o instanceof TestLambdaResult)) return false;
            return ((TestLambdaResult) o).ID == ID;
        }
        public static TestLambdaResult byID(int ID) {
            return new TestLambdaResult(ID, null);
        }
    }

    @Test
    public void testSimpleListening() throws Exception {
        System.out.println(Utils.testFileDocument());
        ListenerGroup g = ListenerGroup.newGroup(Utils::testFileDocument);
        int ID = 0;
        Function<Document, Element> extractElement = d -> d.select("p").first();
        Listener
                .listener()
                .changed((e1, e2) -> !e1.text().equals(e2.text()))
                .element(extractElement)
                .action((e1, e2) -> results.add(new TestLambdaResult(ID, e2)))
                .register(g);
        extractElement
                .apply(Utils.testFileDocument())
                .appendText("ha");
        assertTrue(results.contains(TestLambdaResult.byID(ID)));
    }
}
