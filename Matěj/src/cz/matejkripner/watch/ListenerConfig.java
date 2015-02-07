package cz.matejkripner.watch;

import java.util.function.BiConsumer;

/**
 * @author Matěj Kripner
 * @version 1.0
 */
public class ListenerConfig {
    private int autoChecking;

    private BiConsumer<Integer, Integer> onAutoCheckingChange;

    private ListenerConfig() {
        setToDefault();
    }

    private void setToDefault() {
        autoChecking = 0;
    }
    public static ListenerConfig defaults() {
        return new ListenerConfig();
    }
    public ListenerConfig autoCheckingOn(int delay) {
        validateDelay(delay);
        autoChecking = delay;
        onAutoCheckingChange.accept(0, delay);
        return this;
    }

    private void validateDelay(int delay) {
        if(delay <= 0) throw new IllegalArgumentException("delay has to be greater than zero");
    }
    public ListenerConfig autoCheckingOff() {
        int tmp = autoChecking;
        autoChecking = 0;
        onAutoCheckingChange.accept(tmp, 0);
        return this;
    }
    public int autoChecking() {
        return autoChecking;
    }

    public boolean autoCheckingOn() {
        return autoChecking() != 0;
    }

    public void setOnAutoCheckingChange(BiConsumer<Integer, Integer> onAutoCheckingChange) {
        this.onAutoCheckingChange = onAutoCheckingChange;
    }
}
