package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.modules.MessagesModule;
import ml.volder.unikapi.api.player.PlayerAPI;
import net.labymod.api.Laby;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterMiscellaneousMessageHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterMiscellaneousMessageHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        boolean result = autoTransporterOn(clean);
        boolean result2 = autoTransporterOff(clean);

        return result || result2;
    }

    private boolean autoTransporterOn(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("autotransporter_on"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES){
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                Laby.references().chatExecutor().displayClientMessage(module.getMessage(module.getRawMessage("autoTransporterOn"), null, null, null));
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                Laby.references().chatExecutor().displayActionBar(module.getMessage(module.getRawMessage("autoTransporterOn"), null, null, null));
                return true;
            }
        }
        return false;
    }

    private boolean autoTransporterOff(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("autotransporter_off"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES){
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                Laby.references().chatExecutor().displayClientMessage(module.getMessage(module.getRawMessage("autoTransporterOff"), null, null, null));
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                Laby.references().chatExecutor().displayActionBar(module.getMessage(module.getRawMessage("autoTransporterOff"), null, null, null));
                return true;
            }
        }
        return false;
    }



}
