package ml.volder.transporter.modules.signtoolsmodule;

import ml.volder.transporter.gui.TransporterActivity;
import ml.volder.transporter.modules.SignToolsModule;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.datasystem.Data;
import ml.volder.unikapi.datasystem.DataManager;
import ml.volder.unikapi.guisystem.ModTextures;
import ml.volder.unikapi.guisystem.elements.ControlElement;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.Material;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.types.ResourceLocation;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.tileentitysign.WrappedTileEntitySign;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.awt.*;
import java.time.Instant;

@AutoActivity
public class SignGui extends TransporterActivity {

    private WrappedGuiButton doneBtn;
    private ResourceLocation signTexture;
    private DataManager<Data> dataManager;
    private WrappedTileEntitySign tileEntitySign;

    private SignBuffer signText = new SignBuffer("","","","");
    private static SignBuffer bufferText = new SignBuffer("","","","");

    int selected = 1;

    boolean hoverToggleButton = false;
    boolean hoverCopyButton = false;
    boolean hoverPasteButton = false;
    boolean hoverLoadButton = false;
    boolean hoverSaveButton = false;
    static boolean drawSignTools = false;

    private boolean sendPlacePacket = true;

    public SignGui(DataManager<Data> dataManager, WrappedTileEntitySign tileEntitySign) {
        this.dataManager = dataManager;
        this.tileEntitySign = tileEntitySign;
    }

    public static SignBuffer getBufferedText() {
        return bufferText;
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        clearButtonList();
        InputAPI.getAPI().enableRepeatEvents(true);
        this.doneBtn = new WrappedGuiButton(0, this.getWidth() / 2 - 100, this.getHeight() / 4 + 120,  MinecraftAPI.getAPI().translateLanguageKey("gui.done") == null ? "Done" : MinecraftAPI.getAPI().translateLanguageKey("gui.done"));
        addButton(doneBtn);
        this.signTexture = ModTextures.SIGN;
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(!button.isEnabled())
            return;
        if(button.getId() == 0){
            tileEntitySign.markDirty();
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.sendPlacePacket = true;
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawAutoDimmedBackground(0);
        drawAPI.drawCenteredString(MinecraftAPI.getAPI().translateLanguageKey("sign.edit"), this.getWidth() / 2, 40);
        hoverToggleButton = drawButton( drawSignTools ? new ControlElement.IconData(Material.BARRIER) : new ControlElement.IconData(Material.OAK_SIGN), getWidth() / 2 + DrawAPI.getAPI().getStringWidth(MinecraftAPI.getAPI().translateLanguageKey("sign.edit")) / 2 + 10, 40 + (drawAPI.getFontHeight()/2) - 8, 16, mouseX, mouseY);

        drawAPI.bindTexture(signTexture);
        drawAPI.drawTexture(getWidth() / 2 - 24*2, 120 - 12*2, 256D, 256D, 24*4, 12*4);

        renderText(getWidth() / 2 - 24*2, 120 - 12*2);

        if(drawSignTools) {
            int width = 120;
            int height = 12*8;
            int outlineWidth = 1;

            int x = getWidth() / 2 - 24*2 - width - 8;
            int y = 120 - 12*4;

            drawAPI.drawRect(x, y, x + width, y + height, new Color(60, 63, 65, 140).getRGB());
            drawAPI.drawRect(x, y, x + width, y + outlineWidth, ModColor.toRGB(128,128, 128, 255));
            drawAPI.drawRect(x, y + height - outlineWidth, x + width, y + height, ModColor.toRGB(128,128, 128, 255));
            drawAPI.drawRect(x, y, x + outlineWidth, y + height, ModColor.toRGB(128,128, 128, 255));
            drawAPI.drawRect(x + width - outlineWidth, y, x + width, y + height, ModColor.toRGB(128,128, 128, 255));

            drawSign(x + width/2 - 24*2, 120 - 12*2);
            drawAPI.drawCenteredString("Sign Tools", x + width / 2, y + (((120 - 12*2) - y)/2) - drawAPI.getFontHeight() / 2);

            hoverPasteButton = drawButton(ModTextures.BUTTON_PASTE, x + width/2 + 24*2 - 14, y + height/2 + 12*2 + (y + height - (y + height/2 + 12*2))/2 - 7, 14, mouseX, mouseY);
            hoverCopyButton = drawButton(ModTextures.BUTTON_COPY, x + width/2 + 24*2 - 30, y + height/2 + 12*2 + (y + height - (y + height/2 + 12*2))/2 - 7, 14, mouseX, mouseY);

            hoverLoadButton = drawButton(ModTextures.BUTTON_GET, x + width/2 - 24*2, y + height/2 + 12*2 + (y + height - (y + height/2 + 12*2))/2 - 7, 14, mouseX, mouseY);
            hoverSaveButton = drawButton(ModTextures.BUTTON_PUT, x + width/2 - 24*2 + 16, y + height/2 + 12*2 + (y + height - (y + height/2 + 12*2))/2 - 7, 14, mouseX, mouseY);
        }
    }

    boolean state = true;
    long lastToggle = -1;

    private void renderText(int xSignCorner, int ySignCorner) {
        if(Instant.now().minusMillis(lastToggle).toEpochMilli() > 250){
            state = !state;
            lastToggle = Instant.now().toEpochMilli();
        }
        renderLine(signText.getLine(1), 1, xSignCorner, ySignCorner, selected == 1);
        renderLine(signText.getLine(2), 2, xSignCorner, ySignCorner, selected == 2);
        renderLine(signText.getLine(3), 3, xSignCorner, ySignCorner, selected == 3);
        renderLine(signText.getLine(4), 4, xSignCorner, ySignCorner, selected == 4);
    }

    private void renderLine(String text, int lineNumber, int xSignCorner, int ySignCorner, boolean isSelected) {
        int xCenter = xSignCorner + 24*2;
        int y = ySignCorner + 4 + (10)*(lineNumber-1);
        if(isSelected && state){
            text = "> " + text + " <";
        }
        DrawAPI.getAPI().drawString(text, xCenter - DrawAPI.getAPI().getStringWidth(text)/2, y, ModColor.BLACK.getColor().getRGB());
    }

    private void drawSign(int xSignCorner, int ySignCorner) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.bindTexture(signTexture);
        drawAPI.drawTexture(xSignCorner, ySignCorner, 256D, 256D, 24*4, 12*4);
        renderLine(bufferText.getLine(1), 1, xSignCorner, ySignCorner, false);
        renderLine(bufferText.getLine(2), 2, xSignCorner, ySignCorner, false);
        renderLine(bufferText.getLine(3), 3, xSignCorner, ySignCorner, false);
        renderLine(bufferText.getLine(4), 4, xSignCorner, ySignCorner, false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(hoverToggleButton)
            drawSignTools = !drawSignTools;
        if(hoverCopyButton) {
            bufferText.copy(signText);
        }else if(hoverPasteButton) {
            signText.copy(bufferText);
        }else if(hoverSaveButton) {
            sendPlacePacket = false;
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new SaveSignGui(this, dataManager, bufferText));
        }else if(hoverLoadButton) {
            sendPlacePacket = false;
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new LoadSignGui(dataManager, this, bufferText));
        }

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {

    }

    @Override
    public void handleMouseInput() {

    }

    @Override
    public void keyTyped(char typedChar, Key key) {
        if(key.equals(Key.ARROW_DOWN) || key.equals(Key.ENTER) || key.equals(Key.NUMPAD_ENTER)) {
            selected += 1;
            if(selected > 4)
                selected = 1;
        } else if(key.equals(Key.ARROW_UP)) { //KEY_UP
            selected -= 1;
            if(selected < 1)
                selected = 4;
        }

        if (key.equals(Key.ESCAPE)) // ESCAPE
        {
            this.actionPerformed(this.doneBtn);
        }

        if(key.equals(SignToolsModule.getCopyKey()) && InputAPI.getAPI().isCtrlKeyDown()) {
            bufferText.copy(signText);
            return;
        }

        if(key.equals(SignToolsModule.getPasteKey()) && InputAPI.getAPI().isCtrlKeyDown()) {
            signText.copy(bufferText);
            return;
        }

        String s = signText.getLine(selected); //TODO fjern formatting (farver) - Mineraft implmentation er i kommentaren under
        //String s = this.tileSign.signText[this.editLine].getUnformattedText();

        if (key.equals(Key.BACK) && s.length() > 0)
        {
            s = s.substring(0, s.length() - 1);
        }

        if (MinecraftAPI.getAPI().isAllowedCharacter(typedChar) && DrawAPI.getAPI().getStringWidth(s + typedChar) <= 90)
        {
            s += typedChar;
        }

        signText.setLine(selected, s);
    }

    @Override
    public void onGuiClosed() {
        InputAPI.getAPI().enableRepeatEvents(false);
        if(sendPlacePacket) {
                tileEntitySign.setLine(0, signText.getLine(1));
                tileEntitySign.setLine(1, signText.getLine(2));
                tileEntitySign.setLine(2, signText.getLine(3));
                tileEntitySign.setLine(3, signText.getLine(4));
                tileEntitySign.sendUpdatePacket();
                tileEntitySign.setEditable(true);
        }
    }

    private boolean drawButton(ResourceLocation resourceLocation, int x, int y, int buttonSize, int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        boolean hover = mouseX > x && mouseX < x + buttonSize && mouseY > y && mouseY < y + buttonSize;
        int colorA = hover ? ModColor.toRGB(10, 10, 10, 255) : ModColor.toRGB(220, 220, 220, 255);
        int colorB = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(0, 0, 0, 255);
        int colorC = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(180, 180, 180, 255);
        drawAPI.drawRectangle(x, y, x + buttonSize, y + buttonSize, colorA);
        drawAPI.drawRectangle(x + 1, y + 1, x+ buttonSize + 1, y + buttonSize + 1, colorB);
        drawAPI.drawRectangle(x + 1, y + 1, x + buttonSize, y + buttonSize, colorC);
        drawAPI.bindTexture(resourceLocation);
        drawAPI.drawTexture(x + (hover ? 1 : 0), y + (hover ? 1 : 0), 256.0D, 256.0D, buttonSize, buttonSize, 0.8F);
        return hover;
    }

    private boolean drawButton(ControlElement.IconData iconData, int x, int y, int buttonSize, int mouseX, int mouseY) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        boolean hover = mouseX > x && mouseX < x + buttonSize && mouseY > y && mouseY < y + buttonSize;
        int colorA = hover ? ModColor.toRGB(10, 10, 10, 255) : ModColor.toRGB(220, 220, 220, 255);
        int colorB = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(0, 0, 0, 255);
        int colorC = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(180, 180, 180, 255);
        drawAPI.drawRectangle(x, y, x + buttonSize, y + buttonSize, colorA);
        drawAPI.drawRectangle(x + 1, y + 1, x+ buttonSize + 1, y + buttonSize + 1, colorB);
        drawAPI.drawRectangle(x + 1, y + 1, x + buttonSize, y + buttonSize, colorC);
        double xOffset = 0;
        double yOffset = 0;
        if(iconData.hasMaterialIcon() && iconData.getMaterialIcon() == Material.OAK_SIGN) {
            yOffset = -0.5;
        }
        else if(iconData.hasMaterialIcon() && iconData.getMaterialIcon() == Material.BARRIER) {
            xOffset = 0.5;
            yOffset = 0.5;
        }
        drawAPI.drawItem(iconData.getMaterialIcon(), iconData.getItemDamage(), x + (hover ? 1 : 0) + xOffset, y + (hover ? 1 : 0) + yOffset, "", 1);
        return hover;
    }
}
