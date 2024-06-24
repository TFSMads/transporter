package ml.volder.transporter.utils;

public class Parser {

  public static Integer parseInt(String input) {
    input = input.replace(".", "");
    return Integer.parseInt(input);
  }

  public static String parseFormattedItemName(String input) {
    input = input.replace(" ", "_");
    return input.toLowerCase();
  }

}
