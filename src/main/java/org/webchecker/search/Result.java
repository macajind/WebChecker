package org.webchecker.search;

import com.sun.istack.internal.NotNull;
import org.jsoup.nodes.Element;

import java.util.HashMap;

/**
 * Class representing one result of searching.
 *
 * @author Matěj Kripner &lt;kripnermatej@gmail.com&gt;
 * @version 1.0
 */
public class Result {

    // Element, which was this result got from.
    private final Element src;

    /* Variables to add more information about this result. Use of this technology is fully up to user and definition
       of Parasite#extractResults(org.jsoup.nodes.Document) method. */
    private final HashMap<String, Object> variables;

    /**
     * Simple constructor that assign given value to {@link #src} field.
     *
     * @param src the getElement which has been this result extracted from
     * @throws IllegalArgumentException if given src parameter is {@code null}
     */
    public Result(@NotNull Element src) throws IllegalArgumentException {
        if (src == null) throw new IllegalArgumentException("Parameter src can't be null!");
        this.src = src;
        variables = new HashMap<>();
    }

    /**
     * Factory method. In fact do the same as the {@link #Result(org.jsoup.nodes.Element)} constructor, but van be used
     * for easier syntax.
     *
     * @param src the getElement which has been the result extracted from
     * @return the result
     */
    public static Result result(Element src) {
        return new Result(src);
    }

    /**
     * Puts the given key and value into the {@link #variables} map.
     *
     * @param key   the key
     * @param value the value
     * @return this
     */
    public Result addVariable(String key, Object value) {
        variables.put(key, value);
        return this;
    }

    /**
     * Returns a value from the inner variables map, which was assigned to given key by {@link #addVariable(String, Object)},
     * or {@code null} if the key is not present in inner variables.
     *
     * @param key the key
     * @return value from the inner variables map, which was assigned to given key by {@link #addVariable(String, Object)},
     * or {@code null} if the key is not present in inner variables.
     */
    public Object varValue(String key) {
        return variables.get(key);
    }

    /**
     * Simple getter, that returns a value of the src field.
     *
     * @return the value of the src field
     */
    public Element getElement() {
        return src;
    }

    @Override
    public String toString() {
        return src.toString();
    }
}
