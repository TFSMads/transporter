package ml.volder.transporter.modules;

import ml.volder.transporter.settings.accesors.SettingRegistryAccessor;
import ml.volder.transporter.settings.widgets.TransporterModuleWidget;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.logger.Logger;
import ml.volder.unikapi.utils.LoadTimer;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.AbstractSetting;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ModuleManager {

    private List<TransporterModuleWidget> transporterModuleWidgetList = new ArrayList<>();

    public List<TransporterModuleWidget> getTransporterModuleWidgetList() {
        return transporterModuleWidgetList;
    }

    private Map<Class<? extends SimpleModule>, ModuleInfo> registeredModules = new HashMap<>();
    private Map<Class<? extends SimpleModule>, SimpleModule> loadedModules = new HashMap<>();

    private Map<Class<? extends SimpleModule>, ModuleInfo> getLoadOrderedModulesMap(Collection<ModuleInfo> modules) {
        // Create a map to represent the graph of modules and their dependencies
        Map<Class<? extends SimpleModule>, List<Class<? extends SimpleModule>>> graph = new HashMap<>();
        for (ModuleInfo module : modules) {
            graph.put(module.klass, module.dependsOnList);
        }

        // Initialize visited and recursion stacks for DFS
        Map<Class<? extends SimpleModule>, Boolean> visited = new HashMap<>();
        for (ModuleInfo module : modules) {
            visited.put(module.klass, false);
        }
        Stack<Class<? extends SimpleModule>> stack = new Stack<>();

        // Perform DFS to do topological sorting
        for (ModuleInfo module : modules) {
            if (!visited.get(module.klass)) {
                topologicalSortUtil(module.klass, graph, visited, stack);
            }
        }

        // Create a list in the order determined by topological sorting
        Map<Class<? extends SimpleModule>, ModuleInfo> sortedModules = new HashMap<>();
        while (!stack.isEmpty()) {
            Class<? extends SimpleModule> moduleClass = stack.pop();
            for (ModuleInfo module : modules) {
                if (module.klass == moduleClass) {
                    sortedModules.put(module.klass, module);
                    break;
                }
            }
        }

        return sortedModules;
    }

    private static void topologicalSortUtil(Class<? extends SimpleModule> moduleClass,
                                            Map<Class<? extends SimpleModule>, List<Class<? extends SimpleModule>>> graph,
                                            Map<Class<? extends SimpleModule>, Boolean> visited,
                                            Stack<Class<? extends SimpleModule>> stack) {
        visited.put(moduleClass, true);
        List<Class<? extends SimpleModule>> dependencies = graph.get(moduleClass);
        if (dependencies != null) {
            for (Class<? extends SimpleModule> dependency : dependencies) {
                if (!visited.get(dependency)) {
                    topologicalSortUtil(dependency, graph, visited, stack);
                }
            }
        }
        stack.push(moduleClass);
    }

    private boolean isSupported(Class<? extends SimpleModule> klass) {
        return true;
    }
    public void initModules() {
        LoadTimer.start("initModules");
        getLoadOrderedModulesMap(registeredModules.values()).forEach((klass, moduleInfo) -> {
            if(isSupported(klass)) {
                try {
                    LoadTimer.start(moduleInfo.name + "-init");
                    loadedModules.put(klass, klass.getDeclaredConstructor(ModuleInfo.class).newInstance(moduleInfo).init());
                    UnikAPI.LOGGER.finest("Initialized module: " + moduleInfo.displayName + " (" + moduleInfo.name + ") in " + LoadTimer.finishLoadingTask(moduleInfo.name + "-init"));
                } catch (Exception e) {
                    UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.WARNING, e);
                    UnikAPI.LOGGER.warning("Kunne ikke init modulet: " + moduleInfo.displayName + " (" + moduleInfo.name + ")");
                } finally {
                    LoadTimer.assureFinished(moduleInfo.name + "-init");
                }
            }
        });
        UnikAPI.LOGGER.finest("Initialized " + loadedModules.size() + " modules successfully in " + LoadTimer.finishLoadingTask("initModules") + "!");
    }

    public void enableModules() {
        LoadTimer.start("enableModules");
        loadedModules.forEach((klass, module) -> {
            try {
                LoadTimer.start(module.getModuleName() + "-enable");
                module.loadConfig();
                module.enable();
                TransporterModuleWidget moduleWidget = module.getModuleWidget();
                transporterModuleWidgetList.add(moduleWidget);
                try {
                    module.fillSettings(new SettingRegistryAccessor() {
                        @Override
                        public void add(AbstractSetting setting) {
                            moduleWidget.addSubSetting(setting);
                        }

                        @Override
                        public void addAll(List<Setting> settings) {
                            moduleWidget.addSubSettings(settings);
                        }

                        @Override
                        public boolean reset() {
                            return moduleWidget.resetSubSettings();
                        }
                    });
                } catch (Exception e) {
                    UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.WARNING, e);
                    UnikAPI.LOGGER.warning("Kunne ikke tilføje settings til modulet: " + module.getDisplayName() + " (" + module.getModuleName() + ")");
                }

                UnikAPI.LOGGER.finest("Enabled module: " + module.getDisplayName() + " (" + module.getModuleName() + ")" + " in " + LoadTimer.finishLoadingTask(module.getModuleName() + "-enable"));
            } catch (Exception e) {
                UnikAPI.LOGGER.printStackTrace(Logger.LOG_LEVEL.WARNING, e);
                UnikAPI.LOGGER.warning("Kunne ikke load modulet: " + module.getDisplayName() + " (" + module.getModuleName() + ")");
            } finally {
                LoadTimer.assureFinished(module.getModuleName() + "-enable");
            }
        });
        UnikAPI.LOGGER.finest("Enabled " + loadedModules.size() + " modules successfully in " + LoadTimer.finishLoadingTask("enableModules") + "!");
    }

    public SimpleModule getModule(String name) {
        AtomicReference<SimpleModule> module = new AtomicReference<>(null);
        registeredModules.values().stream().filter((moduleInfo -> moduleInfo.name.equals(name))).findAny().ifPresent((moduleInfo -> module.set(getModule(moduleInfo.klass))));
        return module.get();
    }

    public <T> T getModule(Class<T> klass) {
        SimpleModule module = loadedModules.getOrDefault(klass, null);
        if(module.getClass().isAssignableFrom(klass))
            return (T) module;
        return null;
    }

    public ModuleInfo registerModule(String name, String displayName, String description, Class<? extends SimpleModule> klass) {
        ModuleInfo moduleInfo = new ModuleInfo(name, displayName, description, klass);
        registeredModules.put(klass, moduleInfo);
        UnikAPI.LOGGER.finest("Registered module: " + moduleInfo.displayName + " (" + moduleInfo.name + ")");
        return moduleInfo;
    }

    public void registerModules() {
        LoadTimer.start("registerModules");
        ModuleInfo moduleInfo = registerModule("autoGetModule", "Auto Get", "En feature til at automatisk tage ting fra din transporter.", AutoGetModule.class);
        moduleInfo.dependsOnList.add(ServerModule.class);
        moduleInfo = registerModule("autoTransporter", "Auto Transporter", "En feature til at putte item i din transporter for dig.", AutoTransporter.class);
        moduleInfo.dependsOnList.add(ServerModule.class);
        registerModule("serverSelector", "Server Selector", "En feature til at skifte nemt og hurtigt mellem server på sa.", ServerListModule.class);
        moduleInfo = registerModule("messageModule", "Beskeder", "En feature der benytter beskeder i chatten til at samle data omkring din transporter.", MessagesModule.class);
        moduleInfo.dependsOnList.add(ServerModule.class);
        moduleInfo = registerModule("mcmmoModule", "McMMO", "En feature der tilføjer moduler der viser nogen af dine McMMO stats!", McmmoModule.class);
        moduleInfo.dependsOnList.add(GuiModulesModule.class);
        moduleInfo = registerModule("serverTrackerModule", "Server Tracker", "En feature der tracker hvilken server du er forbundet til.", ServerModule.class);
        moduleInfo.dependsOnList.add(GuiModulesModule.class);
        registerModule("guiModule", "Gui Moduler", "En feature der kan vise relevant info omkring din transporter.", GuiModulesModule.class);
        moduleInfo = registerModule("transporterMenuModule", "Transporter Menu", "En feature til nemt at flytte items til og fra din transporter.", TransporterMenuModule.class);
        moduleInfo.dependsOnList.add(ServerModule.class);
        registerModule("signToolsModule", "Sign Tools", "En feature til at forbedre skilte så man kan kopiere og indsætte.", SignToolsModule.class);
        moduleInfo = registerModule("balanceTrackerModule", "Balance", "En feature der tracker hvor mange EMs du har.", BalanceModule.class);
        moduleInfo.dependsOnList.add(ServerModule.class);
        //registerModule("transporterStatsModule", "Transporter Stats", "En feature der tracker diverse stats.", TransporterStatsModule.class);
        UnikAPI.LOGGER.finest("Registered " + registeredModules.size() + " modules successfully in " + LoadTimer.finishLoadingTask("registerModules") + "!");
    }

    public Map<Class<? extends SimpleModule>, SimpleModule> getLoadedModules() {
        return loadedModules;
    }

    private static ModuleManager instance;
    public static synchronized ModuleManager getInstance()
    {
        if (instance == null)
            instance = new ModuleManager();
        return instance;
    }

    public static class ModuleInfo {
        public final String name;
        public final String displayName;
        public final String description;
        public final Class<? extends SimpleModule> klass;

        public final List<Class<? extends SimpleModule>> dependsOnList = new ArrayList<>();

        public ModuleInfo(String name, String displayName, String description, Class<? extends SimpleModule> klass) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.klass = klass;
        }
    }
}