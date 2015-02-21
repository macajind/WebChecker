package org.webchecker.forms;

import java.util.ArrayList;

/**
 * Set of HTML form input elements with type radio and same name attribute.
 * Extends from {@link Input}, but differs itself by considering also default values of input elements.
 *
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class RadioInput extends Input {

    private final ArrayList<String> defaultValues;

    /**
     * Calls default {@link Input#Input(String, Type)} constructor, but also sets this {@link RadioInput#defaultValues}.
     *
     * @param name          {@link String} name of the {@link RadioInput}
     * @param defaultValues {@link java.util.ArrayList} of default values possible for this {@link RadioInput}
     */
    public RadioInput(String name, ArrayList<String> defaultValues) {
        super(name, Type.RADIO);
        this.defaultValues = defaultValues;
    }

    /**
     * Calls default {@link Input#Input(String, Type, String)} constructor, but also sets this {@link RadioInput#defaultValues}.
     *
     * @param name          {@link String} name of the {@link Input}
     * @param value         {@link String} value of the {@link Input}
     * @param defaultValues {@link java.util.ArrayList} of default values possible for this {@link RadioInput}
     */
    public RadioInput(String name, String value, ArrayList<String> defaultValues) {
        super(name, Type.RADIO, value);
        this.defaultValues = defaultValues;
    }

    /**
     * Getter for the {@link RadioInput#defaultValues}.
     *
     * @return {@link RadioInput#defaultValues} of this {@link RadioInput}
     */
    public ArrayList<String> getDefaultValues() {
        return defaultValues;
    }

    /**
     * Sets {@link RadioInput#value} from {@link RadioInput#defaultValues} by index of the value in that list.
     *
     * @param index {@link java.lang.Integer} position of default value in {@link RadioInput#defaultValues}
     * @throws IndexOutOfBoundsException if index is {@code null} or is out of range ({@code index < 0 || index >= size() })
     */
    public void setDefaultValue(Integer index) throws IndexOutOfBoundsException {
        this.setValue(this.defaultValues.get(index));
    }

    /**
     * Gets {@link java.lang.Integer} index position of the last occurrence of default value from
     * {@link RadioInput#defaultValues}.
     *
     * @param value {@link String} name of the default value
     * @return the {@link java.lang.Integer} index of the last occurrence of the default value name in {@link RadioInput#defaultValues},
     * or -1 if this {@link RadioInput#defaultValues} does not contain this default value, that apply also for {@code null}
     */
    public Integer containsDefaultValue(String value) {
        return this.defaultValues.lastIndexOf(value);
    }
}
