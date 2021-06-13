/* Decompiler 19ms, total 480ms, lines 92 */
package dk.transporter.mads_gamer_dk.guis;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import java.io.IOException;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.gui.elements.Scrollbar.EnumMouseAction;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class LobbySelecterGui extends GuiScreen {
    private Scrollbar scrollbar = new Scrollbar(18);
    private TransporterAddon addon;
    private Boolean interacted = false;

    public LobbySelecterGui(TransporterAddon addon) {
        this.addon = addon;
    }

    public void updateScreen() {
    }

    public void initGui() {
        super.initGui();
        this.scrollbar.init();
        this.scrollbar.setPosition(this.width / 2 + 122, 44, this.width / 2 + 126, this.height - 32 - 3);
        Integer buttonWidth = this.width / 5;
        this.buttonList.add(new GuiButton(51275, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 - 50), buttonWidth, 20, this.addon.getServerString(7)));
        this.buttonList.add(new GuiButton(51276, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 - 25), buttonWidth, 20, this.addon.getServerString(6)));
        this.buttonList.add(new GuiButton(51277, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 - 0), buttonWidth, 20, this.addon.getServerString(5)));
        this.buttonList.add(new GuiButton(51278, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 + 25), buttonWidth, 20, this.addon.getServerString(4)));
        this.buttonList.add(new GuiButton(51279, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 + 50), buttonWidth, 20, this.addon.getServerString(3)));
        this.buttonList.add(new GuiButton(51280, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 + 75), buttonWidth, 20, this.addon.getServerString(2)));
        this.buttonList.add(new GuiButton(51281, this.width / 2 - buttonWidth / 2, this.height - (this.height / 2 + 100), buttonWidth, 20, this.addon.getServerString(1)));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("Clicked: Button with id " + button.id);
        super.actionPerformed(button);
        if (button.id == 51275) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(7));
        } else if (button.id == 51276) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(6));
        } else if (button.id == 51277) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(5));
        } else if (button.id == 51278) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(4));
        } else if (button.id == 51279) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(3));
        } else if (button.id == 51280) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(2));
        } else if (button.id == 51281) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + this.addon.getServerString(1));
        }

        this.interacted = true;
        Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        double yPos = 45.0D + this.scrollbar.getScrollY() + 3.0D;
        LabyMod.getInstance().getDrawUtils().drawCenteredString(ModColor.cl("8")+ModColor.cl("l")+"["+ModColor.cl("a")+ModColor.cl("l")+" Server Selecter "+ModColor.cl("8")+ModColor.cl("l")+"]", (double)(this.width / 2), 20.0D, 2.0D);
        LabyMod.getInstance().getDrawUtils().drawCenteredString(ModColor.cl("f")+"Klik på den server du vil tilslutte! ", (double)(this.width / 2), 50.0D, 1.0D);
        this.scrollbar.draw();
        Mouse.setGrabbed(false);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, EnumMouseAction.CLICKED);
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, EnumMouseAction.DRAGGING);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}