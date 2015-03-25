package org.webchecker.forms;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Forms class test
 *
 * @author Filip Sohajek <filip.sohajek@gmail.com>
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @version 1.0
 */
public class FormsTest {

    private final Forms formsInstance;

    public FormsTest() throws IOException {
        formsInstance = Forms.getInstance();
        formsInstance.openDocument(Jsoup.parse(new File("tests/org/webchecker/forms/test.html"), "UTF-8", "http://localhost"));
    }

    @Test
    public void testDocumentRetrieve() throws IOException {
        assertThat(formsInstance.getDocument(), instanceOf(Document.class));
    }

    @Test
    public void testFormRetr() throws MalformedURLException {
        Form form = formsInstance.selectForm("form");
        assertNotNull(form);
    }
}
