package ml.volder.transporter.gui;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.util.List;

@AutoActivity
public class UpdateFailedScreen extends TransporterActivity {

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
        String text = "Kunne ikke fuldføre opdatering da java ikke blev fundet!" +
                "Prøv at installere java eller opdater Transporter Addon manuelt!";
        List<String> stringList =drawAPI.listFormattedStringToWidth(text, getWidth() / 2);
        int y = 80;

        for(String line : stringList) {

            Laby.references().renderPipeline().componentRenderer().builder()
                    .text(line)
                    .color(NamedTextColor.RED.getValue())
                    .pos(getWidth() / 2, y)
                    .centered(true)
                    .render(Laby4DrawAPI.getRenderStack());

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
