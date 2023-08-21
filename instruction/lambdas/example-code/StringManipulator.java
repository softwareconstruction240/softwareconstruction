import java.util.function.Function;

public class StringManipulator {
    public static void main(String[] args) {
        StringManipulator manipulator = new StringManipulator();
        System.out.println(manipulator.manipulateString("my string", str -> str == null ? null : str.toUpperCase()));
        System.out.println(manipulator.manipulateString("my string with spaces that will be removed", str -> str == null ? null : str.replaceAll(" ", "")));

    }
    public String manipulateString(String str, Function<String, String> manipulationFunction) {
        return manipulationFunction.apply(str);
    }
}
