package org.webchecker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class State {
    private Map<String, String> cookies;

    public State(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public State() {
        this(new HashMap<>());
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}

