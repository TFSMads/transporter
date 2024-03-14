package ml.volder.transporter.gui;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.util.List;

public class UpdateFailedScreen extends WrappedGuiScreen {

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {

    }

    @Override
    public void actionPerformed(WrappedGuiButton wrappedGuiButton) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawBackground(0);
        drawAPI.drawCenteredString("Transporter Addon - Opdaterings System!", getWidth() / 2, 50, 2);
        String text = ModColor.RED +
                "Kunne ikke fuldføre opdatering da java ikke blev fundet!" +
                "Prøv at installere java eller opdater Transporter Addon manuelt!";
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
