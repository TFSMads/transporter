package ml.volder.transporter.modules.signtoolsmodule;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ml.volder.transporter.gui.elements.ModTextField;
import ml.volder.transporter.jsonmanager.Data;
import ml.volder.transporter.jsonmanager.DataManager;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

public class RemoveSavedSignGui extends WrappedGuiScreen {
    private WrappedGuiScreen lastScreen;
    private ModTextField fieldServer;
    private WrappedGuiButton buttonDelete;
    private boolean displayError = false;
    private long shakingError = 0L;
    private DataManager<Data> dataManager;

    public RemoveSavedSignGui(WrappedGuiScreen lastScreen, DataManager<Data> dataManager) {
        this.lastScreen = lastScreen;
        this.dataManager = dataManager;
    }

    @Override
    public void initGui() {
        InputAPI.getAPI().enableRepeatEvents(true);
        clearButtonList();
        this.fieldServer = new ModTextField(0, this.getWidth() / 2 - 110, this.getHeight() / 2 - 15, 220, 20);
        this.fieldServer.setMaxStringLength(90000);
        addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 110, this.getHeight() / 2 + 15, 100, 20, "Cancel"));
        this.buttonDelete = new WrappedGuiButton(2, this.getWidth() / 2, this.getHeight() / 2 + 15, 110, 20, "Delete");
        addButton(this.buttonDelete);
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
        drawAPI.drawString( "Title på skiltet:", this.getWidth() / 2 - 110, this.getHeight() / 2 - 30);
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        switch (button.getId()) {
            case 1: {
                PlayerAPI.getAPI().openGuiScreen(lastScreen);
                break;
            }
            case 2: {
                this.deleteSign();
                PlayerAPI.getAPI().openGuiScreen(lastScreen);
            }
        }

    }

    private void deleteSign() {
        String server = this.fieldServer.getText();

        JsonObject data = dataManager.getSettings().getData();
        if (data.has("signs")){
            JsonArray servers = data.getAsJsonArray("signs");
            JsonArray newServers = new JsonArray();
            for(JsonElement json : servers){
                JsonObject jsonObj = json.getAsJsonObject();
                String serverName = jsonObj.get("title").toString();
                if (!serverName.equals("\"" + server + "\"")){
                    newServers.add(jsonObj);
                }
            }
            data.add("signs", newServers);
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

