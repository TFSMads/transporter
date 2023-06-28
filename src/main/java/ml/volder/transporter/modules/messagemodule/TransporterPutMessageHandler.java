package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.unikapi.api.player.PlayerAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterPutMessageHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterPutMessageHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        return matchPutManglerMessage(clean) || matchPutSuccessMessage(clean);
    }

    private boolean matchPutManglerMessage(String clean){
        final Pattern pattern = Pattern.compile("Du har ikke noget (" + module.getItemRegex() + ") at putte i\\.");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByChatName(matcher.group(1));
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES) {
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("putFailed"), item.getDisplayName().toLowerCase(), null, null));
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("putFailed"), item.getDisplayName().toLowerCase(), null, null));
                return true;
            }
        }
        return false;
    }

    private boolean matchPutSuccessMessage(String clean){
        final Pattern pattern = Pattern.compile("Du har puttet nu puttet ([0-9]+) (" + module.getItemRegex() + ") i din transporter");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByChatName(matcher.group(2));
            item.setAmountInTransporter(item.getAmountInTransporter()+Integer.parseInt(matcher.group(1)));
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES) {
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("putSuccess"), item.getDisplayName().toLowerCase(), matcher.group(1), String.valueOf(item.getAmountInTransporter())));
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("putSuccess"), item.getDisplayName().toLowerCase(), matcher.group(1), String.valueOf(item.getAmountInTransporter())));
                return true;
            }
        }
        return false;
    }
}
