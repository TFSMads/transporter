package ml.volder.unikapi.utils;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;

public class ColorUtils {

    public static TextColor getTextColorFromColorCode(char colorCode) {
        switch (colorCode) {
            case '0':
                return NamedTextColor.BLACK;
            case '1':
                return NamedTextColor.DARK_BLUE;
            case '2':
                return NamedTextColor.DARK_GREEN;
            case '3':
                return NamedTextColor.DARK_AQUA;
            case '4':
                return NamedTextColor.DARK_RED;
            case '5':
                return NamedTextColor.DARK_PURPLE;
            case '6':
                return NamedTextColor.GOLD;
            case '7':
                return NamedTextColor.GRAY;
            case '8':
                return NamedTextColor.DARK_GRAY;
            case '9':
                return NamedTextColor.BLUE;
            case 'a':
                return NamedTextColor.GREEN;
            case 'b':
                return NamedTextColor.AQUA;
            case 'c':
                return NamedTextColor.RED;
            case 'd':
                return NamedTextColor.LIGHT_PURPLE;
            case 'e':
                return NamedTextColor.YELLOW;
            case 'l':
                return TextColor.color(0,0,1);
            case 'n':
                return TextColor.color(0,0,2);
            case 'o':
                return TextColor.color(0,0,3);
            case 'm':
                return TextColor.color(0,0,4);
            case 'k':
                return TextColor.color(0,0,5);
            case 'r':
                return NamedTextColor.WHITE;
            default:
                return NamedTextColor.WHITE;
        }
    }

    public static TextDecoration getTextDecorationFromDecorationCode(char decorationCode) {
        switch (decorationCode) {
            case 'l':
                return TextDecoration.BOLD;
            case 'n':
                return TextDecoration.UNDERLINED;
            case 'o':
                return TextDecoration.ITALIC;
            case 'm':
                return TextDecoration.STRIKETHROUGH;
            case 'k':
                return TextDecoration.OBFUSCATED;
            default:
                return null;
        }
    }

    public static int toRGB(int r, int g, int b, int a) {
        return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
    }

    /***
     * Colorize a string with the Minecraft color codes
     * @param string the string to colorize
     * @return a colorized Component
     */
    public static Component createColoredComponent(String string) {
        return createColoredComponent(string, false);
    }

    /***
     * Colorize a string with the Minecraft color codes
     * @param string the string to colorize
     * @return a colorized Component
     */
    public static Component createColoredComponent(String string, boolean removeColorCodes) {
        Component returnComponent = Component.text("");

        //Seperate the string into different colored parts
        String[] parts = string.split("&");

        TextColor currentColor = NamedTextColor.WHITE;
        TextDecoration currentDecoration = null;

        boolean first = true;

        //Iterate over the parts
        for (String part : parts) {
            //If the part is empty, skip it
            if (part.isEmpty()) {
                continue;
            }

            boolean isColorCode = false;

            //Get the color code
            char colorCode = part.charAt(0);
            if(colorCode =='0' ||
                    colorCode == '1' ||
                    colorCode == '2' ||
                    colorCode == '3' ||
                    colorCode == '4' ||
                    colorCode == '5' ||
                    colorCode == '6' ||
                    colorCode == '7' ||
                    colorCode == '8' ||
                    colorCode == '9' ||
                    colorCode == 'a' ||
                    colorCode == 'b' ||
                    colorCode == 'c' ||
                    colorCode == 'd' ||
                    colorCode == 'e' ||
                    colorCode == 'f' ||
                    colorCode == 'l' ||
                    colorCode == 'n' ||
                    colorCode == 'o' ||
                    colorCode == 'm' ||
                    colorCode == 'k' ||
                    colorCode == 'r') {
                isColorCode = removeColorCodes;
            }
            //Get the color
            TextColor color = getTextColorFromColorCode(colorCode);
            if(!(color.color().getRed() == 0
                    && color.color().getGreen() == 0
                    && color.color().getBlue() >= 1
                    && color.color().getBlue() <= 5)) {
                currentColor = color;
            }

            TextDecoration decoration = getTextDecorationFromDecorationCode(colorCode);
            if (decoration != null) {
                currentDecoration = decoration;
            }
            if(decoration == null) {
                if(colorCode == 'r') {
                    currentColor = NamedTextColor.WHITE;
                    currentDecoration = null;
                }
            }

            if(isColorCode) {
                part = part.substring(1);
            }

            //Create a new component with the color
            Component component = Component
                    .text(((first && !string.startsWith("&")) || isColorCode ? "" : "&") + part)
                    .color(currentColor);

            if(currentDecoration != null) {
                component.undecorate(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED);
                component = component.decorate(currentDecoration);
            } else {
                component.undecorate(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED);
            }

            //Append the component to the return component
            returnComponent = returnComponent.append(component);
            first = false;
        }

        if((string.endsWith("&") && string.length() > 1) || string.equals("&")) {
            if(!removeColorCodes)
                returnComponent = returnComponent.append(Component.text("&"));
        }

        return returnComponent;
    }

}
