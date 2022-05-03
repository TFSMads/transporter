package dk.transporter.mads_gamer_dk.guis.getItemsGui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.guis.serverSelector.AddServerGui;
import dk.transporter.mads_gamer_dk.guis.serverSelector.RemoveServerGui;
import dk.transporter.mads_gamer_dk.guis.serverSelector.ServerInfoRenderer;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.SettingsCategory;
import net.labymod.settings.elements.*;
import net.labymod.support.DashboardConnector;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetItemsConfigGui extends GuiScreen {
    private Scrollbar scrollbar = new Scrollbar(1);
    private GuiScreen lastScreen;
    private SettingsElement mouseOverElement;
    private List<CategorySettingsElement> buttonCategoryElements = new ArrayList();
    private List<SettingsElement> listedElementsStored = new ArrayList();
    private List<SettingsElement> tempElementsStored = new ArrayList();
    private ArrayList<SettingsElement> path = new ArrayList();
    private GuiButton buttonGoBack;
    private double preScrollPos = 0.0D;
    private boolean closed = false;
    private boolean skipDrawDescription = false;

    private TransporterAddon addon;

    public GetItemsConfigGui(GuiScreen lastScreen, TransporterAddon addon) {
        this.addon = addon;
        this.lastScreen = lastScreen;
    }

    public void initGui() {
        super.initGui();
        this.doQuery((String)null);
        this.buttonList.clear();
        this.scrollbar.setPosition(this.width / 2 + 122, 80, this.width / 2 + 122 + 4, this.height - 35);
        this.scrollbar.setSpeed(20);
        this.scrollbar.update(this.listedElementsStored.size());
        this.scrollbar.init();
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height - 25, LanguageManager.translate("button_save_changes")));
    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    private void doQuery(String query) {
        this.tempElementsStored.clear();
        if (this.path.isEmpty()) {
            Iterator var2 = new SettingElements().getCategories(addon).iterator();

            while(var2.hasNext()) {
                SettingsCategory settingsCategory = (SettingsCategory)var2.next();
                this.queryCategory(settingsCategory, query);
            }
        } else {
            SettingsElement currentOpenElement = (SettingsElement)this.path.get(this.path.size() - 1);
            this.tempElementsStored.addAll(currentOpenElement.getSubSettings().getElements());
        }

        this.listedElementsStored = this.tempElementsStored;
    }

    private void queryCategory(SettingsCategory settingsCategory, String query) {
        List<SettingsElement> elementsToAdd = new ArrayList();
        Iterator var4 = settingsCategory.getSettings().getElements().iterator();

        while(true) {
            SettingsElement element;
            do {
                do {
                    do {
                        do {
                            if (!var4.hasNext()) {
                                if (!elementsToAdd.isEmpty()) {
                                    this.tempElementsStored.add(new HeaderElement(settingsCategory.getTitle()));
                                }

                                var4 = elementsToAdd.iterator();

                                while(var4.hasNext()) {
                                    element = (SettingsElement)var4.next();
                                    this.tempElementsStored.add(element);
                                }

                                var4 = settingsCategory.getSubList().iterator();

                                while(var4.hasNext()) {
                                    SettingsCategory subCategory = (SettingsCategory)var4.next();
                                    this.queryCategory(subCategory, query);
                                }

                                return;
                            }

                            element = (SettingsElement)var4.next();
                            if (query == null || settingsCategory.getTitle().toLowerCase().contains(query) || this.isSettingElement(query, element)) {
                                elementsToAdd.add(element);
                            }
                        } while(query == null);
                    } while(query.isEmpty());
                } while(element.getSubSettings() == null);
            } while(element.getSubSettings().getElements().isEmpty());

            Iterator var6 = element.getSubSettings().getElements().iterator();

            while(var6.hasNext()) {
                SettingsElement subElement = (SettingsElement)var6.next();
                if (this.isSettingElement(query, subElement)) {
                    elementsToAdd.add(subElement);
                }
            }
        }
    }

    private boolean isSettingElement(String query, SettingsElement settingsElement) {
        return settingsElement.getDisplayName().toLowerCase().contains(query) || settingsElement.getDescriptionText() != null && settingsElement.getDescriptionText().toLowerCase().contains(query);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.drawSettingsList(mouseX, mouseY, this.height - 30);
        draw.drawOverlayBackground(0, 75);
        draw.drawGradientShadowTop(75.0D, 0.0D, (double)this.width);
        draw.drawOverlayBackground(this.height - 30, this.height);
        draw.drawGradientShadowBottom((double)(this.height - 30), 0.0D, (double)this.width);
        this.drawHeadButtons(mouseX, mouseY);
        this.scrollbar.draw(mouseX, mouseY);
        if (!this.path.isEmpty()) {
            SettingsElement currentOpenElement = (SettingsElement)this.path.get(this.path.size() - 1);
            draw.drawString(currentOpenElement.getDisplayName(), (double)(this.width / 2 - 95), 55.0D);
            if (currentOpenElement instanceof ControlElement) {
                ControlElement control = (ControlElement)currentOpenElement;
                ControlElement.IconData iconData = control.getIconData();
                if (iconData.hasTextureIcon()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(iconData.getTextureIcon());
                    LabyMod.getInstance().getDrawUtils().drawTexture((double)(this.width / 2 + 100), 50.0D, 256.0D, 256.0D, 16.0D, 16.0D);
                } else if (iconData.hasMaterialIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawItem(iconData.getMaterialIcon().createItemStack(), (double)(this.width / 2 + 100), 50.0D, (String)null);
                }
            }
        }
        if (!this.skipDrawDescription) {
            this.drawDescriptions(mouseX, mouseY, 75, this.height - 30);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }



    private void drawHeadButtons(int mouseX, int mouseY) {
        int midX = this.width / 2;
        int buttonLength = this.width / (this.buttonCategoryElements.size() + 1);
        int buttonHeight = 25;
        int spaceLength = this.width / (this.buttonCategoryElements.size() + 2) / 20;
        if (buttonLength > 123) {
            buttonLength = 123;
        }

        if (buttonLength < 70) {
            buttonLength = 70;
        }

        if (spaceLength > 50) {
            spaceLength = 50;
        }

        int totalLength = (buttonLength + spaceLength) * this.buttonCategoryElements.size() - spaceLength;
        int posX = midX - totalLength / 2;
        int posY = 15;

        for(Iterator var10 = this.buttonCategoryElements.iterator(); var10.hasNext(); posX += buttonLength + spaceLength) {
            CategorySettingsElement element = (CategorySettingsElement)var10.next();
            element.draw(posX, posY, posX + buttonLength, posY + buttonHeight, mouseX, mouseY);
        }

    }

    private void drawSettingsList(int mouseX, int mouseY, int maxY) {
        this.mouseOverElement = null;
        this.skipDrawDescription = false;
        double totalEntryHeight = 0.0D;

        for(int zLevel = 0; zLevel < 2; ++zLevel) {
            double posY = 80.0D + this.scrollbar.getScrollY();
            int midX = this.width / 2;
            int elementLength = 120;
            totalEntryHeight = 0.0D;

            SettingsElement element;
            for(Iterator var11 = this.listedElementsStored.iterator(); var11.hasNext(); totalEntryHeight += (double)(element.getEntryHeight() + 1)) {
                element = (SettingsElement)var11.next();
                if ((!(element instanceof DropDownElement) || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 0 || (element instanceof DropDownElement || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 1) {
                    if (element instanceof DropDownElement) {
                        ((DropDownElement)element).getDropDownMenu().setMaxY(maxY);
                        if (((DropDownElement)element).getDropDownMenu().isOpen()) {
                            this.skipDrawDescription = true;
                        }
                    }

                    element.draw(midX - elementLength, (int)posY, midX + elementLength, (int)posY + element.getEntryHeight(), mouseX, mouseY);
                    if (element.isMouseOver()) {
                        this.mouseOverElement = element;
                    }
                }

                posY += (double)(element.getEntryHeight() + 1);
            }
        }

        this.scrollbar.setEntryHeight(totalEntryHeight / (double)this.listedElementsStored.size());
        this.scrollbar.update(this.listedElementsStored.size());
    }

    private void drawDescriptions(int mouseX, int mouseY, int minY, int maxY) {
        Iterator var5 = this.listedElementsStored.iterator();

        while(var5.hasNext()) {
            SettingsElement element = (SettingsElement)var5.next();
            if (element.isMouseOver() && mouseY > minY && mouseY < maxY) {
                element.drawDescription(mouseX, mouseY, this.width);
            }
        }

    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 5) {
            this.closed = true;
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.closed) {
            Iterator var4 = this.listedElementsStored.iterator();

            SettingsElement element;
            while(var4.hasNext()) {
                element = (SettingsElement)var4.next();
                if (element instanceof DropDownElement && ((DropDownElement)element).onClickDropDown(mouseX, mouseY, mouseButton)) {
                    return;
                }
            }

            var4 = this.buttonCategoryElements.iterator();

            while(var4.hasNext()) {
                CategorySettingsElement element2 = (CategorySettingsElement)var4.next();
                element2.mouseClicked(mouseX, mouseY, mouseButton);
            }

            var4 = this.listedElementsStored.iterator();

            while(var4.hasNext()) {
                element = (SettingsElement)var4.next();
                element.unfocus(mouseX, mouseY, mouseButton);
                if (element.isMouseOver()) {
                    element.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }

            if (this.mouseOverElement != null && this.mouseOverElement instanceof ControlElement) {
                ControlElement element2 = (ControlElement)this.mouseOverElement;
                if (element2.hasSubList() && element2.getButtonAdvanced().isMouseOver() && element2.getButtonAdvanced().enabled) {
                    if(element2.getDisplayName().equals("§7Valgte Item")){
                        Minecraft.getMinecraft().displayGuiScreen(new ItemSelectGui(addon,this));
                    }else{
                        element2.getButtonAdvanced().playPressSound(this.mc.getSoundHandler());
                        this.path.add(element2);
                        this.preScrollPos = this.scrollbar.getScrollY();
                        this.scrollbar.setScrollY(0.0D);
                        this.initGui();
                    }
                }
            }

            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);

        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Iterator var4 = this.listedElementsStored.iterator();

        while(var4.hasNext()) {
            SettingsElement element = (SettingsElement)var4.next();
            element.mouseRelease(mouseX, mouseY, state);
        }

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        Iterator var6 = this.listedElementsStored.iterator();

        while(var6.hasNext()) {
            SettingsElement element = (SettingsElement)var6.next();
            element.mouseClickMove(mouseX, mouseY, clickedMouseButton);
        }

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }


    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }

    public void updateScreen() {
        super.updateScreen();
    }
}
