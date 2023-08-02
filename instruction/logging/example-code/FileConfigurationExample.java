import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Specify the configuration file by adding the following VM option in the run configuration
 * or you will get the default logging configuration:
 * <p>
 * -Djava.util.logging.config.file=logging.properties
 */
public class FileConfigurationExample {

    private static Logger logger = Logger.getLogger("FileConfigurationExample");

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
