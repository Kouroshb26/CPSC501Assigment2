

public class Inspector {

    public static void main(String[] args) {
        new Inspector().inspect(new ClassD[12], true);
    }

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

    }
}
