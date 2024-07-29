package ml.volder.transporter.modules.transportermenumodule;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.TransporterMenuModule;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class TransporterMenu extends WrappedGuiScreen {

    private int entryWidth = 120;
    private int entryHeight = 37;
    private Scrollbar scrollbar = new Scrollbar(entryHeight);

    private List<TransporterMenuEntry> itemEntries = new ArrayList<>();


  private WrappedGuiButton buttonMenuOptions;

  private void createButton() {
    this.buttonMenuOptions = new WrappedGuiButton(11, 0, 0, 23, 20, "");
  }

  private void renderButtonMenuOptions(int mouseX, int mouseY) {
      this.buttonMenuOptions.setX(this.getWidth() - (23 + 5));
      this.buttonMenuOptions.setY(this.getHeight() - 25);
      this.buttonMenuOptions.setEnabled(true);
      this.buttonMenuOptions.drawButton(mouseX, mouseY);
      DrawAPI drawAPI = DrawAPI.getAPI();
      drawAPI.bindTexture(ModTextures.BUTTON_ADVANCED);
      drawAPI.drawTexture((double)(this.buttonMenuOptions.getX() + 4), (double)(this.buttonMenuOptions.getY() + 3), 0.0, 0.0, 256.0, 256.0, 14.0, 14.0, 2.0F);
  }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        itemEntries.clear();
        for(Item item : ModuleManager.getInstance().getModule(TransporterMenuModule.class).getActiveItems()) {
          itemEntries.add(new TransporterMenuEntry(item, entryWidth, entryHeight));
        }
        this.scrollbar.setSpeed(39);
        this.scrollbar.init();
        createButton();
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

        List<TransporterMenuEntry> visibleEntries = itemEntries.subList(firstIndex, lastIndex);

        if(lastIndex == itemEntries.size() && lastIndex > visibleEntriesAmount) {
          startY -= (getHeight() - 90);
          startY -= Math.abs(scrollY) - (lastIndex / entryAmountHorizontal) * (entryHeight + 2);
        }

        for(TransporterMenuEntry entry : visibleEntries) {
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
        drawAPI.drawCenteredString(ModColor.cl("a")+ModColor.cl("l")+"Transporter Menu", (double)(this.getWidth() / 2), 20.0D, 2.0D);

        this.scrollbar.setPosition(startX + entriesWidth + 3, 43, startX + entriesWidth + 6, getHeight() - 42);
        this.scrollbar.update(rows + 5);

        if(rows < inViewEntryRows){
          this.scrollbar.update(0);
        }

        this.scrollbar.draw();

        renderButtonMenuOptions(mouseX, mouseY);

        for(TransporterMenuEntry entry : visibleEntries) {
            entry.drawHoverText();
        }
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(mouseY > 41 && mouseY < getHeight() - 40)
            itemEntries.forEach(transporterMenuEntry -> transporterMenuEntry.mouseClicked(mouseX, mouseY, mouseButton));
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);

        if(buttonMenuOptions.isEnabled() && buttonMenuOptions.isMouseOver()) {
          PlayerAPI.getAPI().openGuiScreen(new TransporterMenuSettingsGui());
        }
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
