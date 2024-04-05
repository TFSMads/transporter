package ml.volder.unikapi.guisystem.elements;



import java.util.*;

public class Settings {
    private List<SettingsElement> elements = new ArrayList();

    public Settings() {
    }

    public Settings(SettingsElement... element) {
        this.elements.addAll(Arrays.asList(element));
    }

    public List<SettingsElement> getElements() {
        return this.elements;
    }

    public Settings add(SettingsElement settingsElement) {
        this.elements.add(settingsElement);
        return this;
    }

    public Settings addAll(ArrayList<SettingsElement> settingsElements) {
        this.elements.addAll(settingsElements);
        this.sort();
        return this;
    }

    private void sort() {
        Collections.sort(this.elements, new Comparator<SettingsElement>() {
            public int compare(SettingsElement e1, SettingsElement e2) {
                return e1.getSortingId() - e2.getSortingId();
            }
        });
    }
}
