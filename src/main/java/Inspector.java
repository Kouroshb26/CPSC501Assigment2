

public class Inspector {

    public static void main(String[] args) {
        new Inspector().inspect(new ClassD[12], true);
    }

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {


        //Name
        println("The class name is " + c.getSimpleName(), depth);


        //Supper Class
        if (c.getSuperclass() != null) {
            println(" The super class is " + c.getSuperclass().getSimpleName(), depth);
            inspectClass(c.getSuperclass(), c.getSuperclass().cast(obj), recursive, depth + 1);
        }

    }

    public String getPrefixString(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }


    public void println(String output, int depth) {
        System.out.println(getPrefixString(depth) + output);
    }
}
