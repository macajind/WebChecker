package org.webchecker.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integration test for Search Module.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class IntegrationTest {

    @Test
    public void testSimpleSearching() throws Exception {
        Parasite p = new Parasite() {
            @Override
            public String url(String query) {
                return null;
            }

            @Override
            public Results extractResults(Document doc) {
                return new Results(doc.select("body li.result"));
            }
        };

        final String text1 = "Hey, result one!";
        final String text2 = "What?! Hi result two!";
        final String text3 = "Amazing, lets do this!";

        Document doc = Jsoup.parse("<body>" +
                "<ul>" +
                "<li class=\"result\">" + text1 + "</li>" +
                "<li class=\"result\">" + text2 + "</li>" +
                "<li class=\"result\">" + text3 + "</li>" +
                "</ul>" +
                "</body>");
        Results r = p.extractResults(doc);
        List<Result> results = r.getResults();

        assertEquals(results.size(), 3);
        assertEquals(results.get(0).getElement().text(), text1);
        assertEquals(results.get(1).getElement().text(), text2);
        assertEquals(results.get(2).getElement().text(), text3);
    }
}
