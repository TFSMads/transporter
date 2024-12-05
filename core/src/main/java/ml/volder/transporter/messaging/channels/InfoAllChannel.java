package ml.volder.transporter.messaging.channels;

import ml.volder.transporter.messaging.TAByteBuf;
import ml.volder.unikapi.UnikAPI;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class InfoAllChannel extends Channel {

    private static InfoAllChannel instance;

    public static InfoAllChannel init() {
        if (instance != null) {
            throw new IllegalStateException("InfoAllChannel already initialized");
        }
        instance = new InfoAllChannel();
        return instance;
    }

    private InfoAllChannel() {
        super(4, "infoall");
    }

    public void sendPayload(String... excludeItems) {
        TAByteBuf buf = TAByteBuf.create();
        buf.writeByte(excludeItems.length);
        UnikAPI.LOGGER.debug("ignore items (sendPayload):");
        for (String item : excludeItems) {
            buf.writeUTF(item);
            UnikAPI.LOGGER.debug(item);
        }

        handle((TAByteBuf) buf.resetReaderIndex());
        sendPayload(buf.toByteArray());
    }

    private void handle(TAByteBuf buf) {
        List<String> ignore = new ArrayList<>();
        int ignoreLength = buf.readByte();
        for (int i = 0; i < ignoreLength; i++) {
            ignore.add(buf.readUTF());
        }

        UnikAPI.LOGGER.debug("ignore items (handle):");
        ignore.forEach(UnikAPI.LOGGER::debug);
    }

    @Override
    public void handleIncomingPayload(byte[] payload) {
        //TODO
    }
}
