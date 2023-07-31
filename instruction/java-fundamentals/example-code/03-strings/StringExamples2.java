public class StringExamples2 {

    public static void main(String [] args) {
        // length() example
        System.out.println("length() example:");
        String s1 = "Hello";
        System.out.printf("'%s' has %d characters\n", s1, s1.length());
        System.out.println();

        // charAt() example
        System.out.println("charAt() example:");
        System.out.printf("The character at position 1 is '%c'\n", s1.charAt(1));
        System.out.println();

        // trim() example
        System.out.println("trim() example:");
        String untrimmed = " Hello CS 240 Students   ";
        System.out.printf("The trimmed version of '%s' is '%s'\n", untrimmed, untrimmed.trim());
        System.out.println();

        // startsWith(String) example
        System.out.println("startsWith(String) example:");
        System.out.printf("True or False, Untrimmed starts with 'H': %b\n", untrimmed.startsWith("H"));
        System.out.printf("True or False, Untrimmed starts with ' ': %b\n", untrimmed.startsWith(" "));
        System.out.println();

        // indexOf examples
        System.out.println("indexOf examples:");
        System.out.printf("Index of %c is %d\n", 'e', s1.indexOf('e'));
        System.out.printf("Index of %s is %d\n", "He", s1.indexOf("He"));
        System.out.printf("Index of %s is %d\n", "Her", s1.indexOf("Her"));
        System.out.println();

        //substring examples
        System.out.println("substring examples:");
        System.out.printf("%s\n", s1.substring(1));
        System.out.printf("%s\n", s1.substring(3, 5));
    }
}
