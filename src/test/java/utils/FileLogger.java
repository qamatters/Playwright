package utils;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.*;

class FileLogger {

    private static final Logger logger = Logger.getLogger(FileLogger.class.getName());
    private static FileHandler fileHandler = null;

    /**
     * Logs a message to a log file at test-output/test-<timestamp>.log
     * 
     * @param message   The message to be logged.
     * @param timestamp The timestamp for log file.
     */
    public static void log(String message, String timestamp) {
        try {
            // condition only runs on first call, otherwise each thread creates its own log file
            if (fileHandler == null){
                // Create a FileHandler that writes log messages to a file
                fileHandler = new FileHandler(String.format("test-output/logs/test-%s.log", timestamp),true);
                // fileHandler.setFormatter(new SimpleFormatter()); prints out a timestamp, Classname & methodname
                fileHandler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return record.getMessage() + System.lineSeparator();
                    }
                });
                logger.addHandler(fileHandler);
                logger.setUseParentHandlers(false);
            }

            logger.info(message);

        } catch (IOException e) {
            logger.severe("Failed to initialize logger handler: " + e.getMessage());
        }
    }
}
