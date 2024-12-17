package ml.volder.transporter.gui;

import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.api.input.impl.Laby4InputAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guibutton.impl.Laby4GuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.Mouse;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.render.matrix.Stack;

import java.util.ArrayList;
import java.util.List;

public class TransporterActivity extends SimpleActivity {

    List<Laby4GuiButton> buttonList = new ArrayList<>();
    private Laby4GuiButton selectedButton;

    public void addButton(WrappedGuiButton wrappedGuiButton) {
        buttonList.add(wrappedGuiButton.getHandle(Laby4GuiButton.class));
    }

    public void removeButton(WrappedGuiButton wrappedGuiButton) {
        buttonList.remove(wrappedGuiButton.getHandle(Laby4GuiButton.class));
    }

    public void clearButtonList() {
        buttonList.clear();
    }

    public int getWidth() {
        return (int) this.bounds().getWidth();
    }

    public void setWidth(int width) {
        this.resize(width, getHeight());
    }

    public int getHeight() {
        return (int) this.bounds().getHeight();
    }

    public void setHeight(int height) {
        this.resize(getWidth(), height);

    }

    @Override
    public void render(ScreenContext context) {
        Stack currentStack = Laby4DrawAPI.CURRENT_RENDER_STACK;
        Laby4DrawAPI.CURRENT_RENDER_STACK = context.stack();

        Mouse mouse = context.mouse();

        this.drawScreen(mouse.getX(), mouse.getY(), context.getTickDelta());
        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            this.buttonList.get(i).drawButton(context.stack(), mouse.getX(), mouse.getY());
        }
        Laby4DrawAPI.CURRENT_RENDER_STACK = currentStack;
    }

    @Override
    public void onCloseScreen() {
        super.onCloseScreen();
        this.onGuiClosed();
    }

    @Override
    public void initialize(Parent parent) {
        buttonList.clear();
        this.initGui();
    }

    @Override
    public boolean keyPressed(net.labymod.api.client.gui.screen.key.Key key, InputType type) {
        if(key.equals(net.labymod.api.client.gui.screen.key.Key.ESCAPE)) {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
        }
        ml.volder.unikapi.keysystem.Key convertedKey = Laby4KeyMapper.convert(key);
        if(convertedKey.isCharacter() && !Laby4InputAPI.getAPI().isCtrlKeyDown())
            return false;
        if(Laby4InputAPI.getAPI().isCtrlKeyDown() && Laby4InputAPI.getAPI().isAltKeyDown())
            return false;
        this.keyTyped(convertedKey.getCharacter(), convertedKey);
        return false;
    }

    @Override
    public boolean charTyped(net.labymod.api.client.gui.screen.key.Key key, char character) {
        this.keyTyped(character, Laby4KeyMapper.convert(key));
        return false;
    }

    @Override
    public boolean mouseReleased(MutableMouse mouse, net.labymod.api.client.gui.screen.key.MouseButton mouseButton) {
        if (this.selectedButton != null && mouseButton.isLeft())
        {
            this.selectedButton.mouseReleased(mouse.getX(), mouse.getY());
            this.selectedButton = null;
        }
        this.mouseReleased(mouse.getX(), mouse.getY(), Laby4KeyMapper.convert(mouseButton));
        return false;
    }

    @Override
    public boolean mouseClicked(MutableMouse mouse, net.labymod.api.client.gui.screen.key.MouseButton mouseButton) {
        this.mouseClicked(mouse.getX(), mouse.getY(), Laby4KeyMapper.convert(mouseButton));

        if (mouseButton.isLeft())
        {
            for (int i = 0; i < this.buttonList.size(); ++i)
            {
                Laby4GuiButton guibutton = this.buttonList.get(i);

                if (guibutton.mousePressed(mouse.getX(), mouse.getY()))
                {
                    this.selectedButton = guibutton;
                    guibutton.playPressSound();
                    this.actionPerformed(guibutton.getWrapper());
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(MutableMouse mouse, double scrollDelta) {
        Laby4InputAPI.SCROLL_DELTA = (float) scrollDelta;
        this.handleMouseInput();
        return false;
    }

    @Override
    public boolean mouseDragged(MutableMouse mouse, net.labymod.api.client.gui.screen.key.MouseButton button, double deltaX, double deltaY) {
        this.mouseClickMove(mouse.getX(), mouse.getY(), Laby4KeyMapper.convert(button), 0);
        return false;
    }


    @Override
    public void updateKeyRepeatingMode(boolean enabled) {
        Laby.labyAPI().minecraft().updateKeyRepeatingMode(enabled);
    }

    // Methods for use in subclasses
    public void updateScreen() {

    }

    public void initGui() {

    }

    public void actionPerformed(WrappedGuiButton button) {

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {

    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {

    }

    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {

    }

    public void handleMouseInput() {

    }

    public void keyTyped(char typedChar, Key key) {

    }

    public void onGuiClosed() {

    }
}
