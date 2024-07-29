package ml.volder.unikapi.logger;

import ml.volder.unikapi.UnikAPI;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public interface Logger {

    AtomicReference<DEBUG_LEVEL> debugLevel = new AtomicReference<>(DEBUG_LEVEL.DISABLED);
    AtomicReference<LOG_LEVEL> logLevel = new AtomicReference<>(LOG_LEVEL.INFO);
    AtomicBoolean printStackTrace = new AtomicBoolean(false);


    //Method to check if the logger is enabled at level
    default boolean isEnabled(LOG_LEVEL logLevel) {
        return this.logLevel.get().ordinal() <= logLevel.ordinal();
    }

    default void finest(String string) {
        log(LOG_LEVEL.FINEST, string);
    }
    default void finer(String string) {
        log(LOG_LEVEL.FINER, string);
    }
    default void fine(String string) {
        log(LOG_LEVEL.FINE, string);
    }
    default void info(String string) {
        log(LOG_LEVEL.INFO, string);
    }
    default void warning(String string) {
        log(LOG_LEVEL.WARNING, string);
    }
    default void severe(String string){
        log(LOG_LEVEL.SEVERE, string);
    }

    /**
     * Print the stack trace of an exception
     * @param logLevel The log level
     * @param e The exception
     */
    default void printStackTrace(LOG_LEVEL logLevel, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        //Print stack trace using logger
        log(logLevel, sw.toString());
    }

    /**
     * Log a message
     * @param logLevel The log level
     * @param string The message
     *
     * IMPORTANT: Implementations should check if the log level is enabled before logging
     */
    void log(LOG_LEVEL logLevel, String string);

    default void debug(String string) {
        debug(string, DEBUG_LEVEL.LOWEST);
    }

    default void debug(String string, DEBUG_LEVEL debugLevel){
        //return if debug level is disabled
        if(this.debugLevel.get() == DEBUG_LEVEL.DISABLED)
            return;
        //If debug level is higher than the current debug level, then return
        if(debugLevel.ordinal() < this.debugLevel.get().ordinal())
            return;
        info("[DEBUG:" + debugLevel +"] " + string);
    }

    default void printLoggerInfo() {
        UnikAPI.LOGGER.info("Debug level is " + Logger.debugLevel.get());
        UnikAPI.LOGGER.info("Log level is " + Logger.logLevel.get());
        UnikAPI.LOGGER.info("Print stack trace is " + Logger.printStackTrace.get());
        if(Logger.debugLevel.get() == Logger.DEBUG_LEVEL.TESTING) {
            testLogger();
        }
    }

    default void testLogger() {
        UnikAPI.LOGGER.info("Testing logger:");
        UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.TESTING);
        UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.LOWEST);
        UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.LOW);
        UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.MEDIUM);
        UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.HIGH);
        UnikAPI.LOGGER.debug("This is a debug message", Logger.DEBUG_LEVEL.HIGHEST);
        UnikAPI.LOGGER.finest("This is a finest message");
        UnikAPI.LOGGER.finer("This is a finer message");
        UnikAPI.LOGGER.fine("This is a fine message");
        UnikAPI.LOGGER.info("This is an info message");
        UnikAPI.LOGGER.warning("This is a warning message");
        UnikAPI.LOGGER.severe("This is a severe message");
    }

    /*
     * Debug levels:
     * TESTING: Only for testing purposes (used in development)
     * LOWEST: Least important debug messages
     * LOW:
     * MEDIUM:
     * HIGH:
     * HIGHEST: Most important debug messages
     * DISABLED: No debug messages
     */
    enum DEBUG_LEVEL {
        TESTING,
        LOWEST,
        LOW,
        MEDIUM,
        HIGH,
        HIGHEST,
        DISABLED
    }


    enum LOG_LEVEL {
        FINEST,
        FINER,
        FINE,
        INFO,
        WARNING,
        SEVERE
    }
}
