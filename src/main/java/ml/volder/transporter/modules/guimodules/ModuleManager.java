package ml.volder.transporter.modules.guimodules;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.classes.items.Item;
import ml.volder.transporter.gui.elements.ControlElement;
import ml.volder.transporter.modules.GuiModulesModule;
import ml.volder.transporter.modules.guimodules.elements.ModuleCategoryElement;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.utils.FormatingUtils;

public class ModuleManager {

    private GuiModulesModule guiModulesModule;

    private ModuleCategoryElement itemAmountModules;
    private ModuleCategoryElement itemValueModules;
    private ModuleCategoryElement otherModules;


    public ModuleManager(GuiModulesModule guiModulesModule) {
        this.guiModulesModule = guiModulesModule;
    }

    public void registerCategories() {
        itemAmountModules = new ModuleCategoryElement("Item Antal", new ControlElement.IconData(Material.IRON_INGOT));
        guiModulesModule.getGuiModuleCategories().add(itemAmountModules);
        itemValueModules = new ModuleCategoryElement("Item Værdi", new ControlElement.IconData(Material.EMERALD));
        guiModulesModule.getGuiModuleCategories().add(itemValueModules);
        otherModules = new ModuleCategoryElement("Andre", new ControlElement.IconData(Material.OAK_SIGN));
        guiModulesModule.getGuiModuleCategories().add(otherModules);
    }

    public void registerModules() {
        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            GuiModule module = new GuiModule(2,2, item.getCommandName() + "-antal", item.getDisplayName(), false, guiModulesModule.getDataManager(), itemAmountModules) {
                @Override
                public String getDisplayValue() {
                    return TransporterAddon.getInstance().getMessagesModule().isFeatureActive() ? (item.getAmountInTransporter() == null ? "0" : FormatingUtils.formatNumber(item.getAmountInTransporter())) : "Besked featuren er deaktiveret!";
                }
            };
            ControlElement.IconData iconData = new ControlElement.IconData(item.getMaterial());
            iconData.setItemDamage(item.getItemDamage());
            module.setIconData(iconData);
            guiModulesModule.getGuiModuleList().add(module);
        }

        for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
            GuiModule module = new GuiModule(2,2, item.getCommandName() + "-value", item.getDisplayName(), false, guiModulesModule.getDataManager(), itemValueModules) {
                @Override
                public String getDisplayValue() {
                    return TransporterAddon.getInstance().getMessagesModule().isFeatureActive()
                            ?   (item.getAmountInTransporter() == null
                                ? "0 EMs"
                                : FormatingUtils.formatNumber((long) (item.getAmountInTransporter() / 6400) * item.getSellValue()) + " EMs"
                                )
                            : "Besked featuren er deaktiveret!";
                }
            };
            ControlElement.IconData iconData = new ControlElement.IconData(item.getMaterial());
            iconData.setItemDamage(item.getItemDamage());
            module.setIconData(iconData);
            guiModulesModule.getGuiModuleList().add(module);
        }

        GuiModule module = new GuiModule(2,2, "autoTransporterAktiv", "Auto Transporter", false, guiModulesModule.getDataManager(), otherModules){
            @Override
            public String getDisplayValue() {
                return TransporterAddon.getInstance().getAutoTransporter().isFeatureActive() && TransporterAddon.getInstance().getAutoTransporter().isEnabled() ? ModColor.GREEN + "Til" : ModColor.RED + "Fra";
            }
        };
        module.setIconData(new ControlElement.IconData(Material.DIODE));
        guiModulesModule.getGuiModuleList().add(module);

        module = new GuiModule(2,2, "autoGetAktiv", "Auto Get", false, guiModulesModule.getDataManager(), otherModules){
            @Override
            public String getDisplayValue() {
                return TransporterAddon.getInstance().getAutoGetModule().isFeatureActive() && TransporterAddon.getInstance().getAutoGetModule().isEnabled() ? ModColor.GREEN + "Til" : ModColor.RED + "Fra";
            }
        };
        module.setIconData(new ControlElement.IconData(Material.REDSTONE_LAMP));
        guiModulesModule.getGuiModuleList().add(module);

        module = new GuiModule(2, 2, "transporterValue", "Transporter Værdi", false, guiModulesModule.getDataManager(), otherModules) {
            @Override
            public String getDisplayValue() {
                if(!TransporterAddon.getInstance().getMessagesModule().isFeatureActive())
                    return "Besked featuren er deaktiveret!";
                double value = 0;
                for(Item item : TransporterAddon.getInstance().getTransporterItemManager().getItemList()) {
                    value += (item.getAmountInTransporter() == null
                            ? 0
                            : (item.getAmountInTransporter() / 6400) * item.getSellValue()
                    );
                }
                return FormatingUtils.formatNumber((long) value) + " EMs";
            }
        };
        module.setIconData(new ControlElement.IconData(Material.EMERALD));
        guiModulesModule.getGuiModuleList().add(module);

        for (GuiModule m : guiModulesModule.getGuiModuleList()) {
            m.loadConfig(guiModulesModule.getDataManager());
        }
    }
}
