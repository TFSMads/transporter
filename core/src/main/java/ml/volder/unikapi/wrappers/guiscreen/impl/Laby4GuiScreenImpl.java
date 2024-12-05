package ml.volder.unikapi.wrappers.guiscreen.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.api.input.impl.Laby4InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.keysystem.impl.Laby4KeyMapper;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guibutton.impl.Laby4GuiButton;
import ml.volder.unikapi.wrappers.guiscreen.IGuiScreenImpl;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.render.matrix.Stack;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4GuiScreenImpl extends LabyScreen implements IGuiScreenImpl {

  public WrappedGuiScreen getWrapper() {
    return screen;
  }

  WrappedGuiScreen screen;
  List<Laby4GuiButton> buttonList = new ArrayList<>();
  private Laby4GuiButton selectedButton;

  private int width;
  private int height;

  public Laby4GuiScreenImpl(WrappedGuiScreen screen) {
    this.screen = screen;
  }

  //region Methods from interface IGuiScreenImpl
  @Override
  public void addButton(WrappedGuiButton wrappedGuiButton) {
    buttonList.add(wrappedGuiButton.getHandle(Laby4GuiButton.class));
  }

  @Override
  public void removeButton(WrappedGuiButton wrappedGuiButton) {
    buttonList.remove(wrappedGuiButton.getHandle(Laby4GuiButton.class));
  }

  @Override
  public void clearButtonList() {
    buttonList.clear();
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public void setWidth(int width) {
    this.resize(width, getHeight());
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void setHeight(int height) {
    this.resize(getWidth(), height);

  }
  //endregion

  //region Methods from LabyScreen class

  @Override
  public void resize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(ScreenContext context) {
    internalRender(context.stack(), context.mouse(), context.getTickDelta());
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    //internalRender(stack, mouse, partialTicks);
  }

  private void internalRender(Stack stack, MutableMouse mouse, float partialTicks) {
    Stack currentStack = Laby4DrawAPI.CURRENT_RENDER_STACK;
    Laby4DrawAPI.CURRENT_RENDER_STACK = stack;
    screen.drawScreen(mouse.getX(), mouse.getY(), partialTicks);
    for (int i = 0; i < this.buttonList.size(); ++i)
    {
      this.buttonList.get(i).drawButton(stack, mouse.getX(), mouse.getY());
    }
    Laby4DrawAPI.CURRENT_RENDER_STACK = currentStack;
  }

  @Override
  public boolean renderBackground(ScreenContext context) {
    return true;
  }

  @Override
  public void renderOverlay(ScreenContext context) {

  }

  @Override
  public void renderHoverComponent(ScreenContext context) {

  }

  @Override
  public void onCloseScreen() {
    super.onCloseScreen();
    screen.onGuiClosed();
  }

  @Override
  public @NotNull Object mostInnerScreen() {
    return this;
  }

  @Override
  public @NotNull ScreenInstance mostInnerScreenInstance() {
    return this;
  }


  @Override
  public void tick() {

  }

  @Override
  public void initialize(Parent parent) {
    buttonList.clear();
    screen.initGui();
  }

  @Override
  protected void postInitialize() {

  }

  @Override
  public void updateKeyRepeatingMode(boolean enabled) {
    Laby.labyAPI().minecraft().updateKeyRepeatingMode(enabled);
  }

  @Override
  public Parent getParent() {
    return null;
  }

  @Override
  public Bounds bounds() {
    return null;
  }

  @Override
  public Parent getRoot() {
    return null;
  }

  @Override
  public boolean keyPressed(Key key, InputType type) {
    if(key.equals(Key.ESCAPE)) {
      PlayerAPI.getAPI().openGuiScreen(null);
    }
    ml.volder.unikapi.keysystem.Key convertedKey = Laby4KeyMapper.convert(key);
    if(convertedKey.isCharacter() && !Laby4InputAPI.getAPI().isCtrlKeyDown())
      return false;
    if(Laby4InputAPI.getAPI().isCtrlKeyDown() && Laby4InputAPI.getAPI().isAltKeyDown())
      return false;
    screen.keyTyped(convertedKey.getCharacter(), convertedKey);
    return false;
  }

  @Override
  public boolean keyReleased(Key key, InputType type) {
    return false;
  }

  @Override
  public boolean charTyped(Key key, char character) {
    screen.keyTyped(character, Laby4KeyMapper.convert(key));
    return false;
  }

  @Override
  public boolean mouseReleased(MutableMouse mouse, MouseButton mouseButton) {
    if (this.selectedButton != null && mouseButton.isLeft())
    {
      this.selectedButton.mouseReleased(mouse.getX(), mouse.getY());
      this.selectedButton = null;
    }
    screen.mouseReleased(mouse.getX(), mouse.getY(), Laby4KeyMapper.convert(mouseButton));
    return false;
  }

  @Override
  public boolean mouseClicked(MutableMouse mouse, MouseButton mouseButton) {
    screen.mouseClicked(mouse.getX(), mouse.getY(), Laby4KeyMapper.convert(mouseButton));

    if (mouseButton.isLeft())
    {
      for (int i = 0; i < this.buttonList.size(); ++i)
      {
        Laby4GuiButton guibutton = this.buttonList.get(i);

        if (guibutton.mousePressed(mouse.getX(), mouse.getY()))
        {
          this.selectedButton = guibutton;
          guibutton.playPressSound();
          screen.actionPerformed(guibutton.getWrapper());
        }
      }
    }

    return false;
  }

  @Override
  public boolean mouseScrolled(MutableMouse mouse, double scrollDelta) {
    Laby4InputAPI.SCROLL_DELTA = (float) scrollDelta;
    screen.handleMouseInput();
    return false;
  }

  @Override
  public boolean mouseDragged(MutableMouse mouse, MouseButton button, double deltaX, double deltaY) {
    screen.mouseClickMove(mouse.getX(), mouse.getY(), Laby4KeyMapper.convert(button), 0);
    return false;
  }

  @Override
  public boolean fileDropped(MutableMouse mouse, List<Path> paths) {
    return false;
  }

  @Override
  public void doScreenAction(String action, Map<String, Object> parameters) {

  }
  //endregion
}
