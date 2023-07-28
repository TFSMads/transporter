package ml.volder.transporter.modules.signtoolsmodule;

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

public class SaveSignGui extends WrappedGuiScreen {
    private WrappedGuiScreen lastScreen;
    private ModTextField fieldServer;
    private WrappedGuiButton buttonAdd;
    private boolean displayError = false;
    private long shakingError = 0L;
    private DataManager<Data> dataManager;
    private SignBuffer signBuffer;

    private boolean error = false;

    public SaveSignGui(WrappedGuiScreen lastScreen, DataManager<Data> dataManager, SignBuffer signBuffer) {
        this.lastScreen = lastScreen;
        this.dataManager = dataManager;
        this.signBuffer = signBuffer;
    }

    @Override
    public void initGui() {
        InputAPI.getAPI().enableRepeatEvents(true);
        clearButtonList();
        this.fieldServer = new ModTextField(0, this.getWidth() / 2 - 110, this.getHeight() / 2 - 15, 220, 20);
        this.fieldServer.setMaxStringLength(90000);
        addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 110, this.getHeight() / 2 + 15, 100, 20, "Cancel"));
        this.buttonAdd = new WrappedGuiButton(2, this.getWidth() / 2, this.getHeight() / 2 + 15, 110, 20, "Save");
        addButton(buttonAdd);
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
        drawAPI.drawString( "Skriv en title til skiltet:", this.getWidth() / 2 - 110, this.getHeight() / 2 - 30);
        if(error){
            drawAPI.drawString( "Der er allerede et skilt med denne title!", this.getWidth() / 2 - 110, this.getHeight() / 2 + 40, 0xED4337);
        }
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        switch (button.getId()) {
            case 1: {
                PlayerAPI.getAPI().openGuiScreen(this.lastScreen);
                break;
            }
            case 2: {
                if(addSign()){
                    PlayerAPI.getAPI().openGuiScreen(this.lastScreen);
                }else{
                    this.error = true;
                }

            }
        }

    }

    private JsonObject createSignJsonObject(String signTitle){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", signTitle);
        JsonObject signLines = new JsonObject();
        signLines.addProperty("0", signBuffer.getLine(1));
        signLines.addProperty("1", signBuffer.getLine(2));
        signLines.addProperty("2", signBuffer.getLine(3));
        signLines.addProperty("3", signBuffer.getLine(4));
        jsonObject.add("content", signLines);
        return jsonObject;
    }

    private boolean addSign() {
        String signTitle = this.fieldServer.getText();
        JsonObject data = dataManager.getSettings().getData();
        if (data.has("signs")){
            JsonArray signsData = data.getAsJsonArray("signs");
            for (JsonElement elm: signsData) {
                if(elm.getAsJsonObject().has("title") && elm.getAsJsonObject().get("title").getAsString().equals(signTitle)){
                    return false;
                }
            }
            signsData.add(createSignJsonObject(signTitle));
            data.add("signs", signsData);
            dataManager.save();
        }else{
            JsonArray signsData = new JsonArray();
            signsData.add(createSignJsonObject(signTitle));
            data.add("signs", signsData);
            dataManager.save();
        }
        return true;
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
            PlayerAPI.getAPI().openGuiScreen(this.lastScreen);
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

