package ml.volder.transporter.gui;

import ml.volder.transporter.classes.csv.CsvFile;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.guisystem.elements.ModTextField;
import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.wrappers.guibutton.WrappedGuiButton;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.ScreenWrapper;
import net.labymod.api.client.gui.screen.activity.AutoActivity;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@AutoActivity
public class CsvEditor extends TransporterActivity {

    private Consumer<CsvFile> saveCallback;

    public static void openEditor(InputStream inputStream) {
        CsvEditor csvEditor = new CsvEditor(new CsvFile(inputStream, ','), Laby.labyAPI().minecraft().minecraftWindow().currentScreen());
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(csvEditor);
    }

    public static void openEditor(File file, char seperator, Consumer<CsvFile> saveCallback) {
        CsvEditor csvEditor = new CsvEditor(CsvFile.fromFile(file, seperator), Laby.labyAPI().minecraft().minecraftWindow().currentScreen());
        csvEditor.saveCallback = saveCallback;
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(csvEditor);
    }

    /**
     * Width of the editor - percentage of the screen width
     */
    private final int editorWidth = 90;

    /**
     * Height of the editor - percentage of the screen height
     */
    private final int editorHeight = 80;

    /**
     * The csv file that is being edited
     */
    private CsvFile csvFile;

    /**
     * The width of each column
     */
    private int columnWidth = 100;

    /**
     * The height of each row
     */
    private int rowHeight = 50;

    /**
     * List of rows
     */
    private List<List<CsvEditorEntry>> csvEditorEntries;

    private Scrollbar scrollbar = new Scrollbar(1);

    private CsvEditorEntry hoveredEntry = null;

    private final ScreenWrapper previousScreen;

    public CsvEditor(CsvFile csvFile, ScreenWrapper previousScreen) {
        this.csvFile = csvFile;
        this.previousScreen = previousScreen;

        reloadCsvFile();
    }

    private void reloadCsvFile() {
        csvEditorEntries = new ArrayList<>();
        for (int row = 0; row < csvFile.getRowCount(); row++) {
            List<CsvEditorEntry> csvEditorEntriesRow = new ArrayList<>();
            csvFile.getRowEntries(row).forEach(csvEntry -> csvEditorEntriesRow.add(new CsvEditorEntry(csvEntry)));
            csvEditorEntries.add(csvEditorEntriesRow);
        }
    }

    private void updateScrollbar() {
        this.scrollbar.setSpeed(20);
        this.scrollbar.setEntryHeight(rowHeight);
        this.scrollbar.update(csvFile.getRowCount());
        this.scrollbar.init();
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        //Update column width and row height
        columnWidth = (int) (getWidth() / 100D * editorWidth / csvFile.getColumnCount());
        rowHeight = DrawAPI.getAPI().getFontHeight() + 16;

        int marginLeft = (getWidth() / 100 * (100 - editorWidth)) / 2;
        int marginTop = (getHeight() / 100 * (100 - editorHeight)) / 2;

        this.scrollbar.setPosition(this.getWidth() - marginLeft /2, marginTop, this.getWidth() - marginLeft /2 + 4, this.getHeight() - marginTop);
        updateScrollbar();

        this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 100, 20, 22, 20, "<"));
        this.addButton(new WrappedGuiButton(2, this.getWidth() / 2 + 50, getHeight() - marginTop/2 - 10, 100, 20, "Tilføj række"));
        this.addButton(new WrappedGuiButton(3, this.getWidth() / 2 - 125, getHeight() - marginTop/2 - 10, 100, 20, "Fjern række"));
    }

    @Override
    public void actionPerformed(WrappedGuiButton button) {
        if(button.getId() == 1) {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(previousScreen);
        }
        else if (button.getId() == 2) {
            csvFile.addRow();
            reloadCsvFile();
            updateScrollbar();
        }
        else if (button.getId() == 3) {
            csvFile.removeLastRow();
            reloadCsvFile();
            updateScrollbar();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawAutoDimmedBackground(this.scrollbar.getScrollY());

        int marginLeft = (getWidth() / 100 * (100 - editorWidth)) / 2;
        int marginTop = (getHeight() / 100 * (100 - editorHeight)) / 2;

        hoveredEntry = null;

        //Draw rows
        for (List<CsvEditorEntry> csvEditorEntriesRow : csvEditorEntries) {
            for (CsvEditorEntry csvEditorEntry : csvEditorEntriesRow) {
                if(csvEditorEntry.draw(
                        marginLeft + (csvEditorEntriesRow.indexOf(csvEditorEntry) * columnWidth),
                        (int) (marginTop + (csvEditorEntries.indexOf(csvEditorEntriesRow) * rowHeight) + scrollbar.getScrollY()),
                        columnWidth,
                        rowHeight,
                        mouseX,
                        mouseY
                )) {
                    hoveredEntry = csvEditorEntry;
                }
            }
        }

        //set hoveredEntry to null if mouse is outside editor
        if(mouseY < marginTop || mouseY > getHeight() - marginTop) {
            hoveredEntry = null;
        }

        this.scrollbar.draw(mouseX, mouseY);


        drawAPI.drawOverlayBackground(0, marginTop);
        drawAPI.drawGradientShadowTop(marginTop, 0.0D, (double)this.getWidth());
        drawAPI.drawOverlayBackground(this.getHeight() - marginTop, this.getHeight());
        drawAPI.drawGradientShadowBottom((double)(this.getHeight() - marginTop), 0.0D, (double)this.getWidth());

        drawAPI.drawString("Transporter Addon - Indstilinger", (double)(this.getWidth() / 2 - 100 + 30), 25.0D);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if(hoveredEntry != null) {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new EntryEditor(hoveredEntry.csvEntry, this));
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, MouseButton mouseButton, long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }

    @Override
    public void handleMouseInput() {
        this.scrollbar.mouseInput();
    }

    @Override
    public void keyTyped(char typedChar, Key key) {

    }

    @Override
    public void onGuiClosed() {
        csvFile.save();
        if(saveCallback != null) {
            saveCallback.accept(csvFile);
        }
    }

    private static class CsvEditorEntry {
        public CsvFile.CsvEntry csvEntry;

        public CsvEditorEntry(CsvFile.CsvEntry csvEntry) {
            this.csvEntry = csvEntry;
        }

        public boolean draw(int x, int y, int width, int height, int mouseX, int mouseY) {
            DrawAPI drawAPI = DrawAPI.getAPI();
            drawAPI.drawRectangle(
                    x,
                    y,
                    x + width,
                    y + height,
                    new Color(43, 45, 48, 255).getRGB()
            );
            drawAPI.drawRectangle(
                    x + 1,
                    y + 1,
                    x + width - 1,
                    y + height - 1,
                    new Color(30, 31, 34, 255).getRGB()
            );
            String trimmedString = drawAPI.trimStringToWidth(csvEntry.entryValue, width - 10);
            drawAPI.drawString(trimmedString, x + 3, y + 8);

            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }



    @AutoActivity
    public class EntryEditor extends TransporterActivity {
        private TransporterActivity backgroundScreen;
        private ModTextField expandedField;

        CsvFile.CsvEntry csvEntry;

        public EntryEditor(CsvFile.CsvEntry csvEntry, TransporterActivity backgroundScreen) {
            this.backgroundScreen = backgroundScreen;
            this.csvEntry = csvEntry;
        }

        public void initGui() {
            this.backgroundScreen.setWidth(this.getWidth());
            this.backgroundScreen.setHeight(this.getHeight());
            this.expandedField = new ModTextField(0, this.getWidth() / 2 - 150, this.getHeight() / 4 + 45, 300, 20);
            this.expandedField.setFocused(true);
            this.expandedField.setMaxStringLength(10000);
            this.expandedField.setText(csvEntry.entryValue);
            this.addButton(new WrappedGuiButton(1, this.getWidth() / 2 - 50, this.getHeight() / 4 + 85, 100, 20, "Done"));
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            DrawAPI drawAPI = DrawAPI.getAPI();
            drawAPI.drawRect(0.0, 0.0, (double)this.getWidth(), (double)this.getHeight(), Integer.MIN_VALUE);
            drawAPI.drawRect((double)(this.getWidth() / 2 - 165), (double)(this.getHeight() / 4 + 35), (double)(this.getWidth() / 2 + 165), (double)(this.getHeight() / 4 + 120), Integer.MIN_VALUE);
            this.expandedField.drawTextBox();
        }

        public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
            this.expandedField.mouseClicked(mouseX, mouseY, mouseButton);
            csvEntry.entryValue = this.expandedField.getText();
        }

        public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        }

        public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        }

        public void handleMouseInput() {
        }

        public void keyTyped(char typedChar, Key key) {
            if (key.equals(Key.ESCAPE)) {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.backgroundScreen);

            }

            if (this.expandedField.textboxKeyTyped(typedChar, key)) {
                csvEntry.entryValue = this.expandedField.getText();
            }

        }

        public void onGuiClosed() {}

        public void updateScreen() {
            this.backgroundScreen.updateScreen();
            this.expandedField.updateCursorCounter();
        }

        public void actionPerformed(WrappedGuiButton button) {
            if (button.getId() == 1) {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.backgroundScreen);
            }
        }

        public TransporterActivity getBackgroundScreen() {
            return this.backgroundScreen;
        }
    }
}
