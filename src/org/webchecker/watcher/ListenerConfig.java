package org.webchecker.watcher;

import java.util.function.BiConsumer;

/**
 * Class represents a configuration of a listener. It contains only one parameter yet, but can be
 * easily extend. There is no rule of number of listeners to one config, but one listener have to have
 * exact one config.
 *
 * @author Matěj Kripner
 * @version 1.0
 * @see #autoChecking
 */
public class ListenerConfig {

    /**
     * Min value of {@link #autoChecking}. Has to be positive.
     */
    public static final int MIN_AUTO_CHECKING = 25;
    /**
     * Represents the preferred delay between two listening of the listener. If the value is 0,
     * listening should not be automatic and auto checking is off.
     *
     * @see #onAutoCheckingChange
     * @see #autoChecking()
     * @see #autoCheckingOn()
     * @see #autoCheckingOff()
     * @see #autoCheckingOn(int)
     */
    private int autoChecking;
    /**
     * This function will be called when change of {@link #autoChecking} parameter occurs.
     */
    private BiConsumer<Integer, Integer> onAutoCheckingChange;

    /**
     * Constructor that sets this config to the default state
     *
     * @see #setToDefault()
     */
    private ListenerConfig() {
        setToDefault();
    }

    /**
     * Sets this config to the default state.
     */
    private void setToDefault() {
        autoChecking = 0;
    }

    /**
     * Returns new config in the default state
     *
     * @return New config in the default state
     */
    public static ListenerConfig defaults() {
        return new ListenerConfig();
    }

    /**
     * Turn auto checking on with the given delay. The given delay has to be valid. In fact, delay is valid when:<br />
     * {@code
     * delay >= {@link #MIN_AUTO_CHECKING}
     * }
     *
     * @param delay The new value of delay between two listening of a listener
     * @return This
     * @see #validateDelay(int)
     */
    public ListenerConfig autoCheckingOn(int delay) {
        validateDelay(delay);
        autoChecking = delay;
        runOnAutoCheckingChange(0, delay);
        return this;
    }

    /**
     * Checks that the given delay is valid. In fact, delay is valid when:<br />
     * {@code
     * delay >= {@link #MIN_AUTO_CHECKING}
     * }
     *
     * @param delay The value to check
     */
    private void validateDelay(int delay) {
        if (delay < MIN_AUTO_CHECKING)
            throw new IllegalArgumentException("delay can not be less than " + MIN_AUTO_CHECKING + ", but was " + delay);
    }

    /**
     * Turn the auto checking off
     *
     * @return This
     */
    public ListenerConfig autoCheckingOff() {
        int tmp = autoChecking;
        autoChecking = 0;
        runOnAutoCheckingChange(tmp, 0);
        return this;
    }

    /**
     * Is not null, run {@link #onAutoCheckingChange}
     *
     * @param oldValue Old value of {@link #autoChecking}
     * @param newValue New value of {@link #autoChecking}
     */
    private void runOnAutoCheckingChange(int oldValue, int newValue) {
        if (onAutoCheckingChange != null) onAutoCheckingChange.accept(oldValue, newValue);
    }

    /**
     * Simple return value of {@link #autoChecking} attribute. If auto checking is off, return zero
     *
     * @return Value of {@link #autoChecking} attribute
     */
    public int autoChecking() {
        return autoChecking;
    }

    /**
     * Tests that autoChecking is on
     *
     * @return Auto checking is on
     */
    public boolean autoCheckingOn() {
        return autoChecking() != 0;
    }

    /**
     * Simple sets the value of {@link #onAutoCheckingChange} attribute to given value
     *
     * @param onAutoCheckingChange New value of {@link #onAutoCheckingChange} attribute
     */
    public void setOnAutoCheckingChange(BiConsumer<Integer, Integer> onAutoCheckingChange) {
        this.onAutoCheckingChange = onAutoCheckingChange;
    }
}
