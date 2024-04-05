package ml.volder.unikapi.logger;

public class DefaultLogger implements Logger{

    private java.util.logging.Logger logger;
    public DefaultLogger(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    @Override
    public void log(LOG_LEVEL logLevel, String string) {
        if(!isEnabled(logLevel))
            return;
        switch (logLevel) {
            case FINEST:
                logger.finest(string);
                break;
            case FINER:
                logger.finer(string);
                break;
            case FINE:
                logger.fine(string);
                break;
            case INFO:
                logger.info(string);
                break;
            case WARNING:
                logger.warning(string);
                break;
            case SEVERE:
                logger.severe(string);
                break;
        }
    }
}
