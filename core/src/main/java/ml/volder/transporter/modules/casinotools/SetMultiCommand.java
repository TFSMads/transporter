package ml.volder.transporter.modules.casinotools;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

import java.util.function.Consumer;

public class SetMultiCommand extends Command {

  private Consumer<Double> changeListener;

  public SetMultiCommand(Consumer<Double> changeListener) {
    super("setmulti", "setmultiplier");
    this.changeListener = changeListener;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if(arguments.length < 1){
      this.displayMessage(Component.text("Korekt brug: /setmulti <multi>", NamedTextColor.RED));
      return true;
    }
    double multi;
    try {
      multi = Double.parseDouble(arguments[0]);
    } catch (NumberFormatException exception) {
      this.displayMessage(Component.text("Multiplieren skal være et tal!", NamedTextColor.RED));
      return true;
    }
    if(multi < 0) {
      this.displayMessage(Component.text("Multiplieren kan ikke være under 0!", NamedTextColor.RED));
      return true;
    }

    changeListener.accept(multi);
    this.displayMessage(Component.text("Multiplieren blev ændret til " + multi, NamedTextColor.GREEN));

    return true;
  }
}
