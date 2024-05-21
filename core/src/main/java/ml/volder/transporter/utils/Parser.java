package ml.volder.transporter.utils;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger.DEBUG_LEVEL;

public class Parser {

  public static Integer parseInt(String input) {
    input = input.replace(".", "");
    UnikAPI.LOGGER.debug("Parsing input: " + input + " (" + Integer.parseInt(input) + ")", DEBUG_LEVEL.TESTING);
    return Integer.parseInt(input);
  }

  public static String parseFormattedItemName(String input) {
    input = input.replace(" ", "_");
    return input.toLowerCase();
  }

}
