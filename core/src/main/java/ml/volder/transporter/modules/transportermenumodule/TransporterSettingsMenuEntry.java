package ml.volder.transporter.modules.transportermenumodule;

import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ScrollableGrid;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.TransporterMenuModule;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.utils.ColorUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.render.matrix.Stack;

import java.util.Collections;

public class TransporterSettingsMenuEntry extends ScrollableGrid.Entry {
    private int width = 120;
    private int height = 37;

    private int outlineWidth = 1;

    private Item item;

    private boolean isActive = false;
    private boolean isMouseOver = false;

    public TransporterSettingsMenuEntry(Item item, int width, int height) {
        this.item = item;
        this.width = width;
        this.height = height;
        this.isActive = ModuleManager.getInstance().getModule(TransporterMenuModule.class).isActiveItem(item);
    }

    public void render(int x, int y, int mouseX, int mouseY, Stack stack) {
        hoverText = null;
        hoverTextX = -1000;
        hoverTextY = -1000;
        if(item == null)
            return;
        isMouseOver = mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
        DrawAPI drawAPI = DrawAPI.getAPI();
        int color = !isActive
                ? ColorUtils.toRGB(100, 50, 50, isMouseOver ? 90 : 70)
                : ColorUtils.toRGB(50, 100, 50, isMouseOver ? 90 : 70);
        drawAPI.drawRect(x, y, x + width, y + height, color);
        drawAPI.drawRect(x, y, x + width, y + outlineWidth, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y + height - outlineWidth, x + width, y + height, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x, y, x + outlineWidth, y + height, ColorUtils.toRGB(128,128, 128, 255));
        drawAPI.drawRect(x + width - outlineWidth, y, x + width, y + height, ColorUtils.toRGB(128,128, 128, 255));

        drawAPI.drawItem(item.getMaterial(), item.getItemDamage(), x + 2, y + 3, "", 2);
        drawAPI.drawString(item.getDisplayName().length() >= 15 ? item.getDisplayName().substring(0, 14) : item.getDisplayName(), x + 32 + 4, y + 5, 0xFFFFFF);

        Component component = Component.text(isActive ? "Aktiv" : "Ikke Aktiv").color(isActive ? NamedTextColor.GREEN : NamedTextColor.RED);

        Laby.references().renderPipeline().componentRenderer().builder()
                .text(component)
                .pos(x + 32 + 4, y + 14)
                .scale(0.8F)
                .render(stack);

        if(isMouseOver) {
            hoverText = "Klik for at  " + (isActive ? "deaktivere" : "aktivere") + " denne item i transporter menuen!";
            hoverTextX = mouseX;
            hoverTextY = mouseY;
        }
    }

    private String hoverText = null;
    private int hoverTextX = -1000;
    private int hoverTextY = -1000;

    public void renderHoverText() {
        if(hoverText == null || hoverTextX == -1000 || hoverTextY == -1000)
            return;
        DrawAPI.getAPI().drawHoverText(Collections.singletonList(hoverText), hoverTextX, hoverTextY);
    }



    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(!mouseButton.isLeft())
            return;
        if(isMouseOver) {
            isActive = !isActive;
            if(isActive) {
                ModuleManager.getInstance().getModule(TransporterMenuModule.class).addActiveItem(item);
            }else {
                ModuleManager.getInstance().getModule(TransporterMenuModule.class).removeActiveItem(item);
            }
        }
    }
}
