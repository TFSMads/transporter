package ml.volder.transporter.modules.messagemodule;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.MessagesModule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransporterInfoHandler implements IMessageHandler {

    MessagesModule module;

    public TransporterInfoHandler(MessagesModule module) {
        this.module = module;
    }

    @Override
    public boolean messageReceived(String msg, String clean) {
        final Pattern pattern = Pattern.compile(" - ([A-Za-z0-9:_]+) ([0-9]+)");
        final Matcher matcher = pattern.matcher(clean);
        if (matcher.find()) {
            for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()){
                if(matcher.group(1).equals(item.getTransporterInfoName())){
                    item.setAmountInTransporter(Integer.parseInt(matcher.group(2)));
                    return false;
                }
            }
        }
        return false;
    }


}
