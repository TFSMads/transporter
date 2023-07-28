package ml.volder.transporter.modules.serverlistmodule;



import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.types.ResourceLocation;

import java.util.List;

public class ServerInfoRenderer {
    private ResourceLocation serverIcon;
    private String serverName;
    private String serverMotd;

    public ServerInfoRenderer(String serverName) {
        this.init(serverName);
    }

    public void init(String serverName) {
        this.serverName = serverName;
        this.serverMotd = "Klik for at tilslutte.";
        this.serverIcon = ModTextures.SA_LOGO;
    }

    public void drawEntry(int x, int y, int listWidth, int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawString(serverName, x + 32 + 3, y + 1, 0xFFFFFF);
        List<String> list = drawAPI.listFormattedStringToWidth(serverMotd, listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            drawAPI.drawString(list.get(i), x + 32 + 3, y + 12 + drawAPI.getFontHeight() * i, 0x808080);
        }
        this.drawServerIcon(x, y, serverIcon);
    }




    protected void drawServerIcon(int posX, int posY, ResourceLocation resourceLocation) {
        DrawAPI.getAPI().bindTexture(resourceLocation);
        DrawAPI.getAPI().drawTexture(posX, posY, 255.0D, 255.0D, 32.0D, 32.0D);
    }


}
