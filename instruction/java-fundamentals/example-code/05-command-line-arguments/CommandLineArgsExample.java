public class CommandLineArgsExample {

    public static void main(String [] args) {

        for(int i = 0; i < args.length; i++) {
            String message = String.format("Argument %d is %s", i, args[i]);
            System.out.println(message);
        }
    }
}
