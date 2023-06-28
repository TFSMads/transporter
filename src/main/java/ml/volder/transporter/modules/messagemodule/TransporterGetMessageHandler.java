package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.unikapi.api.player.PlayerAPI;

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
        final Pattern pattern = Pattern.compile("Du har ikke nok items i din transporter!");
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
        final Pattern pattern = Pattern.compile("^Du har taget ([0-9]+) (" + module.getItemRegex() + ") fra din transporter.");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByChatName(matcher.group(2));
            item.setAmountInTransporter(item.getAmountInTransporter()-Integer.parseInt(matcher.group(1)));
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES) {
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("getSuccess"), item.getDisplayName().toLowerCase(), matcher.group(1), String.valueOf(item.getAmountInTransporter())));
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("getSuccess"), item.getDisplayName().toLowerCase(), matcher.group(1), String.valueOf(item.getAmountInTransporter())));
                return true;
            }
        }
        return false;
    }
}
