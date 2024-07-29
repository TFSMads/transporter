package ml.volder.unikapi.wrappers.guibutton;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.logger.Logger;

import java.lang.reflect.Constructor;

public class WrappedGuiButton {

    private IGuiButtonImpl impl;

    public WrappedGuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    private static ApiProvider<Class> apiProvider = new ApiProvider<>("WrappedGuiButton");

    public WrappedGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        Class<? extends IGuiButtonImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.wrappers.guibutton.impl", ApiReferenceStorage::getVersionedGuiButton,IGuiButtonImpl.class);
        if(klass != null) {
            try {
                Constructor constructor = klass.getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class, String.class, WrappedGuiButton.class);
                impl = (IGuiButtonImpl) constructor.newInstance(buttonId, x, y, widthIn, heightIn, buttonText, this);
            } catch (Exception e) {
                UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.FINE, e);
            }
        }
    }

    public WrappedGuiButton(IGuiButtonImpl refference)
    {
        impl = refference;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    public int getHoverState(boolean mouseOver)
    {
        return impl.getCurrentHoverState(mouseOver);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(int mouseX, int mouseY)
    {
        impl.drawButton(mouseX, mouseY);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    public void mouseDragged(int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(int mouseX, int mouseY)
    {
        return impl.mousePressed(mouseX, mouseY);
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return impl.isHovered();
    }

    public int getId() {
        return impl.getId();
    }

    public <T> T getHandle(Class<T> c){
        return impl.getHandle(c);
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void setX(int x) {
        impl.setX(x);
    }

    public void setY(int y) {
        impl.setY(y);
    }

    public int getX() {
        return impl.getX();
    }

    public int getY() {
        return impl.getY();
    }

    public void setWidth(int width) {
        impl.setButtonWidth(width);
    }

    public void setHeight(int height) {
        impl.setButtonHeight(height);
    }

    public int getWidth() {
        return impl.getWidth();
    }

    public int getHeight() {
        return impl.getHeight();
    }

    public void setEnabled(boolean isEnabled) {
        impl.setEnabled(isEnabled);
    }

    public boolean isEnabled() {
        return impl.isEnabled();
    }

    public void setDisplayString(String displayString) {
        impl.setDisplayString(displayString);
    }

    public String getDisplayString() {
        return impl.getDisplayString();
    }
}
