package org.webchecker.login;

import org.jsoup.nodes.Document;
import org.webchecker.forms.Form;

/**
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public final class Login {

    private static final int MINUTES_TO_MILLISECONDS = 60000;

    /**
     * @author Jindřich Máca (Tuník)
     * @version 1.0
     */
    private class LoginThread extends Thread {

        private final Long sleepTime;

        /**
         * @param sleepTime in milliseconds
         */
        public LoginThread(Long sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {

        }
    }

    private static final Login instance = null;
    private Form loginForm = null;

    private Login() {
        //Exists only to defeat instantiation.
    }

    /**
     * @return
     */
    public static Login getInstance() {
        return instance;
    }

    /**
     * @param loginForm
     */
    public void prepare(Form loginForm) {
        this.loginForm = loginForm;
    }

    /**
     *
     */
    public void unprepare() {
        this.loginForm = null;
    }

    /**
     * Should be disabled, when automatic logging is on.
     */
    public Document login() {
        return null;
    }

    /**
     * @return
     */
    public Form getLoginForm() {
        return this.loginForm;
    }

    /**
     * @param minutes
     */
    public void startLogging(Long minutes) {
        new LoginThread(minutes * MINUTES_TO_MILLISECONDS);
    }

    public void stopLogging() {

    }
}
