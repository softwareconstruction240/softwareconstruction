import java.util.Arrays;

public class ComparatorWithLambda {
    public static void main(String[] args) {
        Arrays.sort(args, (first, second) -> Integer.compare(first.length(), second.length()));
    }
}
