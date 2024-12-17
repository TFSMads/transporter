
package ml.volder.transporter.modules.signtoolsmodule;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ml.volder.transporter.gui.TransporterActivity;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

@AutoActivity
public class LoadSignGui extends TransporterActivity {
    private Scrollbar scrollbar;
    private DataManager<Data> dataManager;
    private String hoverEntry;
    private ScreenInstance lastScreen;
    private SignBuffer signBuffer;

    public LoadSignGui(DataManager<Data> dataManager, ScreenInstance lastScreen, SignBuffer signBuffer) {
        this.dataManager = dataManager;
        this.lastScreen = lastScreen;
        this.signBuffer = signBuffer;
    }

    public void updateScreen() {
    }

    @Override
    public void initGui() {
        this.scrollbar = new Scrollbar(52);
        this.scrollbar.setPosition(this.getWidth() / 2 + 150 + 4, 41, this.getWidth() / 2 + 150 + 4 + 6, this.getHeight() - 40);
        this.scrollbar.setSpeed(20);
        this.scrollbar.setSpaceBelow(5);

        addButton(new WrappedGuiButton(30007, this.getWidth() / 2 - 50 - 5 - 100, this.getHeight() - 30, 100, 20, "Remove Save"));
        addButton(new WrappedGuiButton(30006, this.getWidth() / 2 - 50, this.getHeight() - 30, 100, 20, "Cancel"));
        addButton(new WrappedGuiButton(30005, this.getWidth() / 2 + 50 + 5, this.getHeight() - 30, 100, 20, "Clear Saves"));
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        switch (button.getId()) {
            case 30005:
                dataManager.getSettings().getData().remove("signs");
                dataManager.getSettings().getData().add("signs", new JsonArray());
                dataManager.save();
                this.hoverEntry = "";
                break;
            case 30006:
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(lastScreen);
                break;
            case 30007:
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new RemoveSavedSignGui(this, dataManager));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        DrawAPI drawAPI = DrawAPI.getAPI();

        drawAPI.drawAutoDimmedBackground(this.scrollbar.getScrollY());

        JsonObject data = dataManager.getSettings().getData();

        if (data.has("signs")){
            JsonArray signs = data.getAsJsonArray("signs");

            this.scrollbar.update(signs.size());

            int midX = this.getWidth() / 2;
            int entryWidth = 300;
            int entryHeight = 52;
            double posY = 45.0 + this.scrollbar.getScrollY();

            boolean hoveringEntry = false;

            for(JsonElement json : signs) {
                JsonObject jsonObj = json.getAsJsonObject();
                String signTitle = jsonObj.get("title").toString();
                signTitle = signTitle.replace("\"", "");

                JsonObject jsonObject = jsonObj.has("content") ? jsonObj.get("content").getAsJsonObject() : new JsonObject();

                String[] signLines = new String[] {jsonObject.has("0") ? jsonObject.get("0").getAsString() : "l1",jsonObject.has("1") ? jsonObject.get("1").getAsString() : "l2",jsonObject.has("2") ? jsonObject.get("2").getAsString() : "l3",jsonObject.has("3") ? jsonObject.get("3").getAsString() : "l4"};


                SignInfoRenderer signInfoRenderer = new SignInfoRenderer(signTitle, signLines);
                if((double)mouseY > posY && (double)mouseY < posY + (double)entryHeight && mouseX > midX - entryWidth / 2 && mouseX < midX + entryWidth / 2 + 5){
                    this.hoverEntry = signTitle;
                    hoveringEntry = true;
                    drawAPI.drawRect(midX - entryWidth / 2 - 2, posY - 2.0, midX + entryWidth / 2 + 2, posY + (double)entryHeight - 2.0, ModColor.toRGB(128,128, 128, 255));
                    drawAPI.drawRect(midX - entryWidth / 2 - 1, posY - 1.0, midX + entryWidth / 2 + 1, posY + (double)entryHeight - 3.0, ModColor.toRGB(0, 0, 0, 255));
                }
                signInfoRenderer.drawEntry(midX - entryWidth / 2, (int)posY, entryWidth + 5, mouseX, mouseY);
                posY += (double)entryHeight;
            }
            if(!hoveringEntry){this.hoverEntry = null;}
        }

        drawAPI.drawOverlayBackground(0, 41);
        drawAPI.drawGradientShadowTop(41.0, 0.0, this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 40, this.getHeight());
        drawAPI.drawGradientShadowBottom(this.getHeight() - 40, 0.0, this.getWidth());

        drawAPI.drawCenteredString("Sign Tools - Gemte Skilte", (double)(this.getWidth() / 2), 20.0D, 2.0D);
        this.scrollbar.draw();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if(hoverEntry != null){
            JsonArray data = dataManager.getSettings().getData().get("signs").getAsJsonArray();

            JsonObject obj = new JsonObject();
            for (JsonElement entry: data) {
                if(entry.getAsJsonObject().get("title").getAsString().equals(hoverEntry)){
                    obj = entry.getAsJsonObject().has("content") ? entry.getAsJsonObject().get("content").getAsJsonObject() : new JsonObject();
                }
            }

            signBuffer.setLine(1, obj.has("0") ? obj.get("0").getAsString() : "");
            signBuffer.setLine(2, obj.has("1") ? obj.get("1").getAsString() : "");
            signBuffer.setLine(3, obj.has("2") ? obj.get("2").getAsString() : "");
            signBuffer.setLine(4, obj.has("3") ? obj.get("3").getAsString() : "");

            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(lastScreen);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }

    @Override
    public void handleMouseInput() {
        this.scrollbar.mouseInput();
    }

    @Override
    public void keyTyped(char typedChar, Key key) {

    }

    @Override
    public void onGuiClosed() {

    }
}