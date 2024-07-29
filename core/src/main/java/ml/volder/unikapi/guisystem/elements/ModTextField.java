package ml.volder.unikapi.guisystem.elements;


import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.api.input.InputAPI;
import ml.volder.unikapi.api.minecraft.MinecraftAPI;
import ml.volder.unikapi.keysystem.Key;
import ml.volder.unikapi.keysystem.MouseButton;
import ml.volder.unikapi.types.ModColor;
import ml.volder.unikapi.utils.MathUtils;
import ml.volder.unikapi.utils.StringUtils;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.function.Predicate;

public class ModTextField {
    private final int id;
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    private boolean canLoseFocus = true;
    private boolean isFocused;
    private boolean isEnabled = true;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;
    private boolean visible = true;
    private Predicate<String> predicate = s -> true;
    private boolean blackBox = true;
    private boolean modPasswordBox = false;
    private String modBlacklistWord = "";
    private boolean colorBarEnabled = false;
    private ModColor hoveredModColor = null;
    private String colorAtCursor = null;
    private String placeHolder;
    private boolean backgroundColor = false;

    public ModTextField(int componentId, int x, int y, int par5Width, int par6Height) {
        this.id = componentId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
    }

    private void setClipboardString(String copyText)
    {
        if (!StringUtils.isEmpty(copyText))
        {
            try
            {
                StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner)null);
            }
            catch (Exception ignored) {}
        }
    }

    private String getClipboardString()
    {
        try
        {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);

            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception ignored) {}

        return "";
    }

    public void setBackgroundColor(boolean backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isBackgroundColor() {
        return this.backgroundColor;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void setText(String p_146180_1_) {
        if (this.predicate.test(p_146180_1_)) {
            if (p_146180_1_.length() > this.maxStringLength) {
                this.text = p_146180_1_.substring(0, this.maxStringLength);
            } else {
                this.text = p_146180_1_;
            }

            this.setCursorPosition(0);
        }

    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getPlaceHolder() {
        return this.placeHolder;
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }

    public void func_175205_a(Predicate<String> p_175205_1_) {
        this.predicate = p_175205_1_;
    }

    public void writeText(String p_146191_1_) {
        String s = "";
        String s1 = MinecraftAPI.getAPI().filterAllowedCharacters(p_146191_1_);
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - (i - j);
        if (this.text.length() > 0) {
            s = s + this.text.substring(0, i);
        }

        int l;
        if (k < s1.length()) {
            s = s + s1.substring(0, k);
            l = k;
        } else {
            s = s + s1;
            l = s1.length();
        }

        if (this.text.length() > 0 && j < this.text.length()) {
            s = s + this.text.substring(j);
        }

        if (this.predicate.test(s)) {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);
        }

    }

    public void deleteWords(int p_146177_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
        }

    }

    public void deleteFromCursor(int p_146175_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean flag = p_146175_1_ < 0;
                int i = flag ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
                int j = flag ? this.cursorPosition : this.cursorPosition + p_146175_1_;
                String s = "";
                if (i >= 0) {
                    s = this.text.substring(0, i);
                }

                if (j < this.text.length()) {
                    s = s + this.text.substring(j);
                }

                if (this.predicate.test(s)) {
                    this.text = s;
                    if (flag) {
                        this.moveCursorBy(p_146175_1_);
                    }
                }
            }
        }

    }

    public int getId() {
        return this.id;
    }

    public int getNthWordFromCursor(int p_146187_1_) {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }

    public int getNthWordFromPos(int p_146183_1_, int p_146183_2_) {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
        int i = p_146197_2_;
        boolean flag = p_146197_1_ < 0;
        int j = Math.abs(p_146197_1_);

        for(int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while(p_146197_3_ && i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while(p_146197_3_ && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }

                while(i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    public void moveCursorBy(int p_146182_1_) {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }

    public void setCursorPosition(int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        int i = this.text.length();
        this.cursorPosition = MathUtils.clamp_int(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean textboxKeyTyped(char p_146201_1_, Key key) {
        if (!this.isFocused) {
            return false;

        } else if (key.equals(Key.A) && InputAPI.getAPI().isCtrlKeyDown() && !InputAPI.getAPI().isShiftKeyDown() && !InputAPI.getAPI().isAltKeyDown()) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (key.equals(Key.C) && InputAPI.getAPI().isCtrlKeyDown() && !InputAPI.getAPI().isShiftKeyDown() && !InputAPI.getAPI().isAltKeyDown()) {
            if (!this.isPasswordBox()) {
                setClipboardString(this.getSelectedText());
            }

            return true;
        } else if (key.equals(Key.V) && InputAPI.getAPI().isCtrlKeyDown() && !InputAPI.getAPI().isShiftKeyDown() && !InputAPI.getAPI().isAltKeyDown()) {
            if (this.isEnabled) {
                this.writeText(getClipboardString());
            }

            return true;
        } else if (key.equals(Key.X) && InputAPI.getAPI().isCtrlKeyDown() && !InputAPI.getAPI().isShiftKeyDown() && !InputAPI.getAPI().isAltKeyDown()) {
            if (!this.isPasswordBox()) {
                setClipboardString(this.getSelectedText());
            }

            if (this.isEnabled) {
                this.writeText("");
            }

            return true;
        } else {
            if(key.equals(Key.BACK)) {
                if (InputAPI.getAPI().isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(-1);
                    }
                } else if (this.isEnabled) {
                    this.deleteFromCursor(-1);
                }

                return true;
            }else if(key.equals(Key.HOME)) {
                if (InputAPI.getAPI().isShiftKeyDown()) {
                    this.setSelectionPos(0);
                } else {
                    this.setCursorPositionZero();
                }

                return true;
            }else if(key.equals(Key.ARROW_LEFT)) {
                if (InputAPI.getAPI().isShiftKeyDown()) {
                    if (InputAPI.getAPI().isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() - 1);
                    }
                } else if (InputAPI.getAPI().isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(-1));
                } else {
                    this.moveCursorBy(-1);
                }

                return true;
            }else if(key.equals(Key.ARROW_RIGHT)) {
                if (InputAPI.getAPI().isShiftKeyDown()) {
                    if (InputAPI.getAPI().isCtrlKeyDown()) {
                        this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                    } else {
                        this.setSelectionPos(this.getSelectionEnd() + 1);
                    }
                } else if (InputAPI.getAPI().isCtrlKeyDown()) {
                    this.setCursorPosition(this.getNthWordFromCursor(1));
                } else {
                    this.moveCursorBy(1);
                }

                return true;
            }else if(key.equals(Key.DELETE)) {
                if (InputAPI.getAPI().isCtrlKeyDown()) {
                    if (this.isEnabled) {
                        this.deleteWords(1);
                    }
                } else if (this.isEnabled) {
                    this.deleteFromCursor(1);
                }

                return true;
            } else {
                if (MinecraftAPI.getAPI().isAllowedCharacter(p_146201_1_)) {
                    if (this.isEnabled) {
                        this.writeText(Character.toString(p_146201_1_));
                    }

                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public boolean mouseClicked(int p_146192_1_, int p_146192_2_, MouseButton mouseButton) {
        boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;
        if (this.colorBarEnabled && this.hoveredModColor != null) {
            this.writeText("&" + this.hoveredModColor.getColorChar());
            return true;
        } else {
            if (this.canLoseFocus) {
                this.setFocused(flag);
            }

            if (this.isFocused && flag && mouseButton.isLeft()) {
                int i = p_146192_1_ - this.xPosition;
                if (this.enableBackgroundDrawing) {
                    i -= 4;
                }

                DrawAPI drawAPI = DrawAPI.getAPI();

                String s = drawAPI.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
                this.setCursorPosition(drawAPI.trimStringToWidth(s, i).length() + this.lineScrollOffset);
            }

            return this.isFocused;
        }
    }

    public void drawTextBox() {
        if (this.getVisible()) {
            if (!this.getBlacklistWord().isEmpty() && this.getText().contains(this.getBlacklistWord())) {
                this.setText(this.getText().replace(this.getBlacklistWord(), ""));
            }
            DrawAPI drawAPI = DrawAPI.getAPI();

            if (this.getEnableBackgroundDrawing()) {
                if (this.blackBox) {
                    drawAPI.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
                    drawAPI.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
                } else if (this.isFocused) {
                    drawAPI.drawRectBorder((double)(this.xPosition - 1), (double)(this.yPosition - 1), (double)(this.xPosition + this.width + 1), (double)(this.yPosition + this.height + 1), ModColor.toRGB(220, 220, 225, 62), 1.0D);
                    drawAPI.drawRectangle(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ModColor.toRGB(0, 0, 3, 180));
                } else {
                    drawAPI.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ModColor.toRGB(70, 60, 53, 122));
                    drawAPI.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1, ModColor.toRGB(0, 0, 3, 180));
                }
            }

            int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String theText = this.getText().substring(this.lineScrollOffset);
            if (this.isPasswordBox()) {
                theText = theText.replaceAll(".", "*");
            }

            String s = drawAPI.trimStringToWidth(theText, this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
            int j1 = l;
            this.colorAtCursor = null;
            if (k > s.length()) {
                k = s.length();
            }

            if (s.length() > 0) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = l + drawAPI.getStringWidth(this.visualColorForText(s1, true));
            }

            boolean placeHolder = this.placeHolder != null && this.getText().isEmpty() && !this.isFocused;
            boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k1 = j1;
            if (!flag) {
                k1 = j > 0 ? l + this.width : l;
            } else if (flag2) {
                k1 = j1 - 1;
                --j1;
            }

            if (s.length() > 0 && flag && j < s.length()) {
                int var10000 = j1 + drawAPI.getStringWidth(this.visualColorForText(s.substring(j), false));
            }

            drawAPI.drawStringWithShadow(this.visualColorForText(s, false), (float)l, (float)i1, i);
            if (flag1 && !placeHolder) {
                if (flag2) {
                    drawAPI.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + drawAPI.getFontHeight(), -3092272);
                } else {
                    drawAPI.drawStringWithShadow("_", (float)k1, (float)i1, i);
                }
            }

            if (k != j) {
                int l1 = l + drawAPI.getStringWidth(s.substring(0, k));
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + drawAPI.getFontHeight());
            }

            if (placeHolder) {
                drawAPI.drawString(this.placeHolder, k1, i1, Color.LIGHT_GRAY.getRGB());
            }
        }

    }

    public void drawColorBar(int mouseX, int mouseY) {
        if (this.colorBarEnabled) {
            this.hoveredModColor = null;
            int ll = 9;
            int pX = this.xPosition + this.width / 2 - (ModColor.values().length * ll - ll) / 2;
            int pY = this.yPosition + this.height + 5;
            ModColor[] var6 = ModColor.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                ModColor color = var6[var8];
                boolean hovered = mouseX > pX - ll / 2 && mouseX < pX + ll / 2 && mouseY > pY - 1 && mouseY < pY + 9;
                if (hovered) {
                    this.hoveredModColor = color;
                }

                if (this.colorAtCursor != null && this.colorAtCursor.equals("" + color.getColorChar())) {
                    hovered = true;
                }

                DrawAPI drawAPI = DrawAPI.getAPI();

                drawAPI.drawRect(pX - ll / 2, pY - 1, pX + ll / 2, pY + 9, !hovered ? ModColor.toRGB(120, 120, 120, 120) : 2147483647);
                drawAPI.drawCenteredString(color.toString() + color.getColorChar(), (double)pX, (double)pY);
                pX += ll;
            }
        }

    }

    private String visualColorForText(String text, boolean saveCursorColor) {
        String coloredString = "";
        boolean foundColor = false;

        for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '&' && i != text.length() - 1) {
                if (foundColor) {
                    coloredString = coloredString + "&";
                }

                foundColor = true;
            } else {
                if (foundColor) {
                    foundColor = false;
                    coloredString = coloredString + "§" + c + '&';
                    if (saveCursorColor) {
                        this.colorAtCursor = "" + c;
                    }
                }

                coloredString = coloredString + c;
            }
        }

        return coloredString;
    }

    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        int j;
        if (p_146188_1_ < p_146188_3_) {
            j = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = j;
        }

        if (p_146188_2_ < p_146188_4_) {
            j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }

        if (p_146188_3_ > this.xPosition + this.width) {
            p_146188_3_ = this.xPosition + this.width;
        }

        if (p_146188_1_ > this.xPosition + this.width) {
            p_146188_1_ = this.xPosition + this.width;
        }
        DrawAPI drawAPI = DrawAPI.getAPI();
        //TODO make it look more like minecraft chat selected char
        drawAPI.drawRectangle(p_146188_1_, p_146188_2_, p_146188_3_, p_146188_4_, new Color(0, 0, 255, 50).getRGB());
    }

    public void setMaxStringLength(int p_146203_1_) {
        this.maxStringLength = p_146203_1_;
        if (this.text.length() > p_146203_1_) {
            this.text = this.text.substring(0, p_146203_1_);
        }

    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public void setBlacklistWord(String modBlacklistWords) {
        this.modBlacklistWord = modBlacklistWords;
    }

    public String getBlacklistWord() {
        return this.modBlacklistWord;
    }

    public void setPasswordBox(boolean modPasswordBox) {
        this.modPasswordBox = modPasswordBox;
    }

    public boolean isPasswordBox() {
        return this.modPasswordBox;
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    public void setEnableBackgroundDrawing(boolean p_146185_1_) {
        this.enableBackgroundDrawing = p_146185_1_;
    }

    public boolean isBlackBox() {
        return this.blackBox;
    }

    public void setBlackBox(boolean blackBox) {
        this.blackBox = blackBox;
    }

    public void setTextColor(int p_146193_1_) {
        this.enabledColor = p_146193_1_;
    }

    public void setDisabledTextColour(int p_146204_1_) {
        this.disabledColor = p_146204_1_;
    }

    public void setFocused(boolean p_146195_1_) {
        if (p_146195_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }

        this.isFocused = p_146195_1_;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public void setEnabled(boolean p_146184_1_) {
        this.isEnabled = p_146184_1_;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    public void setSelectionPos(int p_146199_1_) {
        int i = this.text.length();
        if (p_146199_1_ > i) {
            p_146199_1_ = i;
        }

        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }

        this.selectionEnd = p_146199_1_;

        if (this.lineScrollOffset > i) {
            this.lineScrollOffset = i;
        }

        DrawAPI drawAPI = DrawAPI.getAPI();

        int j = this.getWidth();
        String s = drawAPI.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
        int k = s.length() + this.lineScrollOffset;
        if (p_146199_1_ == this.lineScrollOffset) {
            this.lineScrollOffset -= drawAPI.trimStringToWidth(this.text, j, true).length();
        }

        if (p_146199_1_ > k) {
            this.lineScrollOffset += p_146199_1_ - k;
        } else if (p_146199_1_ <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
        }

        this.lineScrollOffset = MathUtils.clamp_int(this.lineScrollOffset, 0, i);


    }

    public void setCanLoseFocus(boolean p_146205_1_) {
        this.canLoseFocus = p_146205_1_;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean p_146189_1_) {
        this.visible = p_146189_1_;
    }

    public boolean isColorBarEnabled() {
        return this.colorBarEnabled;
    }

    public void setColorBarEnabled(boolean colorBar) {
        this.colorBarEnabled = colorBar;
    }
}
