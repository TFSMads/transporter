package ml.volder.transporter.modules.signtoolsmodule;

import ml.volder.transporter.gui.ModTextures;
import ml.volder.unikapi.api.draw.DrawAPI;
import ml.volder.unikapi.types.ModColor;

public class SignInfoRenderer {
    private String title;
    private String text;

    private String[] signLines;

    public SignInfoRenderer(String title, String[] signLines) {
        if(signLines.length == 4){
            this.signLines = signLines;
        }else{
            this.signLines = new String[] {"Line 1", "Line 2", "Line 3", "Line 4"};
        }
        this.init(title);
    }

    public void init(String title) {
        this.title = title;
        this.text = "Klik for at indlæse.";
    }



    public void drawEntry(int x, int y, int listWidth, int mouseX, int mouseY) {
        int textOffset = 24*4;
        DrawAPI drawAPI = DrawAPI.getAPI();

        drawAPI.drawString(text, x + textOffset + 3, y + 1,0xFFFFFF, 1.5D);
        drawAPI.drawString(text,x + textOffset + 3, y + 14,0x808080, 1.1D);
        drawSign(x,y);
    }

    private void renderLine(String text, int lineNumber, int xSignCorner, int ySignCorner, boolean isSelected) {
        int xCenter = xSignCorner + 24*2;
        int y = ySignCorner + 4 + (10)*(lineNumber-1);
        DrawAPI.getAPI().drawString(text, xCenter - DrawAPI.getAPI().getStringWidth(text)/2, y, ModColor.BLACK.getColor().getRGB());
    }

    private void drawSign(int xSignCorner, int ySignCorner) {
        DrawAPI drawAPI = DrawAPI.getAPI();
        drawAPI.bindTexture(ModTextures.SIGN);
        drawAPI.drawTexture(xSignCorner, ySignCorner, 256D, 256D, 24*4, 12*4);
        renderLine(signLines[0], 1, xSignCorner, ySignCorner, false);
        renderLine(signLines[1], 2, xSignCorner, ySignCorner, false);
        renderLine(signLines[2], 3, xSignCorner, ySignCorner, false);
        renderLine(signLines[3], 4, xSignCorner, ySignCorner, false);
    }
}
