package ml.volder.unikapi.guisystem.elements;

import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.utils.ColorUtils;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class DropDownMenu<T> {
    private static final DropDownEntryDrawer defaultDrawer = (object, x, y, trimmedEntry) -> DrawAPI.getAPI().drawString(trimmedEntry, x, y);
    private String title;
    private T selected = null;
    private boolean enabled = true;
    private boolean open;
    private T hoverSelected = null;
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    private int maxY = 2147483647;
    private ArrayList<T> list = new ArrayList();
    private DropDownEntryDrawer entryDrawer = null;
    private Scrollbar scrollbar;
    private Consumer<T> hoverCallback;

    public DropDownMenu(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public DropDownMenu<T> fill(T[] values) {
        Object[] var2 = values;
        int var3 = values.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            T value = (T) var2[var4];
            this.list.add(value);
        }

        return this;
    }

    public void onScroll() {
        if (this.scrollbar != null) {
            this.scrollbar.mouseInput();
            this.scrollbar.setScrollY((double)((int)(this.scrollbar.getScrollY() / (double)this.scrollbar.getSpeed()) * this.scrollbar.getSpeed()));
        }

    }

    public void onDrag(int mouseX, int mouseY, MouseButton mouseButton) {
        if (this.scrollbar != null) {
            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        }

    }

    public void onRelease(int mouseX, int mouseY, MouseButton mouseButton) {
        if (this.scrollbar != null) {
            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        }

    }

    public boolean onClick(int mouseX, int mouseY, MouseButton mouseButton) {
        if (this.scrollbar != null && this.scrollbar.isHoverTotalScrollbar(mouseX, mouseY)) {
            this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
            return true;
        } else if (this.enabled && !this.list.isEmpty()) {
            if (mouseX > this.x - 1 && mouseX < this.x + this.width + 1 && mouseY > this.y - 1 && mouseY < this.y + this.height + 1) {
                this.open = !this.open;
                if (this.open && this.list.size() > 10) {
                    if (this.scrollbar == null) {
                        this.scrollbar = new Scrollbar(13);
                    }

                    this.scrollbar.setSpeed(13);
                    this.scrollbar.setListSize(this.list.size());
                    this.scrollbar.setPosition(this.x + this.width - 5, this.y + this.height + 1, this.x + this.width, this.maxY == 2147483647 ? this.y + this.height + 1 + 130 - 1 : this.maxY);
                }

                return true;
            } else if (this.hoverSelected != null) {
                this.selected = this.hoverSelected;
                this.open = false;
                return true;
            } else {
                this.open = false;
                if (!this.open && this.hoverCallback != null) {
                    this.hoverCallback.accept(null);
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX > this.x - 1 && mouseX < this.x + this.width + 1 && mouseY > this.y - 1 && mouseY < this.y + this.height + 1;
    }

    public void draw(int mouseX, int mouseY) {
        T prevHover = this.hoverSelected;
        this.hoverSelected = null;
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, Color.GRAY.getRGB());
        drawAPI.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Color.BLACK.getRGB());
        if (this.selected != null) {
            String trimmedEntry = drawAPI.trimStringToWidth(getDisplayString(this.selected), this.width - 5);
            (this.entryDrawer == null ? defaultDrawer : this.entryDrawer).draw(this.selected, this.x + 5, this.y + this.height / 2 - 4, trimmedEntry);
        }

        if (this.enabled && !this.list.isEmpty()) {
            for(int i = 0; i <= 5; ++i) {
                drawAPI.drawRect(this.x + this.width - 16 + i, this.y + this.height / 2 - 2 + i, this.x + this.width - 5 - i, this.y + this.height / 2 + 1 - 2 + i, Color.LIGHT_GRAY.getRGB());
            }
        }

        if (this.title != null) {
            drawAPI.drawString(drawAPI.trimStringToWidth(this.title, this.width), this.x, this.y - 13);
        }

        if (this.open) {
            this.drawMenuDirect(this.x, this.y, mouseX, mouseY);
        }

        if ((this.hoverSelected != null && prevHover == null || prevHover != null && !prevHover.equals(this.hoverSelected)) && this.hoverCallback != null) {
            this.hoverCallback.accept(this.hoverSelected);
        }

    }

    public void drawMenuDirect(int x, int y, int mouseX, int mouseY) {
        int entryHeight = 13;
        int maxPointY = y + this.height + 2 + 13 * this.list.size();
        boolean buildUp = maxPointY > this.maxY;
        int yPositionList = buildUp ? y - 13 - 1 : y + this.height + 1;
        if (this.scrollbar != null) {
            yPositionList = (int)((double)yPositionList + this.scrollbar.getScrollY());
        }

        for(Iterator var9 = this.list.iterator(); var9.hasNext(); yPositionList += buildUp ? -entryHeight : entryHeight) {
            T option = (T) var9.next();
            if (this.scrollbar == null || yPositionList > y + 8 && yPositionList + entryHeight < this.scrollbar.getPosBottom() + 2) {
                boolean hover = mouseX > x && mouseX < x + this.width && mouseY > yPositionList && mouseY < yPositionList + entryHeight;
                if (hover) {
                    this.hoverSelected = option;
                }

                DrawAPI drawAPI = DrawAPI.getAPI();
                drawAPI.drawRect(x - 1, yPositionList, x + this.width + 1, yPositionList + entryHeight, ColorUtils.toRGB(0, 30, 70, 250));
                drawAPI.drawRect(x, yPositionList + (buildUp ? 1 : 0), x + this.width, yPositionList + entryHeight - 1 + (buildUp ? 1 : 0), hover ? ColorUtils.toRGB(55, 55, 155, 215) : ColorUtils.toRGB(0, 10, 10, 250));
                String trimmedEntry = drawAPI.trimStringToWidth(getDisplayString(option), this.width - 5);
                (this.entryDrawer == null ? defaultDrawer : this.entryDrawer).draw(option, x + 5, yPositionList + 3, trimmedEntry);
            }
        }

        if (this.scrollbar != null) {
            this.scrollbar.draw();
        }

    }

    public void clear() {
        this.open = false;
        this.selected = null;
        this.list.clear();
        this.setSelected(null);
    }

    public void remove(T type) {
        this.list.remove(type);
    }

    public void addOption(T option) {
        this.list.add(option);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getSelected() {
        return this.selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public T getHoverSelected() {
        return this.hoverSelected;
    }

    public void setHoverSelected(T hoverSelected) {
        this.hoverSelected = hoverSelected;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public DropDownEntryDrawer getEntryDrawer() {
        return this.entryDrawer;
    }

    public void setEntryDrawer(DropDownEntryDrawer entryDrawer) {
        this.entryDrawer = entryDrawer;
    }

    public void setHoverCallback(Consumer<T> hoverCallback) {
        this.hoverCallback = hoverCallback;
    }

    public interface DropDownEntryDrawer {
        void draw(Object var1, int var2, int var3, String var4);
    }

    private String getDisplayString(T entry) {
        try {
            Method method = entry.getClass().getDeclaredMethod("getDisplayName", null);
            if(method == null)
                return entry.toString();
            method.setAccessible(true);
            return (String) method.invoke(entry);
        } catch (NoSuchMethodException e) {
            return entry.toString();
        } catch (InvocationTargetException e) {
            return entry.toString();
        } catch (IllegalAccessException e) {
            return entry.toString();
        }
    }
}
