package org.webchecker.forms;

import org.junit.Assert;
import org.junit.Test;

/**
 * CheckboxInput class test
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @version 1.0
 *
 **/
public class CheckboxInputTest {
    @Test
    public void CheckboxInputTest(){
        CheckboxInput checkboxInput = new CheckboxInput("name", "value", "default_value");
        Assert.assertEquals("name", checkboxInput.getName());
        Assert.assertEquals("default_value", checkboxInput.getDefaultValue());
        Assert.assertEquals("value", checkboxInput.getValue());
    }

}
