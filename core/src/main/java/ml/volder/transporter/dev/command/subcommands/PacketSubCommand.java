package ml.volder.transporter.dev.command.subcommands;

import ml.volder.transporter.messaging.PluginMessageHandler;
import ml.volder.transporter.messaging.channels.*;
import net.labymod.api.client.chat.command.SubCommand;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketSubCommand extends SubCommand {
    /**
     * Instantiates a new sub command.
     *
     * @param subPrefix the prefix of the sub command
     * @param aliases   the aliases
     */
    public PacketSubCommand(@NotNull String subPrefix, @NotNull String... aliases) {
        super(subPrefix, aliases);
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if(arguments.length >= 1) {
            if(arguments[0].equalsIgnoreCase("get")) {
                if(arguments.length == 3) {
                    String item = arguments[1];
                    Integer amount = Integer.parseInt(arguments[2]);
                    PluginMessageHandler.getChannel(GetChannel.class).sendPayload(item, amount);
                }
            } else if(arguments[0].equalsIgnoreCase("put")) {
                if(arguments.length == 3) {
                    String item = arguments[1];
                    Integer amount = Integer.parseInt(arguments[2]);
                    PluginMessageHandler.getChannel(PutChannel.class).sendPayload(item, amount);

                }
            } else if(arguments[0].equalsIgnoreCase("send")) {
                if(arguments.length == 4) {
                    String item = arguments[2];
                    Integer amount = Integer.parseInt(arguments[3]);
                    UUID player = UUID.fromString(arguments[1]);
                    PluginMessageHandler.getChannel(SendChannel.class).sendPayload(item, amount, player);
                }
            } else if(arguments[0].equalsIgnoreCase("infoall")) {
                PluginMessageHandler.getChannel(InfoAllChannel.class).sendPayload("sand", "stone", "dirt");
            } else if(arguments[0].equalsIgnoreCase("balance")) {
                PluginMessageHandler.getChannel(BalanceChannel.class).sendPayload();
            }
        }
        return true;
    }
}
