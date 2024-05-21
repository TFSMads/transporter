package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.AutoTransporter;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.MessagesModule.LatestTitle;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.utils.Parser;
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
        boolean result = matchPutManglerMessage(clean);
        boolean result2 = matchPutSuccessMessage(clean);
        boolean result3 = matchPutMineTitle(clean);
        boolean result4 = matchPutMineMessage(clean);

        return result || result2 || result3 || result4;
    }

    private boolean matchPutManglerMessage(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("put_missing"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
          String itemMatch = Parser.parseFormattedItemName(matcher.group("item") != null ? matcher.group("item") : "ukendt");

          Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByName(itemMatch);
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
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("put_success"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
          String itemMatch = Parser.parseFormattedItemName(matcher.group("item") != null ? matcher.group("item") : "ukendt");
          String amountMatch = matcher.group("amount") != null ? matcher.group("amount") : "ukendt";

            Item item = TransporterAddon.getInstance().getTransporterItemManager().getItemByName(itemMatch);
            item.setAmountInTransporter(item.getAmountInTransporter()+ Parser.parseInt(amountMatch));
            MessageModes mode = module.getMessageMode();
            if(mode == MessageModes.NO_MESSAGES) {
                return true;
            }else if(mode == MessageModes.ACTIONBAR_MESSAGES){
                PlayerAPI.getAPI().displayActionBarMessage(module.getMessage(module.getRawMessage("putSuccess"), item.getDisplayName().toLowerCase(), amountMatch, String.valueOf(item.getAmountInTransporter())));
                return true;
            }else if(mode == MessageModes.CHAT_MESSAGES){
                PlayerAPI.getAPI().displayChatMessage(module.getMessage(module.getRawMessage("putSuccess"), item.getDisplayName().toLowerCase(), amountMatch, String.valueOf(item.getAmountInTransporter())));
                return true;
            }
        }
        return false;
    }

    private boolean matchPutMineMessage(String clean){
        //TODO lav besked i actionbar
        if(ModuleManager.getInstance().getModule(MessagesModule.class).LAST_TITLE != LatestTitle.TRANSPORTER_PUT_MINE)
            return false;
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("put_mine_entry"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
                if(Parser.parseFormattedItemName(matcher.group("item")).equals(item.getName())){
                    item.setAmountInTransporter((item.getAmountInTransporter() + Parser.parseInt(matcher.group("amount"))));
                    return ModuleManager.getInstance().getModule(AutoTransporter.class).isEnabled();
                }
            }
        }
        return false;
    }

    private boolean matchPutMineTitle(String clean){
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("put_mine_title"));
        final Matcher matcher = pattern.matcher(clean);
        boolean result = matcher.find();
        if(result)
          ModuleManager.getInstance().getModule(MessagesModule.class).LAST_TITLE = LatestTitle.TRANSPORTER_PUT_MINE;
        return result && ModuleManager.getInstance().getModule(AutoTransporter.class).isEnabled();
    }
}
