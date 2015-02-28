package org.webchecker.search;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ResultsTest {
    @Test
    public void testResultsCreatingFromList() throws Exception {
        List<Result> results = new ArrayList<>();
        Result r1 = Utils.randomResult();
        Result r2 = Utils.randomResult();
        Result r3 = Utils.randomResult();
        results.add(r1);
        results.add(r2);
        results.add(r3);

        Results r = new Results(results);
        assertArrayEquals(results.toArray(), r.results().toArray());
    }

    @Test
    public void testResultsCreatingFromElements() throws Exception {
        List<Element> elements = new ArrayList<>();
        Element e1 = Utils.randomElement();
        Element e2 = Utils.randomElement();
        Element e3 = Utils.randomElement();
        elements.add(e1);
        elements.add(e2);
        elements.add(e3);

        Elements e = new Elements(elements);
        Results r = new Results(e);

        Element[] rsElements = r.results()
                .stream()
                .map(Result::element)
                .toArray(Element[]::new);
        assertArrayEquals(rsElements, elements.toArray());
    }
}