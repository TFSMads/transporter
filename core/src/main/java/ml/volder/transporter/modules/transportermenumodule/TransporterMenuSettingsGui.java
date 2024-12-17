package ml.volder.transporter.modules.transportermenumodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.TransporterActivity;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.guisystem.elements.ModTextField;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.util.ArrayList;
import java.util.List;

@AutoActivity
public class TransporterMenuSettingsGui extends TransporterActivity {

  private int entryWidth = 120;
  private int entryHeight = 37;
  private List<ScrollableGrid.Entry> itemEntries = new ArrayList<>();
  private ScrollableGrid scrollableGrid = new ScrollableGrid(0, 0, 0, 0, entryWidth, entryHeight, 2, 2);

  private WrappedGuiButton buttonBack;
  private ModTextField searchField;
  private String searchText = "";

  @Override
  public void updateScreen() {

  }

  @Override
  public void initGui() {
    itemEntries.clear();
    for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
      itemEntries.add(new TransporterSettingsMenuEntry(item, entryWidth, entryHeight));
    }
    this.buttonBack = new WrappedGuiButton(1, 20, 20, 22, 20, "<");
    this.addButton(buttonBack);

    this.searchField = new ModTextField(1000, getWidth()/2 - 100, this.getHeight() - 20 - 10, 200, 20);
    searchField.setPlaceHolder("Søg efter item...");

    scrollableGrid.initGui();
    scrollableGrid.updateGridSize(getWidth(), getHeight() - 90, 0, 45);
    scrollableGrid.updateEntries(itemEntries);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    if(searchField != null && !searchField.getText().toLowerCase().equals(searchText)) {
      searchText = searchField.getText().toLowerCase();
      itemEntries.clear();
      for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
        if(item.getDisplayName().toLowerCase().contains(searchText) || item.getModernType().toLowerCase().contains(searchText))
          itemEntries.add(new TransporterSettingsMenuEntry(item, entryWidth, entryHeight));
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
    drawAPI.drawCenteredString(
        ModColor.cl("a")+ModColor.cl("l")+"Transporter Menu Settings", this.getWidth() / 2, 20.0D, 2.0D);

    if(searchField != null)
      searchField.drawTextBox();

    scrollableGrid.renderHoverText();
  }

  @Override
  public void actionPerformed(WrappedGuiButton button) {
    if(button.getId() == 1) {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new TransporterMenu());
    }
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
    if(searchField != null)
      this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
    scrollableGrid.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
    scrollableGrid.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
    scrollableGrid.mouseReleased(mouseX, mouseY, mouseButton);
  }

  @Override
  public void handleMouseInput() {
    scrollableGrid.handleMouseInput();
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
