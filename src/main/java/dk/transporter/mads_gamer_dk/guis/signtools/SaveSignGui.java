package dk.transporter.mads_gamer_dk.guis.signtools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.TaDrawUtils;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntitySign;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class SaveSignGui extends GuiScreen {
    private GuiScreen lastScreen;
    private ModTextField fieldServer;
    private GuiButton buttonAdd;
    private boolean displayError = false;
    private long shakingError = 0L;
    private DataManagers dataManagers;
    private TileEntitySign tileSign;

    private boolean error = false;

    public SaveSignGui(GuiScreen lastScreen, TileEntitySign tileSign, DataManagers dataManagers) {
        this.lastScreen = lastScreen;
        this.dataManagers = dataManagers;
        this.tileSign = tileSign;
        //System.out.println("SAVE SERVER: " + saveServer + " THIS: " + this.saveServer);
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.fieldServer = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), this.width / 2 - 110, this.height / 2 - 15, 220, 20);
        this.fieldServer.setMaxStringLength(90000);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 110, this.height / 2 + 15, 100, 20, LanguageManager.translate("button_cancel")));
        this.buttonAdd = new GuiButton(2, this.width / 2, this.height / 2 + 15, 110, 20, "Save");
        this.buttonList.add(this.buttonAdd);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.fieldServer.drawTextBox();
        TaDrawUtils draw = TransporterAddon.getDrawUtils();
        draw.drawString( "Skriv en title til skiltet:", this.width / 2 - 110, this.height / 2 - 30);
        if(error){
            draw.drawString( "Der er allerede et skilt med denne title!", this.width / 2 - 110, this.height / 2 + 40, 0xED4337);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
            case 2: {
                if(addSign()){
                    Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                }else{
                    this.error = true;
                }

            }
        }

    }

    private JsonObject createSignJsonObject(String signTitle){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", signTitle);
        JsonObject signLines = new JsonObject();
        signLines.addProperty("0", tileSign.signText[0].getUnformattedText());
        signLines.addProperty("1", tileSign.signText[1].getUnformattedText());
        signLines.addProperty("2", tileSign.signText[2].getUnformattedText());
        signLines.addProperty("3", tileSign.signText[3].getUnformattedText());
        jsonObject.add("content", signLines);
        return jsonObject;
    }

    private boolean addSign() {
        String signTitle = this.fieldServer.getText();
        JsonObject data = dataManagers.getSignData();
        if (data.has("signs")){
            JsonArray signsData = data.getAsJsonArray("signs");
            for (JsonElement elm: signsData) {
                if(elm.getAsJsonObject().has("title") && elm.getAsJsonObject().get("title").getAsString().equals(signTitle)){
                    return false;
                }
            }
            System.out.println(signsData);
            signsData.add(createSignJsonObject(signTitle));
            data.add("signs", signsData);
            System.out.println(data);
            dataManagers.saveSignData();
        }else{
            JsonArray signsData = new JsonArray();
            System.out.println(signsData);
            signsData.add(createSignJsonObject(signTitle));
            data.add("signs", signsData);
            System.out.println(data);
            dataManagers.saveSignData();
        }
        return true;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldServer.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            return;
        }
        if (keyCode == 28 && this.buttonAdd.enabled) {
            this.actionPerformed(this.buttonAdd);
        }
        this.fieldServer.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.fieldServer.updateCursorCounter();
    }
}

