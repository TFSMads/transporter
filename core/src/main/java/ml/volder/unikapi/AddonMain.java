package ml.volder.unikapi;

import java.util.ArrayList;
import java.util.List;

public abstract class AddonMain {

    public static void callOpenSettings(Object callerInstance) {
        instances.forEach(addonMain -> addonMain.openSettings(callerInstance));
    }

    private static List<AddonMain> instances = new ArrayList<>();

    public AddonMain() {
        instances.add(this);
    }
    public abstract void openSettings(Object callerInstance);
}
