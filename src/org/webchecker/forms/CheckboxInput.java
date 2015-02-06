package org.webchecker.forms;

/**
 * HTML form input element with type checkbox.
 * Extends from {@link Input}, but differs itself by considering also default value of input element.
 *
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class CheckboxInput extends Input {

    private final String defaultValue;

    /**
     * Calls default {@link Input#Input(String, Type)} constructor, but also sets this {@link CheckboxInput#defaultValue}.
     *
     * @param name         {@link String} name of the {@link CheckboxInput}
     * @param defaultValue {@link String} default value of the {@link CheckboxInput}
     */
    public CheckboxInput(String name, String defaultValue) {
        super(name, Type.CHECKBOX);
        this.defaultValue = defaultValue;
    }

    /**
     * Calls default {@link Input#Input(String, Type, String)} constructor, but also sets this {@link CheckboxInput#defaultValue}.
     *
     * @param name         {@link String} name of the {@link CheckboxInput}
     * @param value        {@link String} value of the {@link CheckboxInput}
     * @param defaultValue {@link String} default value of the {@link CheckboxInput}
     */
    public CheckboxInput(String name, String value, String defaultValue) {
        super(name, Type.CHECKBOX, value);
        this.defaultValue = defaultValue;
    }

    /**
     * Getter for the {@link CheckboxInput#defaultValue}.
     *
     * @return {@link CheckboxInput#defaultValue} of this {@link CheckboxInput}
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value to the {@link CheckboxInput}.
     * If the default value is {@code null}, then this {@link CheckboxInput} won't be {@link Form#send} after calling this method.
     */
    public void setDefaultValue() {
        this.setValue(this.defaultValue);
    }
}
