package ml.volder.transporter.dev.command;

import ml.volder.transporter.dev.command.subcommands.EventSubCommand;
import ml.volder.transporter.dev.command.subcommands.PacketSubCommand;
import ml.volder.transporter.dev.command.subcommands.TestGuiSubCommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class DevCommand extends Command {


  public DevCommand() {
    super("dev", "developer");

      this.withSubCommand(new PacketSubCommand("packet", "p"));
      this.withSubCommand(new TestGuiSubCommand("gui", "g"));
      this.withSubCommand(new EventSubCommand("event", "e"));
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    this.displayMessage(Component.text("Developer commands", NamedTextColor.GOLD));
    for (SubCommand subCommand : this.getSubCommands()) {
      this.displayMessage(Component.text(" - /dev " + subCommand.getPrefix(), NamedTextColor.GRAY));
    }

    return true;
  }
}
