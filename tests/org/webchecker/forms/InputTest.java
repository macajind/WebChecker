package org.webchecker.forms;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for {@link Input}.
 *
 * @author Marek Seďa <marecek.nenkovice225@gmail.com>
 * @author Jindřich Máca (Tuník)
 * @version 1.0
 */
public class InputTest {

    private final Input input = new Input("name", Type.TEXT);

    @Test
    public void getTypeTest() {
        Assert.assertEquals(input.getType(), Type.TEXT);
    }

    @Test
    public void getNameTest() {
        Assert.assertEquals(input.getName(), "name");
    }
}
