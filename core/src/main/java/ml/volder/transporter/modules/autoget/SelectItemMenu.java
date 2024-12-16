package ml.volder.transporter.modules.autoget;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.gui.pricegui.PriceMenuEntry;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.guisystem.elements.ModTextField;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class SelectItemMenu extends WrappedGuiScreen {

    private WrappedGuiScreen lastScreen;
    private int entryWidth = 120;
    private int entryHeight = 37;
    private ScrollableGrid scrollableGrid = new ScrollableGrid(0, 0, 0, 0, entryWidth, entryHeight, 2, 2);
    private ModTextField searchField;
    private String searchText = "";
    private List<SelectItemMenuEntry> itemEntries = new ArrayList<>();

    public SelectItemMenu(WrappedGuiScreen lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        itemEntries.clear();
        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            itemEntries.add(new SelectItemMenuEntry(item, entryWidth, entryHeight, lastScreen));
        }

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
                    itemEntries.add(new SelectItemMenuEntry(item, entryWidth, entryHeight, lastScreen));
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
        drawAPI.drawCenteredString(ModColor.WHITE + "Vælg den item Auto Get skal tage fra din Transporter!", (double)(this.getWidth() / 2), 20.0D, 2.0D);

        if(searchField != null)
            searchField.drawTextBox();

        scrollableGrid.renderHoverText();
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {

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
}
