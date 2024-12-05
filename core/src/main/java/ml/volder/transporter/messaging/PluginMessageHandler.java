package ml.volder.transporter.messaging;

import ml.volder.transporter.events.TransporterChannelRegisteredEvent;
import ml.volder.transporter.messaging.channels.*;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.event.EventManager;
import ml.volder.unikapi.event.EventType;
import ml.volder.unikapi.logger.Logger;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class PluginMessageHandler {

    private static final ResourceLocation CHANNEL = ResourceLocation.create("sa", "transporter");
    private static PluginMessageHandler instance;

    //Channels
    private final Map<Integer, Channel> channels = new HashMap<>();
    private final Map<Class<? extends Channel>, Channel> channelsByClass = new HashMap<>();

    private void registerChannel(Channel channel) {
        channels.put(channel.getChannelId(), channel);
        channelsByClass.put(channel.getClass(), channel);
    }

    private final Deque<Channel> senderDeque = new LinkedList<>();
    private boolean isRegistered = false;

    public static PluginMessageHandler getInstance() {
        if (instance == null) {
            instance = new PluginMessageHandler();
        }
        return instance;
    }

    public void init() {
        Laby.references().eventBus().registerListener(this);
        Laby.references().payloadRegistry().registerPayloadChannel(CHANNEL);


        registerChannel(GetChannel.init());
        registerChannel(PutChannel.init());
        registerChannel(SendChannel.init());
        registerChannel(BalanceChannel.init());
        registerChannel(InfoAllChannel.init());
    }


    public void infoAllPayload() {
        TAByteBuf buf = TAByteBuf.create();
        buf.writeVarInt(4); // Channel: 4 = InfoAll
        String[] excludeItems = new String[]{"sand", "stone"};
        buf.writeByte(excludeItems.length);
        for (String item : excludeItems) {
            buf.writeUTF(item);
        }
        byte[] payload = new byte[buf.readableBytes()];
        buf.readBytes(payload);
        Laby.references().serverController().sendPayload(CHANNEL, payload);
    }

    private <T> T internalGetChannel(Class<T> klass) {
        Channel module = channelsByClass.getOrDefault(klass, null);
        if(module.getClass().isAssignableFrom(klass))
            return (T) module;
        throw new IllegalArgumentException("Channel not found");
    }

    public static <T> T getChannel(Class<T> klass) {
        if(instance == null)
            throw new IllegalStateException("PluginMessageHandler not initialized");
        return instance.internalGetChannel(klass);
    }

    private Channel internalGetChannel(int channelId) {
        Channel channel = channels.getOrDefault(channelId, null);
        if(channel == null)
            throw new IllegalArgumentException("Channel not found");
        return channel;
    }

    public static Channel getChannel(int channelId) {
        if(instance == null)
            throw new IllegalStateException("PluginMessageHandler not initialized");
        return instance.internalGetChannel(channelId);
    }

    @Subscribe
    public void onPayload(NetworkPayloadEvent event) {
        if (event.getPayload().length > 0 && event.identifier() != null) {
            UnikAPI.LOGGER.debug("Received payload - side:" + event.side() + " - " + Arrays.toString(event.getPayload()) + " - " + event.identifier());
        }

        if(event.identifier().toString().equals("minecraft:register")) {
            if(event.side() == NetworkPayloadEvent.Side.SEND) {
                isRegistered = false;
            }
            if(event.side() == NetworkPayloadEvent.Side.RECEIVE) {
                TAByteBuf buf = TAByteBuf.create();
                buf.writeBytes(event.getPayload());
                String[] registeredChannels = buf.readNullSeparatedUTF();
                UnikAPI.LOGGER.debug("Registered channel: " + Arrays.toString(registeredChannels), Logger.DEBUG_LEVEL.LOWEST);

                for(String registeredChannel : registeredChannels) {
                    if (registeredChannel.equals(CHANNEL.toString())) {
                        isRegistered = true;
                        EventManager.callEvent(new TransporterChannelRegisteredEvent(EventType.POST));
                    }
                }
            }
        }

        if(event.side() == NetworkPayloadEvent.Side.RECEIVE && event.identifier().equals(CHANNEL) && !senderDeque.isEmpty()) {
            senderDeque.pollLast().handleIncomingPayload(event.getPayload());
        }
    }

    private void internalSendPayload(byte[] payload, Channel sender) {
        if(!isRegistered) {
            UnikAPI.LOGGER.debug("Ignored payload: Payload channel 'sa:transporter' not registered", Logger.DEBUG_LEVEL.LOWEST);
            sender.payloadNotDispatched(payload);
            return;
        }
        senderDeque.push(sender);
        Laby.references().serverController().sendPayload(CHANNEL, payload);
    }

    public static void sendPayload(byte[] payload, Channel sender) {
        if(instance == null)
            return;
        getInstance().internalSendPayload(payload, sender);
    }
}
