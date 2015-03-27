package org.webchecker.login;

import com.sun.istack.internal.NotNull;
import org.jsoup.Connection;
import org.webchecker.State;
import org.webchecker.forms.Form;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Module for logging to a website. Also provide automatic checking for logged and auto logging.
 *
 * @author Matěj Kripner
 * @author Jindřich Máca (Tuník)
 * @author Ondřej Štorc &lt;o.storc@outlook.com&gt;
 * @version 1.0
 */
public final class Login {

    /**
     * Class used for auto checking for logged and auto logging.
     *
     * @author Matěj Kripner
     * @author Jindřich Máca (Tuník)
     * @author Ondřej Štorc &lt;o.storc@outlook.com&gt;
     * @version 1.0
     */
    private static class LoginThread extends Thread {

        // Min value of sleep time.
        public static final Long MIN_SLEEP_TIME = 5L;

        // A time, this thread should wait between two auto checks for.
        private final Long sleepTime;

        // A function, that should determine if we are logged depending on response's cookies.
        private final Predicate<Map<String, String>> isLogged;

        /**
         * Simple constructor for initializing the thread.
         *
         * @param sleepTime {@link #sleepTime} in milliseconds. Value has to be greater than or equal to minimal sleep time
         * @param isLogged  function to see if user is logged in, has to be not {@code null}
         * @throws IllegalArgumentException if the given sleep time is lesser than the minimal sleep time
         *                                  or given isLogged argument is {@code null}
         */
        public LoginThread(Long sleepTime, @NotNull Predicate<Map<String, String>> isLogged) throws IllegalArgumentException {
            if (sleepTime < MIN_SLEEP_TIME)
                throw new IllegalArgumentException("Sleep time has to be greater than " + MIN_SLEEP_TIME + ", but was " + sleepTime);
            if (isLogged == null) throw new IllegalArgumentException("\"isLogged\" parameter cannot be null");
            this.isLogged = isLogged;
            this.sleepTime = sleepTime;
        }

        /**
         * This method will auto check, if we are logged, with given sleep time.
         * <p>
         * {@inheritDoc}
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

    // An instance of Login singleton
    private static final Login instance = new Login();

    // A form with parameters required to log in (filled with data and ready for send)
    private Form loginForm = null;

    // Thread providing auto logging (auto checking)
    private LoginThread loginThread = null;

    // Universal state object containing information about current state (cookies etc.)
    private State state;

    private Login() {
        //Exists only to defeat instantiation.
    }

    /**
     * Getter for singleton instance - return an instance of Login singleton.
     *
     * @return instance of {@link Login} singleton
     */
    public static Login getInstance() {
        return instance;
    }

    /**
     * Prepare the instance of Login class to use. If the instance is already prepared, do nothing.
     *
     * @param loginForm filled with data and ready for send, has to be not-null
     * @param state     object containing information about current state (cookies, etc.). If {@code null}, blank state is used
     * @throws IllegalArgumentException if loginForm argument is null
     */
    public synchronized void prepare(@NotNull Form loginForm, State state) throws IllegalArgumentException {
        if (prepared()) return; // if already prepared, do nothing
        if (loginForm == null) throw new IllegalArgumentException("Login form can't be null!");
        this.loginForm = loginForm;
        this.state = (state == null) ? new State() : state;
    }

    /**
     * Unprepare the instance of Login class.
     *
     * @throws NotPreparedException if the instance is not prepared
     */
    public synchronized void unprepare() throws NotPreparedException {
        checkPrepared();
        stopLogging();
        loginForm = null;
    }

    /**
     * Attempts to log in (in fact, send the login form) and update the state of object.
     *
     * @return response for sent login form
     * @throws NotPreparedException if the instance is not prepared
     * @throws IOException          if sending of the form fails
     */
    public Connection.Response login() throws NotPreparedException, IOException {
        checkPrepared();
        Connection.Response response = loginForm.send(null);
        state.setCookies(response.cookies());
        return response;
    }

    /**
     * Test, if we are logged (the state object is in logged state).
     *
     * @param isLogged function has to determine, if we are logged, depending on response's cookies
     * @return true if we are logged, false otherwise
     * @throws IOException          if something get wrong with connection and/or response
     * @throws NotPreparedException if the instance is not prepared
     */
    public boolean logged(Predicate<Map<String, String>> isLogged) throws IOException, NotPreparedException {
        checkPrepared();
        return isLogged.test(loginForm.send(i -> false).cookies());
    }

    /**
     * Getter for login form which is given to instance by method {@link Login#prepare(org.webchecker.forms.Form, org.webchecker.State)}.
     *
     * @return currently set login form
     * @throws NotPreparedException if the instance is not prepared
     */
    public Form getLoginForm() throws NotPreparedException {
        checkPrepared();
        return this.loginForm;
    }

    /**
     * Method is used for starting automatic login. Always a given time period, the method tries to log on.
     *
     * @param seconds  indicate for how long the method attempt to log in again in seconds
     * @param isLogged {@link Predicate} if user is logged
     * @throws NotPreparedException if the instance is not prepared
     */
    public void startLogging(Long seconds, Predicate<Map<String, String>> isLogged) throws NotPreparedException {
        checkPrepared();
        loginThread = new LoginThread(TimeUnit.SECONDS.toMillis(seconds), isLogged);
        loginThread.start();
    }

    /**
     * Disable auto logging.
     *
     * @throws NotPreparedException if the instance is not prepared
     */
    public void stopLogging() throws NotPreparedException {
        checkPrepared();
        loginThread.interrupt();
        loginThread = null;
    }

    /**
     * Return current state.
     *
     * @return current state
     * @throws NotPreparedException if the instance is not prepared
     */
    public State getCurrentState() throws NotPreparedException {
        checkPrepared();
        return state;
    }

    /**
     * Test, if the auto logging is on.
     *
     * @return the auto logging is on
     * @throws NotPreparedException if the instance is not prepared
     */
    public boolean autoLogging() throws NotPreparedException {
        checkPrepared();
        return loginThread.isAlive();
    }

    /**
     * Determine, if the login instance is prepared for use.
     *
     * @return {code true}, if the Login module is prepared, {@code false} otherwise
     */
    public synchronized boolean prepared() {
        return loginForm != null;
    }

    /**
     * Check, that the login instance is prepared for use.
     *
     * @throws NotPreparedException if it is not prepared
     */
    private void checkPrepared() throws NotPreparedException {
        if (!prepared()) throw new NotPreparedException();
    }

    /**
     * Exception, that should be thrown when a module was not prepared, but it's method was called.
     */
    public class NotPreparedException extends RuntimeException {
        public NotPreparedException() {
            super("Module was not prepared, but it's method was called");
        }
    }
}
