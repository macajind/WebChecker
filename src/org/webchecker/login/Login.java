package org.webchecker.login;

import org.jsoup.nodes.Document;
import org.webchecker.forms.Form;

import java.io.IOException;

/**
 * @author Jindřich Máca (Tuník)
 * @author Ondřej Štorc <o.storc@outlook.com>
 * @version 1.0
 */
public final class Login {

    private static final int MINUTES_TO_MILLISECONDS = 60000;

    /**
     * @author Jindřich Máca (Tuník)
     * @author Ondřej Štorc <o.storc@outlook.com>
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
            while (autoLogin){
                instance.login();
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final Login instance = new Login();
    private Form loginForm = null;
    private LoginThread loginThread = null;
    private boolean autoLogin = false;

    private Login() {
        //Exists only to defeat instantiation.
    }

    /**
     * Getter for {@link Login#instance}
     *
     * @return instance of {@link Login}
     */
    public static Login getInstance() {
        return instance;
    }

    /**
     * Prepare instance of Login class to use
     *
     * @param loginForm filled with data and ready for send
     */
    public void prepare(Form loginForm) {
        this.loginForm = loginForm;
    }

    /**
     * Release the loaded {@link Login#loginForm}
     */
    public void unprepare() {
        this.loginForm = null;
    }

    /**
     * Attempts to log on if the login succeeds and returns the {@link Document} was sent {@link Form}
     * When an exception occurs, the method returns null
     * Should be disabled, when automatic logging is on.
     *
     * @return send document
     */
    public Document login() {
        try {
            return loginForm.send();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Getter for {@link Login#loginForm} which is given to instance by method {@link Login#prepare(Form)}
     *
     * @return currently set {@link Login#loginForm}
     */
    public Form getLoginForm() {
        return this.loginForm;
    }

    /**
     * Method is used for automatic login. Always a given time period, the method tries to log on
     *
     * @param minutes indicate for how long the method attempt to log in again
     */
    public void startLogging(Long minutes) {
        loginThread = new LoginThread(minutes * MINUTES_TO_MILLISECONDS);
        autoLogin = true;
        loginThread.start();
    }

    /**
     * Disable auto logging
     */
    public void stopLogging() {
        autoLogin = false;
    }
}
