package ml.volder.unikapi.utils;

public class MathUtils {
    public static int clamp_int(int num, int min, int max)
    {
        return num < min ? min : (num > max ? max : num);
    }
}
