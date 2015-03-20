package org.webchecker.login;

import com.sun.istack.internal.NotNull;
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
 * Module for logging to a website. Also provide automatic checking for logged and auto logging
 *
 * @author Matěj Kripner
 * @author Jindřich Máca (Tuník)
 * @author Ondřej Štorc <o.storc@outlook.com>
 * @version 1.0
 */
public final class Login {

    /**
     * Class used for auto checking for logged and auto logging
     *
     * @author Matěj Kripner
     * @author Jindřich Máca (Tuník)
     * @author Ondřej Štorc <o.storc@outlook.com>
     * @version 1.0
     */
    private static class LoginThread extends Thread {

        /**
         * Min value of {@link #sleepTime}
         *
         * @see #sleepTime
         */
        public static final Long MIN_SLEEP_TIME = 5L;

        /**
         * A time, this thread should wait between two auto checks for
         */
        private final Long sleepTime;
        /**
         * A function, that should determine if we are logged depending on response's cookies
         */
        private final Predicate<Map<String, String>> isLogged;

        /**
         * Simple constructor for initializing the thread
         *
         * @param sleepTime {@link #sleepTime} in milliseconds. Value has to be greater than or equal to {@link #MIN_SLEEP_TIME}
         * @param isLogged function, see {@link #isLogged}, has to be not-null
         * @throws java.lang.IllegalArgumentException if the given sleep time is lesser than the {@link #MIN_SLEEP_TIME}
         * or given isLogged argument is {@code null}
         */
        public LoginThread(Long sleepTime, @NotNull Predicate<Map<String, String>> isLogged) {
            if(sleepTime < MIN_SLEEP_TIME)
                throw new IllegalArgumentException("Sleep time has to be greater than " + MIN_SLEEP_TIME + ", but was " + sleepTime);
            if(isLogged == null)
                throw new IllegalArgumentException("\"isLogged\" parameter cannot be null");
            this.sleepTime = sleepTime;
            this.isLogged = isLogged;
        }

        /**
         * This method will auto check, if we are logged, with given {@link #sleepTime}
         */
        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    if (!instance.logged(isLogged)) { // not logged
                        instance.login();
                    }
                    Thread.sleep(sleepTime);
                } catch (InterruptedException | IOException e) {
                    interrupt();
                }

            }
        }
    }

    /**
     * An instance of Login singleton
     */
    private static final Login instance = new Login();
    /**
     * A form with parameters required to log in (filled with data and ready for send)
     */
    private Form loginForm = null;
    /**
     * Thread providing auto logging (auto checking)
     */
    private LoginThread loginThread = null;

    /**
     * Universal state object containing information about current state (cookies etc.)
     */
    private State state;

    private Login() {
        //Exists only to defeat instantiation.
    }

    /**
     * Getter for {@link #instance} - return an instance of Login singleton
     *
     * @return instance of {@link Login} singleton
     */
    public static Login getInstance() {
        return instance;
    }

    /**
     * Prepare the instance of Login class to use. If the instance is already prepared, do nothing
     *
     * @param loginForm filled with data and ready for send, has to be not-null
     * @param state object containing information about current state (cookies, etc.). If {@code null}, blank state is used
     * @throws java.lang.NullPointerException if loginForm argument is null
     */
    public synchronized void prepare(Form loginForm, State state) {
        if(prepared()) return; // if already prepared, do nothing
        Objects.requireNonNull(loginForm);
        this.loginForm = loginForm;
        this.state = (state == null) ? new State() : state;
    }

    /**
     * Unprepare the instance of Login class
     *
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public synchronized void unprepare() {
        checkPrepared();
        stopLogging();
        loginForm = null;
    }

    /**
     *  Attempts to log in (in fact, send the login form) and update the {@link #state} object
     *
     * @return response for sent login form
     *
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public Connection.Response login() throws IOException {
        checkPrepared();
        Connection.Response response = loginForm.send(state, null);
        state.setCookies(response.cookies());
        return response;
    }

    /**
     * Test, if we are logged (the {@link #state} object is in logged state)
     *
     * @param isLogged function has to determine, if we are logged, depending on response's cookies
     * @return true if we are logged, false otherwise
     * @throws IOException if something get wrong with connection and/or response
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
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
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public Form getLoginForm() {
        checkPrepared();
        return this.loginForm;
    }

    /**
     * Method is used for starting automatic login. Always a given time period, the method tries to log on
     *
     * @param seconds indicate for how long the method attempt to log in again in seconds
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public void startLogging(Long seconds, Predicate<Map<String, String>> isLogged) {
        checkPrepared();
        loginThread = new LoginThread(TimeUnit.SECONDS.toMillis(seconds), isLogged);
        loginThread.start();
    }

    /**
     * Disable auto logging
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public void stopLogging() {
        checkPrepared();
        loginThread.interrupt();
        loginThread = null;
    }

    /**
     * Return current {@link #state}
     * @return current {@link #state}
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public State getCurrentState() {
        checkPrepared();
        return state;
    }

    /**
     * Test, if the auto logging is on
     * @return the auto logging is on
     * @throws org.webchecker.login.Login.NotPreparedException if the instance is not prepared
     */
    public boolean autoLogging() {
        checkPrepared();
        return loginThread.isAlive();
    }

    /**
     * Determine, if the login instance is prepared for use
     * @return True, if the Login module is prepared, false otherwise
     */
    public synchronized boolean prepared() {
        return loginForm != null;
    }

    /**
     * Check, that the login instance is prepared for use. If not, throw a {@link org.webchecker.login.Login.NotPreparedException}
     */
    private void checkPrepared() {
        if(!prepared()) throw new NotPreparedException();
    }

    /**
     * Exception, that should be thrown when a module was not prepared, but it's method was called
     */
    class NotPreparedException extends RuntimeException {
        public NotPreparedException() {
            super("Module was not prepared, but it's method was called");
        }
    }
}
