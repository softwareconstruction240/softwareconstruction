import java.util.Arrays;
import java.util.Comparator;

public class ComparatorWithoutLambda {
    public static void main(String[] args) {
        Arrays.sort(args, new StringLengthComparator());
    }

    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String first, String second) {
            return Integer.compare(first.length(), second.length());}
    }
}
