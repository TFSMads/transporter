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
public class GetChannel extends Channel {

    private static GetChannel instance;

    public static GetChannel init() {
        if (instance != null) {
            throw new IllegalStateException("GetChannel already initialized");
        }
        instance = new GetChannel();
        return instance;
    }

    private GetChannel() {
        super(0, "get");
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
            item.setAmountInTransporter(item.getAmountInTransporter() + amount);
            UnikAPI.LOGGER.debug("Took " + amount + " " + item.getDisplayName() + " from transporter", Logger.DEBUG_LEVEL.LOWEST);
        }
    }

    @Override
    public void payloadNotDispatched(byte[] payload) {
        itemDeque.clear();
    }
}
