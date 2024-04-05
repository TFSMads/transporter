package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.AutoTransporter;
import ml.volder.transporter.modules.MessagesModule;
import ml.volder.transporter.modules.ModuleManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterInfoHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterInfoHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        final Pattern pattern = Pattern.compile(module.getRegexByMessageId("info_entry"));
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
                if(matcher.group("item").equals(item.getName())){
                    item.setAmountInTransporter(Integer.parseInt(matcher.group("amount")));
                    ModuleManager.getInstance().getModule(AutoTransporter.class).transporterInfoSet();
                    return false;
                }
            }
        }
        return false;
    }


}
