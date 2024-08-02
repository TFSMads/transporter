package ml.volder.transporter.utils;

import org.jetbrains.annotations.Nullable;

public class Parser {

  @Nullable
  public static Integer tryParseInt(String input) {
    try {
      return parseInt(input);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static Integer parseInt(String input) throws NumberFormatException {
    input = input.replace(".", "");
    return Integer.parseInt(input);
  }

  public static String parseFormattedItemName(String input) {
    input = input.replace(" ", "_");
    return input.toLowerCase();
  }

}
