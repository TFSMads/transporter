package dk.transporter.mads_gamer_dk.guis.signtools;

import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.SignBuffer;
import dk.transporter.mads_gamer_dk.utils.TaDrawUtils;
import net.labymod.gui.elements.TabbedGuiButton;
import net.labymod.main.LabyMod;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomGuiEditSign extends GuiScreen
{
    /** Reference to the sign object. */
    private TileEntitySign tileSign;
    /** Counts the number of screen updates. */
    private int updateCounter;
    /** The index of the line that is being edited. */
    private int editLine;
    /** "Done" button for the GUI. */
    private GuiButton doneBtn;
    private TransporterAddon addon;
    private boolean sendPlacePacket = true;

    private boolean isHoveringOpenGui = false;
    private boolean isHoveringCloseGui = false;
    private int signToolsGuiRenderOffset = -1005;
    private List<GuiButton> signToolsGuiButtons = new ArrayList<>();

    public CustomGuiEditSign(TileEntitySign teSign, TransporterAddon addon)
    {
        this.tileSign = teSign;
        this.addon = addon;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);





        GuiButton button = new TabbedGuiButton(new ResourceLocation("transporter/textures/buttons/paste.png"), 12200, (139-7)/2+4, this.height/2+11, 10 + 50, 20, "Paste");
        this.buttonList.add(button);
        this.signToolsGuiButtons.add(button);
        button = new TabbedGuiButton(new ResourceLocation("transporter/textures/buttons/copy.png"), 12201, (139-7)/2+4, this.height/2+33, 10 + 50, 20, "Copy ");
        this.buttonList.add(button);
        this.signToolsGuiButtons.add(button);
        button  = new TabbedGuiButton(new ResourceLocation("transporter/textures/buttons/load.png"),12202, (139-7)/2-64, this.height/2+11, 10 + 50, 20, "Load");
        this.buttonList.add(button);
        this.signToolsGuiButtons.add(button);
        button = new TabbedGuiButton(new ResourceLocation("transporter/textures/buttons/save.png"),12203, (139-7)/2-64, this.height/2+33, 10 + 50, 20, "Save");
        this.buttonList.add(button);
        this.signToolsGuiButtons.add(button);



        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0])));
        this.tileSign.setEditable(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        if(sendPlacePacket) {
            NetHandlerPlayClient nethandlerplayclient = this.mc.getNetHandler();

            if (nethandlerplayclient != null) {
                nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
            }

            this.tileSign.setEditable(true);
        }
    }


    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX,mouseY,mouseButton);
        if (mouseButton == 0)
        {
            if(this.isHoveringOpenGui){
                this.isHoveringOpenGui = false;
                addon.isSignToolsGuiOpen = true;
                this.signToolsGuiRenderOffset = -100000000;
            }else if(this.isHoveringCloseGui){
                this.isHoveringCloseGui = false;
                addon.isSignToolsGuiOpen = false;
                this.signToolsGuiRenderOffset = -5;
            }

        }
    }


    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.updateCounter;
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        System.out.println("CLICK!!!");
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.tileSign.markDirty();
                this.mc.displayGuiScreen((GuiScreen)null);
            }

            if (button.id == 12200) {
                pasteSignBuffer();
            }else if (button.id == 12201) {
                copySign();
            }else if (button.id == 12202) {
                sendPlacePacket = false;
                this.mc.displayGuiScreen(new LoadSignGui(addon, addon.getDataManagers(), this));
            }else if (button.id == 12203) {
                sendPlacePacket = false;
                this.mc.displayGuiScreen(new SaveSignGui(this, tileSign,addon.getDataManagers()));
            }
        }


    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 200)
        {
            this.editLine = this.editLine - 1 & 3;
        }

        if (keyCode == 208 || keyCode == 28 || keyCode == 156)
        {
            this.editLine = this.editLine + 1 & 3;
        }

        String s = this.tileSign.signText[this.editLine].getUnformattedText();

        if (keyCode == 14 && s.length() > 0)
        {
            s = s.substring(0, s.length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRendererObj.getStringWidth(s + typedChar) <= 90)
        {
            s = s + typedChar;
        }

        this.tileSign.signText[this.editLine] = new ChatComponentText(s);

        if (keyCode == 1)
        {
            this.actionPerformed(this.doneBtn);
        }

        if(keyCode == 47){
            if(Keyboard.isKeyDown(29)){
                pasteSignBuffer();
            }
        }
        if(keyCode == 46){
            if(Keyboard.isKeyDown(29)){
                copySign();
            }
        }
        if(keyCode == 31){
            if(Keyboard.isKeyDown(29)){
                sendPlacePacket = false;
                this.mc.displayGuiScreen(new SaveSignGui(this, tileSign,addon.getDataManagers()));
            }
        }
        if(keyCode == 38){
            if(Keyboard.isKeyDown(29)){
                sendPlacePacket = false;
                this.mc.displayGuiScreen(new LoadSignGui(addon, addon.getDataManagers(), this));
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.sendPlacePacket = true;
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40, 16777215);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2), 0.0F, 50.0F);
        float f = 93.75F;
        GlStateManager.scale(-f, -f, -f);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        Block block = this.tileSign.getBlockType();

        if (block == Blocks.standing_sign)
        {
            float f1 = (float)(this.tileSign.getBlockMetadata() * 360) / 16.0F;
            GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int i = this.tileSign.getBlockMetadata();
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }

        if (this.updateCounter / 6 % 2 == 0)
        {
            this.tileSign.lineBeingEdited = this.editLine;
        }




        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
        this.tileSign.lineBeingEdited = -1;
        GlStateManager.popMatrix();

        int guiWidth = 0;



        if(addon.isSignToolsGuiOpen){
            ResourceLocation texture = new ResourceLocation("transporter/textures/gui.png");

            this.mc.getTextureManager().bindTexture(texture);

            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_WIDTH);
            guiWidth = width;
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_HEIGHT);

            int sizeMultiplier = 1;

            if(this.signToolsGuiRenderOffset < -99999){
                this.signToolsGuiRenderOffset = -width;
            }
            if(this.signToolsGuiRenderOffset < -width){
                this.signToolsGuiRenderOffset = -5;
            }

            if(signToolsGuiRenderOffset < -5){
                signToolsGuiRenderOffset += 10;
                if(signToolsGuiRenderOffset > -5){
                    signToolsGuiRenderOffset = -5;
                }
            }else{
                signToolsGuiRenderOffset = -5;
            }

            LabyMod.getInstance().getDrawUtils().drawTexture(signToolsGuiRenderOffset,this.height/2-(height*sizeMultiplier/2),256D,256D,width*sizeMultiplier,height*sizeMultiplier);
        }else{

            if(signToolsGuiRenderOffset > -1000){
                ResourceLocation texture = new ResourceLocation("transporter/textures/gui.png");

                this.mc.getTextureManager().bindTexture(texture);

                int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_WIDTH);
                guiWidth = width;
                int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_HEIGHT);

                int sizeMultiplier = 1;

                if(signToolsGuiRenderOffset+width < 0){
                    signToolsGuiRenderOffset = -9999999;
                }

                signToolsGuiRenderOffset -= 10;

                LabyMod.getInstance().getDrawUtils().drawTexture(signToolsGuiRenderOffset,this.height/2-(height*sizeMultiplier/2),256D,256D,width*sizeMultiplier,height*sizeMultiplier);
            }

            //TODO Ændre så knappen er en class der extender GuiButton istedet for den bliver rendered her
            if(signToolsGuiRenderOffset < -1000){
                ResourceLocation texture = new ResourceLocation("transporter/textures/open.png");

                this.mc.getTextureManager().bindTexture(texture);

                int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_WIDTH);
                int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_HEIGHT);

                int sizeMultiplier = 1;

                this.isHoveringOpenGui = mouseX >= 0 && mouseY >= this.height/2-(height*sizeMultiplier/2) && mouseX < 0 + width && mouseY < this.height/2-(height*sizeMultiplier/2) + height;
                //TODO optimere så den ikke skal bind open.png for at finde width og height inden den tjeker om den skal draw open.png

                texture = this.isHoveringOpenGui ? new ResourceLocation("transporter/textures/open_hover.png") : texture;
                this.mc.getTextureManager().bindTexture(texture); //TODO Kun bind texture en gang.

                LabyMod.getInstance().getDrawUtils().drawTexture(0,this.height/2-(height*sizeMultiplier/2),256D,256D,width*sizeMultiplier,height*sizeMultiplier);

            }
        }

        if(addon.isSignToolsGuiOpen && this.signToolsGuiRenderOffset == -5){
            for (GuiButton button: signToolsGuiButtons) {
                button.enabled = true;
                button.visible = true;
            }

            ResourceLocation texture = new ResourceLocation("transporter/textures/buttons/close.png");

            this.mc.getTextureManager().bindTexture(texture);

            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_WIDTH);
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0, GL11.GL_TEXTURE_HEIGHT);

            this.isHoveringCloseGui = mouseX >= (guiWidth-7)-19 && mouseY >= this.height/2-63 && mouseX < (guiWidth-7)-19+ width && mouseY < this.height/2-63 + height;


            texture = this.isHoveringCloseGui ? new ResourceLocation("transporter/textures/buttons/close_hover.png") : texture;
            this.mc.getTextureManager().bindTexture(texture); //TODO Kun bind texture en gang.

            LabyMod.getInstance().getDrawUtils().drawTexture((guiWidth-7)-19,this.height/2-63,256D,256D,width,height);


            TaDrawUtils drawUtils = TransporterAddon.getDrawUtils();

            drawUtils.drawCenteredString("Kopieret skilt", (guiWidth-7)/2, this.height/2 - 60, 1.3D);
            drawUtils.drawSign((guiWidth-7)/2,this.height/2-18,SignBuffer.signLine1.getUnformattedText(), SignBuffer.signLine2.getUnformattedText(),SignBuffer.signLine3.getUnformattedText(),SignBuffer.signLine4.getUnformattedText(), true, true);
        }else{
            for (GuiButton button: signToolsGuiButtons) {
                button.enabled = false;
                button.visible = false;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void pasteSignBuffer() {
        tileSign.signText[0] = SignBuffer.signLine1;
        tileSign.signText[1] = SignBuffer.signLine2;
        tileSign.signText[2] = SignBuffer.signLine3;
        tileSign.signText[3] = SignBuffer.signLine4;
    }

    public void copySign() {
        SignBuffer.signLine1 = tileSign.signText[0];
        SignBuffer.signLine2 = tileSign.signText[1];
        SignBuffer.signLine3 = tileSign.signText[2];
        SignBuffer.signLine4 = tileSign.signText[3];
    }
}
