package org.webchecker.watcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Utils {
    public static final File TEST_PAGE = new File("/resource/test_page.html");
    public static final String FILE_ENCODING = "UTF-8";

    public static Element randomElement() {
        return Jsoup.parse("<p>text</p>");
    }
    public static Function<Document, Element> randomElementFunction() {
        return d -> d;
    }
    public static BiPredicate<Element, Element> randomChangedFunction() {
        return (e1, e2) -> true;
    }
    public static BiConsumer<Element, Element> randomActionFunction() {
        return (e1, e2) -> System.out.println();
    }
    public static ListenerGroup randomListenerGroup() {
        return ListenerGroup.newGroup(() -> Jsoup.parse(""));
    }
    public static Supplier<Document> randomDocumentToWork() {
        return () -> Jsoup.parse("<p>hello</p>");
    }
    public static Listener randomListener() {
        return Listener
                .listener()
                .changed(Utils.randomChangedFunction())
                .action(Utils.randomActionFunction())
                .element(Utils.randomElementFunction());
    }
    public static Document testFileDocument() {
        try {
            return Jsoup.parse(TEST_PAGE, FILE_ENCODING);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
