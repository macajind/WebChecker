package org.webchecker.search;

import com.sun.istack.internal.NotNull;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A collection of {@link org.webchecker.search.Result} objects.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Results {
    /**
     * A list of results
     */
    private final Result[] results;

    /**
     * Construct {@link org.webchecker.search.Results} from a list of {@link org.webchecker.search.Result results}
     * @param results a list of results; can't be {@code null}
     */
    public Results(@NotNull List<Result> results) {
        this.results = results.toArray(new Result[results.size()]);
    }

    /**
     * Construct {@link org.webchecker.search.Results} from {@link org.jsoup.select.Elements}. Each element in
     * {@link org.jsoup.select.Elements} object has to represent one result
     * @param elements {@link org.jsoup.select.Elements} object. Each element has to represent one result
     */
    public Results(@NotNull Elements elements) {
        results = elements
                .stream()
                .map(Result::result)
                .toArray(Result[]::new);
    }

    /**
     * Return unmodifiable {@link #results list} of results
     * @return unmodifiable {@link #results list} of results
     */
    public List<Result> results() {
        return Collections.unmodifiableList(Arrays.asList(results));
    }
}
