package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.modules.MessagesModule;
import ml.volder.unikapi.api.player.PlayerAPI;
import net.labymod.api.Laby;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterFailedHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterFailedHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("failed_too_fast"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES){
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                Laby.references().chatExecutor().displayClientMessage(module.getMessage(module.getRawMessage("commandDelay"), null, null, null));
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                Laby.references().chatExecutor().displayActionBar(module.getMessage(module.getRawMessage("commandDelay"), null, null, null));
                return true;
            }
        }
        return false;
    }


}
