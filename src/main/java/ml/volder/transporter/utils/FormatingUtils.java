package ml.volder.transporter.utils;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class FormatingUtils {

    public static FORMATTING_MODE formattingMode = FORMATTING_MODE.ENDELSE;
    private static final NavigableMap<Long, String> suffixes = new TreeMap();

    public FormatingUtils() {
    }

    public static String formatNumber(long value) {
        return formatNumber(value, formattingMode);
    }

    public static String formatNumber(long value, FORMATTING_MODE formattingMode) {
        if(formattingMode == FORMATTING_MODE.ENDELSE) {
            return formatEnding(value);
        } else if (formattingMode == FORMATTING_MODE.PUNKTUM) {
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.GERMAN);
            return df.format(value);
        }
        return String.valueOf(value);
    }

    private static String formatEnding(long value) {
        if (value == Long.MIN_VALUE) {
            return formatNumber(-9223372036854775807L);
        } else if (value < 0L) {
            return "-" + formatNumber(-value);
        } else if (value < 1000L) {
            return Long.toString(value);
        } else {
            Map.Entry<Long, String> e = suffixes.floorEntry(value);
            Long divideBy = (Long)e.getKey();
            String suffix = (String)e.getValue();
            long truncated = value / (divideBy / 10L);
            boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
            return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10L + suffix;
        }
    }

    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "G");
        suffixes.put(1000000000000L, "T");
        suffixes.put(1000000000000000L, "P");
        suffixes.put(1000000000000000000L, "E");
    }

    public enum FORMATTING_MODE {
        INGEN("Ingen"),
        PUNKTUM("Punktum"),
        ENDELSE("Endelse");

        private String displayName;

        FORMATTING_MODE(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName(){
            return this.displayName;
        }
    }
}