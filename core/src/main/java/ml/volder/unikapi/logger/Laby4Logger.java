package ml.volder.unikapi.logger;

import net.labymod.api.Laby;
import net.labymod.api.util.logging.Logging;

public class Laby4Logger implements Logger{

  private Logging logger;
  public Laby4Logger(String name) {
    this.logger = Laby.references().loggingFactory().create(name);
  }

  @Override
  public void log(LOG_LEVEL logLevel, String string) {
    if(!isEnabled(logLevel))
      return;
    switch (logLevel) {
      case FINEST:
        logger.info(string);
        break;
      case FINER:
        logger.info(string);
        break;
      case FINE:
        logger.info(string);
        break;
      case INFO:
        logger.info(string);
        break;
      case WARNING:
        logger.warn(string);
        break;
      case SEVERE:
        logger.error(string);
        break;
    }
  }
}
