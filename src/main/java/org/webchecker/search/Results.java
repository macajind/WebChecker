package org.webchecker.search;

import com.sun.istack.internal.NotNull;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A collection of {@link org.webchecker.search.Result} objects.
 *
 * @author Matěj Kripner &lt;kripnermatej@gmail.com&gt;
 * @version 1.0
 */
public class Results {

    // A list of getResults.
    private final Result[] results;

    /**
     * Construct {@link org.webchecker.search.Results} from a list of results.
     *
     * @param results a list of results; shouldn't be {@code null}
     */
    public Results(@NotNull List<Result> results) {
        this.results = results != null ? results.toArray(new Result[results.size()]) : new Result[]{};
    }

    /**
     * Construct {@link org.webchecker.search.Results} from {@link org.jsoup.select.Elements}. Each element in
     * {@link org.jsoup.select.Elements} object has to represent one result.
     *
     * @param elements {@link org.jsoup.select.Elements} object; each element has to represent one result
     */
    public Results(@NotNull Elements elements) {
        this.results = elements != null ? elements.stream().map(Result::result).toArray(Result[]::new) : new Result[]{};
    }

    /**
     * Return unmodifiable list of results.
     *
     * @return unmodifiable list of results
     */
    public List<Result> getResults() {
        return Collections.unmodifiableList(Arrays.asList(results));
    }
}
