package org.webchecker.forms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test case for {@link Forms}.
 *
 * @author Filip Sohajek <filip.sohajek@gmail.com>
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @version 1.0
 */
public class FormsTest {

    private final Forms formsInstance;

    public FormsTest() throws IOException {
        formsInstance = Forms.getInstance();
        URL resource = this.getClass().getClassLoader().getResource("test.html");
        if (resource == null) throw new IOException("Couldn't find test resources!");
        formsInstance.openDocument(Jsoup.parse(new File(resource.getPath()), "UTF-8", "http://localhost"));
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
