package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;

import java.awt.*;

public class Scrollbar {
    private int listSize;
    private double entryHeight;
    private double scrollY;
    private double barLength;
    private double backLength;
    private int posTop;
    private int posBottom;
    private double left;
    private double top;
    private double right;
    private int speed = 10;
    private double clickY;
    private boolean hold;
    private boolean requestBottom;
    private int spaceBelow = 0;

    public void reset() {
        this.scrollY = 0.0D;
    }

    public void init() {
        this.mouseInput();
    }

    public Scrollbar(int entryHeight) {
        this.entryHeight = (double)entryHeight;
        this.setDefaultPosition();
    }

    public void update(int listSize) {
        if (this.listSize != listSize) {
            this.listSize = listSize;
            if (this.requestBottom) {
                this.scrollY = -2.147483648E9D;
                this.requestBottom = false;
                this.checkOutOfBorders();
            }

        }
    }

    public void setPosition(int left, int top, int right, int bottom) {
        this.left = (double)left;
        this.posTop = top;
        this.right = (double)right;
        this.posBottom = bottom;
        this.calc();
    }

    public void calc() {
        double totalPixels = (double)this.listSize * this.entryHeight + (double)this.spaceBelow;
        double backLength = (double)(this.posBottom - this.posTop);
        if (!(backLength >= totalPixels)) {
            double scale = backLength / totalPixels;
            double barLength = scale * backLength;
            double scroll = this.scrollY / scale * scale * scale;
            this.top = -scroll + (double)this.posTop;
            this.barLength = barLength;
            this.backLength = backLength;
        }
    }

    public void setDefaultPosition() {
        this.setPosition(DrawAPI.getAPI().getScaledWidth()  / 2 + 150, 40, DrawAPI.getAPI().getScaledWidth() / 2 + 156, DrawAPI.getAPI().getScaledHeight() - 40);
    }

    public boolean isHidden() {
        if (this.listSize == 0) {
            return true;
        } else {
            return (double)(this.posBottom - this.posTop) >= (double)this.listSize * this.entryHeight + (double)this.spaceBelow;
        }
    }

    public void draw(int mouseX, int mouseY) {
        this.mouseAction(mouseX, mouseY, EnumMouseAction.DRAGGING);
        this.draw();
    }

    public void draw() {
        this.checkOutOfBorders();
        if (!this.isHidden()) {
            this.calc();
            DrawAPI drawAPI = DrawAPI.getAPI();
            //Background rect Black
            drawAPI.drawRectangle((int) this.left, this.posTop, (int) this.right, this.posBottom, new Color(0,0,0,255).getRGB());
            //Shadow rect Dark Gray
            drawAPI.drawRectangle((int) this.left, (int) (this.top + this.barLength), (int) this.right, (int) this.top, new Color(128,128,128,255).getRGB());
            //Slider rect Gray
            drawAPI.drawRectangle((int) this.left, (int) (this.top + this.barLength - 1), (int) this.right - 1, (int) this.top, new Color(192,192,192,255).getRGB());
        }
    }

    public boolean isHoverSlider(int mouseX, int mouseY) {
        return (double)mouseX < this.right && (double)mouseX > this.left && (double)mouseY > this.top && (double)mouseY < this.top + this.barLength;
    }

    public boolean isHoverTotalScrollbar(int mouseX, int mouseY) {
        return (double)mouseX < this.right && (double)mouseX > this.left && mouseY > this.posTop && mouseY < this.posBottom;
    }

    public void mouseAction(int mouseX, int mouseY, EnumMouseAction mouseAction) {
        this.calc();
        double scale = this.backLength / ((double)this.listSize * this.entryHeight + (double)this.spaceBelow);
        double value = (double)((int)((double)(-mouseY) / scale));
        switch(mouseAction) {
            case CLICKED:
                if (this.hold) {
                    this.hold = false;
                } else if (this.isHoverSlider(mouseX, mouseY)) {
                    this.hold = true;
                    this.clickY = value - this.scrollY;
                }
                break;
            case DRAGGING:
                if (this.hold) {
                    this.scrollY = value - this.clickY;
                }
                break;
            case RELEASED:
                this.hold = false;
        }

        this.checkOutOfBorders();
    }

    public void mouseInput() {
        int wheel = InputAPI.getAPI().getEventDWheel();
        if (wheel > 0) {
            this.scrollY += (double)this.speed;
        } else if (wheel < 0) {
            this.scrollY -= (double)this.speed;
        }

        if (wheel != 0) {
            this.checkOutOfBorders();
        }

    }

    public void checkOutOfBorders() {
        if ((double)this.listSize * this.entryHeight + (double)this.spaceBelow + this.scrollY < (double)(this.posBottom - this.posTop)) {
            this.scrollY += (double)(this.posBottom - this.posTop) - ((double)this.listSize * this.entryHeight + (double)this.spaceBelow + this.scrollY);
        }

        if (this.scrollY > 0.0D) {
            this.scrollY = 0.0D;
        }

    }

    public void setPosition(double left, double top, double right, double bottom) {
        this.setPosition((int)left, (int)top, (int)right, (int)bottom);
    }

    public void requestBottom() {
        this.requestBottom = true;
    }

    public void scrollTo(int index) {
        this.scrollY += (double)(this.posBottom - this.posTop) - ((double)index * this.entryHeight + (double)this.spaceBelow + this.scrollY) - (this.entryHeight + (double)this.spaceBelow);
        this.checkOutOfBorders();
    }

    public int getListSize() {
        return this.listSize;
    }

    public double getEntryHeight() {
        return this.entryHeight;
    }

    public double getScrollY() {
        return this.scrollY;
    }

    public double getBarLength() {
        return this.barLength;
    }

    public double getBackLength() {
        return this.backLength;
    }

    public int getPosTop() {
        return this.posTop;
    }

    public int getPosBottom() {
        return this.posBottom;
    }

    public double getLeft() {
        return this.left;
    }

    public double getTop() {
        return this.top;
    }

    public double getRight() {
        return this.right;
    }

    public int getSpeed() {
        return this.speed;
    }

    public double getClickY() {
        return this.clickY;
    }

    public boolean isHold() {
        return this.hold;
    }

    public boolean isRequestBottom() {
        return this.requestBottom;
    }

    public int getSpaceBelow() {
        return this.spaceBelow;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public void setEntryHeight(double entryHeight) {
        this.entryHeight = entryHeight;
    }

    public void setScrollY(double scrollY) {
        this.scrollY = scrollY;
    }

    public void setBarLength(double barLength) {
        this.barLength = barLength;
    }

    public void setBackLength(double backLength) {
        this.backLength = backLength;
    }

    public void setPosTop(int posTop) {
        this.posTop = posTop;
    }

    public void setPosBottom(int posBottom) {
        this.posBottom = posBottom;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setClickY(double clickY) {
        this.clickY = clickY;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public void setRequestBottom(boolean requestBottom) {
        this.requestBottom = requestBottom;
    }

    public void setSpaceBelow(int spaceBelow) {
        this.spaceBelow = spaceBelow;
    }

    public static enum EnumMouseAction {
        CLICKED,
        RELEASED,
        DRAGGING;

        private EnumMouseAction() {
        }
    }
}
