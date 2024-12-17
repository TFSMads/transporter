package ml.volder.transporter.messaging.channels;

import ml.volder.transporter.messaging.TAByteBuf;
import ml.volder.transporter.modules.BalanceModule;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;

import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
public class BalanceChannel extends Channel {

    private static BalanceChannel instance;

    public static BalanceChannel init() {
        if (instance != null) {
            throw new IllegalStateException("BalanceChannel already initialized");
        }
        instance = new BalanceChannel();
        return instance;
    }

    private BalanceChannel() {
        super(6, "balance");
    }

    public void sendPayload() {
        sendPayload(new byte[0]);
    }

    @Override
    public void handleIncomingPayload(byte[] payload) {
        TAByteBuf buf = TAByteBuf.create();
        buf.writeBytes(payload);
        double balance = buf.readDouble();
        ModuleManager.getInstance().getModule(BalanceModule.class).updateBalance(BigDecimal.valueOf(balance));
        UnikAPI.LOGGER.debug("Update balance to " + balance, Logger.DEBUG_LEVEL.LOWEST);
    }
}
