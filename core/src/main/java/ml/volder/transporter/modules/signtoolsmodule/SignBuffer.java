package ml.volder.transporter.modules.signtoolsmodule;

public class SignBuffer {
    private String bufferTextLine1 = "";
    private String bufferTextLine2 = "";
    private String bufferTextLine3 = "";
    private String bufferTextLine4 = "";

    public SignBuffer(String line1, String line2, String line3, String line4) {
        bufferTextLine1 = line1;
        bufferTextLine2 = line2;
        bufferTextLine3 = line3;
        bufferTextLine4 = line4;
    }

    public String getLine(int selected) {
        if(selected == 1){
            return bufferTextLine1;
        }else if(selected == 2){
            return bufferTextLine2;
        }else if(selected == 3){
            return bufferTextLine3;
        }else if(selected == 4){
            return bufferTextLine4;
        }
        return bufferTextLine1;
    }

    public void setLine(int selected, String value) {
        if(selected == 1){
            bufferTextLine1 = value;
        }else if(selected == 2){
            bufferTextLine2 = value;
        }else if(selected == 3){
            bufferTextLine3 = value;
        }else if(selected == 4){
            bufferTextLine4 = value;
        }else {
            bufferTextLine1 = value;
        }
    }

    public void copy(SignBuffer bufferToCopy) {
        setLine(1, bufferToCopy.getLine(1));
        setLine(2, bufferToCopy.getLine(2));
        setLine(3, bufferToCopy.getLine(3));
        setLine(4, bufferToCopy.getLine(4));
    }
}
