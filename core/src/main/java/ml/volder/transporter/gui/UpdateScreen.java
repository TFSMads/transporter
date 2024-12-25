package ml.volder.transporter.gui;

import ml.volder.transporter.updater.UpdateManager;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.util.List;

@AutoActivity
public class UpdateScreen extends TransporterActivity {

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        addButton(new WrappedGuiButton(2, getWidth() / 2 - 175, getHeight() - 50, 150, 20, "Opdatere senere!"));
        addButton(new WrappedGuiButton(1, getWidth() / 2 + 25, getHeight() - 50, 150, 20, "Opdatere"));

    }

    @Override
    public void actionPerformed(WrappedGuiButton wrappedGuiButton) {
        if(wrappedGuiButton.getId() == 1)
            UpdateManager.update();
        if(wrappedGuiButton.getId() == 2)
            MinecraftAPI.getAPI().openMainMenu();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawBackground(0);
        drawAPI.drawCenteredString("Transporter Addon - Opdaterings System!", getWidth() / 2, 50, 2);
        String text = "Der er en nye version af transporter addon tilgængelig. " +
                "Klik på opdatere for at opdatere til den seneste version. " +
                "Vi anbefaler du opdatere med det samme men hvis du ønsker at vente så klik på opdater senere!";
        List<String> stringList =drawAPI.listFormattedStringToWidth(text, getWidth() / 2);
        int y = 80;
        for(String line : stringList) {
            drawAPI.drawCenteredString(line, getWidth() / 2, y);
            y += drawAPI.getFontHeight() + 1;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton, long timeSinceLastClick) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {

    }

    @Override
    public void handleMouseInput() {

    }

    @Override
    public void keyTyped(char typedChar, Key key) {

    }

    @Override
    public void onGuiClosed() {

    }
}
