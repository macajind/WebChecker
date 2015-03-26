package org.webchecker.search;

import com.sun.istack.internal.NotNull;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class representing one result of searching
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Result {
    /**
     * Element, which was this result got from
     */
    private final Element src;
    /**
     * Variables to add more information about this result. Use of this technology is fully up to user and definition
     * of {@link org.webchecker.search.Parasite#extractResults(org.jsoup.nodes.Document)} method
     */
    private final HashMap<String, Object> variables;

    /**
     * Simple constructor that assign given value to {@link #src} field
     * @param src The element which has been this result extracted from. If null, {@link java.lang.NullPointerException}
     *            is thrown
     */
    public Result(@NotNull Element src) {
        Objects.requireNonNull(src);
        this.src = src;
        variables = new HashMap<>();
    }

    /**
     * Factory method. In fact do the same as the {@link #Result(org.jsoup.nodes.Element)} constructor, but van be used
     * for easier syntax
     * @param src The element which has been the result extracted from
     * @return The result
     */
    public static Result result(Element src) {
        return new Result(src);
    }

    /**
     * Puts the given key and value into the {@link #variables} map
     * @param key The key
     * @param value The value
     * @return This
     */
    public Result putVariable(String key, Object value) {
        variables.put(key, value);
        return this;
    }

    /**
     * Returns a value from the {@link #variables} map, which was assigned to given key by {@link #putVariable(String, Object)},
     * or {@code null} if the key is not present in {@link #variables}
     * @param key The key
     * @return A value from the {@link #variables} map, which was assigned to given key by {@link #putVariable(String, Object)},
     *            or {@code null} if the key is not present in {@link #variables}
     */
    public Object varValue(String key) {
        return variables.get(key);
    }

    /**
     * Simple getter, that returns a value of the {@link #src} field
     * @return The value of the {@link #src} field
     */
    public Element element() {
        return src;
    }

    @Override
    public String toString() {
        return src.toString();
    }
}
