package dk.transporter.mads_gamer_dk.guis.TransporterInfoGui;


import dk.transporter.mads_gamer_dk.Items.Item;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import scala.Char;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TransporterInfoGui extends GuiScreen {
    private Scrollbar scrollbar;
    private TransporterAddon addon;
    private String hoverServer;
    private List<GuiButton> getButtons = new ArrayList<>();
    private List<GuiButton> putButtons = new ArrayList<>();

    public TransporterInfoGui(TransporterAddon addon) {
        this.addon = addon;
    }

    public void updateScreen() {
    }

    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar = new Scrollbar(37);
        this.scrollbar.setPosition(this.width / 2 + 150 + 4, 41, this.width / 2 + 150 + 4 + 6, this.height - 40);
        this.scrollbar.setSpeed(20);
        this.scrollbar.setSpaceBelow(5);

        for(Item item : addon.getItems().getAllItems()){
            GuiButton buttonGet = new GuiButton(Integer.parseInt("200000" + addon.getItems().getId(item.getItem()) + "1"), 0, 0, 0, 20, "Get");
            this.buttonList.add(buttonGet);
            getButtons.add(buttonGet);
            GuiButton buttonPut = new GuiButton(Integer.parseInt("200000" + addon.getItems().getId(item.getItem()) + "2"), 0, 0, 0, 20, "Put");
            this.buttonList.add(buttonPut);
            putButtons.add(buttonPut);
        }


    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        char[] chars = String.valueOf(button.id).toCharArray();
        System.out.println(button.id);
        System.out.println(chars.length);
        if(chars.length == 8){
            if(chars[7] == '1'){
                int id = Integer.parseInt(String.valueOf(new char[]{chars[5], chars[6]}));
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter get " + addon.getItems().getItemByID(id).getSaName());
                Minecraft.getMinecraft().thePlayer.playSound("note.pling", 100, 1);
            }else if(chars[7] == '2'){
                int id = Integer.parseInt(String.valueOf(new char[]{chars[5], chars[6]}));
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put " + addon.getItems().getItemByID(id).getSaName());
                Minecraft.getMinecraft().thePlayer.playSound("note.bass", 100, 1);
            }
        }
        Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();

        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());

        int midX = this.width / 2;
        int entryWidth = 300;
        int entryHeight = 37;
        double posY = 45.0 + this.scrollbar.getScrollY();

        this.scrollbar.update(addon.getItems().getAllItems().length);

        Iterator<GuiButton> buttonIterator = getButtons.iterator();
        Iterator<GuiButton> buttonIterator2 = putButtons.iterator();

        for(Item item : addon.getItems().getAllItems()){
            draw.drawRect(midX - entryWidth / 2 - 2, posY - 2.0, midX + entryWidth / 2 + 2, posY -1, ModColor.toRGB(128,128, 128, 255));
            draw.drawRect(midX - entryWidth / 2 - 2, posY + (double)entryHeight - 4.0, midX + entryWidth / 2 + 2, posY + (double)entryHeight - 3.0, ModColor.toRGB(128,128, 128, 255));

            draw.drawRect(midX - entryWidth / 2 - 2, posY - 2.0, midX - entryWidth / 2 - 1, posY + (double)entryHeight - 3.0, ModColor.toRGB(128,128, 128, 255));
            draw.drawRect(midX + entryWidth / 2 + 1, posY - 2.0, midX + entryWidth / 2 + 2, posY + (double)entryHeight - 3.0, ModColor.toRGB(128,128, 128, 255));

            GuiButton button = buttonIterator2.next();

            button.enabled = false;
            LabyModCore.getMinecraft().setButtonXPosition(button, midX + entryWidth / 2 - 55);
            LabyModCore.getMinecraft().setButtonYPosition(button, (int) (posY + 5));
            button.setWidth(50);
            LabyModCore.getMinecraft().drawButton(button, mouseX, mouseY);
            if(!(posY > 35 && posY < 445)){
                LabyModCore.getMinecraft().setButtonXPosition(button, 9999);
            }
            button.enabled = true;

            GuiButton button2 = buttonIterator.next();

            button2.enabled = false;
            LabyModCore.getMinecraft().setButtonXPosition(button2, midX + entryWidth / 2 - 55 - 51);
            LabyModCore.getMinecraft().setButtonYPosition(button2, (int) (posY + 5));
            button2.setWidth(50);
            LabyModCore.getMinecraft().drawButton(button2, mouseX, mouseY);
            if(!(posY > 35 && posY < 445)){
                LabyModCore.getMinecraft().setButtonXPosition(button2, 9999);
            }
            button2.enabled = true;


            ItemStack itemStack = item.getMaterial().createItemStack();
            itemStack.setItemDamage(item.getItemDamage());

            ItemInfoRenderer itemInfoRenderer2 = new ItemInfoRenderer(item.getName(), itemStack, item.getAmount(), this);
            itemInfoRenderer2.drawEntry(midX - entryWidth / 2, (int)posY, entryWidth + 5, mouseX, mouseY);

            posY += (double)entryHeight;
        }





        draw.drawOverlayBackground(0, 41);
        draw.drawGradientShadowTop(41.0, 0.0, this.width);
        draw.drawOverlayBackground(this.height - 40, this.height);
        draw.drawGradientShadowBottom(this.height - 40, 0.0, this.width);

        LabyMod.getInstance().getDrawUtils().drawCenteredString(ModColor.cl("8")+ModColor.cl("l")+"["+ModColor.cl("a")+ModColor.cl("l")+" Transporter Menu "+ModColor.cl("8")+ModColor.cl("l")+"]", (double)(this.width / 2), 20.0D, 2.0D);
        this.scrollbar.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }

    public void drawItem(ItemStack item, double xPosition, double yPosition, String value) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableCull();
        if (item.hasEffect()) {
            GlStateManager.enableDepth();
            this.renderItemIntoGUI(item, xPosition, yPosition);
            GlStateManager.disableDepth();
        } else {
            this.renderItemIntoGUI(item, xPosition, yPosition);
        }

        this.renderItemOverlayIntoGUI(item, xPosition, yPosition, value);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
    }

    private void renderItemOverlayIntoGUI(ItemStack stack, double xPosition, double yPosition, String text) {
        LabyModCore.getRenderImplementation().renderItemOverlayIntoGUI(stack, xPosition, yPosition, text);
    }

    public void renderItemIntoGUI(ItemStack stack, double x, double y) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.setupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ibakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    private void setupGuiTransform(double xPosition, double yPosition, boolean isGui3d) {
        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0f + this.zLevel);
        GlStateManager.translate(8.0f, 8.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, -1.0f);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        if (isGui3d) {
            GlStateManager.scale(70.0f, 70.0f, 70.0f);
            GlStateManager.rotate(210.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.enableLighting();
        } else {
            GlStateManager.scale(112.0f, 112.0f, 112.0f);
            GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.disableLighting();
        }
    }
}
