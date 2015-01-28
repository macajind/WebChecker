package org.webchecker.tests.forms;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.webchecker.forms.Form;
import org.webchecker.forms.Forms;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FormTest {

    Form form;

    public FormTest() throws IOException {
        Forms formsinst = Forms.getInstance();
        System.out.println(System.getProperty("user.dir"));
        formsinst.openDocument(Jsoup.parse(new File("src/org/webchecker/tests/forms/testdocument.html"), "UTF-8", "http://localhost"));
        form = formsinst.selectForm("form");
    }

    @Test
    public void testFill() {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("test1", "Test value for input test1");
        values.put("test2", "Test value for input test2");
        values.put("test3", "true");
        form.fill(values);
        assertNotNull(form.getInputs());
    }

}
