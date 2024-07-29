package ml.volder.unikapi.wrappers.guiscreen;

import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;

public interface IGuiScreenImpl {
    void addButton(WrappedGuiButton button);
    void removeButton(WrappedGuiButton button);
    void clearButtonList();

    int getWidth();
    void setWidth(int width);
    int getHeight();
    void setHeight(int height);


}
