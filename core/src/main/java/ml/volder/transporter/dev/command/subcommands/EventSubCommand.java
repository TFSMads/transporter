package ml.volder.transporter.dev.command.subcommands;

import ml.volder.transporter.dev.test.TestListener;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import org.jetbrains.annotations.NotNull;

public class EventSubCommand extends SubCommand {
    /**
     * Instantiates a new sub command.
     *
     * @param subPrefix the prefix of the sub command
     * @param aliases   the aliases
     */
    public EventSubCommand(@NotNull String subPrefix, @NotNull String... aliases) {
        super(subPrefix, aliases);
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if(arguments.length == 1) {
            if(arguments[0].equalsIgnoreCase("register") || arguments[0].equalsIgnoreCase("r")) {
                Laby.references().eventBus().registerListener(new TestListener());
                displayMessage("TestListener registered");
            }
            if(arguments[0].equalsIgnoreCase("fire") || arguments[0].equalsIgnoreCase("f")) {
                //Laby.fireEvent(new TestEvent());
            }
        } else {
            displayMessage("Usage: /" + prefix + " event <register|fire>");
        }

        return true;
    }
}
