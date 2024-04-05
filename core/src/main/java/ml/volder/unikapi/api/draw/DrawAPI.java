package ml.volder.unikapi.api.draw;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ResourceLocation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public interface DrawAPI {


    //region Other stuff
    int getScaledWidth();
    int getScaledHeight();
    int getTextureWidth();
    int getTextureHeight();

    default void registerTransporterBadgeRenderer(Predicate<UUID> shouldDraw) {}

    default void drawHoverText(List<String> text, int x, int y) {
        int boxWidth = 0;
        for (Iterator<String> lineIterator = text.iterator(); lineIterator.hasNext(); ) {
            String lineText = lineIterator.next();
            int lineWidth = getStringWidth(lineText);
            if (lineWidth > boxWidth)
                boxWidth = lineWidth;
        }
        int startX = x + 12;
        int startY = y - 12;
        int lvt_8_1_ = 8;
        if (text.size() > 1)
            lvt_8_1_ += 2 + (text.size() - 1) * 10;
        if (startX + boxWidth > getScaledWidth())
            startX -= 28 + boxWidth;
        if (startY + lvt_8_1_ + 6 > getScaledHeight())
            startY = getScaledHeight() - lvt_8_1_ - 6;
        int lvt_9_1_ = -267386864;
        drawGradientRect(startX - 3, startY - 4, startX + boxWidth + 3, startY - 3, lvt_9_1_, lvt_9_1_);
        drawGradientRect(startX - 3, startY + lvt_8_1_ + 3, startX + boxWidth + 3, startY + lvt_8_1_ + 4, lvt_9_1_, lvt_9_1_);
        drawGradientRect(startX - 3, startY - 3, startX + boxWidth + 3, startY + lvt_8_1_ + 3, lvt_9_1_, lvt_9_1_);
        drawGradientRect(startX - 4, startY - 3, startX - 3, startY + lvt_8_1_ + 3, lvt_9_1_, lvt_9_1_);
        drawGradientRect(startX + boxWidth + 3, startY - 3, startX + boxWidth + 4, startY + lvt_8_1_ + 3, lvt_9_1_, lvt_9_1_);
        int lvt_10_1_ = 1347420415;
        int lvt_11_1_ = (lvt_10_1_ & 0xFEFEFE) >> 1 | lvt_10_1_ & 0xFF000000;
        drawGradientRect(startX - 3, startY - 3 + 1, startX - 3 + 1, startY + lvt_8_1_ + 3 - 1, lvt_10_1_, lvt_11_1_);
        drawGradientRect(startX + boxWidth + 2, startY - 3 + 1, startX + boxWidth + 3, startY + lvt_8_1_ + 3 - 1, lvt_10_1_, lvt_11_1_);
        drawGradientRect(startX - 3, startY - 3, startX + boxWidth + 3, startY - 3 + 1, lvt_10_1_, lvt_10_1_);
        drawGradientRect(startX - 3, startY + lvt_8_1_ + 2, startX + boxWidth + 3, startY + lvt_8_1_ + 3, lvt_11_1_, lvt_11_1_);
        for (int lvt_12_1_ = 0; lvt_12_1_ < text.size(); lvt_12_1_++) {
            String lvt_13_1_ = text.get(lvt_12_1_);
            drawStringWithShadow(lvt_13_1_, startX, startY, -1);
            if (lvt_12_1_ == 0)
                startY += 2;
            startY += 10;
        }
    }
    //endregion


    //region Texture related

    //TODO include blend boolean in texture draw call (since GL calls through drawAPI is no longer supported)
    //TODO create textureRenderer API

    void drawTexturedModalRect(double left, double top, double right, double bottom);
    void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height);
    void drawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight, float alpha);
    void drawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight);
    void drawTexture(double x, double y, double texturePosX, double texturePosY, double imageWidth, double imageHeight, double maxWidth, double maxHeight, float alpha);
    void drawTexture(double x, double y, double texturePosX, double texturePosY, double imageWidth, double imageHeight, double maxWidth, double maxHeight);
    void bindTexture(ResourceLocation resourceLocation);
    //endregion

    //region String related

    String trimStringToWidth(String text, int width);
    String trimStringToWidth(String text, int width, boolean reverse);
    int getFontHeight();

    int getStringWidth(String text);
    default List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        if (wrapWidth < 10) {
            wrapWidth = 10;
        }
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    default List<String> listFormattedStringToWidth(String str, int wrapWidth, int maxLines) {
        List<String> list = this.listFormattedStringToWidth(str, wrapWidth);
        if (list.size() < maxLines) {
            return list;
        } else {
            List<String> output = new ArrayList();
            int count = 0;
            Iterator var7 = list.iterator();

            while(var7.hasNext()) {
                String line = (String)var7.next();
                ++count;
                output.add(line);
                if (count >= maxLines) {
                    break;
                }
            }

            return output;
        }
    }

    void renderString(String text, float x, float y, boolean shadow, boolean centered, float scale, int color);

    default void drawString(String text, double x, double y) {
        renderString(text, (float) x, (float) y, true, false, 1,16777215);
    }

    default void drawString(String text, int x, int y, int color) {
        renderString(text, (float) x, (float) y, false, false, 1, color);
    }

    default void drawString(String text, double x, double y, int color, double size) {
        renderString(text, (float) x, (float) y, true, false, (float) size, color);
    }

    default void drawStringWithShadow(String text, double x, double y, int color) {
        renderString(text, (float) x, (float) y, true, false, (float) 1, color);
    }

    default void drawRightString(String text, double x, double y) {
        this.drawString(text, x - (double)this.getStringWidth(text), y);
    }

    default void drawRightStringWithShadow(String text, int x, int y, int color) {
        drawStringWithShadow(text, (float)(x - this.getStringWidth(text)), (float)y, color);
    }

    default void drawCenteredString(String text, double x, double y) {
        renderString(text, (float) x, (float) y, true, true, 1F, 16777215);
    }

    default void drawString(String text, double x, double y, double size) {
        renderString(text, (float) x, (float) y, true, false, (float) size, 16777215);
    }

    default void drawCenteredString(String text, double x, double y, double size) {
        renderString(text, (float) x, (float) y, true, true, (float) size, 16777215);

    }

    default void drawRightString(String text, double x, double y, double size) {
        this.drawString(text, x / size - (double)this.getStringWidth(text), y / size);
    }
    //endregion

    //region Rectangle related
    void drawRect(double left, double top, double right, double bottom, int color);

    default boolean drawRect(int mouseX, int mouseY, double left, double top, double right, double bottom, int color, int hoverColor) {
        boolean hover = (double)mouseX > left && (double)mouseX < right && (double)mouseY > top && (double)mouseY < bottom;
        this.drawRect(left, top, right, bottom, hover ? hoverColor : color);
        return hover;
    }

    default boolean drawRect(int mouseX, int mouseY, String displayString, double left, double top, double right, double bottom, int color, int hoverColor) {
        boolean hover = (double)mouseX > left && (double)mouseX < right && (double)mouseY > top && (double)mouseY < bottom;
        this.drawRect(left, top, right, bottom, hover ? hoverColor : color);
        this.drawCenteredString(displayString, left + (right - left) / 2.0D, top + (bottom - top) / 2.0D - 4.0D);
        return hover;
    }

    default void drawRectangle(int left, int top, int right, int bottom, int color) {
        drawRect(left, top, right, bottom, color);
    }

    default void drawRectBorder(double left, double top, double right, double bottom, int color, double thickness) {
        this.drawRect(left + thickness, top, right - thickness, top + thickness, color);
        this.drawRect(right - thickness, top, right, bottom, color);
        this.drawRect(left + thickness, bottom - thickness, right - thickness, bottom, color);
        this.drawRect(left, top, left + thickness, bottom, color);
    }

    void drawGradientRect(int left, int top, int right, int bottom, Color startColor, Color endColor);

    default void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        drawGradientRect(left, top, right, bottom, new Color(startColor), new Color(endColor));
    }

    //endregion

    //region Background related
    default void drawAutoDimmedBackground(double d) {
        if (MinecraftAPI.getAPI() != null && MinecraftAPI.getAPI().isInGame()) {
            this.drawIngameBackground();
        } else {
            this.drawDimmedBackground((int)d);
        }
    }

    default void drawBackground(int tint) {
        this.drawBackground(tint, 0.0D, 64);
    }

    default void drawDimmedBackground(int scroll) {
        this.drawBackground(0, (double)(-scroll), 32);
    }

    default void drawAutoDimmedBackground(int left, int top, int right, int bottom) {
        if (MinecraftAPI.getAPI() != null && MinecraftAPI.getAPI().isInGame()) {
            int startColor = -1072689136;
            int endColor = -804253680;
            float f = (float)(startColor >> 24 & 255) / 255.0F;
            float f1 = (float)(startColor >> 16 & 255) / 255.0F;
            float f2 = (float)(startColor >> 8 & 255) / 255.0F;
            float f3 = (float)(startColor & 255) / 255.0F;
            float f4 = (float)(endColor >> 24 & 255) / 255.0F;
            float f5 = (float)(endColor >> 16 & 255) / 255.0F;
            float f6 = (float)(endColor >> 8 & 255) / 255.0F;
            float f7 = (float)(endColor & 255) / 255.0F;
            this.drawGradientRect(left, top, right, bottom, new Color(f1, f2, f3, f), new Color(f5, f6, f7, f4));
        } else {
            this.drawDimmedOverlayBackground(left, top, right, bottom);
        }
    }

    default void drawGradientShadowTop(double y, double left, double right) {
        int transitionLength = 4;
        drawGradientRect((int)left, (int)y , (int)right, (int)y + transitionLength, new Color(0,0,0,255), new Color(0,0,0,0));
    }

    default void drawGradientShadowBottom(double y, double left, double right) {
        int transitionLength = 4;
        drawGradientRect((int)left, (int)y-transitionLength, (int)right, (int)y, new Color(0,0,0,0), new Color(0,0,0,255));
    }

    default void drawIngameBackground() {
        int startColor = -1072689136;
        int endColor = -804253680;
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        this.drawGradientRect(0, 0, this.getScaledWidth(), this.getScaledHeight(), new Color(f1, f2, f3, f), new Color(f5, f6, f7, f4));
    }

    void drawBackground(int tint, double scrolling, int brightness);
    void drawDimmedOverlayBackground(int left, int top, int right, int bottom);
    void drawOverlayBackground(int startY, int endY);
    //endregion

    //region Item related
    default void renderItemOverlayIntoGUI(double xPosition, double yPosition, String text) {
        if(text == null || text.length() == 0)
            return;
        drawString(text, xPosition + 19D - 2D - getStringWidth(text), yPosition + 6D + 3D);
    }
    default void drawItem(Material material, double xPosition, double yPosition, String value) {
        drawItem(material, 0, xPosition, yPosition, value);
    }

    default void drawItem(Material material, int itemDamage, double xPosition, double yPosition, String value) {
        drawItem(material, itemDamage, xPosition, yPosition, value, 1);
    }

    default void drawItem(Material material, int itemDamage, double xPosition, double yPosition, String value, double scaleMultiplier) {
        renderItemIntoGUI(material, itemDamage, xPosition, yPosition, scaleMultiplier);
        renderItemOverlayIntoGUI(xPosition, yPosition, value);
    }
    void renderItemIntoGUI(Material material, int itemDamage, double x, double y, double scale);
    //endregion

    //region Utils used by default methods

    default boolean isFormatColor(char colorChar)
    {
        return colorChar >= 48 && colorChar <= 57 || colorChar >= 97 && colorChar <= 102 || colorChar >= 65 && colorChar <= 70;
    }

    default boolean isFormatSpecial(char formatChar)
    {
        return formatChar >= 107 && formatChar <= 111 || formatChar >= 75 && formatChar <= 79 || formatChar == 114 || formatChar == 82;
    }

    default String getFormatFromString(String text)
    {
        String s = "";
        int i = -1;
        int j = text.length();

        while ((i = text.indexOf(167, i + 1)) != -1)
        {
            if (i < j - 1)
            {
                char c0 = text.charAt(i + 1);

                if (isFormatColor(c0))
                {
                    s = "\u00a7" + c0;
                }
                else if (isFormatSpecial(c0))
                {
                    s = s + "\u00a7" + c0;
                }
            }
        }

        return s;
    }

    default String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int i = trimStringToWidth(str, wrapWidth).length();

        if (str.length() <= i)
        {
            return str;
        }
        else
        {
            String s = str.substring(0, i);
            char c0 = str.charAt(i);
            boolean flag = c0 == 32 || c0 == 10;
            String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
            return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
        }
    }

    //endregion

    ApiProvider<DrawAPI> apiProvider = new ApiProvider<>("DrawAPI");

    static DrawAPI getAPI() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.api.draw.impl", ApiReferenceStorage::getDrawAPI, DrawAPI.class);
    }

}
