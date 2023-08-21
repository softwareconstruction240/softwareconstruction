package interfaceExample;

public class StringManipulator {
    public static void main(String[] args) {
        StringManipulator manipulator = new StringManipulator();
        System.out.println(manipulator.manipulateString("my string", new Capitalizer()));
        System.out.println(manipulator.manipulateString("my string with spaces that will be removed", new SpaceRemover()));
    }

    public String manipulateString(String str, Function<String, String> manipulationFunction) {
        return manipulationFunction.apply(str);
    }
}
