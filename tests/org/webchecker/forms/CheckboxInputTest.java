package org.webchecker.forms;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for {@link CheckboxInput}.
 *
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class CheckboxInputTest {

    private final CheckboxInput checkboxInput = new CheckboxInput("name", "value", "default_value");

    @Test
    public void getNameTest() {
        Assert.assertEquals("name", checkboxInput.getName());
    }

    @Test
    public void getValueTest() {
        Assert.assertEquals("value", checkboxInput.getValue());
    }

    @Test
    public void getDefaultValueTest() {
        Assert.assertEquals("default_value", checkboxInput.getDefaultValue());
    }
}
