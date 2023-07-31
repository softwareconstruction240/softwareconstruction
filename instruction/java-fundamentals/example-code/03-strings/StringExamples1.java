public class StringExamples1 {

    public static void main(String [] args) {
        // String declaration and assignment
        String s1 = "Hello";
        String s2 = new String("Hello");

        // Reassignment of the s2 reference
        s2 = "BYU";

        // String concatenation
        String s3 = s1 + " " + s2;
        System.out.println(s3);

        // String formatting
        s3 = String.format("%s %s", s1, s2);
        System.out.println(s3);

        // Formatted printing
        System.out.printf("%s %s", s1, s2);
    }
}
