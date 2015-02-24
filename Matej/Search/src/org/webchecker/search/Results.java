package org.webchecker.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class Results {
    private final Result[] results;

    public Results(List<Result> results) {
        this.results = results.toArray(new Result[results.size()]);
    }

    public List<Result> results() {
        return Collections.unmodifiableList(Arrays.asList(results));
    }
}
