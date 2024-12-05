package ml.volder.transporter.modules.transportermenumodule;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.TransporterMenuModule;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class TransporterMenuLayout extends WrappedGuiScreen {

    private int entryWidth = 120;
    private int entryHeight = 37;
    private List<ScrollableGrid.Entry> itemEntries = new ArrayList<>();
    private ScrollableGrid scrollableGrid = new ScrollableGrid(0, 0, 0, 0, entryWidth, entryHeight, 2, 2);
    private boolean flagChanged = false;

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        itemEntries.clear();
        for(Item item : ModuleManager.getInstance().getModule(TransporterMenuModule.class).getActiveItems()) {
          itemEntries.add(new TransporterMenuLayoutEntry(item, entryWidth, entryHeight, this));
        }

        scrollableGrid.initGui();
        scrollableGrid.updateGridSize(getWidth(), getHeight() - 90, 0, 45);
        scrollableGrid.updateEntries(itemEntries);

        addButton(new WrappedGuiButton(1, 20, 20, 22, 20, "<"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(flagChanged) {
            initGui();
            flagChanged = false;
        }
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawAutoDimmedBackground(0);

        scrollableGrid.render(mouseX, mouseY);

        drawAPI.drawOverlayBackground(0, 41);
        drawAPI.drawGradientShadowTop(41.0, 0.0, this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 40, this.getHeight());
        drawAPI.drawGradientShadowBottom(this.getHeight() - 40, 0.0, this.getWidth());
        drawAPI.drawCenteredString(ModColor.cl("a")+ModColor.cl("l")+"Transporter Menu", (double)(this.getWidth() / 2), 20.0D, 2.0D);

        scrollableGrid.renderHoverText();
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 1) {
            PlayerAPI.getAPI().openGuiScreen(new TransporterMenu());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
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

    }

    @Override
    public void onGuiClosed() {

    }

    public void flagChanged() {
        flagChanged = true;
    }
}
