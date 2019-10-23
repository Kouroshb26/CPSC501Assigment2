import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Test
    public void superClass() {
        assertEquals(" The super class is Object", inspector.superClass(String.class, new String(), false, 0));
    }

    @Test
    public void noSuperClass() {
        assertNull(inspector.superClass(Object.class, new Object(), false, 0));
    }

    @Test
    public void interfaceClasses() {
        assertEquals(" The interfaces are: Serializable,Comparable,CharSequence", inspector.interfaceClasses(String.class, new String(), false, 0));

    }

    @Test
    public void noInterfaceClasses() {
        assertNull(inspector.interfaceClasses(Object.class, new Object(), false, 0));

    }
}
