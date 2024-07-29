package ml.volder.unikapi.wrappers.guibutton;

public interface IGuiButtonImpl {
    int getCurrentHoverState(boolean mouseOver);
    boolean mousePressed(int mouseX, int mouseY);
    boolean isHovered();
    void drawButton(int mouseX, int mouseY);

    void setX(int x);
    void setY(int y);
    int getX();
    int getY();
    void setButtonWidth(int width);
    void setButtonHeight(int height);
    int getWidth();
    int getHeight();
    void setEnabled(boolean isEnabled);
    boolean isEnabled();
    void setDisplayString(String displayString);
    String getDisplayString();




    int getId();
    <T> T getHandle(Class<T> c);
}
