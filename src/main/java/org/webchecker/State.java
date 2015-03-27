package org.webchecker;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing state of the login process depending on cookies.
 *
 * @author Matěj Kripner &lt;kripnermatej@gmail.com&gt;
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class State {

    private Map<String, String> cookies;

    /**
     * Constructor of {@link State}.
     *
     * @param cookies cookies representing this {@link State}
     */
    public State(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    /**
     * Constructor of {@link State}.
     */
    public State() {
        this(new HashMap<>());
    }

    /**
     * Getter for cookies of this {@link State}.
     *
     * @return {@link Map} of cookies representing this {@link State}
     */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * Setter for cookies of this {@link State}.
     *
     * @param cookies sets {@link Map} representation of cookies to this {@link State}
     */
    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
