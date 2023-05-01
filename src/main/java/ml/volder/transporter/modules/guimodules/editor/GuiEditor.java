package ml.volder.transporter.modules.guimodules.editor;

import ml.volder.transporter.gui.ModTextures;
import ml.volder.transporter.gui.elements.BooleanElement;
import ml.volder.transporter.gui.elements.ControlElement;
import ml.volder.transporter.gui.elements.Scrollbar;
import ml.volder.transporter.gui.elements.SettingsElement;
import ml.volder.transporter.modules.GuiModulesModule;
import ml.volder.transporter.modules.guimodules.GuiModule;
import ml.volder.transporter.modules.guimodules.elements.ModuleCategoryElement;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.ArrayList;
import java.util.List;

public class GuiEditor extends WrappedGuiScreen {

    private ArrayList<SettingsElement> path = new ArrayList();

    private List<ModuleCategoryElement> moduleCategoryElementList = new ArrayList<>();
    private GuiModulesModule guiModulesModule;
    private GuiModule hoveredGuiModule;
    private GuiModule draggedGuiModule;
    private double dragOffsetX;
    private double dragOffsetY;
    private WrappedGuiButton buttonBack;
    private WrappedGuiScreen lastScreen;
    private Scrollbar scrollbar;

    public GuiEditor(GuiModulesModule guiModulesModule, WrappedGuiScreen lastScreen) {
        this.guiModulesModule = guiModulesModule;
        this.lastScreen = lastScreen;
        this.scrollbar = new Scrollbar(22);
        for(ModuleCategoryElement category : guiModulesModule.getGuiModuleCategories()) {
            category.getSubSettings().getElements().clear();
            moduleCategoryElementList.add(category);
        }

        for (GuiModule guiModule : guiModulesModule.getGuiModuleList()) {

            String key = "modules." + guiModule.getKey() + ".isEnabled";

            BooleanElement booleanElement = new BooleanElement(guiModule.getPrefix(), guiModulesModule.getDataManager(),key, guiModule.getIconData(), (guiModulesModule.getDataManager().getSettings().getData().has(key) && guiModulesModule.getDataManager().getSettings().getData().get(key).getAsBoolean()));
            booleanElement.addCallback(guiModule::setEnabled);
            for (SettingsElement elements : guiModule.getSubSettings(guiModulesModule.getDataManager())) {
                booleanElement.getSubSettings().add(elements);
            }

            booleanElement.setAdvancedButtonCallback(aBoolean -> {
                this.path.add(booleanElement);
            });

            guiModule.getCategory().getSubSettings().add(booleanElement);
        }
        this.buttonBack = new WrappedGuiButton(1, 5, (getHeight() / 24) + 10, 22, 20, "<");
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        double editorWidth = getWidth() / 6;
        double editorHeight = getHeight() / 6 * 5;
        double editorStart = getHeight() / 6 * 0.5;
        double editorEnd = editorStart + editorHeight;
        this.scrollbar.setPosition(editorWidth - 3, editorStart + 2, editorWidth - 1, editorEnd - 2);
        this.scrollbar.setSpeed(22);
        this.scrollbar.update(this.moduleCategoryElementList.size());
        this.scrollbar.init();
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 1) {
            if(!path.isEmpty()){
                path.remove(path.size()-1);
                this.scrollbar.setScrollY(0);
            }else {
                if(lastScreen != null){
                    PlayerAPI.getAPI().openGuiScreen(lastScreen);
                }else{
                    PlayerAPI.getAPI().openGuiScreen(null);
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //Draw Background
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawOverlayBackground(0, this.getHeight());
        drawAPI.drawDimmedBackground(0);
        drawMinecraftBackground();
        //Draw Modules
        drawModules(mouseX, mouseY);
        //Draw Editor
        this.scrollbar.draw(mouseX, mouseY);

        double editorWidth = getWidth() / 6;
        double startY = ((getHeight() / 6) * 0.5) + 2 + this.scrollbar.getScrollY();
        double elementX = editorWidth * 0.02;
        double elementWidth = editorWidth - elementX * 2;
        double totalEntryHeight = 0.0D;
        if(path.isEmpty()){
            for (ModuleCategoryElement moduleCategoryElement : moduleCategoryElementList) {
                moduleCategoryElement.draw((int)elementX, (int)startY, (int)(elementX + elementWidth), (int)startY + moduleCategoryElement.getEntryHeight(), mouseX, mouseY);
                startY += moduleCategoryElement.getEntryHeight() + 2;
                totalEntryHeight += moduleCategoryElement.getEntryHeight() + 2;
            }
            this.scrollbar.setEntryHeight(totalEntryHeight / (double)this.moduleCategoryElementList.size());
            this.scrollbar.update(this.moduleCategoryElementList.size());
        } else {
            if(path.get(path.size()-1).hasSubSettings()) {
                for (SettingsElement settingsElement : path.get(path.size()-1).getSubSettings().getElements()) {
                    settingsElement.draw((int)elementX, (int)startY, (int)(elementX + elementWidth), (int)startY + settingsElement.getEntryHeight(), mouseX, mouseY);
                    startY += settingsElement.getEntryHeight() + 2;
                    totalEntryHeight += settingsElement.getEntryHeight() + 2;
                }
            }
            this.scrollbar.setEntryHeight(totalEntryHeight / (double)this.path.get(path.size()-1).getSubSettings().getElements().size());
            this.scrollbar.update(this.path.get(path.size()-1).getSubSettings().getElements().size());
        }

        drawAPI.drawOverlayBackground(0, (int) ((getHeight() / 6) * 0.5));
        drawAPI.drawOverlayBackground(getHeight() - (int) ((getHeight() / 6) * 0.5) - 5, getHeight());
        drawAPI.drawGradientShadowBottom(getHeight()-((getHeight() / 6) * 0.5) - 5, 0, getWidth());
        drawAPI.drawGradientShadowTop(((getHeight() / 6) * 0.5), 0, getWidth());

        this.buttonBack.drawButton(mouseX, mouseY);
    }

    private void drawMinecraftBackground() {
        DrawAPI drawAPI = DrawAPI.getAPI();
        double backgroundWidth = (getWidth() / 6) * 5;
        double backgroundHeight = (getHeight() / 6) * 5;
        drawAPI.bindTexture(ModTextures.MINECRAFT_BACKGROUND);
        drawAPI.drawTexture((getWidth() / 6),(getHeight() / 6) * 0.5,256,256,backgroundWidth,backgroundHeight);
    }

    private void drawModules(int mouseX, int mouseY) {
        hoveredGuiModule = null;
        DrawAPI drawAPI = DrawAPI.getAPI();

        if(draggedGuiModule != null) {
            double x = draggedGuiModule.getDrawX(getWidth() / 6 * 5) + getWidth() / 6;
            double y = draggedGuiModule.getDrawY(getHeight() / 6 * 5) + getHeight() / 6 * 0.5;
            double width = draggedGuiModule.getWidth();
            double height = drawAPI.getFontHeight();
            drawAPI.drawRect(x,y,x+width,y+height, ModColor.toRGB(128,128, 128, 150));
        }

        guiModulesModule.getGuiModuleList().forEach(guiModule -> {
            if(guiModule.isEnabled()) {
                if(isMouseOverModule(mouseX,mouseY,guiModule)) {
                    hoveredGuiModule = guiModule;
                }
            }
        });

        if(hoveredGuiModule != null && draggedGuiModule == null) {
            double x = hoveredGuiModule.getDrawX(getWidth() / 6 * 5) + getWidth() / 6;
            double y = hoveredGuiModule.getDrawY(getHeight() / 6 * 5) + getHeight() / 6 * 0.5;
            double width = hoveredGuiModule.getWidth();
            double height = drawAPI.getFontHeight();
            drawAPI.drawRect(x,y,x+width,y+height, ModColor.toRGB(128,128, 128, 100));
        }

        guiModulesModule.getGuiModuleList().forEach(guiModule -> {
            guiModule.draw(guiModule.getDrawX(getWidth() / 6 * 5) + getWidth() / 6, guiModule.getDrawY(getHeight() / 6 * 5) + getHeight() / 6 * 0.5);
        });
    }

    private boolean isMouseOverModule(int mouseX, int mouseY, GuiModule guiModule) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        double x = guiModule.getDrawX(getWidth() / 6 * 5) + getWidth() / 6;
        double y = guiModule.getDrawY(getHeight() / 6 * 5) + getHeight() / 6 * 0.5;
        double width = guiModule.getWidth();
        double height = drawAPI.getFontHeight();
        if((mouseX > x && mouseX < x + width) && (mouseY > y && mouseY < y + height)) {
            return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if(mouseY >= getHeight() / 12 && mouseY <= getHeight() / 6 * 5.5){
            if(this.path.isEmpty()) {
                for (ModuleCategoryElement moduleCategoryElement : moduleCategoryElementList) {
                    if (moduleCategoryElement.isMouseOver()) {
                        this.path.add(moduleCategoryElement);
                    }
                }
            }
            if(!this.path.isEmpty()) {
                if(path.get(path.size()-1).hasSubSettings()) {
                    for (SettingsElement settingsElement : path.get(path.size()-1).getSubSettings().getElements()) {
                        if(settingsElement instanceof ControlElement && ((ControlElement)settingsElement).getButtonAdvanced().isMouseOver() && ((ControlElement)settingsElement).getButtonAdvanced().isEnabled()) {
                            ((ControlElement) settingsElement).getAdvancedButtonCallback().accept(true);
                        }
                        settingsElement.unfocus(mouseX, mouseY, mouseButton);
                        settingsElement.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }


        if(this.buttonBack.isMouseOver()) {
            actionPerformed(buttonBack);
        }

        draggedGuiModule = hoveredGuiModule;
        if(draggedGuiModule == null)
            return;
        dragOffsetX = (mouseX - getWidth() / 6) - draggedGuiModule.getDrawX(getWidth() / 6 * 5);
        dragOffsetY = (mouseY - getHeight() / 6 * 0.5) - draggedGuiModule.getDrawY(getHeight() / 6 * 5);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        if(!this.path.isEmpty()) {
            if(path.get(path.size()-1).hasSubSettings()) {
                for (SettingsElement settingsElement : path.get(path.size()-1).getSubSettings().getElements()) {
                    settingsElement.mouseClickMove(mouseX, mouseY, clickedMouseButton);
                }
            }
        }
        if(draggedGuiModule == null || !draggedGuiModule.isEnabled())
            return;
        if(mouseX > getWidth() / 6 && (mouseY > getHeight() / 6 * 0.5 && mouseY < getHeight() - getHeight() / 6 * 0.5)){
            double width = getWidth() / 6 * 5;
            double height = getHeight() / 6 * 5;
            double xPos = mouseX - getWidth() / 6 - dragOffsetX;
            double yPos = mouseY - getHeight() / 6 * 0.5 - dragOffsetY;
            if(((xPos + draggedGuiModule.getWidth()) * 1000/width >= 1000)) {
                xPos = getWidth() / 6 * 5 - draggedGuiModule.getWidth();
            }
            if(((yPos + DrawAPI.getAPI().getFontHeight()) + 5) * 1000/height >= 1000) {
                yPos = getHeight() / 6 * 5 - DrawAPI.getAPI().getFontHeight();
            }
            draggedGuiModule.setX((1000/width * xPos) < 0 ? 0 : (int) (1000 / width * xPos));
            draggedGuiModule.setY((1000/height * yPos) < 0 ? 0 : (int) (1000 / height * yPos));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        if(!this.path.isEmpty()) {
            if(path.get(path.size()-1).hasSubSettings()) {
                for (SettingsElement settingsElement : path.get(path.size()-1).getSubSettings().getElements()) {
                    settingsElement.mouseRelease(mouseX, mouseY, mouseButton);
                }
            }
        }
        if(draggedGuiModule != null)
            draggedGuiModule.savePosition(guiModulesModule.getDataManager());
        draggedGuiModule = null;
        dragOffsetX = 0;
        dragOffsetY = 0;
    }

    @Override
    public void handleMouseInput() {
        this.scrollbar.mouseInput();
    }

    @Override
    public void keyTyped(char typedChar, Key key) {
        if(!this.path.isEmpty()) {
            if(path.get(path.size()-1).hasSubSettings()) {
                for (SettingsElement settingsElement : path.get(path.size()-1).getSubSettings().getElements()) {
                    settingsElement.keyTyped(typedChar, key);
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {

    }
}
