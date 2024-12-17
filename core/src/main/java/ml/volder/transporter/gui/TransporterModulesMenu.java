package ml.volder.transporter.gui;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.*;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@AutoActivity
public class TransporterModulesMenu extends TransporterActivity {

    private static SettingsCategory settingsCategory = new SettingsCategory("Transporter Addon - Indstillinger");

    public static void addSetting(SettingsElement element) {
        settingsCategory.addSetting(element);
    }

    private Scrollbar scrollbar = new Scrollbar(1);
    private ScreenInstance lastScreen;
    private WrappedGuiButton buttonBack;
    private SettingsElement mouseOverElement;
    private List<CategorySettingsElement> buttonCategoryElements = new ArrayList();
    private List<SettingsElement> listedElementsStored = new ArrayList();
    private List<SettingsElement> tempElementsStored = new ArrayList();
    private ArrayList<SettingsElement> path = new ArrayList();
    private boolean closed = false;
    private boolean skipDrawDescription = false;

    private final double startY = 50D;

    private WrappedGuiButton buttonAddonInfo;

    private void createButton() {
        this.buttonAddonInfo = new WrappedGuiButton(11, 0, 0, 23, 20, "");
    }

    private void renderAddonInfoButton(int mouseX, int mouseY) {
        if (this.path.size() < 1 && this.buttonAddonInfo != null) {
            this.buttonAddonInfo.setX(this.getWidth() - (23 + 5));
            this.buttonAddonInfo.setY(this.getHeight() - 25);
            this.buttonAddonInfo.setEnabled(true);
            this.buttonAddonInfo.drawButton(mouseX, mouseY);
            DrawAPI drawAPI = DrawAPI.getAPI();
            drawAPI.bindTexture(ModTextures.BUTTON_ADVANCED);
            drawAPI.drawTexture((double)(this.buttonAddonInfo.getX() + 4), (double)(this.buttonAddonInfo.getY() + 3), 0.0, 0.0, 256.0, 256.0, 14.0, 14.0, 2.0F);
        }
    }

    public TransporterModulesMenu(ScreenInstance lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public void initGui() {
        this.doQuery(null);
        this.clearButtonList();
        this.scrollbar.setPosition(this.getWidth() / 2 + 122, startY, this.getWidth() / 2 + 122 + 4, this.getHeight() - 35);
        this.scrollbar.setSpeed(20);
        this.scrollbar.update(this.listedElementsStored.size());
        this.scrollbar.init();
        this.buttonBack = new WrappedGuiButton(1, this.getWidth() / 2 - 100, 20, 22, 20, "<");
        this.addButton(buttonBack);
        createButton();
    }

    private void doQuery(String query) {
        this.tempElementsStored.clear();
        if (this.path.isEmpty()) {
            this.queryCategory(settingsCategory, query);
        } else {
            SettingsElement currentOpenElement = this.path.get(this.path.size() - 1);
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
                                /*if (!elementsToAdd.isEmpty()) {
                                    this.tempElementsStored.add(new HeaderElement(settingsCategory.getTitle()));
                                }*/

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

    private void drawHeadButtons(int mouseX, int mouseY) {
        int midX = this.getWidth() / 2;
        int buttonLength = this.getWidth() / (this.buttonCategoryElements.size() + 1);
        int buttonHeight = 25;
        int spaceLength = this.getWidth() / (this.buttonCategoryElements.size() + 2) / 20;
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

    private boolean isSettingElement(String query, SettingsElement settingsElement) {
        return settingsElement.getDisplayName().toLowerCase().contains(query) || settingsElement.getDescriptionText() != null && settingsElement.getDescriptionText().toLowerCase().contains(query);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.drawSettingsList(mouseX, mouseY, this.getHeight() - 30);
        drawAPI.drawOverlayBackground(0, 45);
        drawAPI.drawGradientShadowTop(45.0D, 0.0D, (double)this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 30, this.getHeight());
        drawAPI.drawGradientShadowBottom((double)(this.getHeight() - 30), 0.0D, (double)this.getWidth());
        this.drawHeadButtons(mouseX, mouseY);
        this.scrollbar.draw(mouseX, mouseY);
        if (!this.path.isEmpty()) {
            SettingsElement currentOpenElement = (SettingsElement)this.path.get(this.path.size() - 1);
            drawAPI.drawString(currentOpenElement.getDisplayName(), (double)(this.getWidth() / 2 - 100 + 30), 25.0D);
            if (currentOpenElement instanceof ControlElement) {
                ControlElement control = (ControlElement)currentOpenElement;
                ControlElement.IconData iconData = control.getIconData();
                if (iconData.hasTextureIcon()) {
                    drawAPI.bindTexture(iconData.getTextureIcon());
                    drawAPI.drawTexture((double)(this.getWidth() / 2 + 100), 50.0D, 256.0D, 256.0D, 16.0D, 16.0D);
                } /*else if (iconData.hasMaterialIcon()) {
                    drawAPI.drawItem(iconData.getMaterialIcon(), (double)(this.getWidth() / 2 + 100), 50.0D, (String)null);
                }*/
            }
        }else {
            drawAPI.drawString("Transporter Addon - Indstilinger", (double)(this.getWidth() / 2 - 100 + 30), 25.0D);
        }
        if (!this.skipDrawDescription) {
            this.drawDescriptions(mouseX, mouseY, 75, this.getHeight() - 30);
        }

        renderAddonInfoButton(mouseX, mouseY);
    }

    private void drawDescriptions(int mouseX, int mouseY, int minY, int maxY) {
        Iterator var5 = this.listedElementsStored.iterator();

        while(var5.hasNext()) {
            SettingsElement element = (SettingsElement)var5.next();
            if (element.isMouseOver() && mouseY > minY && mouseY < maxY) {
                element.drawDescription(mouseX, mouseY, this.getWidth());
            }
        }

    }

    private void drawSettingsList(int mouseX, int mouseY, int maxY) {
        this.mouseOverElement = null;
        this.skipDrawDescription = false;
        double totalEntryHeight = 0.0D;

        for(int zLevel = 0; zLevel < 2; ++zLevel) {
            double posY = startY + this.scrollbar.getScrollY();
            int midX = this.getWidth() / 2;
            int elementLength = 120;
            totalEntryHeight = 0.0D;

            SettingsElement element;
            for(Iterator var11 = this.listedElementsStored.iterator(); var11.hasNext(); totalEntryHeight += (double)(element.getEntryHeight() + 1)) {
                element = (SettingsElement)var11.next();
                if (!(element instanceof DropDownElement) && zLevel == 0 || (element instanceof DropDownElement) && zLevel == 1) {
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


    @Override
    public void updateScreen() {

    }



    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 1) {
            if(!path.isEmpty()){
                path.remove(path.size()-1);
                this.initGui();
                this.scrollbar.setScrollY(0);
            }else {
                if(lastScreen != null){
                    Laby.labyAPI().minecraft().minecraftWindow().displayScreen(lastScreen);
                }else{
                    Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if (!this.closed) {
            Iterator iterator = this.listedElementsStored.iterator();

            while(iterator.hasNext()) {
                SettingsElement element = (SettingsElement)iterator.next();
                if (element instanceof DropDownElement && ((DropDownElement)element).onClickDropDown(mouseX, mouseY, mouseButton)) {
                    return;
                }
            }


            iterator = this.buttonCategoryElements.iterator();

            while (iterator.hasNext()) {
                CategorySettingsElement element = (CategorySettingsElement) iterator.next();
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }

            iterator = this.listedElementsStored.iterator();

            while (iterator.hasNext()) {
                SettingsElement element = (SettingsElement) iterator.next();
                element.unfocus(mouseX, mouseY, mouseButton);
                if (element.isMouseOver()) {
                    element.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }

            if (this.mouseOverElement != null && (this.mouseOverElement instanceof ControlElement || this.mouseOverElement instanceof ModuleElement)) {
                SettingsElement element = this.mouseOverElement;
                if (element.shouldOpenSubSettings()) {
                    //element.getButtonAdvanced().playPressSound(this.mc.getSoundHandler()); TODO add sound
                    if(element.hasSubSettings()){
                        this.path.add(element);
                        this.scrollbar.setScrollY(0.0D);
                        this.initGui();
                    }
                    if(element instanceof ListContainerElement && ((ListContainerElement) element).getAdvancedButtonCallback() != null) {
                        ((ListContainerElement) element).getAdvancedButtonCallback().accept(true);
                    }
                }
            }
        }

        if(buttonAddonInfo.isEnabled() && buttonAddonInfo.isMouseOver()) {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new AddonInfoScreen(this));
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        Iterator<SettingsElement> iterator = this.listedElementsStored.iterator();

        while(iterator.hasNext()) {
            SettingsElement element = iterator.next();
            element.mouseClickMove(mouseX, mouseY, clickedMouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);

        Iterator<SettingsElement> iterator = this.listedElementsStored.iterator();

        while(iterator.hasNext()) {
            SettingsElement element = iterator.next();
            element.mouseRelease(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void handleMouseInput() {
        this.scrollbar.mouseInput();
    }

    @Override
    public void keyTyped(char typedChar, Key key) {
        Iterator<SettingsElement> iterator = this.listedElementsStored.iterator();

        while(iterator.hasNext()) {
            SettingsElement element = iterator.next();
            element.keyTyped(typedChar, key);
        }
    }

    @Override
    public void onGuiClosed() {

    }
}
