package dk.transporter.mads_gamer_dk.listeners;

import net.labymod.utils.Consumer;
import net.minecraft.network.PacketBuffer;

import java.util.Arrays;

public class PluginMessageListener extends net.labymod.main.listeners.PluginMessageListener {


    @Override
    public void receiveMessage(String channelName, PacketBuffer packetBuffer) {
        super.receiveMessage(channelName, packetBuffer);
        //TODO add server check
    }
}
