package org.webchecker.watcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Support class for testing of Watcher Module.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Utils {

    private static final String FILE_ENCODING = "UTF-8";

    private static File testPage;

    public static Element randomElement() {
        return Jsoup.parse("<p>text</p>");
    }

    public static Function<Document, Element> randomElementFunction() {
        return d -> d;
    }

    public static BiPredicate<Element, Element> randomChangedFunction() {
        return (e1, e2) -> true;
    }

    private static BiConsumer<Element, Element> randomActionFunction() {
        return (e1, e2) -> System.out.println();
    }

    public static ListenerGroup randomListenerGroup() {
        return ListenerGroup.newGroup(() -> Jsoup.parse(""));
    }

    public static Supplier<Document> randomDocumentToWork() {
        return () -> Jsoup.parse("<p>hello</p>");
    }

    public static Listener randomListener() {
        return Listener.listener().setChanged(Utils.randomChangedFunction()).setAction(Utils.randomActionFunction()).setSupplyElement(Utils.randomElementFunction());
    }

    private static File getTestPage() {
        if (testPage == null) {
            URL resource = Utils.class.getClassLoader().getResource("test_page.html");
            if (resource != null) {
                testPage = new File(resource.getPath());
            }
        }
        return testPage;
    }

    public static Document testTestDocument() {
        try {
            return Jsoup.parse(getTestPage(), FILE_ENCODING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateTestDocument(Document doc) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(getTestPage()))) {
            for (String s : doc.toString().split("\n")) {
                w.write(s);
                w.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
