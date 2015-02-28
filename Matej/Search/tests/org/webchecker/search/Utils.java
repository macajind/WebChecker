package org.webchecker.search;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
class Utils {
    public static Element randomElement() {
        return new Element(Tag.valueOf("p"), "");
    }
    public static Result randomResult() {
        return new Result(randomElement());
    }
}
