package ml.volder.transporter.modules.serverlistmodule;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

public class RemoveServerGui extends WrappedGuiScreen {
    private WrappedGuiScreen lastScreen;
    private ModTextField fieldServer;
    private WrappedGuiButton buttonDelete;
    private DataManager<Data> dataManager;

    public RemoveServerGui(WrappedGuiScreen lastScreen, DataManager<Data> dataManager) {
        this.lastScreen = lastScreen;
        this.dataManager = dataManager;
    }

    @Override
    public void initGui() {
        InputAPI.getAPI().enableRepeatEvents(true);
        this.clearButtonList();
        this.fieldServer = new ModTextField(0, this.getWidth() / 2 - 110, this.getHeight() / 2 - 15, 220, 20);
        this.fieldServer.setMaxStringLength(90000);
        this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 110, this.getHeight() / 2 + 15, 100, 20, "Cancel"));
        buttonDelete = new WrappedGuiButton(2, this.getWidth() / 2, this.getHeight() / 2 + 15, 110, 20, "");
        this.addButton(buttonDelete);
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
        this.buttonDelete.setDisplayString("Delete");
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 2)
            this.removeServer();
        PlayerAPI.getAPI().openGuiScreen(lastScreen);
    }

    private void removeServer() {
        String server = this.fieldServer.getText();

        JsonObject data = dataManager.getSettings().getData();
        if (data.has("servers")){
            JsonArray servers = data.getAsJsonArray("servers");
            JsonArray newServers = new JsonArray();
            for(JsonElement json : servers){
                JsonObject jsonObj = json.getAsJsonObject();
                String serverName = jsonObj.get("serverNavn").toString();
                if (!serverName.equals("\"" + server + "\"")){
                    newServers.add(jsonObj);
                }
            }
            data.add("servers", newServers);
            dataManager.save();
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
        if (key.equals(Key.ENTER) && this.buttonDelete.isEnabled()) {
            this.actionPerformed(this.buttonDelete);
        }
        this.fieldServer.textboxKeyTyped(typedChar, key);
    }

    @Override
    public void updateScreen() {
        this.fieldServer.updateCursorCounter();
    }
}

