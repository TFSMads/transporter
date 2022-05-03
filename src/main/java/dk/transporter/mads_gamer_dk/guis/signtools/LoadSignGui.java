
package dk.transporter.mads_gamer_dk.guis.signtools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dk.transporter.mads_gamer_dk.TransporterAddon;
import dk.transporter.mads_gamer_dk.utils.SignBuffer;
import dk.transporter.mads_gamer_dk.utils.data.DataManagers;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.gui.elements.Scrollbar.EnumMouseAction;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;

public class LoadSignGui extends GuiScreen {
    private Scrollbar scrollbar;
    private TransporterAddon addon;
    private DataManagers dataManagers;
    private String hoverEntry;
    private CustomGuiEditSign lastScreen;

    public FontRenderer getFontRenderer(){
        return this.fontRendererObj;
    }

    public LoadSignGui(TransporterAddon addon, DataManagers dataManagers, CustomGuiEditSign lastScreen) {
        this.addon = addon;
        this.dataManagers = dataManagers;
        this.lastScreen = lastScreen;
    }

    public void updateScreen() {
    }

    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar = new Scrollbar(52);
        this.scrollbar.setPosition(this.width / 2 + 150 + 4, 41, this.width / 2 + 150 + 4 + 6, this.height - 40);
        this.scrollbar.setSpeed(20);
        this.scrollbar.setSpaceBelow(5);



        this.buttonList.add(new GuiButton(30007, this.width / 2 - 50 - 5 - 100, this.height - 30, 100, 20, "Remove Save"));
        this.buttonList.add(new GuiButton(30006, this.width / 2 - 50, this.height - 30, 100, 20, "Cancel"));
        this.buttonList.add(new GuiButton(30005, this.width / 2 + 50 + 5, this.height - 30, 100, 20, "Clear Saves"));

        Integer buttonWidth = this.width / 5;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        System.out.println("Clicked: Button with id " + button.id);
        super.actionPerformed(button);
        switch (button.id) {
            case 30005:
                dataManagers.getSignData().remove("signs");
                dataManagers.getSignData().add("signs", new JsonArray());
                dataManagers.saveSignData();
                this.hoverEntry = "";
                break;
            case 30006:
                Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                break;
            case 30007:
                Minecraft.getMinecraft().displayGuiScreen(new RemoveSavedSignGui(this, dataManagers));

        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();

        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());

        JsonObject data = dataManagers.getSignData();

        if (data.has("signs")){
            JsonArray signs = data.getAsJsonArray("signs");

            this.scrollbar.update(signs.size());

            int midX = this.width / 2;
            int entryWidth = 300;
            int entryHeight = 52;
            double posY = 45.0 + this.scrollbar.getScrollY();

            boolean hoveringEntry = false;

            for(JsonElement json : signs) {
                JsonObject jsonObj = json.getAsJsonObject();
                String signTitle = jsonObj.get("title").toString();
                signTitle = signTitle.replace("\"", "");

                JsonObject jsonObject = jsonObj.has("content") ? jsonObj.get("content").getAsJsonObject() : new JsonObject();

                String[] signLines = new String[] {jsonObject.has("0") ? jsonObject.get("0").getAsString() : "l1",jsonObject.has("1") ? jsonObject.get("1").getAsString() : "l2",jsonObject.has("2") ? jsonObject.get("2").getAsString() : "l3",jsonObject.has("3") ? jsonObject.get("3").getAsString() : "l4"};


                SignInfoRenderer signInfoRenderer = new SignInfoRenderer(signTitle, signLines);
                if((double)mouseY > posY && (double)mouseY < posY + (double)entryHeight && mouseX > midX - entryWidth / 2 && mouseX < midX + entryWidth / 2 + 5){
                    this.hoverEntry = signTitle;
                    hoveringEntry = true;
                    draw.drawRect(midX - entryWidth / 2 - 2, posY - 2.0, midX + entryWidth / 2 + 2, posY + (double)entryHeight - 2.0, ModColor.toRGB(128,128, 128, 255));
                    draw.drawRect(midX - entryWidth / 2 - 1, posY - 1.0, midX + entryWidth / 2 + 1, posY + (double)entryHeight - 3.0, ModColor.toRGB(0, 0, 0, 255));
                }
                signInfoRenderer.drawEntry(midX - entryWidth / 2, (int)posY, entryWidth + 5, mouseX, mouseY);
                posY += (double)entryHeight;
            }
            if(!hoveringEntry){this.hoverEntry = null;}
        }

        draw.drawOverlayBackground(0, 41);
        draw.drawGradientShadowTop(41.0, 0.0, this.width);
        draw.drawOverlayBackground(this.height - 40, this.height);
        draw.drawGradientShadowBottom(this.height - 40, 0.0, this.width);

        LabyMod.getInstance().getDrawUtils().drawCenteredString(ModColor.cl("8")+ModColor.cl("l")+"["+ModColor.cl("a")+ModColor.cl("l")+" Gemte Skilte "+ModColor.cl("8")+ModColor.cl("l")+"]", (double)(this.width / 2), 20.0D, 2.0D);
        this.scrollbar.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, EnumMouseAction.CLICKED);
        if(hoverEntry != null){
            System.out.println("Load entry: " + hoverEntry);
            JsonArray data = dataManagers.getSignData().get("signs").getAsJsonArray();

            JsonObject obj = new JsonObject();
            for (JsonElement entry: data) {
                if(entry.getAsJsonObject().get("title").getAsString().equals(hoverEntry)){
                    obj = entry.getAsJsonObject().has("content") ? entry.getAsJsonObject().get("content").getAsJsonObject() : new JsonObject();
                }
            }

            SignBuffer.signLine1 = new ChatComponentText(obj.has("0") ? obj.get("0").getAsString() : "§r");
            SignBuffer.signLine2 = new ChatComponentText(obj.has("1") ? obj.get("1").getAsString() : "§r");
            SignBuffer.signLine3 = new ChatComponentText(obj.has("2") ? obj.get("2").getAsString() : "§r");
            SignBuffer.signLine4 = new ChatComponentText(obj.has("3") ? obj.get("3").getAsString() : "§r");
            lastScreen.pasteSignBuffer();
            Minecraft.getMinecraft().displayGuiScreen(lastScreen);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, EnumMouseAction.DRAGGING);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}