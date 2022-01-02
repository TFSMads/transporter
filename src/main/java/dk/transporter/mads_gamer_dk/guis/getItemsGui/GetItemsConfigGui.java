package dk.transporter.mads_gamer_dk.guis.getItemsGui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.guis.serverSelector.AddServerGui;
import dk.transporter.mads_gamer_dk.guis.serverSelector.RemoveServerGui;
import dk.transporter.mads_gamer_dk.guis.serverSelector.ServerInfoRenderer;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GetItemsConfigGui extends GuiScreen {
    private Scrollbar scrollbar;
    private TransporterAddon addon;
    private DataManagers dataManagers;
    private String hoverServer;

    public GetItemsConfigGui(TransporterAddon addon, DataManagers dataManagers) {
        this.addon = addon;
        this.dataManagers = dataManagers;
    }

    public void updateScreen() {
    }

    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar = new Scrollbar(36);
        this.scrollbar.setPosition(this.width / 2 + 150 + 4, 41, this.width / 2 + 150 + 4 + 6, this.height - 40);
        this.scrollbar.setSpeed(20);
        this.scrollbar.setSpaceBelow(5);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("Clicked: Button with id " + button.id);
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();

        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());

        draw.drawOverlayBackground(0, 41);
        draw.drawGradientShadowTop(41.0, 0.0, this.width);
        draw.drawOverlayBackground(this.height - 40, this.height);
        draw.drawGradientShadowBottom(this.height - 40, 0.0, this.width);

        draw.drawCenteredString(ModColor.cl("8")+ModColor.cl("l")+"["+ModColor.cl("a")+ModColor.cl("l")+" Transporter Get Config "+ModColor.cl("8")+ModColor.cl("l")+"]", (double)(this.width / 2), 20.0D, 2.0D);
        this.scrollbar.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}
