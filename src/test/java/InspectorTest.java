import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author kourosh
 * @since 2019-10-22
 */
public class InspectorTest {
    private Inspector inspector = new Inspector();

    @Test
    public void getPrefixString5() {
        assertEquals("\t\t\t\t\t", inspector.getPrefixString(5));
    }


    @Test
    public void getPrefixString2() {
        assertEquals("\t\t", inspector.getPrefixString(2));
    }

    @Test
    public void getPrefixString0() {
        assertEquals("", inspector.getPrefixString(0));
    }
}
