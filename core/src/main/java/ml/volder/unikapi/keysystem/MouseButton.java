package ml.volder.unikapi.keysystem;

import org.jetbrains.annotations.NotNull;

public class MouseButton extends Key {

    public static void initialize() {

    }

    private static int currentMouseButtonId = 0;

    public static final Key LEFT = new MouseButton("LEFT").register().action();
    public static final Key RIGHT = new MouseButton("RIGHT").register().action();
    public static final Key MIDDLE = new MouseButton("MIDDLE").register().action();
    public static final Key M4 = new MouseButton("M4").register().action();
    public static final Key M5 = new MouseButton("M5").register().action();
    public static final Key M6 = new MouseButton("M6").register().action();
    public static final Key M7 = new MouseButton("M7").register().action();
    public static final Key M8 = new MouseButton("M8").register().action();

    protected MouseButton(String name) {
        super(name, KeyMapper.DEFAULT_CHAR);
        this.id = currentMouseButtonId;
        currentMouseButtonId++;
    }

    @NotNull
    public static MouseButton get(int keyId) {
        MouseButton mouseButton = KeyMapper.getMouseButton(keyId);
        if (mouseButton != null) {
            return mouseButton;
        }

        mouseButton = new MouseButton("M" + keyId);
        mouseButton.unknown = true;
        mouseButton.id = keyId;
        KeyMapper.getVersionedInstance().register(mouseButton, keyId);
        return mouseButton;
    }

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isRight() {
        return this == RIGHT;
    }

    public boolean isMiddle() {
        return this == MIDDLE;
    }
}
