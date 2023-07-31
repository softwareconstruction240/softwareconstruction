public class SpecialCharacterExamples {

    public static void main(String [] args) {
        String stringWithNewline = "Hello\nBYU";
        System.out.println(stringWithNewline);

        System.out.println();

        String stringWithTab = "Hello\tBYU";
        System.out.println(stringWithTab);

        System.out.println();

        String stringWithBackspace = "Hello BYU\b";
        System.out.println(stringWithBackspace);

        System.out.println();

        System.out.println("A \u22C0 B \u21D2 A");
    }
}
