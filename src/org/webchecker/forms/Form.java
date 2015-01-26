package org.webchecker.forms;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * HTML form element.
 * Allows user to fill in form {@link Input}s of supported {@link Type}s
 * and {@link Form#send} either by {@link Method#GET} or {@link Method#POST} method.
 *
 * @author Tunik
 * @version 1.0
 */
public class Form {

    private URL action;
    private Method method;
    private ArrayList<Input> inputs;

    /**
     * Construct all inner structure of the form. Its inputs, action and method.
     * Includes only inputs of known types and inputs with names!
     *
     * @param formElement
     * @param location
     */
    public Form(Element formElement, URL location) {
    }

    /**
     * Support method for creating full path url of action.
     */
    private void setAction(URL location, String action) {
    }

    /**
     * Support method for dealing with radio inputs, because of theirs possibility to have same name.
     */
    private Input processRadioInputs(Elements inputs) {
        return null;
    }

    /**
     * Returns collection of {@link Input} for user to see them and possibility of filling them.
     * {@link Input} with {@code null} {@link Input#value} will not be {@link Form#send()} in request,
     * on the other hand every other {@link Input} will.
     * {@link Input}s are not checked for rightness of theirs values e.g. you can set {@link Input#value} for checkbox complete different,
     * then it was originally there.
     *
     * @return collection of {@link Input} for user to see them and possibility of filling them
     */
    public ArrayList<Input> getInputs() {
        return inputs;
    }

    /**
     * Gives user the possibility to fill all the {@link Input}s at once with predefined collection of pairs.
     * First item in the pair represents {@link Input#name} and second one represents {@link Input#value}.
     * If filled value is {@code null}, then this {@link Input} won't be {@link Form#send()}.
     *
     * @param inputsValues collection of pairs, where first item represents {@link Input#name} and second one represents {@link Input#value}
     */
    public void fill(HashMap<String, String> inputsValues) {
        for (Input input : this.inputs) {
            if (inputsValues.containsKey(input.getName())) input.setValue(inputsValues.get(input.getName()));
        }
    }

    /**
     * Send {@link Form} in current filled/part-filled/not filled state to his {@link Form#action} url address.
     * Sending proceeds different depending on {@link Form#method}, which can be {@link Method#GET} or {@link Method#POST}.
     *
     * @return response page in form of {@link Document}
     */
    public Document send() {
        switch (this.method) {
            case GET:
                return sendGET();
            case POST:
                return sendPOST();
            default:
                throw new IllegalStateException("Application should never throw this exception.");
        }
    }

    /**
     * Should send form by GET method.
     * Must build its request format.
     * What happens when request fails in some way or return another HTTP code then 200?
     *
     * @return
     */
    Document sendGET() {
        return null;
    }

    /**
     * Should send form by POST method.
     * Must build its request format.
     * What happens when request fails in some way or return another HTTP code then 200?
     *
     * @return
     */
    Document sendPOST() {
        return null;
    }

    /**
     * Getter for {@link Form#action} {@link URL} address setup in construction of the {@link Form}.
     *
     * @return {@link Form#action} {@link URL} address
     */
    public URL getAction() {
        return action;
    }

    /**
     * Getter for {@link Form#method} type setup in construction of the {@link Form}.
     * This type could be either {@link Method#GET} or {@link Method#POST}.
     *
     * @return {@link Form#method} type
     */
    public Method getMethod() {
        return method;
    }
}
