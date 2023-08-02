import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammaticConfigurationExample {
    private static Logger logger;

    static {
        try {
            initLog();
        } catch (IOException e) {
            System.out.println("Could not initialize log: " + e.getMessage());
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.FINEST;

        logger = Logger.getLogger("ProgrammaticConfigurationExample");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
//        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        FileHandler fileHandler = new FileHandler("log.txt", false);
        fileHandler.setLevel(logLevel);
//        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    public static void main(String[] args) {
        logger.info("This is my info log message.");
        logger.fine("This is my fine log message");

        try {
            throw new Exception("An exception occurred");
        } catch (Exception ex) {
            logger.severe("This is my exception message");
            logger.log(Level.SEVERE, "This is my message logged with the exception", ex);
        }
    }
}
