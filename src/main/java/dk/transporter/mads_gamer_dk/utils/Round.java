package dk.transporter.mads_gamer_dk.utils;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;



public class Round {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
