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
import java.util.function.Predicate;

/**
 * HTML form element.
 * Allows user to fill in form {@link Input}s of supported {@link Type}s
 * and {@link Form#send} either by {@link Method#GET} or {@link Method#POST} method.
 *
 * @author Ondřej Štorc <o.storc@outlook.com>
 * @author Filip Sohajek <filip.sohajek@gmail.com>
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class Form {

    private URL action;
    private final Method method;
    private final ArrayList<Input> inputs = new ArrayList<>();

    /**
     * Construct all inner structure of the form. Its inputs, action and method.
     * Includes only inputs of known types and inputs with names!
     *
     * @param formElement is HTML element extracted from document
     * @param location    is {@link URL}
     */
    public Form(Element formElement, URL location) {
        //Setting action
        setAction(location, formElement.attr("action").trim());

        //Setting method
        String method = formElement.attr("method");
        this.method = Method.valueOf(method.equals("") ? "GET" : method.trim().toUpperCase());

        //Extracting inputs
        Elements inputs = formElement.getElementsByTag("input");
        Elements inputsToSkip = new Elements();
        for (Element input : inputs) {
            if (inputsToSkip.contains(input)) continue;
            if (Type.containsType(input.attr("type"))) {
                String name = input.attr("name").trim();
                Type type = Type.valueOf(input.attr("type").trim().toUpperCase());
                String value = input.attr("value").trim();
                switch (type) {
                    case CHECKBOX:
                        this.inputs.add(input.hasAttr("checked") ? new CheckboxInput(name, value, value) : new CheckboxInput(name, value));
                        break;
                    case RADIO:
                        Elements radioInputs = inputs.select("[type=\"" + Type.RADIO.getType() + "\"][name=\"" + name + "\"]");
                        this.inputs.add(this.processRadioInputs(name, radioInputs));
                        inputsToSkip.addAll(radioInputs);
                        break;
                    default:
                        this.inputs.add(new Input(name, type, value));
                }
            }
        }
    }

    /**
     * Support method for dealing with radio inputs, because of theirs possibility to have same name and more values.
     *
     * @param name        name of the list of radio inputs
     * @param radioInputs list of radio inputs
     * @return {@link RadioInput} properly build from list of radio inputs
     */
    private RadioInput processRadioInputs(String name, Elements radioInputs) {
        ArrayList<String> defaultValues = new ArrayList<>();
        String value = null;
        for (Element radioInput : radioInputs) {
            String defaultValue = radioInput.attr("value").trim();
            if (radioInput.hasAttr("checked")) {
                value = defaultValue;
            }
            defaultValues.add(defaultValue);
        }
        return value == null ? new RadioInput(name, defaultValues) : new RadioInput(name, value, defaultValues);
    }

    /**
     * Support method for creating full path url of action.
     *
     * @param location is {@link URL} of file from which application download {@link Document}
     * @param action   is absolute or relative address; this address is one of attributes of HTML form
     */
    private void setAction(URL location, String action) {
        try {
            this.action = new URL(location, action);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
        this.inputs.stream().filter(input -> inputsValues.containsKey(input.getName())).forEach(input -> input.setValue(inputsValues.get(input.getName())));
    }

    /**
     * Send {@link Form} in current filled/part-filled/not filled state to his {@link Form#action} url address.
     * Sending proceeds different depending on {@link Form#method}, which can be {@link Method#GET} or {@link Method#POST}.
     * When sending fail in some way or response status code is different from 200, application wil return {@link null}
     *
     * @return response page in form of {@link Document}
     * @throws java.io.IOException if sending of the form fails or it returns HTTP respond code other then 200
     */
    public Document send() throws IOException {
        return send(null).parse();
    }

    public Connection.Response send(Predicate<Input> sendInput) throws IOException {
        // Get all notnull data for which return given predicate true
        HashMap<String, String> inputsMap = new HashMap<>();
        sendInput = (sendInput == null) ? Input::isFilled : sendInput.and(Input::isFilled); // Input is filled and should be included
        inputs.stream().filter(sendInput).forEach(input -> inputsMap.put(input.getName(), input.getValue()));

        Connection.Response response = Jsoup.connect(action.getPath()).data(inputsMap).method(method).execute();
        if (response.statusCode() == 200) return response;
        else throw new IOException(response.statusMessage());
    }

    /**
     * Getter for action {@link URL} address setup in construction of the {@link Form}.
     *
     * @return action {@link URL} address
     */
    public URL getAction() {
        return action;
    }

    /**
     * Getter for method type setup in construction of the {@link Form}.
     * This type could be either {@link Method#GET} or {@link Method#POST}.
     *
     * @return method type of this {@link Form}
     */
    public Method getMethod() {
        return method;
    }
}
