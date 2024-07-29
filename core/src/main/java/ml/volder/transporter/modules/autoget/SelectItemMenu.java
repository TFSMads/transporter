package ml.volder.transporter.modules.autoget;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.unikapi.api.draw.DrawAPI;
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
    private Scrollbar scrollbar = new Scrollbar(entryHeight);

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
        this.scrollbar.setSpeed(39);
        this.scrollbar.init();
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
        int rows = 1;

        for(SelectItemMenuEntry entry : itemEntries) {
            if(currentX + entryWidth >= startX + entriesWidth) {
                rows++;
                currentX = startX;
                startY += entryHeight + 2;
            }
            entry.draw((int)currentX, (int)(startY + this.scrollbar.getScrollY()), mouseX, mouseY);
            currentX += entryWidth + 2;
        }

        drawAPI.drawOverlayBackground(0, 41);
        drawAPI.drawGradientShadowTop(41.0, 0.0, this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 40, this.getHeight());
        drawAPI.drawGradientShadowBottom(this.getHeight() - 40, 0.0, this.getWidth());
        drawAPI.drawCenteredString(ModColor.WHITE + "Vælg den item Auto Get skal tage fra din Transporter!", (double)(this.getWidth() / 2), 20.0D, 2.0D);

        this.scrollbar.setPosition(startX + entriesWidth + 3, 43, startX + entriesWidth + 6, getHeight() - 42);
        this.scrollbar.update(rows);
        this.scrollbar.draw();
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(mouseY > 41 && mouseY < getHeight() - 40)
            itemEntries.forEach(selectItemMenuEntry -> selectItemMenuEntry.mouseClicked(mouseX, mouseY, mouseButton));
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
