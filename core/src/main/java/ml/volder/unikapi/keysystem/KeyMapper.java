package ml.volder.unikapi.keysystem;

import ml.volder.unikapi.api.ApiManager;
import ml.volder.unikapi.api.ApiProvider;
import ml.volder.unikapi.api.ApiReferenceStorage;
import ml.volder.unikapi.api.input.InputAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class KeyMapper {

    private static ApiProvider<KeyMapper> apiProvider = new ApiProvider<>("KeyMapper");

    public static KeyMapper getVersionedInstance() {
        return ApiManager.getAPI(apiProvider, "ml.volder.unikapi.keysystem.impl", ApiReferenceStorage::getVersionedKeyMapper, KeyMapper.class);
    }

    public static final char DEFAULT_CHAR = '\u0000';
    protected final Map<String, Key> keys = new HashMap<>();
    protected final Map<String, MouseButton> mouseButtons = new HashMap<>();

    public static void registerKey(@NotNull Key key) {
        getVersionedInstance().register(key);
    }

    public static @NotNull String getKeyName(@NotNull Key key) {
        return getVersionedInstance().getNameByKey(key);
    }

    public static @Nullable Key getKey(@NotNull String name) {
        return getVersionedInstance().getKeyByName(name);
    }

    public static @Nullable Key getKey(int keyId) {
        return getVersionedInstance().getKeyById(keyId);
    }

    public static @Nullable MouseButton getMouseButton(int mouseButtonId) {
        return getVersionedInstance().getMouseButtonById(mouseButtonId);
    }

    public static int getKeyId(@NotNull Key key) {
        if (key instanceof MouseButton) {
            return getVersionedInstance().getIdByMouseButton((MouseButton) key);
        }
        return getVersionedInstance().getIdByKey(key);
    }

    @Deprecated
    public static int getMouseButton(@NotNull MouseButton mouseButton) {
        return getVersionedInstance().getIdByMouseButton(mouseButton);
    }

    public static char getCharacter(@NotNull Key key) {
        return getVersionedInstance().getChar(key);
    }

    public static boolean isPressed(@NotNull Key key) {
        return getVersionedInstance().isKeyPressed(key);
    }

    public static InputType getInputType(@NotNull Key key) {
        Objects.requireNonNull(key, "Key cannot be null");
        if (key.isAction() || (InputAPI.getAPI().isCtrlKeyDown() && !InputAPI.getAPI().isAltKeyDown())) {
            return InputType.ACTION;
        } else {
            return InputType.CHARACTER;
        }
    }

    protected KeyMapper() {

    }

    public void register(@NotNull Key key) {
        Objects.requireNonNull(key, "Key cannot be null");
        String actualName = key.getName();
        if (this.keys.containsKey(actualName) || this.mouseButtons.containsKey(actualName)) {
            throw new IllegalArgumentException("Key " + actualName + " is already registered!");
        }

        if (key instanceof MouseButton) {
            this.mouseButtons.put(actualName, (MouseButton) key);
        } else {
            this.keys.put(actualName, key);
        }
    }

    public @Nullable Key getKeyByName(@NotNull String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        Key mouseButton = this.mouseButtons.get(name);
        if (mouseButton != null) {
            return mouseButton;
        }

        return this.keys.get(name);
    }

    public abstract @NotNull String getNameByKey(@NotNull Key key);

    public abstract int getIdByKey(@NotNull Key key);

    public abstract int getIdByMouseButton(@NotNull MouseButton mouseButton);

    public abstract @Nullable Key getKeyById(int keyId);

    public abstract @Nullable MouseButton getMouseButtonById(int mouseButtonId);

    public abstract char getChar(@NotNull Key key);

    public abstract boolean isKeyPressed(@NotNull Key key);

    public abstract void initialize();

    public abstract void register(@NotNull Key key, int keyId);

}
