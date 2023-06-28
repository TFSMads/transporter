package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.modules.MessagesModule;
import ml.volder.unikapi.api.player.PlayerAPI;

public class TransporterFailedHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterFailedHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        if(clean.equals("Du skriver kommandoer for hurtigt!") || clean.equals("Du gør dette for hurtigt!")) {
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES){
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("cooldown"), null, null, null));
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("cooldown"), null, null, null));
                return true;
            }
        }

        if(clean.equals("Du har ikke nok plads i din inventory")) {
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES){
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("getFull"), null, null, null));
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("getFull"), null, null, null));
                return true;
            }
        }
        return false;
    }


}
