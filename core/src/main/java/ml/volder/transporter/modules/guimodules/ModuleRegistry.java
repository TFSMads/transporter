package ml.volder.transporter.modules.guimodules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.*;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.widgets.ModuleSystem;
import net.labymod.api.util.I18n;

public class ModuleRegistry {

    private static String NO_DATA_TRANSLATION;


    private GuiModulesModule guiModulesModule;

    private Object itemAmountCategory;

    private Object itemValueCategory;
    private Object otherCategory;


    private boolean isActiveOnCurrentServer() {
        if(!guiModulesModule.isViewOnSelected())
            return true;
        if(TransporterAddon.getInstance().getServerList().contains(ModuleManager.getInstance().getModule(ServerModule.class).getCurrentServer()))
            return true;
        return false;
    }

    public ModuleRegistry(GuiModulesModule guiModulesModule) {
        this.guiModulesModule = guiModulesModule;
        NO_DATA_TRANSLATION = I18n.translate("sa-transporter.guiModules.nodata");
        ModuleSystem.shouldRenderPredicate = () -> guiModulesModule.isFeatureActive() && TransporterAddon.isEnabled() && isActiveOnCurrentServer();
    }

    public void registerCategories() {

        itemAmountCategory = ModuleSystem.registerCategory("Item Antal", Material.IRON_INGOT, "Widgets der viser antallet af en item du har i din transporter");
        itemValueCategory = ModuleSystem.registerCategory("Item Værdi", Material.EMERALD, "Widgets der viser værdien af en item du har i din transporter");
        otherCategory = ModuleSystem.registerCategory("Andre", Material.OAK_SIGN, "Widgets til andre ting i Transporter Addon!");
    }

    public void registerModules() {
        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            ModuleSystem.registerModule(
                    item.getModernType() + "-antal",
                    item.getDisplayName(),
                    false,
                    itemAmountCategory,
                    item.getMaterial(),
                    s -> {
                        if(!TransporterAddon.getInstance().getTransporterItemManager().hasTransporterData())
                            return NO_DATA_TRANSLATION;
                        return ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive() ? (item.getAmountInTransporter() == null ? "0" : FormatingUtils.formatNumber(item.getAmountInTransporter())) : "Besked featuren er deaktiveret!";
                    }
            );
        }

        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            ModuleSystem.registerModule(
                    item.getModernType() + "-value",
                    item.getDisplayName(),
                    false,
                    itemValueCategory,
                    item.getMaterial(),
                    s -> {
                        if(!TransporterAddon.getInstance().getTransporterItemManager().hasTransporterData())
                            return NO_DATA_TRANSLATION;
                        return ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive()
                                ?   (item.getAmountInTransporter() == null
                                ? "0 EMs"
                                : FormatingUtils.formatNumber((long)((item.getAmountInTransporter().doubleValue() / 6400) * item.getSellValue().doubleValue())) + " EMs"
                        )
                                : I18n.translate("sa-transporter.guiModules.messageFeatureInactive");
                    }
            );
        }

        ModuleSystem.registerModule(
                "autoTransporterAktiv",
                "Auto Transporter",
                false,
                otherCategory,
                Material.DIODE,
                s -> {
                    if(!TransporterAddon.getInstance().getTransporterItemManager().hasTransporterData())
                        return NO_DATA_TRANSLATION;
                    return ModuleManager.getInstance().getModule(AutoTransporter.class).isFeatureActive() && ModuleManager.getInstance().getModule(AutoTransporter.class).isEnabled() ? ModColor.GREEN + "Til" : ModColor.RED + "Fra";
                }
        );

        ModuleSystem.registerModule(
                "autoGetAktiv",
                "Auto Get",
                false,
                otherCategory,
                Material.REDSTONE_LAMP,
                s -> {
                    if(!TransporterAddon.getInstance().getTransporterItemManager().hasTransporterData())
                        return NO_DATA_TRANSLATION;
                    return ModuleManager.getInstance().getModule(AutoGetModule.class).isFeatureActive() && ModuleManager.getInstance().getModule(AutoGetModule.class).isEnabled() ? ModColor.GREEN + "Til" : ModColor.RED + "Fra";
                }
        );

        ModuleSystem.registerModule(
                "transporterValue",
                "Transporter Værdi",
                false,
                otherCategory,
                Material.EMERALD,
                s -> {
                    if(!ModuleManager.getInstance().getModule(MessagesModule.class).isFeatureActive())
                        return I18n.translate("sa-transporter.guiModules.messageFeatureInactive");
                    if(!TransporterAddon.getInstance().getTransporterItemManager().hasTransporterData())
                        return NO_DATA_TRANSLATION;
                    double value = 0;
                    for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
                        value += (item.getAmountInTransporter() == null
                                ? 0
                                : (item.getAmountInTransporter().doubleValue() / 6400) * item.getSellValue()
                        );
                    }
                    return FormatingUtils.formatNumber((long) value) + " EMs";                }
        );

        ModuleSystem.registerModule(
                "transporter-balance",
                "Balance",
                false,
                otherCategory,
                Material.EMERALD,
                s -> ModuleManager.getInstance().getModule(BalanceModule.class).isFeatureActive()
                            ? FormatingUtils.formatNumber(ModuleManager.getInstance().getModule(BalanceModule.class).getBalance().longValue()) + " EMs"
                            : I18n.translate("sa-transporter.guiModules.balanceFeatureInactive")
        );

        ModuleSystem.registerModule(
                "signtools-toogle",
                "Åben Editor",
                false,
                otherCategory,
                Material.OAK_SIGN,
                s -> ModuleManager.getInstance().getModule(SignToolsModule.class).isFeatureActive()
                        ? ModuleManager.getInstance().getModule(SignToolsModule.class).isOpenSignEditor() ? ModColor.GREEN + "Ja" : ModColor.RED + "Nej"
                        : I18n.translate("sa-transporter.guiModules.signtoolsFeatureInactive")
        );
    }

    public Object getItemAmountCategory() {
        return itemAmountCategory;
    }

    public Object getItemValueCategory() {
        return itemValueCategory;
    }

    public Object getOtherCategory() {
        return otherCategory;
    }

}
