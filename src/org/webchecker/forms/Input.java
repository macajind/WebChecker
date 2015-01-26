package org.webchecker.forms;

/**
 * HTML form input element.
 * Only considers name, value and type attributes of HTML input element.
 *
 * @author Tunik
 * @version 1.0
 */
public class Input {

    private final String name;
    private String value = null;
    private final Type type;

    /**
     * Constructor that leaves the {@link Input#value} set to {@code null}, that means, this input won't be {@link Form#send}.
     * Other then known {@link Type}s of {@link Input} should not be constructed, so parameter type can not be {@code null}.
     * Parameter name can not be empty or {@code null}, because {@link Form} doesn't create such {@link Input}s.
     *
     * @param name {@link String} name of the {@link Input}
     * @param type {@link Type} of the {@link Input}
     */
    public Input(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Constructor that sets all {@link Input} attributes and by this, makes it ready to be {@link Form#send}.
     * Other then known {@link Type}s of {@link Input} should not be constructed, so parameter {@link Input#Input#type} can not be {@code null}.
     * {@link Input#Input#name} can not be empty or {@code null}.
     * If {@link Input#Input#value} is {@code null}, this input won't be {@link Form#send}.
     *
     * @param name  {@link String} name of the {@link Input}
     * @param type  {@link Type} of the {@link Input}
     * @param value {@link String} value of the {@link Input}
     */
    public Input(String name, Type type, String value) {
        this(name, type);
        this.value = value;
    }

    /**
     * Getter for the {@link Input#name}.
     *
     * @return {@link String} value of the {@link Input#name}
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the {@link Input#value}.
     *
     * @return {@link String} value of the {@link Input#value}
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter for the {@link Input#type}.
     *
     * @return {@link Type} of the {@link Input#type}
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter for the {@link Input#value}.
     * If you set parameter value to {@code null}, this {@link Input} won't be {@link Form#send}.
     *
     * @param value {@link String} value to be set to the {@link Input#value}
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Checks if the {@link Input} is filled.
     * Not filled {@link Input} won't be {@link Form#send}.
     *
     * @return {@code true} if {@link Input} is filled, otherwise {@code false}
     */
    public Boolean isFilled() {
        return this.value != null;
    }
}
