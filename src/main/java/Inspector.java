import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Inspector {

    public static void main(String[] args) throws Exception {
        new Inspector().inspect(new String[][]{{"hello", "helloWolrd"}}, true);
    }

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        if (c.isArray()) {
            println(depth, "This class is an array type with name " + c.getSimpleName());
            println(depth, "This is an array of " + c.getComponentType().getSimpleName());
            println(depth, "This array length is  " + Array.getLength(obj));
            println(depth, "The contents of the array is : " + objectToString(obj, c));
            if (!c.getComponentType().isPrimitive() && recursive) {
                for (int i = 0; i < Array.getLength(obj); i++) {
                    inspectClass(c.getComponentType(), Array.get(obj, i), recursive, depth + 1);
                }
            }
        } else {
            println(depth, "-----------------Start--------------------------");
            //Name
            println(depth, "The class name is " + c.getSimpleName());


            //Supper Class
            superClass(c, obj, recursive, depth);

            //Interfaces
            interfaceClasses(c, obj, recursive, depth);

            //Constructors
            if (c.getDeclaredConstructors().length > 0) {
                System.out.println();
                println(depth, "The constructors are:");
                for (Constructor constructor : c.getDeclaredConstructors()) {
                    println(depth, "===========================================");
                    println(depth, " The name is " + constructor.getName());
                    println(depth, " The parameters types are (" + Arrays.stream(constructor.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                    println(depth, " The modifiers are " + Modifier.toString(constructor.getModifiers()));
                }
            }

            //Declared Methods
            if (c.getDeclaredMethods().length > 0) {
                System.out.println();
                println(depth, "The methods are:");
                for (Method method : c.getDeclaredMethods()) {
                    println(depth, "===========================================");
                    println(depth, " The name is " + method.getName());
                    println(depth, " The exceptions are (" + Arrays.stream(method.getExceptionTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                    println(depth, " The parameters types are (" + Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                    println(depth, " The return type is " + method.getReturnType().getSimpleName());
                    println(depth, " The modifiers are " + Modifier.toString(method.getModifiers()));
                }
            }

            //Declared Fields
            if (c.getDeclaredFields().length > 0) {
                System.out.println();
                println(depth, "The fields are:");
                for (Field field : c.getDeclaredFields()) {
                    field.setAccessible(true);
                    println(depth, "===========================================");
                    println(depth, " The name is " + field.getName());
                    println(depth, " The type is " + field.getType().getSimpleName());
                    println(depth, " The modifiers are " + Modifier.toString(field.getModifiers()));
                    try {
                        if (obj != null) {
                            Object fieldObject = field.get(obj);
                            println(depth, " The value of the field is " + objectToString(fieldObject, field.getType()));
                            if (recursive && !field.getType().isPrimitive()) {
                                inspectClass(field.getType(), fieldObject, recursive, depth + 1);
                            }
                        } else {
                            println(depth, "Object is null, thus no field value can be accessed");
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        println(depth, "-----------------End--------------------------");
    }


    String interfaceClasses(Class c, Object obj, boolean recursive, int depth) {
        String result = null;
        if (c.getInterfaces().length > 0) {
            System.out.println();
            result = " The interfaces are: " + Arrays.stream(c.getInterfaces()).map(Class::getSimpleName).collect(Collectors.joining(","));
            println(depth, result);
            Arrays.stream(c.getInterfaces()).forEach(inter -> inspectClass(inter, inter.cast(obj), recursive, depth + 1));
        }
        return result;
    }

    String superClass(Class c, Object obj, boolean recursive, int depth) {
        String result = null;
        if (c.getSuperclass() != null) {
            System.out.println();
            result = " The super class is " + c.getSuperclass().getSimpleName();
            println(depth, result);
            inspectClass(c.getSuperclass(), c.getSuperclass().cast(obj), recursive, depth + 1);
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


    private void println(int depth, String output) {
        System.out.println(getPrefixString(depth) + output);
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
            return obj.getClass() + "@" + Integer.toHexString(System.identityHashCode(obj));
        }
    }
}
