import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class StringSelectorExample {
    private static List<String> strings = Arrays.asList(
            "Two roads diverged in a yellow wood,",
            "And sorry I could not travel both",
            "And be one traveler, long I stood",
            "And looked down one as far as I could",
            "To where it bent in the undergrowth;",

            "Then took the other, as just as fair,",
            "And having perhaps the better claim,",
            "Because it was grassy and wanted wear;",
            "Though as for that the passing there",
            "Had worn them really about the same,",

            "And both that morning equally lay",
            "In leaves no step had trodden black.",
            "Oh, I kept the first for another day!",
            "Yet knowing how way leads on to way,",
            "I doubted if I should ever come back.",

            "I shall be telling this with a sigh",
            "Somewhere ages and ages hence:",
            "Two roads diverged in a wood, and I—",
            "I took the one less traveled by,",
            "And that has made all the difference.");

    public static void main(String[] args) {
        System.out.println("All:");
        List<String> selected = StringSelectorExample.select(strings, x -> true);
        printStrings(selected, System.lineSeparator());

        System.out.println("Lines starting with 'I ':");
        selected = StringSelectorExample.select(strings, x -> x.startsWith("I "));
        printStrings(selected, System.lineSeparator());

        System.out.println("Lines starting with 'And ':");
        selected = StringSelectorExample.select(strings, x -> x.startsWith("And "));
        printStrings(selected, System.lineSeparator());

        System.out.println("Lines containing 'road':");
        selected = StringSelectorExample.select(strings, x -> x.contains("road"));
        printStrings(selected, null);
    }

    public static List<String> select(Collection<String> strings, Predicate<String> selector) {
        List<String> selectedStrings = new ArrayList<>();

        for (String aString : strings) {
            if (selector.test(aString)) {
                selectedStrings.add(aString);
            }
        }

        return selectedStrings;
    }

    private static void printStrings(List<String> strings, String suffix) {
        for (String str : strings) {
            System.out.println(str);
        }

        if (suffix != null) {
            System.out.print(suffix);
        }
    }
}
