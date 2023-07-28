package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.unikapi.api.player.PlayerAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterSendMessageHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterSendMessageHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        return matchSendMessage(clean) || matchSendOffline(clean) || matchSendSelf(clean) || matchModtagMessage(clean);
    }

    private boolean matchSendMessage(String clean){
        final Pattern pattern = Pattern.compile("^Du sendte ([0-9]+) (" + module.getItemRegex() + ") til ([a-zA-Z0-9_]+)$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByChatName(matcher.group(2));
            item.setAmountInTransporter(item.getAmountInTransporter()-Integer.parseInt(matcher.group(1)));
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("sendSuccess"), item.getDisplayName().toLowerCase(), matcher.group(1), String.valueOf(item.getAmountInTransporter()), matcher.group(3)));
            return true;
        }
        return false;
    }

    private boolean matchSendOffline(String clean){
        final Pattern pattern = Pattern.compile("^Denne spiller er ikke online$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("sendOffline"), null, null, null));
            return true;
        }
        return false;
    }

    private boolean matchSendSelf(String clean){
        final Pattern pattern = Pattern.compile("^Du kan ikke sende til dig selv$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("sendSelf"), null, null, null));
            return true;
        }
        return false;
    }

    private boolean matchModtagMessage(String clean){
        final Pattern pattern = Pattern.compile("^Du modtog ([0-9]+) (" + module.getItemRegex() + ") fra ([a-zA-Z0-9_]+)$");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByChatName(matcher.group(2));
            item.setAmountInTransporter(item.getAmountInTransporter()+Integer.parseInt(matcher.group(1)));
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("modtagSuccess"), item.getDisplayName().toLowerCase(), matcher.group(1), String.valueOf(item.getAmountInTransporter()), matcher.group(3)));
            return true;
        }
        return false;
    }
}
