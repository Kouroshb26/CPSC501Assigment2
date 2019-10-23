import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Inspector {

    public static void main(String[] args) {
        new Inspector().inspect(new ClassD[12], true);
    }

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        //Todo Not sure how to deal with arrays
        if (c.isArray()) {
            println(depth, "This class is an array type with name " + c.getSimpleName());
            println(depth, "This is an array of " + c.getComponentType().getSimpleName());
            for (int i = 0; i < Array.getLength(obj); i++) {
                inspectClass(c.getComponentType(), Array.get(obj, i), recursive, depth + 1);
            }
        }


        //Name
        println(depth, "The class name is " + c.getSimpleName());


        //Supper Class
        if (c.getSuperclass() != null) {
            println(depth, " The super class is " + c.getSuperclass().getSimpleName());
            inspectClass(c.getSuperclass(), c.getSuperclass().cast(obj), recursive, depth + 1);
        }

        //Interfaces
        if (c.getInterfaces().length > 0) {
            println(depth, " The interfaces are: " + Arrays.stream(c.getInterfaces()).map(Class::getSimpleName).collect(Collectors.joining(",")));
            Arrays.stream(c.getInterfaces()).forEach(inter -> inspectClass(inter, inter.cast(obj), recursive, depth + 1));
        }

        //Constructors
        if (c.getDeclaredConstructors().length > 1) {
            println(depth, "The constructors are:");
            for (Constructor constructor : c.getDeclaredConstructors()) {
                println(depth, "===========================================");
                println(depth, " The name is " + constructor.getName());
                println(depth, " The parameters types are (" + Arrays.stream(constructor.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",")) + ")");
                println(depth, " The modifiers are " + Modifier.toString(constructor.getModifiers()));
            }
        }

        //Declared Methods
        if (c.getDeclaredMethods().length > 1) {
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
        if (c.getDeclaredFields().length > 1) {
            println(depth, "The fields are:");
            for (Field field : c.getDeclaredFields()) {
                println(depth, "===========================================");
                println(depth, " The name is " + field.getName());
                println(depth, " The type is " + field.getType().getSimpleName());
                println(depth, " The modifiers are " + Modifier.toString(field.getModifiers()));
                try {
                    if (!Modifier.isPrivate(field.getModifiers())) {
                        Object fieldObject = field.get(obj);
                        println(depth, " The value of the field is " + fieldObject.toString());
                        if (recursive) {
                            inspectClass(fieldObject.getClass(), fieldObject, recursive, depth + 1);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getPrefixString(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }


    public void println(int depth, String output) {
        System.out.println(getPrefixString(depth) + output);
    }
}
