package dk.transporter.mads_gamer_dk.utils;

import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TaDrawUtils extends DrawUtils {
    public void drawString(String text, double x, double y, double size, int color) {
        GL11.glPushMatrix();
        GL11.glScaled(size, size, size);
        this.drawString(text, x / size, y / size,  color);
        GL11.glPopMatrix();
    }

    public void drawString(String text, double x, double y, double size, int color, boolean dropShadow) {
        GL11.glPushMatrix();
        GL11.glScaled(size, size, size);
        this.drawString(text, x / size, y / size, color, dropShadow);
        GL11.glPopMatrix();
    }

    public void drawString(String text, double x, double y, int color, boolean dropShadow) {
        this.fontRenderer.drawString(text, (float)x, (float)y, color, dropShadow);
    }

    public void drawString(String text, double x, double y, boolean dropShadow) {
        this.fontRenderer.drawString(text, (float)x, (float)y, 16777215, dropShadow);
    }

    public void drawString(String text, double x, double y, int color) {
        this.fontRenderer.drawString(text, (float)x, (float)y, color, true);
    }

    public void drawSign(int x, int y, String line1, String line2, String line3, String line4){
        ResourceLocation texture = new ResourceLocation("transporter/textures/sign.png");
        //TODO use texture from users texturepack


        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_WIDTH);
        int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_HEIGHT);

        int sizeMultiplier = 4;

        LabyMod.getInstance().getDrawUtils().drawTexture(x,y,256D,256D,width*sizeMultiplier,height*sizeMultiplier);


        fontRenderer.drawString(line1, (x+width*2) - (fontRenderer.getStringWidth(line1) / 2), y+4, 0);
        fontRenderer.drawString(line2, (x+width*2) - (fontRenderer.getStringWidth(line2) / 2), y+(height-2)+4, 0);
        fontRenderer.drawString(line3, (x+width*2) - (fontRenderer.getStringWidth(line3) / 2), y+(height-2)*2+4, 0);
        fontRenderer.drawString(line4, (x+width*2) - (fontRenderer.getStringWidth(line4) / 2), y+(height-2)*3+4, 0);
    }

    public void drawSign(int x, int y, String line1, String line2, String line3, String line4, boolean centerX, boolean centerY){
        ResourceLocation texture = new ResourceLocation("transporter/textures/sign.png");
        //TODO use texture from users texturepack


        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_WIDTH);
        int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_HEIGHT);

        int sizeMultiplier = 4;

        int xOffset = centerX ? width/2*sizeMultiplier : 0;
        int yOffset = centerY ? height/2*sizeMultiplier : 0;

        LabyMod.getInstance().getDrawUtils().drawTexture(x-xOffset,y-yOffset,256D,256D,width*sizeMultiplier,height*sizeMultiplier);


        fontRenderer.drawString(line1, (x+width*2) - (fontRenderer.getStringWidth(line1) / 2)-xOffset, y+4-yOffset, 0);
        fontRenderer.drawString(line2, (x+width*2) - (fontRenderer.getStringWidth(line2) / 2)-xOffset, y+(height-2)+4-yOffset, 0);
        fontRenderer.drawString(line3, (x+width*2) - (fontRenderer.getStringWidth(line3) / 2)-xOffset, y+(height-2)*2+4-yOffset, 0);
        fontRenderer.drawString(line4, (x+width*2) - (fontRenderer.getStringWidth(line4) / 2)-xOffset, y+(height-2)*3+4-yOffset, 0);
    }

}
