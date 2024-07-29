package ml.volder.unikapi.utils;

import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;

import java.util.HashMap;

public class LoadTimer {

    private static final HashMap<String, Long> timers = new HashMap<>();

    long startTimestamp;

    public static void start(String loadTaskName) {
        timers.put(loadTaskName, System.currentTimeMillis());
    }

    private static long getDuration(String loadTaskName) {
        if(!timers.containsKey(loadTaskName)) return 0;
        return System.currentTimeMillis() - timers.get(loadTaskName);
    }

    private static String getDurationString(String loadTaskName) {
        return getDuration(loadTaskName) + " millis";
    }

    public static String finishLoadingTask(String loadTaskName) {
        String ret = getDurationString(loadTaskName);
        timers.remove(loadTaskName);
        return ret;
    }

    public static void finishLoadingTaskAndLog(String loadTaskName) {
        UnikAPI.LOGGER.log(Logger.LOG_LEVEL.INFO, "Finished loading " + loadTaskName + " loaded in " + finishLoadingTask(loadTaskName));
    }

    public static void assureFinished(String loadTaskName) {
        timers.remove(loadTaskName);
    }

}
