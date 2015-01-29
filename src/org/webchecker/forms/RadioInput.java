package org.webchecker.forms;

import java.util.ArrayList;

/**
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class RadioInput extends Input {

    private final ArrayList<String> defaultValues;

    /**
     * Constructor that leaves the {@link Input#value} set to {@code null}, that means,
     * this input won't be {@link Form#send}. Other then known {@link Type}s of {@link
     * Input} should not be constructed, so parameter type can not be {@code null}.
     * Parameter name can not be empty or {@code null}, because {@link Form} doesn't
     * create such {@link Input}s.
     *
     * @param name          {@link String} name of the {@link Input}
     * @param type          {@link Type} of the {@link Input}
     * @param defaultValues
     */
    public RadioInput(String name, Type type, ArrayList<String> defaultValues) {
        super(name, type);
        this.defaultValues = defaultValues;
    }

    /**
     * Constructor that sets all {@link Input} attributes and by this, makes it ready
     * to be {@link Form#send}. Other then known {@link Type}s of {@link Input} should
     * not be constructed, so parameter {@link Input#Input#type} can not be {@code
     * null}. {@link Input#Input#name} can not be empty or {@code null}. If {@link
     * Input#Input#value} is {@code null}, this input won't be {@link Form#send}.
     *
     * @param name          {@link String} name of the {@link Input}
     * @param type          {@link Type} of the {@link Input}
     * @param value         {@link String} value of the {@link Input}
     * @param defaultValues
     */
    public RadioInput(String name, Type type, String value, ArrayList<String> defaultValues) {
        super(name, type, value);
        this.defaultValues = defaultValues;
    }

    public ArrayList<String> getDefaultValues() {
        return defaultValues;
    }

    /**
     * Sets input value from list of default values by its id.
     *
     * @param id
     */
    public void setValue(Integer id) {

    }

    /**
     * @param value
     */
    public Integer containsDefaultValue(String value) {
        return null;
    }
}