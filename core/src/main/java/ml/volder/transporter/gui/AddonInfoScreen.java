package ml.volder.transporter.gui;

import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.SimpleModule;
import ml.volder.transporter.updater.UpdateManager;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.draw.impl.Laby4DrawAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.loader.Laby4Loader;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

@AutoActivity
public class AddonInfoScreen extends TransporterActivity {
    private static String jarChecksum = null;
    private ScreenInstance lastScreen;
    private WrappedGuiButton buttonBack;

    public AddonInfoScreen(ScreenInstance lastScreen) {
        this.lastScreen = lastScreen;
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        this.clearButtonList();
        this.buttonBack = new WrappedGuiButton(1, this.getWidth() / 2 - 100, 20, 22, 20, "<");
        this.addButton(buttonBack);
        if(jarChecksum == null) {
            Path filePath = Paths.get(UpdateManager.getJarLocation());

            try {
                byte[] data = Files.readAllBytes(filePath);
                byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
                jarChecksum = new BigInteger(1, hash).toString(16);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void actionPerformed(WrappedGuiButton wrappedGuiButton) {
        if(wrappedGuiButton.getId() == 1) {
            if(lastScreen != null){
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(lastScreen);
            }else{
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
            }
        }
    }

    int drawY;
    private void drawDebugLine(TextComponent line, int x) {
        Laby.references().renderPipeline().componentRenderer().builder()
                .text(line)
                .pos(x, drawY)
                .render(Laby4DrawAPI.getRenderStack());
        drawY += DrawAPI.getAPI().getFontHeight()+2;
    }

    private void drawDebugLine(String line) {
        drawDebugLine(Component.text(line), 10);
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawAutoDimmedBackground(0);
        drawAPI.drawOverlayBackground(0, 45);
        drawAPI.drawGradientShadowTop(45.0D, 0.0D, (double)this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - 30, this.getHeight());
        drawAPI.drawGradientShadowBottom((double)(this.getHeight() - 30), 0.0D, (double)this.getWidth());
        drawAPI.drawString("Transporter Addon - Addon Info", (double)(this.getWidth() / 2 - 100 + 30), 25.0D);

        LabyAPI labyAPI = Laby.labyAPI();

        drawY = 50;
        drawDebugLine("Addon version: " + UpdateManager.currentVersion + " (" + jarChecksum + ")");

        drawDebugLine("Addon namespace: " + Laby4Loader.namespace());

        drawDebugLine("Understøttet Minecraft versioner: " + UnikAPI.getMinecraftVersion());

        drawDebugLine("Minecraft version: " + labyAPI.minecraft().getVersion());

        drawDebugLine("Labymod version: " + labyAPI.getVersion() + " (" + labyAPI.getBranchName() + ")");

        drawDebugLine("Addon lokation: " + UpdateManager.getJarLocation());

        drawDebugLine("System Info: " + System.getProperty("os.name") + " - " + System.getProperty("os.version") + " - " +  System.getProperty("os.arch"));

        drawDebugLine("Features:");
        for (SimpleModule module : ModuleManager.getInstance().getLoadedModules().values()) {

            TextComponent component = Component.text(module.getDisplayName() + ": ");
            if(module.isFeatureActive()) {
                component.append(Component.text("Aktiv").color(NamedTextColor.GREEN));
            } else {
                component.append(Component.text("Deaktiveret").color(NamedTextColor.RED));
            }


            drawDebugLine(component, 20);
        }

    }

    @Override
    public void mouseClicked(int i, int i1, MouseButton mouseButton) {

    }

    @Override
    public void mouseClickMove(int i, int i1, MouseButton mouseButton, long l) {

    }

    @Override
    public void mouseReleased(int i, int i1, MouseButton mouseButton) {

    }

    @Override
    public void handleMouseInput() {

    }

    @Override
    public void keyTyped(char c, Key key) {

    }

    @Override
    public void onGuiClosed() {

    }
}
