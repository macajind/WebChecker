package org.webchecker.login;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.webchecker.forms.Form;
import org.webchecker.forms.Input;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class FormMock extends Form {
    private Map<String, String> lastRequestCookies;
    private Map<String, String> nextResponseCookies;

    private long sendCount;

    public FormMock(Element formElement, URL location) {
        super(formElement, location);
        resetSendCount();
    }


    public static FormMock getSimpleFormMock() throws MalformedURLException {
        String urlS = "https://www.nothing.com/";
        Element e = new Element(Tag.valueOf("form"), urlS);
        URL url = new URL(urlS);
        return new FormMock(e, url);
    }

    public Map<String, String> getLastRequestCookies() {
        return lastRequestCookies;
    }

    public void setNextResponseCookies(Map<String, String> nextResponseCookies) {
        this.nextResponseCookies = nextResponseCookies;
    }

    @Override
    public Connection.Response send(Map<String, String> cookies, Predicate<Input> sendInput) throws IOException {
        lastRequestCookies = cookies;
        sendCount++;
        return new Connection.Response() {
            @Override
            public int statusCode() {
                return 0;
            }

            @Override
            public String statusMessage() {
                return null;
            }

            @Override
            public String charset() {
                return null;
            }

            @Override
            public String contentType() {
                return null;
            }

            @Override
            public Document parse() throws IOException {
                return new Document("https://www.nothing.com");
            }

            @Override
            public String body() {
                return null;
            }

            @Override
            public byte[] bodyAsBytes() {
                return new byte[0];
            }

            @Override
            public URL url() {
                return null;
            }

            @Override
            public Connection.Response url(URL url) {
                return null;
            }

            @Override
            public Connection.Method method() {
                return null;
            }

            @Override
            public Connection.Response method(Connection.Method method) {
                return null;
            }

            @Override
            public String header(String s) {
                return null;
            }

            @Override
            public Connection.Response header(String s, String s1) {
                return null;
            }

            @Override
            public boolean hasHeader(String s) {
                return false;
            }

            @Override
            public Connection.Response removeHeader(String s) {
                return null;
            }

            @Override
            public Map<String, String> headers() {
                return null;
            }

            @Override
            public String cookie(String s) {
                return null;
            }

            @Override
            public Connection.Response cookie(String s, String s1) {
                return null;
            }

            @Override
            public boolean hasCookie(String s) {
                return false;
            }

            @Override
            public Connection.Response removeCookie(String s) {
                return null;
            }

            @Override
            public Map<String, String> cookies() {
                return nextResponseCookies;
            }
        };
    }

    public long getSendCount() {
        return sendCount;
    }
    public void resetSendCount() {
        sendCount = 0;
    }
}
