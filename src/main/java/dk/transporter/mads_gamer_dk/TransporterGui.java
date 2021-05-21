package dk.transporter.mads_gamer_dk;

import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;

public class TransporterGui {


    private Scrollbar scrollbar;
    private TransporterAddon addon;
    private Boolean interacted;


    public static void draw() {
        System.out.println("OPEN GUI");
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(new Scrollbar(18).getScrollY());
    }




}
