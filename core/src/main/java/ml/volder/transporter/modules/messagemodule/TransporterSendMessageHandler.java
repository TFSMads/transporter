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
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("send_message"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            String amountMatch = matcher.group("amount") != null ? matcher.group("amount") : "ukendt";
            String itemMatch = matcher.group("item") != null ? matcher.group("item") : "ukendt";
            String playerMatch = matcher.group("player") != null ? matcher.group("player") : "ukendt";

            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByName(itemMatch);
            item.setAmountInTransporter(item.getAmountInTransporter()-Integer.parseInt(amountMatch));
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("sendSuccess"), item.getDisplayName().toLowerCase(), amountMatch, String.valueOf(item.getAmountInTransporter()), playerMatch));
            return true;
        }
        return false;
    }

    private boolean matchSendOffline(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("send_offline"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("sendOffline"), null, null, null));
            return true;
        }
        return false;
    }

    private boolean matchSendSelf(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("send_self"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("sendSelf"), null, null, null));
            return true;
        }
        return false;
    }

    private boolean matchModtagMessage(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("send_receive"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            String amountMatch = matcher.group("amount") != null ? matcher.group("amount") : "ukendt";
            String itemMatch = matcher.group("item") != null ? matcher.group("item") : "ukendt";
            String playerMatch = matcher.group("player") != null ? matcher.group("player") : "ukendt";

            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByName(itemMatch);
            item.setAmountInTransporter(item.getAmountInTransporter()+Integer.parseInt(amountMatch));
            PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("modtagSuccess"), item.getDisplayName().toLowerCase(), amountMatch, String.valueOf(item.getAmountInTransporter()), playerMatch));
            return true;
        }
        return false;
    }
}
