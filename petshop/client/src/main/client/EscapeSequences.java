package client;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    private static final String SET_TEXT_COLOR = "\u001b[";

    public static final String GREEN = SET_TEXT_COLOR + "36m";
    public static final String BLUE = SET_TEXT_COLOR + "32m";
    public static final String RED = SET_TEXT_COLOR + "31m";
    public static final String RESET = SET_TEXT_COLOR + "0m";
}