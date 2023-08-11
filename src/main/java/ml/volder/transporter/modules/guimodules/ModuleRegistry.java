package ml.volder.transporter.modules.guimodules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.modules.BalanceModule;
import ml.volder.transporter.modules.GuiModulesModule;
import ml.volder.transporter.modules.McmmoModule;
import ml.volder.transporter.modules.ServerModule;
import ml.volder.transporter.utils.FormatingUtils;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.widgets.ModuleSystem;

public class ModuleRegistry {



    private GuiModulesModule guiModulesModule;

    private Object itemAmountCategory;
    private Object itemValueCategory;
    private Object otherCategory;


    public ModuleRegistry(GuiModulesModule guiModulesModule) {
        this.guiModulesModule = guiModulesModule;
        ModuleSystem.shouldRenderPredicate = () -> guiModulesModule.isFeatureActive() && TransporterAddon.isEnabled();
    }

    public void registerCategories() {

        itemAmountCategory = ModuleSystem.registerCategory("Item Antal", Material.IRON_INGOT, "Widgets der viser antallet af en item du har i din transporter");
        itemValueCategory = ModuleSystem.registerCategory("Item Værdi", Material.EMERALD, "Widgets der viser værdien af en item du har i din transporter");
        otherCategory = ModuleSystem.registerCategory("Andre", Material.OAK_SIGN, "Widgets til andre ting i Transporter Addon!");
    }

    public void registerModules() {
        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            ModuleSystem.registerModule(
                    item.getCommandName() + "-antal",
                    item.getDisplayName(),
                    false,
                    itemAmountCategory,
                    item.getMaterial(),
                    s -> {
                        if(!TransporterAddon.getInstance().getAutoTransporter().hasTransporterData())
                            return "Ingen Transporter Data tilgængelig! (skriv /transporter info)";
                        return TransporterAddon.getInstance().getMessagesModule().isFeatureActive() ? (item.getAmountInTransporter() == null ? "0" : FormatingUtils.formatNumber(item.getAmountInTransporter())) : "Besked featuren er deaktiveret!";
                    }
            );
        }

        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            ModuleSystem.registerModule(
                    item.getCommandName() + "-value",
                    item.getDisplayName(),
                    false,
                    itemValueCategory,
                    item.getMaterial(),
                    s -> {
                        if(!TransporterAddon.getInstance().getAutoTransporter().hasTransporterData())
                            return "Ingen Transporter Data tilgængelig! (skriv /transporter info)";
                        return TransporterAddon.getInstance().getMessagesModule().isFeatureActive()
                                ?   (item.getAmountInTransporter() == null
                                ? "0 EMs"
                                : FormatingUtils.formatNumber((long)((item.getAmountInTransporter().doubleValue() / 6400) * item.getSellValue().doubleValue())) + " EMs"
                        )
                                : "Besked featuren er deaktiveret!";
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
                    if(!TransporterAddon.getInstance().getAutoTransporter().hasTransporterData())
                        return "Ingen Transporter Data tilgængelig! (skriv /transporter info)";
                    return TransporterAddon.getInstance().getAutoTransporter().isFeatureActive() && TransporterAddon.getInstance().getAutoTransporter().isEnabled() ? ModColor.GREEN + "Til" : ModColor.RED + "Fra";
                }
        );

        ModuleSystem.registerModule(
                "autoGetAktiv",
                "Auto Get",
                false,
                otherCategory,
                Material.REDSTONE_LAMP,
                s -> {
                    if(!TransporterAddon.getInstance().getAutoTransporter().hasTransporterData())
                        return "Ingen Transporter Data tilgængelig! (skriv /transporter info)";
                    return TransporterAddon.getInstance().getAutoGetModule().isFeatureActive() && TransporterAddon.getInstance().getAutoGetModule().isEnabled() ? ModColor.GREEN + "Til" : ModColor.RED + "Fra";
                }
        );

        ModuleSystem.registerModule(
                "transporterValue",
                "Transporter Værdi",
                false,
                otherCategory,
                Material.EMERALD,
                s -> {
                    if(!TransporterAddon.getInstance().getMessagesModule().isFeatureActive())
                        return "Besked featuren er deaktiveret!";
                    if(!TransporterAddon.getInstance().getAutoTransporter().hasTransporterData())
                        return "Ingen Transporter Data tilgængelig! (skriv /transporter info)";
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
                "balance",
                "Balance",
                false,
                otherCategory,
                Material.EMERALD,
                s -> BalanceModule.isActive()
                            ? FormatingUtils.formatNumber(BalanceModule.getBalance()) + " EMs"
                            : "Balance featuren er ikke aktiv!"
        );

        McmmoModule.registerModules();
        ServerModule.registerModules(otherCategory);
    }

}
