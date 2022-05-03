package dk.transporter.mads_gamer_dk.guis.serverSelector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.utils.data.Data;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class AddServerGui extends GuiScreen {
    private GuiScreen lastScreen;
    private ModTextField fieldServer;
    private GuiButton buttonAdd;
    private boolean saveServer;
    private boolean displayError = false;
    private long shakingError = 0L;
    private DataManagers dataManagers;

    public AddServerGui(GuiScreen lastScreen, boolean saveServer, DataManagers dataManagers) {
        this.lastScreen = lastScreen;
        this.saveServer = saveServer;
        this.dataManagers = dataManagers;
        //System.out.println("SAVE SERVER: " + saveServer + " THIS: " + this.saveServer);
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.fieldServer = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), this.width / 2 - 110, this.height / 2 - 15, 220, 20);
        this.fieldServer.setMaxStringLength(90000);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 110, this.height / 2 + 15, 100, 20, LanguageManager.translate("button_cancel")));
        this.buttonAdd = new GuiButton(2, this.width / 2, this.height / 2 + 15, 110, 20, "");
        this.buttonList.add(this.buttonAdd);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.fieldServer.drawTextBox();
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawString( "Server Navn:", this.width / 2 - 110, this.height / 2 - 30);
        if(saveServer){ this.buttonAdd.displayString = "Add"; }else{ this.buttonAdd.displayString = "Connect"; }

        boolean bl = this.buttonAdd.enabled = !this.fieldServer.getText().isEmpty();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
            case 2: {
                this.addServer();
                if(!saveServer){Minecraft.getMinecraft().thePlayer.closeScreen(); }else{Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);}
            }
        }

    }

    private void addServer() {
        String server = this.fieldServer.getText();
        if(saveServer){
            JsonObject data = dataManagers.getServerData();
            System.out.println("ADD SERVER " + server);
            if (data.has("servers")){
                JsonArray servers = data.getAsJsonArray("servers");
                System.out.println(servers);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("serverNavn", server);
                servers.add(jsonObject);
                data.add("servers", servers);
                System.out.println(data);
                dataManagers.saveServerData();
            }else{
                JsonArray servers = new JsonArray();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("serverNavn", server);
                servers.add(jsonObject);
                System.out.println(servers);
                data.add("servers", servers);
                System.out.println(data);
                dataManagers.saveServerData();
            }
        }else{
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + server);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldServer.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            return;
        }
        if (keyCode == 28 && this.buttonAdd.enabled) {
            this.actionPerformed(this.buttonAdd);
        }
        this.fieldServer.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.fieldServer.updateCursorCounter();
    }
}

