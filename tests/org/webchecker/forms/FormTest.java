package org.webchecker.forms;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Form class test
 *
 * @author Filip Sohajek <filip.sohajek@gmail.com>
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @version 1.0
 */
public class FormTest {

    private Form form;
    public FormTest() throws IOException {
        Forms formsInstance = Forms.getInstance();
        System.out.println(System.getProperty("user.dir"));
        formsInstance.openDocument(Jsoup.parse(new File("tests/org/webchecker/forms/test.html"), "UTF-8", "http://localhost"));
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
    @Test
    public void testInputs(){
        ArrayList<Input> inputs = form.getInputs();
        assertEquals(inputs.get(0).getType(), Type.TEXT);
        assertEquals(inputs.get(1).getType(), Type.TEXT);
        assertEquals(inputs.get(2).getType(), Type.RADIO);
        assertEquals(inputs.get(0).getName(), "test1");
        assertEquals(inputs.get(1).getName(), "test2");
        assertEquals(inputs.get(2).getName(), "test3");
    }
}
