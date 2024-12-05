package ml.volder.transporter.messaging.channels;

import ml.volder.transporter.messaging.PluginMessageHandler;
import ml.volder.transporter.messaging.TAByteBuf;

public abstract class Channel {
    private final int channelId;
    private final String channelName;

    protected Channel(int channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public abstract void handleIncomingPayload(byte[] payload);
    public void payloadNotDispatched(byte[] payload) {
        // Do nothing
    }

    public void sendPayload(byte[] payload, Channel channel) {
        TAByteBuf buf = TAByteBuf.create();
        buf.writeVarInt(channel.channelId);
        buf.writeBytes(payload);
        PluginMessageHandler.sendPayload(buf.toByteArray(), channel);
    }

    public void sendPayload(byte[] payload, int channelId) {
        sendPayload(payload, PluginMessageHandler.getChannel(channelId));
    }
    public void sendPayload(byte[] payload) {
        sendPayload(payload, this);
    }

    public int getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }
}
