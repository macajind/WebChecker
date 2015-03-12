package org.webchecker.login;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.webchecker.State;
import org.webchecker.forms.Form;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author Matěj Kripner
 * @author Jindřich Máca (Tuník)
 * @author Ondřej Štorc <o.storc@outlook.com>
 * @version 1.0
 */
public final class Login {

    /**
     * @author Matěj Kripner
     * @author Jindřich Máca (Tuník)
     * @author Ondřej Štorc <o.storc@outlook.com>
     * @version 1.0
     */
    private class LoginThread extends Thread {

        private final Long sleepTime;
        private final Predicate<Map<String, String>> isLogged;

        /**
         * @param sleepTime in milliseconds
         * @param isLogged
         */
        public LoginThread(Long sleepTime, Predicate<Map<String, String>> isLogged) {
            this.sleepTime = sleepTime;
            this.isLogged = isLogged;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    if (!instance.logged(isLogged)) {
                        instance.login();
                    }
                    Thread.sleep(sleepTime);
                } catch (InterruptedException | IOException e) {
                    interrupt();
                }

            }
        }
    }

    private static final Login instance = new Login();
    private Form loginForm = null;
    private LoginThread loginThread = null;

    private State state;

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
    public synchronized void prepare(Form loginForm, State state) {
        Objects.requireNonNull(loginForm);
        this.loginForm = loginForm;
        this.state = (state == null) ? new State() : state;
    }

    /**
     * Release the loaded {@link Login#loginForm}
     */
    public synchronized void unprepare() {
        checkPrepared();
        loginThread.interrupt();
        loginThread = null;
        this.loginForm = null;
    }

    /**
     * Attempts to log on if the login succeeds and returns the {@link Document} was sent {@link Form}
     * When an exception occurs, the method returns null
     * Should be disabled, when automatic logging is on.
     *
     * @return send document
     */
    public Connection.Response login() throws IOException {
        checkPrepared();
        Connection.Response response = loginForm.send(state, null);
        state.setCookies(response.cookies());
        return response;
    }
    public boolean logged(Predicate<Map<String, String>> isLogged) throws IOException {
        checkPrepared();
        return isLogged
                .test(loginForm.send((State) null, i -> false).cookies());
    }

    /**
     * Getter for {@link Login#loginForm} which is given to instance by method
     * {@link Login#prepare(org.webchecker.forms.Form, org.webchecker.State)}
     *
     * @return currently set {@link Login#loginForm}
     */
    public Form getLoginForm() {
        checkPrepared();
        return this.loginForm;
    }

    /**
     * Method is used for automatic login. Always a given time period, the method tries to log on
     *
     * @param minutes indicate for how long the method attempt to log in again
     */
    public void startLogging(Long minutes, Predicate<Map<String, String>> isLogged) {
        checkPrepared();
        loginThread = new LoginThread(TimeUnit.MINUTES.toMillis(minutes), isLogged);
        loginThread.start();
    }

    /**
     * Disable auto logging
     */
    public void stopLogging() {
        checkPrepared();
        loginThread.interrupt();
    }

    public State getCurrentState() {
        checkPrepared();
        return state;
    }

    public boolean autoLogging() {
        checkPrepared();
        return loginThread.isAlive();
    }

    public synchronized boolean isPrepared() {
        return loginForm != null;
    }
    private void checkPrepared() {
        if(!isPrepared()) throw new NotPreparedException();
    }

    class NotPreparedException extends RuntimeException {
        public NotPreparedException() {
            super("Module was not prepared, but it's method was called");
        }
    }
}
