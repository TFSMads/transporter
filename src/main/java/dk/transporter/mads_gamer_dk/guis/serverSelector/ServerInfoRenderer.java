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

        /*GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getIcons());
        Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, k * 10, 176 + l * 8, 10, 8, 256.0f, 256.0f);
        if (this.serverData != null && this.serverData.getBase64EncodedIconData() != null && !Objects.equal(this.serverData.getBase64EncodedIconData(), this.base64)) {
            this.base64 = this.serverData.getBase64EncodedIconData();
            this.prepareServerIcon();
        }
        if (this.dynamicTexture != null) {
            this.drawServerIcon(x, y, this.serverIcon);
        } else {
            this.drawServerIcon(x, y, UNKNOWN_SERVER);
        }
        int i1 = mouseX - x;
        int j1 = mouseY - y;
        if (s != null) {
            if (i1 >= listWidth - 15 && i1 <= listWidth - 5 && j1 >= 0 && j1 <= 8 && !s1.isEmpty()) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, s1.split("\n"));
            } else if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8 && !s.isEmpty()) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, s.split("\n"));
            }
        }*/
    }


    /*public boolean drawJoinServerButton(int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
        if (mouseX > x && mouseX < x + listWidth && mouseY > y && mouseY < y + slotHeight) {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            int k1 = mouseX - x;
            if (k1 < 32 && k1 > 16) {
                Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                return true;
            }
            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
        }
        return false;
    }*/

    /*public boolean drawSaveServerButton(int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
        if (mouseX > x && mouseX < x + listWidth && mouseY > y && mouseY < y + slotHeight) {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            int k1 = mouseX - x;
            int l1 = mouseY - y;
            if (k1 < 16 && l1 > 16) {
                Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                return true;
            }
            Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
        }
        return false;
    }*/

    protected void drawServerIcon(int posX, int posY, ResourceLocation resourceLocation) {
        LabyMod.getInstance().getDrawUtils().drawImageUrl("https://tfsmads.github.io/Img/sa.png", posX, posY, 255.0D, 255.0D, 32.0D, 32.0D);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    /*private void prepareServerIcon() {
        if (this.serverData.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.dynamicTexture = null;
        } else {
            BufferedImage bufferedimage;
            block8: {
                ByteBuf bytebuf = Unpooled.copiedBuffer(this.serverData.getBase64EncodedIconData(), Charsets.UTF_8);
                ByteBuf bytebuf1 = Base64.decode(bytebuf);
                try {
                    bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(bytebuf1));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break block8;
                }
                catch (Throwable throwable) {
                    this.serverData.setBase64EncodedIconData(null);
                }
                finally {
                    bytebuf.release();
                    bytebuf1.release();
                }
                return;
            }
            if (this.dynamicTexture == null) {
                this.dynamicTexture = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.serverIcon, this.dynamicTexture);
            }
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.dynamicTexture.getTextureData(), 0, bufferedimage.getWidth());
            this.dynamicTexture.updateDynamicTexture();
        }
    }*/

    /*public ServerInfoRenderer setIndex(int index) {
        this.index = index;
        return this;
    }*/

    /*public boolean canReachServer() {
        return this.canReachServer && this.serverData != null && !this.serverData.isPinging();
    }

    public ServerPingerData getServerData() {
        return this.serverData;
    }

    public ResourceLocation getServerIcon() {
        return this.serverIcon;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public ServerData getLabymodServerData() {
        return this.labymodServerData;
    }

    public void setLabymodServerData(ServerData labymodServerData) {
        this.labymodServerData = labymodServerData;
    }

    public int getIndex() {
        return this.index;
    }*/
}
