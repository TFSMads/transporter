package dk.transporter.mads_gamer_dk.guis.getItemsGui;

import dk.transporter.mads_gamer_dk.guis.TransporterInfoGui.TransporterInfoGui;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private String itemName;
    private String itemInfoLine2;
    private ItemSelectGui gui;
    private ItemStack item;




    public ItemRenderer(String serverName, ItemStack item, ItemSelectGui gui) {
        this.item = item;
        this.gui = gui;
        this.init(serverName);
    }

    public void init(String itemName) {
        this.itemName = itemName;
        this.itemInfoLine2 = "Klik her for at vælge denne item.";
    }

    public void drawEntry(int x, int y, int listWidth, int mouseX, int mouseY) {
        LabyModCore.getMinecraft().getFontRenderer().drawString(itemName, x + 32 + 3, y + 1, 0xFFFFFF);
        List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(itemInfoLine2, listWidth - 32 - 2);
        for (int i = 0; i < Math.min(list.size(), 2); ++i) {
            LabyModCore.getMinecraft().getFontRenderer().drawString(list.get(i), x + 32 + 3, y + 12 + LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * i, 0x808080);
        }
        this.drawServerIcon(x, y);
    }




    protected void drawServerIcon(int posX, int posY) {
        gui.drawItem(item,posX+8,posY+8,"");
    }


}
