package dk.transporter.mads_gamer_dk.guis;

import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.Items.Items;
import dk.transporter.mads_gamer_dk.Items.TransporterItems;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class TransporterGui extends GuiScreen {

    private Scrollbar scrollbar = new Scrollbar(18);
    private TransporterAddon addon;
    private Boolean interacted = false;

    public Integer col1;
    public Integer col2;
    public Integer col3;

    public Integer xDistance;
    public Integer yDistance;

    private Items items;

    public TransporterGui(TransporterAddon addon, Items items) {
        this.addon = addon;
        this.items = items;

    }

    public void updateScreen() {

    }
    public void initGui() {
        super.initGui();

        this.scrollbar.init();
        this.scrollbar.setPosition(this.width / 2 + 122, 44, this.width / 2 + 126, this.height - 32 - 3);

        Integer buttonWidth = this.width / 20;

        xDistance = this.width / 30;

        yDistance = this.height / 20;

        Integer third = this.width/3;

        col1 = (third)-(third/2)-(buttonWidth/2);
        col2 = (third*2)-(third/2)-(buttonWidth/2);
        col3 = (third*3)-(third/2)-(buttonWidth/2);


        Integer slot = 0;

        TransporterItems items[] = TransporterItems.values();
        for(TransporterItems item : items) {
            if(slot >= 0 && slot <= 10){
                this.buttonList.add(new GuiButton(10000+slot, col1 - xDistance -(buttonWidth/2), (this.height - (this.height/5))-(slot*yDistance), buttonWidth, 20, "Get"));
                this.buttonList.add(new GuiButton(11000+slot, col1 + xDistance + (buttonWidth/2), (this.height - (this.height/5))-(slot*yDistance), buttonWidth, 20, "Put"));
            }else if(slot >= 11 && slot <= 21){
                this.buttonList.add(new GuiButton(10000+slot, col2 - xDistance -(buttonWidth/2), (this.height - (this.height/5))-((slot-11)*yDistance), buttonWidth, 20, "Get"));
                this.buttonList.add(new GuiButton(11000+slot, col2 + xDistance + (buttonWidth/2), (this.height - (this.height/5))-((slot-11)*yDistance), buttonWidth, 20, "Put"));
            }else if(slot >= 22 && slot <= 32){
                this.buttonList.add(new GuiButton(10000+slot, col3 - xDistance -(buttonWidth/2), (this.height - (this.height/5))-((slot-22)*yDistance), buttonWidth, 20, "Get"));
                this.buttonList.add(new GuiButton(11000+slot, col3 + xDistance + (buttonWidth/2), (this.height - (this.height/5))-((slot-22)*yDistance), buttonWidth, 20, "Put"));
            }
            slot++;
        }


    }


    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("Clicked: Button with id " + button.id);
        super.actionPerformed(button);


        if(button.id >= 10000 && button.id < 11000){
            Integer index = button.id - 10000;
            System.out.println("Index: " + index);
            if(index == 1){
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter get Sand:1");
            }
            System.out.println("Item: " + this.items.getItemByID(index).getItem().toString());
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter get " + this.items.getItemByID(index).getItem().toString());
        }

        else if(button.id >= 11000 && button.id < 12000){
            Integer index = button.id - 11000;
            if(index == 1){
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put Sand:1");
            }
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/transporter put " + this.items.getItemByID(index).getItem().toString());

        }
        interacted = true;
        Minecraft.getMinecraft().thePlayer.closeScreen();

    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        double yPos = 45.0D + this.scrollbar.getScrollY() + 3.0D;


        LabyMod.getInstance().getDrawUtils().drawCenteredString(ModColor.cl("8")+ModColor.cl("l")+"["+ModColor.cl("a")+ModColor.cl("l")+" TRANSPORTER "+ModColor.cl("8")+ModColor.cl("l")+"]", this.width / 2, 20, 2);

        LabyMod.getInstance().getDrawUtils().drawCenteredString(ModColor.cl("f")+"Klik på den item du vil tage ud af din transporter! ", this.width / 2, 50, 1);


        Integer slot = 0;


        TransporterItems items[] = TransporterItems.values();
        for(TransporterItems item : items) {
            if(slot >= 0 && slot <= 10){
                if(false) {
                    String name = this.items.getName(item);
                    LabyMod.getInstance().getDrawUtils().drawString("§f" + name, col1, (this.height - (this.height / 5)) - (slot * yDistance) + 5, 1);
                }else{

                    ItemStack itemStack = new ItemStack(Blocks.sand);
                    if(this.items.getItemByID(slot).isInstanceOfBlock()){
                        Block block = this.items.getItemByID(slot).getBlock();
                        itemStack = new ItemStack(block);
                    }else{
                        Item mItem = this.items.getItemByID(slot).getMItem();
                        itemStack = new ItemStack(mItem);
                    }

                    itemStack.setItemDamage(this.items.getItemByID(slot).getItemDamage());

                    LabyMod.getInstance().getDrawUtils().drawItem(itemStack, col1+16, (this.height - (this.height / 5)) - (slot * yDistance) + 5, "");

                }
            }else if(slot >= 11 && slot <= 21){
                if(false) {
                    String name = this.items.getName(item);
                    LabyMod.getInstance().getDrawUtils().drawString("§f" + name, col2, (this.height - (this.height / 5)) - ((slot-11) * yDistance) + 5, 1);
                }else{

                    ItemStack itemStack = new ItemStack(Blocks.sand);
                    if(this.items.getItemByID(slot).isInstanceOfBlock()){
                        Block block = this.items.getItemByID(slot).getBlock();
                        itemStack = new ItemStack(block);
                    }else{
                        Item mItem = this.items.getItemByID(slot).getMItem();
                        itemStack = new ItemStack(mItem);
                    }

                    itemStack.setItemDamage(this.items.getItemByID(slot).getItemDamage());

                    LabyMod.getInstance().getDrawUtils().drawItem(itemStack, col2+16, (this.height - (this.height / 5)) - ((slot-11) * yDistance) + 5, "");

                }
            }else if(slot >= 22 && slot <= 32){
                if(false) {
                    String name = this.items.getName(item);
                    LabyMod.getInstance().getDrawUtils().drawString("§f" + name, col3, (this.height - (this.height / 5)) - ((slot-22) * yDistance) + 5, 1);
                }else{

                    ItemStack itemStack = new ItemStack(Blocks.sand);
                    if(this.items.getItemByID(slot).isInstanceOfBlock()){
                        Block block = this.items.getItemByID(slot).getBlock();
                        itemStack = new ItemStack(block);
                    }else{
                        Item mItem = this.items.getItemByID(slot).getMItem();
                        itemStack = new ItemStack(mItem);
                    }

                    itemStack.setItemDamage(this.items.getItemByID(slot).getItemDamage());

                    LabyMod.getInstance().getDrawUtils().drawItem(itemStack, col3+16, (this.height - (this.height / 5)) - ((slot-22) * yDistance) + 5, "");

                }
            }
            slot++;
        }


        this.scrollbar.draw();

        Mouse.setGrabbed(false);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
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




}
