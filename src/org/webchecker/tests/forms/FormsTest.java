package org.webchecker.tests.forms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.webchecker.forms.Form;
import org.webchecker.forms.Forms;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class FormsTest {

    Forms formsinst;

    Form form;

    public FormsTest() throws IOException {
        formsinst = Forms.getInstance();
        formsinst.openDocument(Jsoup.parse(new File("src/org/webchecker/tests/forms/testdocument.html"), "UTF-8", "http://localhost"));
    }

    @Test
    public void testDocumentRetrieve() throws IOException {
        assertThat(formsinst.getDocument(), instanceOf(Document.class));
    }

    @Test
    public void testFormRetr() throws MalformedURLException {
        form = formsinst.selectForm("form");
        assertThat(form, instanceOf(Form.class));
    }

}
