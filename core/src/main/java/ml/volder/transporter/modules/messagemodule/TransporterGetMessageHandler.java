package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.utils.Parser;
import ml.volder.unikapi.api.player.PlayerAPI;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterGetMessageHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterGetMessageHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        return matchGetManglerMessage(clean) || matchGetSuccessMessage(clean);
    }

    private boolean matchGetManglerMessage(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("get_missing"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES) {
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("getFailed"), null, null, null));
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("getFailed"), null, null, null));
                return true;
            }
        }
        return false;
    }

    private boolean matchGetSuccessMessage(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("get_success"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            String itemMatch = Parser.parseFormattedItemName(matcher.group("item") != null ? matcher.group("item") : "ukendt");
            String amountMatch = matcher.group("amount") != null ? matcher.group("amount") : "ukendt";
            Integer total = matcher.group("total") != null ? Parser.tryParseInt(matcher.group("total")) : null;

            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByType(itemMatch);
            item.setAmountInTransporter(Objects.requireNonNullElseGet(total, () -> item.getAmountInTransporter() - Parser.parseInt(amountMatch)));
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES) {
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("getSuccess"), item.getDisplayName().toLowerCase(), amountMatch, String.valueOf(item.getAmountInTransporter())));
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("getSuccess"), item.getDisplayName().toLowerCase(), amountMatch, String.valueOf(item.getAmountInTransporter())));
                return true;
            }
        }
        return false;
    }
}
