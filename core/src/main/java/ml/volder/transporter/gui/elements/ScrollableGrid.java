package ml.volder.transporter.gui.elements;

import ml.volder.unikapi.guisystem.elements.Scrollbar;
import ml.volder.unikapi.keysystem.MouseButton;
import net.labymod.api.client.render.matrix.Stack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScrollableGrid {

    private double width;
    private double height;
    private int x;
    private int y;

    private final int entryWidth;
    private final int entryHeight;
    private final int spacingHorizontal;
    private final int spacingVertical;
    private List<? extends Entry> entries;

    private final Scrollbar scrollbar;

    public ScrollableGrid(double width, double height, int x, int y, int entryWidth, int entryHeight, int spacingHorizontal, int spacingVertical) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
        this.spacingHorizontal = spacingHorizontal;
        this.spacingVertical = spacingVertical;
        this.entries = new ArrayList<>();
        this.scrollbar = new Scrollbar(entryHeight + spacingVertical);
        this.scrollbar.setSpeed(39);
    }

    public void render(int mouseX, int mouseY, Stack stack) {
        int entryAmountHorizontal = (int)Math.floor(width / (entryWidth + spacingHorizontal));
        double entriesWidth = entryAmountHorizontal * (entryWidth + spacingHorizontal); //Total width of all entries including spacing between them
        double startX = (width - entriesWidth) / 2 + x; //Start x position of the first entry so that all entries are centered

        int rows = (int) Math.ceil((double) entries.size() / entryAmountHorizontal); //Amount of rows that the entries will take up

        int scrollY = (int)this.scrollbar.getScrollY();
        int outOfViewEntryRows = Math.abs(scrollY) / (entryHeight + spacingVertical); //Amount of rows that are out of view above the visible area
        int inViewEntryRows = (int) (height / (entryHeight + spacingVertical)); //Amount of rows that are visible
        int outOfViewAmount = entryAmountHorizontal * outOfViewEntryRows; //Amount of entries that are out of view above the visible area
        int visibleEntriesAmount = entryAmountHorizontal * (inViewEntryRows + 2); //Amount of entries that are visible

        int firstIndex = Math.max(Math.min(outOfViewAmount, entries.size() - visibleEntriesAmount), 0);
        int lastIndex = Math.max(Math.min(outOfViewAmount + visibleEntriesAmount, entries.size()),0);

        Collection<? extends Entry> visibleEntries = entries.subList(firstIndex, lastIndex);

        int offset = scrollY + Math.max(Math.min(outOfViewEntryRows, rows - inViewEntryRows - 2), 0) * (entryHeight + spacingVertical); //Offset for the y position of the entries

        int currentX = (int) startX;
        int currentY = y;

        for(Entry entry : visibleEntries) {
            if(currentX + entryWidth >= startX + entriesWidth) {
                currentX = (int) startX;
                currentY += entryHeight + spacingVertical;
            }
            entry.render(currentX, currentY + offset, mouseX, mouseY, stack);
            currentX += entryWidth + spacingHorizontal;
        }

        this.scrollbar.setPosition(startX + entriesWidth + 3, y, startX + entriesWidth + 6, y + height);
        this.scrollbar.update(rows);

        if(rows < inViewEntryRows){
            this.scrollbar.update(0);
        }

        this.scrollbar.draw();

        hoverEntries = visibleEntries;
    }

    private Collection<? extends Entry> hoverEntries = new ArrayList<>();

    public void renderHoverText() {
        for(Entry entry : hoverEntries) {
            entry.renderHoverText();
        }
    }

    public void initGui() {
        this.scrollbar.init();
    }

    public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {
        if(mouseY > y && mouseY < y + height && mouseX > x && mouseX < x + width)
            entries.forEach(transporterMenuEntry -> transporterMenuEntry.mouseClicked(mouseX, mouseY, mouseButton));
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }

    public void mouseClickMove(int mouseX, int mouseY, MouseButton clickedMouseButton, long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    public void mouseReleased(int mouseX, int mouseY, MouseButton mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }

    public void handleMouseInput() {
        this.scrollbar.mouseInput();
    }

    public void updateEntries(List<? extends Entry> entries) {
        this.entries = entries;
    }

    public void resetScroll() {
        this.scrollbar.reset();
    }

    public void updateGridSize(double width, double height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public static abstract class Entry {
        public abstract void render(int x, int y, int mouseX, int mouseY, Stack stack);

        public abstract void renderHoverText();

        public void mouseClicked(int mouseX, int mouseY, MouseButton mouseButton) {

        }
    }
}
