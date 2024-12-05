package ml.volder.unikapi.wrappers.guibutton.impl;

import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.wrappers.guibutton.IGuiButtonImpl;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.render.draw.ResourceRenderer;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
@SupportedClient(clientBrand = "labymod4", minecraftVersion = "*")
public class Laby4GuiButton implements IGuiButtonImpl {

  public WrappedGuiButton getWrapper() {
    return guiButton;
  }

  private WrappedGuiButton guiButton;

  protected static final ResourceLocation buttonTextures = ModTextures.WIDGETS.toLaby();
  /** Button width in pixels */
  public int width;
  /** Button height in pixels */
  public int height;
  /** The x position of this control. */
  public int xPosition;
  /** The y position of this control. */
  public int yPosition;
  /** The string displayed on this control. */
  public String displayString;
  public int id;
  /** True if this control is enabled, false to disable. */
  public boolean enabled;
  /** Hides the button completely if false. */
  public boolean visible;
  protected boolean hovered;
  public int packedFGColour; //FML

  private boolean callDrawButton = true;

  public Laby4GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, WrappedGuiButton button) {
    this(buttonId, x, y, widthIn, heightIn, buttonText);
    this.guiButton = button;
  }

  public Laby4GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
  {
    this.width = 200;
    this.height = 20;
    this.enabled = true;
    this.visible = true;
    this.id = buttonId;
    this.xPosition = x;
    this.yPosition = y;
    this.width = widthIn;
    this.height = heightIn;
    this.displayString = buttonText;
  }


  @Override
  public boolean isHovered() {
    return this.isMouseOver();
  }

  @Override
  public void drawButton(int i, int i1) {
    callDrawButton = false;
    this.drawButton(Laby4DrawAPI.getRenderStack(), i, i1);
  }

  @Override
  public void setX(int i) {
    this.xPosition = i;
  }

  @Override
  public void setY(int i) {
    this.yPosition = i;
  }

  @Override
  public int getX() {
    return xPosition;
  }

  @Override
  public int getY() {
    return yPosition;
  }

  @Override
  public void setButtonWidth(int i) {
    this.width = i;
  }

  @Override
  public void setButtonHeight(int i) {
    this.height = i;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public void setEnabled(boolean b) {
    this.enabled = b;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setDisplayString(String s) {
    this.displayString = s;
  }

  @Override
  public String getDisplayString() {
    return this.displayString;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public <T> T getHandle(Class<T> aClass) {
    return (T)this;
  }

  @Override
  public int getCurrentHoverState(boolean b) {
    return getHoverState(b);
  }

  /**
   * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
   * this button.
   */
  protected int getHoverState(boolean mouseOver)
  {
    int i = 1;

    if (!this.enabled)
    {
      i = 0;
    }
    else if (mouseOver)
    {
      i = 2;
    }

    return i;
  }

  /**
   * Draws this button to the screen.
   */
  public void drawButton(Stack stack, int mouseX, int mouseY)
  {
    if (this.visible)
    {
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
      int i = this.getHoverState(this.hovered);

      ResourceRenderer resourceRenderer = Laby.references().resourceRenderer();

      Laby4DrawAPI.bindLabyTexture(buttonTextures);
      DrawAPI drawAPI = DrawAPI.getAPI();

      drawAPI.drawTexturedModalRect(xPosition, yPosition, 0, 46 + i * 20, width / 2, height);
      drawAPI.drawTexturedModalRect(xPosition + this.width / 2, yPosition, 200 - this.width / 2, 46 + i * 20, width / 2, height);

      this.mouseDragged(mouseX, mouseY);
      int j = 14737632;

      if (packedFGColour != 0)
      {
        j = packedFGColour;
      }
      else
      if (!this.enabled)
      {
        j = 10526880;
      }
      else if (this.hovered)
      {
        j = 16777120;
      }

      TextRenderer textRenderer = Laby4DrawAPI.getTextRenderer();
      textRenderer.pos(this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2)
          .shadow(true)
          .centered(true)
          .scale(1)
          .color(j)
          .text(displayString);

      textRenderer.render(stack);
    }
    if(callDrawButton)
      guiButton.drawButton(mouseX, mouseY);
    callDrawButton = true;
  }

  /**
   * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
   */
  protected void mouseDragged(int mouseX, int mouseY)
  {
    guiButton.mouseDragged(mouseX, mouseY);
  }

  /**
   * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
   */
  public void mouseReleased(int mouseX, int mouseY)
  {
    guiButton.mouseReleased(mouseX, mouseY);
  }

  /**
   * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
   * e).
   */
  public boolean mousePressed(int mouseX, int mouseY)
  {
    return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
  }

  /**
   * Whether the mouse cursor is currently over the button.
   */
  public boolean isMouseOver()
  {
    return this.hovered;
  }

  public void drawButtonForegroundLayer(int mouseX, int mouseY)
  {
    guiButton.drawButtonForegroundLayer(mouseX, mouseY);
  }

  public void playPressSound()
  {
    Laby.references().minecraftSounds().playButtonPress();
  }

  public int getButtonWidth()
  {
    return this.width;
  }

  public void setWidth(int width)
  {
    this.width = width;
  }

}
