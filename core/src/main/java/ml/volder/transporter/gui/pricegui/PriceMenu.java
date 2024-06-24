package ml.volder.transporter.gui.pricegui;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.transportermenumodule.TransporterMenuEntry;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class PriceMenu extends WrappedGuiScreen {

    private WrappedGuiScreen lastScreen;
    private boolean isInBackground = false;
    private int entryWidth = 120;
    private int entryHeight = 37;
    private Scrollbar scrollbar = new Scrollbar(entryHeight);

    private List<PriceMenuEntry> itemEntries = new ArrayList<>();

    public PriceMenu(WrappedGuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        isInBackground = false;
        itemEntries.clear();
        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            itemEntries.add(new PriceMenuEntry(this, item, entryWidth, entryHeight));
        }
        this.scrollbar.setSpeed(39);
        this.scrollbar.init();

        this.addButton(new WrappedGuiButton(1, 5, 10, 22, 21, "<"));
        this.addButton(new WrappedGuiButton(2, 5, getHeight() - 31, 120, 21, "Opdatere alle priser"));
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

      List<PriceMenuEntry> visibleEntries = itemEntries.subList(firstIndex, lastIndex);

      if(lastIndex == itemEntries.size()) {
        startY -= (getHeight() - 90);
        startY -= Math.abs(scrollY) - (lastIndex / entryAmountHorizontal) * (entryHeight + 2);
      }

      for(PriceMenuEntry entry : visibleEntries) {
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
        drawAPI.drawCenteredString(ModColor.cl("a")+ModColor.cl("l")+"Værdi indstillinger", (double)(this.getWidth() / 2), 10.0D, 2.0D);

        this.scrollbar.setPosition(startX + entriesWidth + 3, 43, startX + entriesWidth + 6, getHeight() - 42);
        this.scrollbar.update(rows + 5);
        this.scrollbar.draw();

        for(PriceMenuEntry entry : visibleEntries) {
            entry.drawHoverText();
        }
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 1) {
            PlayerAPI.getAPI().openGuiScreen(lastScreen);
        }
        else if(button.getId() == 2) {
            itemEntries.forEach(PriceMenuEntry::updateSellValueFromPriceServer);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(mouseY > 41 && mouseY < getHeight() - 40)
            itemEntries.forEach(priceMenuEntry -> priceMenuEntry.mouseClicked(mouseX, mouseY, mouseButton));
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

    public void setInBackground(boolean inBackground) {
        isInBackground = inBackground;
    }

    public boolean isInBackground() {
        return isInBackground;
    }
}
