package org.webchecker.tests.forms;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.webchecker.forms.Form;
import org.webchecker.forms.Forms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

/**
 * Form class test
 *
 * @author Filip Sohajek <filip.sohajek@gmail.com>
 * @version 1.0
 */
public class FormTest {

    private Form form;

    public FormTest() throws IOException {
        Forms formsInstance = Forms.getInstance();
        System.out.println(System.getProperty("user.dir"));
        formsInstance.openDocument(Jsoup.parse(new File("src/org/webchecker/tests/forms/test.html"), "UTF-8", "http://localhost"));
        form = formsInstance.selectForm("form");
    }

    @Test
    public void testFill() {
        HashMap<String, String> values = new HashMap<>();
        values.put("test1", "Test value for input test1");
        values.put("test2", "Test value for input test2");
        values.put("test3", "true");
        form.fill(values);
        assertNotNull(form.getInputs());
    }
}
