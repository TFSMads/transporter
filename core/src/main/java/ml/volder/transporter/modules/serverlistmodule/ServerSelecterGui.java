
package ml.volder.transporter.modules.serverlistmodule;

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
import ml.volder.unikapi.utils.ColorUtils;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

@AutoActivity
public class ServerSelecterGui extends TransporterActivity {
    private Scrollbar scrollbar;
    private String hoverServer;
    private DataManager<Data> dataManager;

    public ServerSelecterGui(DataManager<Data> dataManager) {
        this.dataManager = dataManager;
    }

    public void updateScreen() {
    }

    @Override
    public void initGui() {
        this.scrollbar = new Scrollbar(36);
        this.scrollbar.setPosition(this.getWidth() / 2 + 150 + 4, 41, this.getWidth() / 2 + 150 + 4 + 6, this.getHeight() - 40);
        this.scrollbar.setSpeed(20);
        this.scrollbar.setSpaceBelow(5);

        addButton(new WrappedGuiButton(30007, this.getWidth() / 2 - 50 - 5 - 100, this.getHeight() - 30, 100, 20, "Remove server"));
        addButton(new WrappedGuiButton(30006, this.getWidth() / 2 - 50, this.getHeight() - 30, 100, 20, "Direct Connect"));
        addButton(new WrappedGuiButton(30005, this.getWidth() / 2 + 50 + 5, this.getHeight() - 30, 100, 20, "Add server"));
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        switch (button.getId()) {
            case 30005:
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new AddServerGui(this, true, dataManager));
                break;
            case 30006:
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new AddServerGui(this, false, dataManager));
                break;
            case 30007:
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new RemoveServerGui(this, dataManager));

        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        DrawAPI drawAPI = DrawAPI.getAPI();

        drawAPI.drawAutoDimmedBackground(this.scrollbar.getScrollY());

        JsonObject data = dataManager.getSettings().getData();

        if (data.has("servers")){
            JsonArray servers = data.getAsJsonArray("servers");

            this.scrollbar.update(servers.size());

            int midX = this.getWidth() / 2;
            int entryWidth = 300;
            int entryHeight = 36;
            double posY = 45.0 + this.scrollbar.getScrollY();

            boolean hoveringServer = false;

            for(JsonElement json : servers) {
                JsonObject jsonObj = json.getAsJsonObject();
                String serverName = jsonObj.get("serverNavn").toString();
                serverName = serverName.replace("\"", "");
                ServerInfoRenderer serverInfoRenderer = new ServerInfoRenderer(serverName);
                if((double)mouseY > posY && (double)mouseY < posY + (double)entryHeight && mouseX > midX - entryWidth / 2 && mouseX < midX + entryWidth / 2 + 5){
                    this.hoverServer = serverName;
                    hoveringServer = true;
                    drawAPI.drawRect(midX - entryWidth / 2 - 2, posY - 2.0, midX + entryWidth / 2 + 2, posY + (double)entryHeight - 2.0, ColorUtils.toRGB(128,128, 128, 255));
                    drawAPI.drawRect(midX - entryWidth / 2 - 1, posY - 1.0, midX + entryWidth / 2 + 1, posY + (double)entryHeight - 3.0, ColorUtils.toRGB(0, 0, 0, 255));
                }
                serverInfoRenderer.drawEntry(midX - entryWidth / 2, (int)posY, entryWidth + 5, mouseX, mouseY);
                posY += (double)entryHeight;
            }
            if(!hoveringServer){this.hoverServer = null;}
        }

        drawAPI.drawOverlayBackground(0, 41);
        drawAPI.drawGradientShadowTop(41.0, 0.0, this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 40, this.getWidth());
        drawAPI.drawGradientShadowBottom(this.getHeight() - 40, 0.0, this.getWidth());

        TextComponent component = Component.text("Server Selecter").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD);

        Laby.references().renderPipeline().componentRenderer().builder()
                .text(component)
                .pos((float) getWidth() / 2, 20)
                .scale(2)
                .centered(true)
                .render(getStack());

        this.scrollbar.draw();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if(hoverServer != null){
            PlayerAPI.getAPI().sendCommand("server " + hoverServer);
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