package dk.transporter.mads_gamer_dk.guis.serverSelector;

import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ServerInfoRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private ResourceLocation serverIcon;
    private String base64;
    private DynamicTexture dynamicTexture;
    private boolean canReachServer = false;
    private int index = 0;
    private String serverName;
    private String serverMotd;




    public ServerInfoRenderer(String serverName) {
        this.init(serverName);
    }

    public void init(String serverName) {
        this.serverName = serverName;
        this.serverMotd = "Klik for at tilslutte.";
        this.serverIcon = new ResourceLocation("transporter/textures/icons/sa.png");
        this.dynamicTexture = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
    }

    public void drawEntry(int x, int y, int listWidth, int mouseX, int mouseY) {
        LabyModCore.getMinecraft().getFontRenderer().drawString(serverName, x + 32 + 3, y + 1, 0xFFFFFF);
        List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(serverMotd, listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            LabyModCore.getMinecraft().getFontRenderer().drawString(list.get(i), x + 32 + 3, y + 12 + LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * i, 0x808080);
        }
        this.drawServerIcon(x, y, new ResourceLocation("transporter/servers/icons/sa.png"));
    }




    protected void drawServerIcon(int posX, int posY, ResourceLocation resourceLocation) {
        LabyMod.getInstance().getDrawUtils().drawImageUrl("https://tfsmads.github.io/Img/sa.png", posX, posY, 255.0D, 255.0D, 32.0D, 32.0D);
    }


}
