

public class Inspector {

    public static void main(String[] args) {
        new Inspector().inspect(new ClassD[12], true);
    }

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        String prefix = stringBuilder.toString();


        //Name
        System.out.println(prefix + "The class name is " + c.getSimpleName());


        //Supper Class
        if (c.getSuperclass() != null) {
            System.out.println(prefix + " The super class is " + c.getSuperclass().getSimpleName());
            inspectClass(c.getSuperclass(), c.getSuperclass().cast(obj), recursive, depth + 1);
        }

    }
}
