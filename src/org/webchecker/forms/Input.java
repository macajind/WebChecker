package org.webchecker.forms;

import com.sun.istack.internal.NotNull;

/**
 * HTML form input element.
 * Only considers name, value and type attributes of HTML input element.
 *
 * @author Jindřich Máca (Tuník)
 * @author Matěj Kripner<kripnermatej@gmail.com>
 * @version 1.0
 */
public class Input {

    private final String name;
    private String value = null;
    private final Type type;

    /**
     * Constructor that leaves the input value set to {@code null}, that means, this input won't be {@link Form#send}.
     * <p>
     * Other then known {@link Type}s of {@link Input} should not be constructed, so parameter type can not be {@code null}.
     * Parameter name can not be empty or {@code null}, because {@link Form} doesn't create such {@link Input}s.
     * If the given parameters do not correspond with this rules, an {@link IllegalArgumentException} is thrown.
     *
     * @param name {@link String} name of the {@link Input}
     * @param type {@link Type} of the {@link Input}
     * @throws IllegalArgumentException if one or both of given parameters are {@code null}, or the name parameter is empty
     */
    public Input(@NotNull String name, @NotNull Type type) throws IllegalArgumentException {
        if (name == null || name.isEmpty() || type == null)
            throw new IllegalArgumentException("The name and type arguments can't be null.");
        this.name = name;
        this.type = type;
    }

    /**
     * Constructor that sets all {@link Input} attributes and by this, makes it ready to be {@link Form#send}.
     * <p>
     * Other then known {@link Type}s of {@link Input} should not be constructed, so parameter input type can not be {@code null}.
     * Input name can not be empty or {@code null}.
     * If input value is {@code null}, this input won't be {@link Form#send}.
     *
     * @param name  {@link String} name of the {@link Input}
     * @param type  {@link Type} of the {@link Input}
     * @param value {@link String} value of the {@link Input}
     * @throws IllegalArgumentException if name or type parameter is {@code null}, or the name parameter is empty
     */
    public Input(String name, Type type, String value) throws IllegalArgumentException {
        this(name, type);
        this.value = value;
    }

    /**
     * Getter for the input name.
     *
     * @return {@link String} value of the input name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the input value.
     *
     * @return {@link String} value of the input value
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter for the input type.
     *
     * @return {@link Type} of the input type
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter for the input value.
     * If you set parameter value to {@code null}, this {@link Input} won't be {@link Form#send}.
     *
     * @param value {@link String} value to be set to the input value
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
