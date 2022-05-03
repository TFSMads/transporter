package dk.transporter.mads_gamer_dk.guis.serverSelector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

public class RemoveServerGui extends GuiScreen {
    private GuiScreen lastScreen;
    private ModTextField fieldServer;
    private GuiButton buttonDelete;
    private boolean displayError = false;
    private long shakingError = 0L;
    private DataManagers dataManagers;

    public RemoveServerGui(GuiScreen lastScreen, DataManagers dataManagers) {
        this.lastScreen = lastScreen;
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
        this.buttonDelete = new GuiButton(2, this.width / 2, this.height / 2 + 15, 110, 20, "");
        this.buttonList.add(this.buttonDelete);
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
        this.buttonDelete.displayString = "Delete";
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
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            }
        }

    }

    private void addServer() {
        String server = this.fieldServer.getText();

        JsonObject data = dataManagers.getServerData();
        System.out.println("DELETE SERVER " + server);
        if (data.has("servers")){
            JsonArray servers = data.getAsJsonArray("servers");
            JsonArray newServers = new JsonArray();
            System.out.println(servers);
            for(JsonElement json : servers){
                JsonObject jsonObj = json.getAsJsonObject();
                String serverName = jsonObj.get("serverNavn").toString();
                System.out.println("SN: " + serverName);
                if (!serverName.equals("\"" + server + "\"")){
                    newServers.add(jsonObj);
                }
            }
            data.add("servers", newServers);
            System.out.println(data);
            dataManagers.saveServerData();
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
        if (keyCode == 28 && this.buttonDelete.enabled) {
            this.actionPerformed(this.buttonDelete);
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

