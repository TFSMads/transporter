package ml.volder.transporter.utils;

public class ArchDetect {
    public static boolean isIntel() {
        // x86, x86_64
        return System.getProperty("os.arch").startsWith("x86");
    }

    public static boolean isAMD() {
        // amd, amd64
        return System.getProperty("os.arch").startsWith("amd");
    }

    public static boolean isARM() {
        // armv4, armv4t, armv5t, armv5te, armv5tej, armv6, armv7
        return System.getProperty("os.arch").startsWith("arm");
    }

    public static boolean isMIPS() {
        // mips, mips64
        return System.getProperty("os.arch").startsWith("mips");
    }
}
