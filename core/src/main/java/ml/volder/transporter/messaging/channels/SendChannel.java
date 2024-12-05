package ml.volder.transporter.messaging.channels;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.messaging.TAByteBuf;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;

import javax.inject.Singleton;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

@Singleton
public class SendChannel extends Channel {

    private static SendChannel instance;

    public static SendChannel init() {
        if (instance != null) {
            throw new IllegalStateException("SendChannel already initialized");
        }
        instance = new SendChannel();
        return instance;
    }

    private SendChannel() {
        super(2, "send");
    }


    private final Deque<String> itemDeque = new LinkedList<>();

    public void sendPayload(String item, Integer amount, UUID player) {
        TAByteBuf buf = TAByteBuf.create();
        buf.writeVarLong(player.getMostSignificantBits());
        buf.writeVarLong(player.getLeastSignificantBits());
        buf.writeUTF(item);
        buf.writeVarInt(amount);
        sendPayload(buf.toByteArray());
        itemDeque.push(item);
    }

    @Override
    public void handleIncomingPayload(byte[] payload) {
        TAByteBuf buf = TAByteBuf.create();
        buf.writeBytes(payload);
        int amount = buf.readVarInt();
        if(!itemDeque.isEmpty()) {
            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByType(itemDeque.pollLast());
            if(item == null)
                return;
            item.setAmountInTransporter(item.getAmountInTransporter() - amount);
            UnikAPI.LOGGER.debug("Send " + amount + " of " + item.getDisplayName(), Logger.DEBUG_LEVEL.LOWEST);
        }
    }

    @Override
    public void payloadNotDispatched(byte[] payload) {
        itemDeque.clear();
    }
}
