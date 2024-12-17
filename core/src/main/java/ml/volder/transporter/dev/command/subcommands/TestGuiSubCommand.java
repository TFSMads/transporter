package ml.volder.transporter.dev.command.subcommands;

import ml.volder.transporter.dev.test.TestActivity;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import org.jetbrains.annotations.NotNull;

public class TestGuiSubCommand extends SubCommand {
    /**
     * Instantiates a new sub command.
     *
     * @param subPrefix the prefix of the sub command
     * @param aliases   the aliases
     */
    public TestGuiSubCommand(@NotNull String subPrefix, @NotNull String... aliases) {
        super(subPrefix, aliases);
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        Laby.labyAPI().minecraft().executeOnRenderThread(() -> Laby.labyAPI().minecraft().executeNextTick(() ->Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new TestActivity())));
        return true;
    }
}
