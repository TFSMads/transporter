package ml.volder.transporter.modules.serverlistmodule;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.guisystem.elements.ModTextField;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

public class AddServerGui extends WrappedGuiScreen {
    private WrappedGuiScreen lastScreen;
    private ModTextField fieldServer;
    private WrappedGuiButton buttonAdd;
    private boolean saveServer;
    private DataManager<Data> dataManager;

    public AddServerGui(WrappedGuiScreen lastScreen, boolean saveServer, DataManager<Data> dataManagers) {
        this.lastScreen = lastScreen;
        this.saveServer = saveServer;
        this.dataManager = dataManagers;
    }

    @Override
    public void initGui() {
        InputAPI.getAPI().enableRepeatEvents(true);
        this.clearButtonList();
        this.fieldServer = new ModTextField(0, this.getWidth() / 2 - 110, this.getHeight() / 2 - 15, 220, 20);
        this.fieldServer.setMaxStringLength(90000);
        this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 110, this.getHeight() / 2 + 15, 100, 20, "Cancel"));
        buttonAdd = new WrappedGuiButton(2, this.getWidth() / 2, this.getHeight() / 2 + 15, 110, 20, "");
        this.addButton(buttonAdd);
    }

    @Override
    public void onGuiClosed() {
        InputAPI.getAPI().enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawBackground(0);
        this.fieldServer.drawTextBox();
        drawAPI.drawString( "Server Navn:", this.getWidth() / 2 - 110, this.getHeight() / 2 - 30);
        if(saveServer){ this.buttonAdd.setDisplayString("Add");}else{ this.buttonAdd.setDisplayString("Connect");}

    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        switch (button.getId()) {
            case 1: {
                PlayerAPI.getAPI().openGuiScreen(this.lastScreen);
                break;
            }
            case 2: {
                this.addServer();
                if(!saveServer){PlayerAPI.getAPI().openGuiScreen(null); }else{PlayerAPI.getAPI().openGuiScreen(this.lastScreen);}
            }
        }

    }

    private void addServer() {
        String server = this.fieldServer.getText();
        if(saveServer){
            JsonObject data = dataManager.getSettings().getData();
            if (data.has("servers")){
                JsonArray servers = data.getAsJsonArray("servers");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("serverNavn", server);
                servers.add(jsonObject);
                data.add("servers", servers);
                dataManager.save();
            }else{
                JsonArray servers = new JsonArray();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("serverNavn", server);
                servers.add(jsonObject);
                data.add("servers", servers);
                dataManager.save();
            }
        }else{
            PlayerAPI.getAPI().sendCommand("server " + server);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        this.fieldServer.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {

    }

    @Override
    public void handleMouseInput() {

    }

    @Override
    public void keyTyped(char typedChar, Key key) {
        if (key.equals(Key.ESCAPE)) {
            PlayerAPI.getAPI().openGuiScreen(lastScreen);
            return;
        }
        if (key.equals(Key.ENTER) && this.buttonAdd.isEnabled()) {
            this.actionPerformed(this.buttonAdd);
        }
        this.fieldServer.textboxKeyTyped(typedChar, key);
    }

    @Override
    public void updateScreen() {
        this.fieldServer.updateCursorCounter();
    }
}

