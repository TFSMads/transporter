package ml.volder.transporter.gui.pricegui;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.modules.transportermenumodule.TransporterSettingsMenuEntry;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.guisystem.elements.ModTextField;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenWrapper;

import java.util.ArrayList;
import java.util.List;

public class PriceMenu extends WrappedGuiScreen {

    private ScreenWrapper lastScreen;
    private boolean isInBackground = false;
    private int entryWidth = 120;
    private int entryHeight = 37;
    private List<PriceMenuEntry> itemEntries = new ArrayList<>();
    private ScrollableGrid scrollableGrid = new ScrollableGrid(0, 0, 0, 0, entryWidth, entryHeight, 2, 2);

    private ModTextField searchField;
    private String searchText = "";

    public PriceMenu(ScreenWrapper lastScreen) {
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
        this.addButton(new WrappedGuiButton(1, 5, 10, 22, 20, "<"));
        this.addButton(new WrappedGuiButton(2, 5, getHeight() - 31, 120, 20, "Opdatere alle priser"));

        scrollableGrid.initGui();
        scrollableGrid.updateGridSize(getWidth(), getHeight() - 90, 0, 45);
        scrollableGrid.updateEntries(itemEntries);

        this.searchField = new ModTextField(1000, getWidth()/2 - 100, this.getHeight() - 20 - 10, 200, 20);
        searchField.setPlaceHolder("Søg efter item...");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(searchField != null && !searchField.getText().toLowerCase().equals(searchText)) {
            searchText = searchField.getText().toLowerCase();
            itemEntries.clear();
            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
                if(item.getDisplayName().toLowerCase().contains(searchText) || item.getModernType().toLowerCase().contains(searchText))
                    itemEntries.add(new PriceMenuEntry(this, item, entryWidth, entryHeight));
            }
            this.scrollableGrid.resetScroll();
            this.scrollableGrid.updateEntries(itemEntries);
        }

        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawAutoDimmedBackground(0);

        scrollableGrid.render(mouseX, mouseY);

        drawAPI.drawOverlayBackground(0, 41);
        drawAPI.drawGradientShadowTop(41.0, 0.0, this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 40, this.getHeight());
        drawAPI.drawGradientShadowBottom(this.getHeight() - 40, 0.0, this.getWidth());
        drawAPI.drawCenteredString(ModColor.cl("a")+ModColor.cl("l")+"Værdi indstillinger", (double)(this.getWidth() / 2), 10.0D, 2.0D);

        if(searchField != null)
            searchField.drawTextBox();

        scrollableGrid.renderHoverText();
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 1) {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(lastScreen);
        }
        else if(button.getId() == 2) {
            itemEntries.forEach(PriceMenuEntry::updateSellValueFromPriceServer);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(searchField != null)
            this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollableGrid.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        this.scrollableGrid.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollableGrid.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() {
        this.scrollableGrid.handleMouseInput();
    }

    @Override
    public void keyTyped(char typedChar, Key key) {
        if(searchField != null)
            searchField.textboxKeyTyped(typedChar, key);
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
