package org.webchecker.forms;

import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
import org.webchecker.forms.Form;
import org.webchecker.forms.Forms;
import org.webchecker.forms.Input;
import org.webchecker.forms.Type;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test case for {@link Form}.
 *
 * @author Filip Sohajek <filip.sohajek@gmail.com>
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @version 1.0
 */
public class FormTest {

    private final Form form;

    public FormTest() throws IOException {
        Forms formsInstance = Forms.getInstance();
        System.out.println(System.getProperty("user.dir"));
        URL resource = this.getClass().getClassLoader().getResource("test.html");
        if (resource == null) throw new IOException("Couldn't find test resources!");
        formsInstance.openDocument(Jsoup.parse(new File(resource.getPath()), "UTF-8", "http://localhost"));
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
    public void testInputs() {
        ArrayList<Input> inputs = form.getInputs();
        Assert.assertEquals(inputs.get(0).getType(), Type.TEXT);
        assertEquals(inputs.get(1).getType(), Type.TEXT);
        assertEquals(inputs.get(2).getType(), Type.RADIO);
        assertEquals(inputs.get(0).getName(), "test1");
        assertEquals(inputs.get(1).getName(), "test2");
        assertEquals(inputs.get(2).getName(), "test3");
    }
}
