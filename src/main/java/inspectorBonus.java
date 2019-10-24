import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kourosh
 * @since 2019-10-23
 */
public class inspectorBonus {

    public static void main(String[] args) { //Example arguments: Inspector ClassD true
        if (args.length != 3) {
            System.err.println("The program needs exactly three arguments");
        }

        try {
            Class inspectorClass = Class.forName(args[0]);
            Class toInspectClass = Class.forName(args[1]);

            boolean recursive = Boolean.valueOf(args[2]);

            Object inspector = inspectorClass.newInstance();
            Object toInspect = toInspectClass.newInstance();

            Method inspect = inspectorClass.getMethod("inspect", Object.class, boolean.class);
            inspect.setAccessible(true);
            inspect.invoke(inspector, toInspect, recursive);


        } catch (ClassNotFoundException e) {
            System.out.println("Could not find the class you provided.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Due to security I was not able to access new instance or invoke method");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("Could not create an instance of the classes provided");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("Could not find the inspect method in the inspector class. Make sure it has the signature inspect(Object,boolean");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("The inspect method threw an exception.");
            e.printStackTrace();
        }

    }
}
