package ml.volder.transporter.modules.guimodules;

import ml.volder.unikapi.types.ModColor;

public class Text {
    private String text;
    private int color;

    public Text(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public Text(String text, int color, boolean bold, boolean italic, boolean underline) {
        if (bold) {
            text = ModColor.cl('l') + text;
        }

        if (italic) {
            text = ModColor.cl('o') + text;
        }

        if (underline) {
            text = ModColor.cl('n') + text;
        }

        this.text = text;
        this.color = color;
    }

    public static Text getText(String text) {
        return new Text(text, 16777215);
    }

    public static Text getText(String text, int color) {
        return new Text(text, color);
    }

    public static Text getText(String text, int r, int g, int b) {
        return getText(text, ModColor.toRGB(r, g, b, 255));
    }

    public static Text getText(String text, int r, int g, int b, int a) {
        return getText(text, ModColor.toRGB(r, g, b, a));
    }

    public boolean isDefaultColor() {
        return this.color == 16777215;
    }

    public String getText() {
        return this.text;
    }

    public int getColor() {
        return this.color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
