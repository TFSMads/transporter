package ml.volder.unikapi.wrappers.guiscreen;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;

import java.lang.reflect.Constructor;

public abstract class WrappedGuiScreen {

    private IGuiScreenImpl impl;
    private static ApiProvider<Class> apiProvider = new ApiProvider<>("WrappedGuiScreen");
    public WrappedGuiScreen() {
        Class<? extends IGuiScreenImpl> klass = ApiManager.getClassAPI(apiProvider, "ml.volder.unikapi.wrappers.guiscreen.impl", ApiReferenceStorage::getVersionedGuiScreen,IGuiScreenImpl.class);
        if(klass != null) {
            try {
                Constructor constructor = klass.getDeclaredConstructor(WrappedGuiScreen.class);
                impl = (IGuiScreenImpl) constructor.newInstance(this);
            } catch (Exception e) {
                UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.FINE, e);
            }
        }
    }

    public abstract void updateScreen();

    public abstract void initGui();

    public abstract void actionPerformed(WrappedGuiButton button);

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton);

    public abstract void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick);

    public abstract void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton);

    public abstract void handleMouseInput();

    public abstract void keyTyped(char typedChar, Key key);

    public abstract void onGuiClosed();

    public <T> T  getHandle(Class<T> c) {
        return (T) impl;
    }

    public void addButton(WrappedGuiButton button) {
        impl.addButton(button);
    }

    public void removeButton(WrappedGuiButton button) {
        impl.removeButton(button);
    }

    public void clearButtonList() {
        impl.clearButtonList();
    }

    public int getWidth() {
        return impl.getWidth();
    }

    public void setWidth(int width) {
        impl.setWidth(width);
    }

    public int getHeight() {
        return impl.getHeight();
    }

    public void setHeight(int height) {
        impl.setHeight(height);
    }

}
