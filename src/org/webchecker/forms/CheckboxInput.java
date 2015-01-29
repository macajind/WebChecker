package org.webchecker.forms;

/**
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class CheckboxInput extends Input {

    private final String defaultValue;

    /**
     * Constructor that leaves the {@link Input#value} set to {@code null}, that means,
     * this input won't be {@link Form#send}. Other then known {@link Type}s of {@link
     * Input} should not be constructed, so parameter type can not be {@code null}.
     * Parameter name can not be empty or {@code null}, because {@link Form} doesn't
     * create such {@link Input}s.
     *
     * @param name         {@link String} name of the {@link Input}
     * @param type         {@link Type} of the {@link Input}
     * @param defaultValue
     */
    public CheckboxInput(String name, Type type, String defaultValue) {
        super(name, type);
        this.defaultValue = defaultValue;
    }

    /**
     * Constructor that sets all {@link Input} attributes and by this, makes it ready
     * to be {@link Form#send}. Other then known {@link Type}s of {@link Input} should
     * not be constructed, so parameter {@link Input#Input#type} can not be {@code
     * null}. {@link Input#Input#name} can not be empty or {@code null}. If {@link
     * Input#Input#value} is {@code null}, this input won't be {@link Form#send}.
     *
     * @param name         {@link String} name of the {@link Input}
     * @param type         {@link Type} of the {@link Input}
     * @param value        {@link String} value of the {@link Input}
     * @param defaultValue
     */
    public CheckboxInput(String name, Type type, String value, String defaultValue) {
        super(name, type, value);
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets default value of the {@link CheckboxInput}.
     */
    public void setValue() {
        this.setValue(this.defaultValue);
    }
}