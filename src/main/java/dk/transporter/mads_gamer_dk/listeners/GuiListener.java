package dk.transporter.mads_gamer_dk.listeners;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.guis.signtools.CustomGuiEditSign;
import dk.transporter.mads_gamer_dk.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiListener {
    private TransporterAddon addon;

    public GuiListener(TransporterAddon addon) {
        this.addon = addon;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if(event.gui instanceof GuiEditSign){
            TileEntitySign sign = (TileEntitySign) ReflectionUtils.getPrivateFieldValueByType(event.gui, GuiEditSign.class, TileEntitySign.class);
            event.setCanceled(true);
            Minecraft.getMinecraft().displayGuiScreen(new CustomGuiEditSign(sign, addon));
        }
    }
}
