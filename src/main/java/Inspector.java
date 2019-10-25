import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        String prefix = getPrefixString(depth);
        if (c.isArray()) {
            array(c, obj, recursive, depth, prefix);
        }
        System.out.println(prefix + "-----------------Start--------------------------");
        //Name
        System.out.println(prefix + "The class name is " + c.getSimpleName());


        //Supper Class
        superClass(c, obj, recursive, depth, prefix);

        //Interfaces
        interfaces(c, obj, recursive, depth, prefix);

        //Constructors
        constructors(c, prefix);

        //Methods
        methods(c, prefix);

        //Declared Fields
        fields(c, obj, recursive, depth, prefix);

        System.out.println(prefix + "-----------------End--------------------------");

    }

    private void array(Class c, Object obj, boolean recursive, int depth, String prefix) {
        System.out.println(prefix + "This class is an array type " + c.getSimpleName());
        System.out.println(prefix + "This is an array of " + c.getComponentType().getSimpleName());
        System.out.println(prefix + "This array length is  " + Array.getLength(obj));
        System.out.println(prefix + "The contents of the array is : " + objectToString(obj, c));
        for (int i = 0; i < Array.getLength(obj); i++) {
            if (!c.getComponentType().isPrimitive() && recursive && Array.get(obj, i) != null) {
                inspectClass(c.getComponentType(), Array.get(obj, i), recursive, depth + 1);
            }
        }
    }

    private void fields(Class c, Object obj, boolean recursive, int depth, String prefix) {
        if (c.getDeclaredFields().length > 0) {
            System.out.println();
            System.out.println(prefix + "The fields are:");
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(prefix + "===========================================");
                System.out.println(prefix + " The name is " + field.getName());
                System.out.println(prefix + " The type is " + field.getType().getSimpleName());
                System.out.println(prefix + " The modifiers are " + Modifier.toString(field.getModifiers()));
                try {
                    if (obj != null) {
                        Object fieldObject = field.get(obj);
                        System.out.println(prefix + " The value of the field is " + objectToString(fieldObject, field.getType()));
                        if (recursive && !field.getType().isPrimitive() && fieldObject != null) {
                            inspectClass(field.getType(), fieldObject, recursive, depth + 1);
                        }
                    } else {
                        System.out.println(prefix + "Object is null, thus no field value can be accessed");
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void methods(Class c, String prefix) {
        //Declared Methods
        if (c.getDeclaredMethods().length > 0) {
            System.out.println();
            System.out.println(prefix + "The methods are:");
            for (Method method : c.getDeclaredMethods()) {
                System.out.println(prefix + "===========================================");
                System.out.println(prefix + " The name is " + method.getName());
                System.out.println(prefix + " The exceptions are (" + Arrays.stream(method.getExceptionTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                System.out.println(prefix + " The parameters types are (" + Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                System.out.println(prefix + " The return type is " + method.getReturnType().getSimpleName());
                System.out.println(prefix + " The modifiers are " + Modifier.toString(method.getModifiers()));
            }
        }
    }

    private void constructors(Class c, String prefix) {
        //Constructors
        if (c.getDeclaredConstructors().length > 0) {
            System.out.println();
            System.out.println(prefix + "The constructors are:");
            for (Constructor constructor : c.getDeclaredConstructors()) {
                System.out.println(prefix + "===========================================");
                System.out.println(prefix + " The name is " + constructor.getName());
                System.out.println(prefix + " The parameters types are (" + Arrays.stream(constructor.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                System.out.println(prefix + " The modifiers are " + Modifier.toString(constructor.getModifiers()));
            }
        }
    }


    String interfaces(Class c, Object obj, boolean recursive, int depth, String prefix) {
        String result;
        if (c.getInterfaces().length > 0) {
            System.out.println();
            result = " The interfaces are: " + Arrays.stream(c.getInterfaces()).map(Class::getSimpleName).collect(Collectors.joining(","));
            System.out.println(prefix + result);
            Arrays.stream(c.getInterfaces()).forEach(inter -> inspectClass(inter, inter.cast(obj), recursive, depth + 1));
        } else {
            result = " There are no interfaces";
            System.out.println(prefix + result);
        }
        return result;
    }

    String superClass(Class c, Object obj, boolean recursive, int depth, String prefix) {
        String result;
        if (c.getSuperclass() != null) {
            System.out.println();
            result = " The super class is " + c.getSuperclass().getSimpleName();
            System.out.println(prefix + result);
            inspectClass(c.getSuperclass(), c.getSuperclass().cast(obj), recursive, depth + 1);
        } else {
            result = " There is no superclass";
            System.out.println(prefix + result);
        }
        return result;
    }

    String getPrefixString(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }

    String objectToString(Object obj, Class<?> type) {
        if (obj == null) {
            return "null";
        } else if (type.isPrimitive()) {
            return obj.toString();

        } else if (type.isArray()) {
            StringBuilder result = new StringBuilder();
            result.append("[");
            for (int i = 0; i < Array.getLength(obj); i++) {
                result.append(objectToString(Array.get(obj, i), type.getComponentType()));
                if (i + 1 != Array.getLength(obj)) {
                    result.append(",");
                }
            }
            result.append("]");
            return result.toString();

        } else {
            return type.getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        }
    }
}
