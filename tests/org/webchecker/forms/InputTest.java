package org.webchecker.forms;

import org.junit.Assert;
import org.junit.Test;

/**
 * Input class test
 *
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @version 1.0
 */
public class InputTest {
    @Test
    public void inputTest(){
        Input input = new Input("name", Type.TEXT);
        Assert.assertEquals(input.getType(), Type.TEXT);
        Assert.assertEquals(input.getName(), "name");
    }

}
