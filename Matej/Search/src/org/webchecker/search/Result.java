package org.webchecker.search;

import org.jsoup.nodes.Element;

import java.util.HashMap;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Result {
    private final Element src;
    private final HashMap<String, Object> variables;

    public Result(Element src) {
        this.src = src;
        variables = new HashMap<>();
    }
    public static Result result(Element src) {
        return new Result(src);
    }

    public Result putVariable(String key, Object value) {
        variables.put(key, value);
        return this;
    }
    public Object varValue(String key) {
        return variables.get(key);
    }
    public Element element() {
        return src;
    }

    @Override
    public String toString() {
        return src.toString();
    }
}
