package ml.volder.transporter.gui;

import ml.volder.transporter.TransporterAddon;
import ml.volder.transporter.modules.ModuleManager;
import ml.volder.transporter.modules.SimpleModule;
import ml.volder.transporter.updater.UpdateManager;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.api.player.PlayerAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import ml.volder.unikapi.wrappers.guiscreen.WrappedGuiScreen;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddonInfoScreen extends WrappedGuiScreen {
    private static String jarChecksum = null;
    private WrappedGuiScreen lastScreen;
    private WrappedGuiButton buttonBack;

    public AddonInfoScreen(WrappedGuiScreen lastScreen) {
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
                PlayerAPI.getAPI().openGuiScreen(lastScreen);
            }else{
                PlayerAPI.getAPI().openGuiScreen(null);
            }
        }
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

        int drawY = 50;
        drawAPI.drawString("Addon version: " + UpdateManager.currentVersion + " (" + jarChecksum + ")", 10, drawY);
        drawY += drawAPI.getFontHeight()+2;
        drawAPI.drawString("Understøttet Minecraft versioner: " + UnikAPI.getMinecraftVersion(), 10, drawY);
        drawY += drawAPI.getFontHeight()+2;
        drawAPI.drawString("Client brand: " + UnikAPI.getClientBrand() + (UnikAPI.getOtherVersion() != null ? " v:" + UnikAPI.getOtherVersion() : ""), 10, drawY);
        drawY += drawAPI.getFontHeight()+2;
        drawAPI.drawString("Addon lokation: " + UpdateManager.getJarLocation(), 10, drawY);
        drawY += drawAPI.getFontHeight()+2;
        drawAPI.drawString("System Info: " + System.getProperty("os.name") + " - " + System.getProperty("os.version") + " - " +  System.getProperty("os.arch"), 10, drawY);
        drawY += drawAPI.getFontHeight()+2;
        drawAPI.drawString("Features:", 10, drawY);
        for (SimpleModule module : ModuleManager.getInstance().getLoadedModules().values()) {
            drawY += drawAPI.getFontHeight()+2;
            drawAPI.drawString(module.getDisplayName() + ": " + (module.isFeatureActive() ? ModColor.GREEN + "Aktiv" : ModColor.RED + "Deaktiveret"), 20, drawY);
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
