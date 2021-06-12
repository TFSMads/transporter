package dk.transporter.mads_gamer_dk.guis;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

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




    }


    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("Clicked: Button with id " + button.id);
        super.actionPerformed(button);

        interacted = true;
        Minecraft.getMinecraft().thePlayer.closeScreen();

    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        double yPos = 45.0D + this.scrollbar.getScrollY() + 3.0D;

        LabyMod.getInstance().getDrawUtils().drawCenteredString("§8§l[ §a§lServer Selecter §8§l]", this.width / 2, 20, 2);

        LabyMod.getInstance().getDrawUtils().drawCenteredString("§fKlik på den server du vil tilslutte! ", this.width / 2, 50, 1);


        this.scrollbar.draw();

        Mouse.setGrabbed(false);
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


