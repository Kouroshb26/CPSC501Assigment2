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

    @Test
    public void superClass() {
        assertEquals(" The super class is Object", inspector.superClass(String.class, new String(), false, 0));
    }

    @Test
    public void noSuperClass() {
        assertEquals(" There is no superclass", inspector.superClass(Object.class, new Object(), false, 0));
    }

    @Test
    public void interfaceClasses() {
        assertEquals(" The interfaces are: Serializable,Comparable,CharSequence", inspector.interfaces(String.class, new String(), false, 0));

    }

    @Test
    public void noInterfaceClasses() {
        assertEquals(" There are no interfaces", inspector.interfaces(Object.class, new Object(), false, 0));

    }
}
