package ml.volder.transporter.modules.mcmmomodule;

public interface McmmoSkill {
    void updateLevel(int level);
    int getLevel();
    String getId();

    default void onChatMessageReceive(String clean) {}
    default void registerModules(Object category) {}

    default void registerModules(Object category, Object powerupCategory) {
        registerModules(category);
    }

}
