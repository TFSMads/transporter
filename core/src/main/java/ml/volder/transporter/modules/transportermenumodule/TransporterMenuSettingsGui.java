package ml.volder.transporter.modules.transportermenumodule;

import java.util.ArrayList;
import java.util.List;
import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

public class TransporterMenuSettingsGui extends WrappedGuiScreen {

  private int entryWidth = 120;
  private int entryHeight = 37;
  private Scrollbar scrollbar = new Scrollbar(entryHeight);
  private List<TransporterSettingsMenuEntry> itemEntries = new ArrayList<>();

  private WrappedGuiButton buttonBack;

  @Override
  public void updateScreen() {

  }

  @Override
  public void initGui() {
    itemEntries.clear();
    for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
      itemEntries.add(new TransporterSettingsMenuEntry(item, entryWidth, entryHeight));
    }
    this.scrollbar.setSpeed(39);
    this.scrollbar.init();
    this.buttonBack = new WrappedGuiButton(1, 20, 20, 22, 20, "<");
    this.addButton(buttonBack);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    DrawAPI drawAPI = DrawAPI.getAPI();
    drawAPI.drawAutoDimmedBackground(0);

    double emptySpaceWidth = getWidth();
    int entryAmountHorizontal = (int)Math.floor(emptySpaceWidth / (entryWidth + 2));
    double entriesWidth = entryAmountHorizontal * (entryWidth + 2);
    double startX = (emptySpaceWidth - entriesWidth) / 2;
    double currentX = startX;

    double startY = 45;
    int rows = (int)Math.ceil((double)itemEntries.size() / (double)entryAmountHorizontal);

    //Only draw the entries that are visible
    int scrollY = (int)this.scrollbar.getScrollY();
    int outOfViewEntryRows = Math.abs(scrollY) / (entryHeight + 2);
    int inViewEntryRows = (getHeight() - 90) / (entryHeight + 2) + 1;
    int outOfViewAmount = entryAmountHorizontal * outOfViewEntryRows;
    int visibleEntriesAmount = entryAmountHorizontal * inViewEntryRows;

    int firstIndex = Math.max(Math.min(outOfViewAmount, itemEntries.size() - visibleEntriesAmount), 0);
    int lastIndex = Math.max(Math.min(outOfViewAmount + visibleEntriesAmount, itemEntries.size()),0);

    List<TransporterSettingsMenuEntry> visibleEntries = itemEntries.subList(firstIndex, lastIndex);

    if(lastIndex == itemEntries.size()) {
      startY -= (getHeight() - 90);
      startY -= Math.abs(scrollY) - (lastIndex / entryAmountHorizontal) * (entryHeight + 2);
    }

    for(TransporterSettingsMenuEntry entry : visibleEntries) {
      if(currentX + entryWidth >= startX + entriesWidth) {
        currentX = startX;
        startY += entryHeight + 2;
      }
      entry.draw((int)currentX, (int)(startY), mouseX, mouseY);
      currentX += entryWidth + 2;
    }

    drawAPI.drawOverlayBackground(0, 41);
    drawAPI.drawGradientShadowTop(41.0, 0.0, this.getWidth());
    drawAPI.drawOverlayBackground(this.getHeight() - 40, this.getHeight());
    drawAPI.drawGradientShadowBottom(this.getHeight() - 40, 0.0, this.getWidth());
    drawAPI.drawCenteredString(
        ModColor.cl("a")+ModColor.cl("l")+"Transporter Menu Settings", (double)(this.getWidth() / 2), 20.0D, 2.0D);

    this.scrollbar.setPosition(startX + entriesWidth + 3, 43, startX + entriesWidth + 6, getHeight() - 42);
    this.scrollbar.update(rows + 5);
    this.scrollbar.draw();

    for(TransporterSettingsMenuEntry entry : visibleEntries) {
      entry.drawHoverText();
    }
  }

  @Override
  public void actionPerformed(WrappedGuiButton button) {
    if(button.getId() == 1) {
      PlayerAPI.getAPI().openGuiScreen(new TransporterMenu());
    }
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
    if(mouseY > 41 && mouseY < getHeight() - 40)
      itemEntries.forEach(transporterMenuEntry -> transporterMenuEntry.mouseClicked(mouseX, mouseY, mouseButton));
    this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
  }

  @Override
  public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
    this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
    this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
  }

  @Override
  public void handleMouseInput() {
    this.scrollbar.mouseInput();
  }

  @Override
  public void keyTyped(char typedChar, Key key) {

  }

  @Override
  public void onGuiClosed() {

  }
}
