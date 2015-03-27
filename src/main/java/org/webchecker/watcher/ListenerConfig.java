package org.webchecker.watcher;

import java.util.function.BiConsumer;

/**
 * Class represents a configuration of a listener. It contains only one parameter yet, but can be
 * easily extend. There is no rule of number of listeners to one config, but one listener have to have
 * exact one config.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 * @see Listener
 */
public class ListenerConfig {

    /**
     * Min value of auto checking delay. Has to be positive.
     */
    public static final int MIN_AUTO_CHECKING = 25;

    /* Represents the preferred delay between two listening of the listener. If the value is 0,
       listening should not be automatic and auto checking is off. */
    private int autoChecking;

    // This function will be called when change of #autoChecking parameter occurs.
    private BiConsumer<Integer, Integer> onAutoCheckingChange;

    // Constructor that sets this config to the default state.
    private ListenerConfig() {
        setToDefault();
    }

    // Sets this config to the default state.
    private void setToDefault() {
        autoChecking = 0;
    }

    /**
     * Returns new config in the default state.
     *
     * @return new config in the default state
     */
    public static ListenerConfig getDefaults() {
        return new ListenerConfig();
    }

    /**
     * Turn auto checking on with the given delay.
     * <p>
     * The given delay has to be valid. In fact, delay is valid when:<br />
     * {@code
     * delay >= {@link #MIN_AUTO_CHECKING}
     * }
     *
     * @param delay the new value of delay between two listening of a listener
     * @return this
     * @throws IllegalArgumentException if validation of the given delay fails
     * @see #validateDelay(int)
     */
    public ListenerConfig autoCheckingOn(int delay) throws IllegalArgumentException {
        validateDelay(delay);
        autoChecking = delay;
        runOnAutoCheckingChange(0, delay);
        return this;
    }

    /**
     * Checks that the given delay is valid.
     * <p>
     * In fact, delay is valid when:<br />
     * {@code
     * delay >= {@link #MIN_AUTO_CHECKING}
     * }
     *
     * @param delay the value to check
     * @throws IllegalArgumentException if value of the given delay is not valid
     */
    private void validateDelay(int delay) throws IllegalArgumentException {
        if (delay < MIN_AUTO_CHECKING)
            throw new IllegalArgumentException("delay can not be less than " + MIN_AUTO_CHECKING + ", but was " + delay);
    }

    /**
     * Turn the auto checking off.
     *
     * @return this
     */
    public ListenerConfig autoCheckingOff() {
        int tmp = autoChecking;
        autoChecking = 0;
        runOnAutoCheckingChange(tmp, 0);
        return this;
    }

    /**
     * Is not {@code null}, run auto checking change function.
     *
     * @param oldValue old value of auto checking
     * @param newValue new value of auto checking
     */
    private void runOnAutoCheckingChange(int oldValue, int newValue) {
        if (onAutoCheckingChange != null) onAutoCheckingChange.accept(oldValue, newValue);
    }

    /**
     * Simply return value of auto checking attribute. If auto checking is off, return zero.
     *
     * @return value of auto checking attribute
     */
    public int getAutoChecking() {
        return autoChecking;
    }

    /**
     * Tests that auto checking is on.
     *
     * @return auto checking is on
     */
    public boolean isAutoCheckingOn() {
        return getAutoChecking() != 0;
    }

    /**
     * Simple sets the value of auto checking change function attribute to given value.
     *
     * @param onAutoCheckingChange new value of auto checking change function attribute
     */
    public void setOnAutoCheckingChange(BiConsumer<Integer, Integer> onAutoCheckingChange) {
        this.onAutoCheckingChange = onAutoCheckingChange;
    }
}
