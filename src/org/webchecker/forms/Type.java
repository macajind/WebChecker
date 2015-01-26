package org.webchecker.forms;

/**
 * Supported {@link Input} types.
 * It is also used to verify if the type of HTML input element is supported.
 *
 * @author Tunik
 * @version 1.0
 */
public enum Type {
    TEXT("text"), PASSWORD("password"), RADIO("radio"), CHECKBOX("checkbox"), HIDDEN("hidden");
    private final String type;

    private Type(String type) {
        this.type = type;
    }

    /**
     * Getter fot the {@link String} representation of the {@link Type}.
     *
     * @return {@link String} representation of the {@link Type}
     */
    public String getType() {
        return type;
    }

    /**
     * Tests if {@link String} parameter testType matches with one of the {@link Type} {@link String} representations.
     * This method is used to find out, if HTML input element has supported {@link Type}.
     * If parameter testType is {@code null}, there will be simply no match.
     *
     * @param testType {@link String} type of HTML input element
     * @return {@code true} if finds match with one of the {@link Type} {@link String} representations, otherwise {@code false}
     */
    public static Boolean containsType(String testType) {
        for (Type type : Type.values()) {
            if (type.getType().equals(testType)) return true;
        }
        return false;
    }
}
