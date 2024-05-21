package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.AutoTransporter;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.MessagesModule.LatestTitle;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.utils.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterInfoHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterInfoHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        matchInfoTitle(clean);
        if(ModuleManager.getInstance().getModule(MessagesModule.class).LAST_TITLE != LatestTitle.TRANSPORTER_INFO)
          return false;
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("info_entry"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
                if(Parser.parseFormattedItemName(matcher.group("item")).equals(item.getName())){
                    item.setAmountInTransporter(Parser.parseInt(matcher.group("amount")));
                    ModuleManager.getInstance().getModule(AutoTransporter.class).transporterInfoSet();
                    return false;
                }
            }
        }
        return false;
    }

  private void matchInfoTitle(String clean){
    final Pattern pattern = Pattern.compile(module.getRegexByMessageId("info_title"));
    final Matcher matcher = pattern.matcher(clean);
    boolean result = matcher.find();
    if(result)
      ModuleManager.getInstance().getModule(MessagesModule.class).LAST_TITLE = LatestTitle.TRANSPORTER_INFO;
  }


}
