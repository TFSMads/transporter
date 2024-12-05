package ml.volder.transporter.messaging.channels;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.messaging.TAByteBuf;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;

import javax.inject.Singleton;
import java.util.Deque;
import java.util.LinkedList;

@Singleton
public class PutChannel extends Channel {

    private static PutChannel instance;

    public static PutChannel init() {
        if (instance != null) {
            throw new IllegalStateException("PutChannel already initialized");
        }
        instance = new PutChannel();
        return instance;
    }

    private PutChannel() {
        super(1, "put");
    }


    private final Deque<String> itemDeque = new LinkedList<>();

    public void sendPayload(String item, Integer amount) {
        TAByteBuf buf = TAByteBuf.create();
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
            UnikAPI.LOGGER.debug("Put " + amount + " " + item.getDisplayName() + " in transporter", Logger.DEBUG_LEVEL.LOWEST);
        }
    }

    @Override
    public void payloadNotDispatched(byte[] payload) {
        itemDeque.clear();
    }
}
