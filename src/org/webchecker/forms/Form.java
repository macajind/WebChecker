package org.webchecker.forms;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
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
     * @param formElement is HTML element extracted from document
     * @param location is {@link URL}
     */
    public Form(Element formElement, URL location) {
        setAction(location, formElement.attr("action"));
        method = Method.valueOf(formElement.attr("method"));

        Elements radioInputs = new Elements();
        inputs.add(processRadioInputs(formElement.getElementsByTag("input[type=\"radio\"]")));
        for (Element input : formElement.getElementsByTag("input")) {
            if (!input.attr("type").equals("radio") && Type.containsType(input.attr("type"))) {
                inputs.add(new Input(input.attr("name"), Type.valueOf(input.attr("type")), input.attr("value")));
            }else if (Type.containsType(input.attr("type"))){
                radioInputs.add(input);
            }
        }
        inputs.add(processRadioInputs(radioInputs));
    }

    /**
     * Support method for creating full path url of action.
     *
     * @param location is {@link URL} of file from which application download {@link Document}
     * @param action is absolute or relative address. This address is one of attributes of HTML form
     */
    private void setAction(URL location, String action){
        try {
            this.action = new URL(location, action);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Support method for dealing with radio inputs, because of theirs possibility to have same name.
     *
     * @param inputs with type radio
     * @return radio input which have attribute 'checked'
     */
    private Input processRadioInputs(Elements inputs) {
        if(inputs == null) return null;
        for (Element input : inputs){
            if(input.hasAttr("checked"))
                return new Input(input.attr("name"), Type.RADIO, input.attr("checked"));
        }
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
            if (inputsValues.containsKey(input.getName()))
                input.setValue(inputsValues.get(input.getName()));
        }
    }

    /**
     * Send {@link Form} in current filled/part-filled/not filled state to his {@link Form#action} url address.
     * Sending proceeds different depending on {@link Form#method}, which can be {@link Method#GET} or {@link Method#POST}.
     * When sending fail in some way or response status code is different from 200, application wil return {@link null}
     *
     * @return response page in form of {@link Document}
     */
    public Document send() {
        HashMap<String, String> inputsMap = new HashMap<>();
        inputs.forEach(input -> inputsMap.put(input.getName(), input.getValue()));

        try {
            Connection.Response response = Jsoup.connect(action.getPath()).data(inputsMap).method(method).execute();
            if(response.statusCode() == 200)
                return response.parse();
            return null;
        } catch (IOException e) {
            return null;
        }
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
