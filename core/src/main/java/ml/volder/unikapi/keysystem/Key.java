package ml.volder.unikapi.keysystem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Key {
    public static void initialize() {

    }

    private static int currentId = 0;

    private static final KeyMapper KEY_MAPPER = KeyMapper.getVersionedInstance();

    public static final Key NONE = new Key("NONE", KeyMapper.DEFAULT_CHAR).register();

    // Row 1
    public static final Key ESCAPE = new Key("ESCAPE", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F1 = new Key("F_1", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F2 = new Key("F_2", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F3 = new Key("F_3", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F4 = new Key("F_4", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F5 = new Key("F_5", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F6 = new Key("F_6", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F7 = new Key("F_7", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F8 = new Key("F_8", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F9 = new Key("F_9", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F10 = new Key("F_10", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F11 = new Key("F_11", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F12 = new Key("F_12", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F13 = new Key("F_13", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F14 = new Key("F_14", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F15 = new Key("F_15", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F16 = new Key("F_16", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F17 = new Key("F_17", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F18 = new Key("F_18", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F19 = new Key("F_19", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F20 = new Key("F_20", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F21 = new Key("F_21", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F22 = new Key("F_22", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F23 = new Key("F_23", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F24 = new Key("F_24", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key F25 = new Key("F_25", KeyMapper.DEFAULT_CHAR).register().action();

    // Row 2
    public static final Key GRAVE = new Key("GRAVE", KeyMapper.DEFAULT_CHAR).register();
    public static final Key NUM1 = new Key("NUM_1", '1').register();
    public static final Key NUM2 = new Key("NUM_2",'2').register();
    public static final Key NUM3 = new Key("NUM_3",'3').register();
    public static final Key NUM4 = new Key("NUM_4",'4').register();
    public static final Key NUM5 = new Key("NUM_5",'5').register();
    public static final Key NUM6 = new Key("NUM_6",'6').register();
    public static final Key NUM7 = new Key("NUM_7",'7').register();
    public static final Key NUM8 = new Key("NUM_8",'8').register();
    public static final Key NUM9 = new Key("NUM_9",'9').register();
    public static final Key NUM0 = new Key("NUM_0",'0').register();
    public static final Key MINUS = new Key("MINUS",'-').register();
    public static final Key EQUAL = new Key("EQUAL",'=').register();
    public static final Key BACK = new Key("BACK", KeyMapper.DEFAULT_CHAR).register().action();

    // Row 3
    public static final Key TAB = new Key("TAB", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key Q = new Key("Q",'q').register();
    public static final Key W = new Key("W",'w').register();
    public static final Key E = new Key("E",'e').register();
    public static final Key R = new Key("R",'r').register();
    public static final Key T = new Key("T",'t').register();
    public static final Key Y = new Key("Y",'y').register();
    public static final Key U = new Key("U",'u').register();
    public static final Key I = new Key("I",'i').register();
    public static final Key O = new Key("O",'o').register();
    public static final Key P = new Key("P",'p').register();
    public static final Key L_BRACKET = new Key("LEFT_BRACKET",'[').register();
    public static final Key R_BRACKET = new Key("RIGHT_BRACKET", ']').register();
    public static final Key BACKSLASH = new Key("BACKSLASH", '\\').register();

    // Row 4
    public static final Key CAPS_LOCK = new Key("CAPSLOCK", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key A = new Key("A", 'a').register();
    public static final Key S = new Key("S", 's').register();
    public static final Key D = new Key("D", 'd').register();
    public static final Key F = new Key("F", 'f').register();
    public static final Key G = new Key("G", 'g').register();
    public static final Key H = new Key("H", 'h').register();
    public static final Key J = new Key("J", 'j').register();
    public static final Key K = new Key("K", 'k').register();
    public static final Key L = new Key("L", 'l').register();
    public static final Key SEMICOLON = new Key("SEMICOLON" , ';').register();
    public static final Key APOSTROPHE = new Key("APOSTROPHE", '\'').register();
    public static final Key ENTER = new Key("ENTER", KeyMapper.DEFAULT_CHAR).register().action();

    // Row 5
    public static final Key L_SHIFT = new Key("LEFT_SHIFT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key Z = new Key("Z", 'z').register();
    public static final Key X = new Key("X", 'x').register();
    public static final Key C = new Key("C", 'c').register();
    public static final Key V = new Key("V", 'v').register();
    public static final Key B = new Key("B", 'b').register();
    public static final Key N = new Key("N", 'n').register();
    public static final Key M = new Key("M", 'm').register();
    public static final Key COMMA = new Key("COMMA", ',').register();
    public static final Key PERIOD = new Key("PERIOD",'.').register();
    public static final Key SLASH = new Key("SLASH",'/').register();
    public static final Key R_SHIFT = new Key("RIGHT_SHIFT",KeyMapper.DEFAULT_CHAR).register().action();

    // Row 6
    public static final Key L_CONTROL = new Key("LEFT_CONTROL", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key L_WIN = new Key("LEFT_WIN", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key L_ALT = new Key("LEFT_ALT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key SPACE = new Key("SPACE", KeyMapper.DEFAULT_CHAR).register();
    public static final Key R_ALT = new Key("RIGHT_ALT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key R_WIN = new Key("RIGHT_WIN", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key MENU = new Key("MENU", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key R_CONTROL = new Key("RIGHT_CONTROL", KeyMapper.DEFAULT_CHAR).register().action();

    // Arrow Keys
    public static final Key ARROW_LEFT = new Key("ARROW_LEFT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key ARROW_DOWN = new Key("ARROW_DOWN", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key ARROW_RIGHT = new Key("ARROW_RIGHT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key ARROW_UP = new Key("ARROW_UP", KeyMapper.DEFAULT_CHAR).register().action();

    // Other
    public static final Key INSERT = new Key("INSERT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key HOME = new Key("HOME", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key PAGE_UP = new Key("PAGE_UP", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key DELETE = new Key("DELETE", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key END = new Key("END", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key PAGE_DOWN = new Key("PAGE_DOWN", KeyMapper.DEFAULT_CHAR).register().action();

    // Other
    public static final Key PRINT = new Key("PRINT", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key SCROLL = new Key("SCROLL", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key PAUSE = new Key("PAUSE", KeyMapper.DEFAULT_CHAR).register().action();

    // NUMPAD
    public static final Key NUM_LOCK = new Key("NUMPAD_LOCK", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key DIVIDE = new Key("NUMPAD_DIVIDE", '/').register();
    public static final Key MULTIPLY = new Key("NUMPAD_MULTIPLY",'*').register();
    public static final Key SUBTRACT = new Key("NUMPAD_SUBTRACT",'-').register();
    public static final Key ADD = new Key("NUMPAD_ADD",'+').register();
    public static final Key NUMPAD_EQUAL = new Key("NUMPAD_EQUAL",'=').register();
    public static final Key NUMPAD_ENTER = new Key("NUMPAD_ENTER", KeyMapper.DEFAULT_CHAR).register().action();
    public static final Key DECIMAL = new Key("DUMPAD_DECIMAL", ',').register();
    public static final Key NUMPAD0 = new Key("NUMPAD_0", '0').register();
    public static final Key NUMPAD1 = new Key("NUMPAD_1", '1').register();
    public static final Key NUMPAD2 = new Key("NUMPAD_2", '2').register();
    public static final Key NUMPAD3 = new Key("NUMPAD_3", '3').register();
    public static final Key NUMPAD4 = new Key("NUMPAD_4", '4').register();
    public static final Key NUMPAD5 = new Key("NUMPAD_5", '5').register();
    public static final Key NUMPAD6 = new Key("NUMPAD_6", '6').register();
    public static final Key NUMPAD7 = new Key("NUMPAD_7", '7').register();
    public static final Key NUMPAD8 = new Key("NUMPAD_8", '8').register();
    public static final Key NUMPAD9 = new Key("NUMPAD_9", '9').register();

    public static final Key WORLD_1 = new Key("WORLD_1", KeyMapper.DEFAULT_CHAR).register();
    public static final Key WORLD_2 = new Key("WORLD_2", KeyMapper.DEFAULT_CHAR).register();

    protected String name;

    protected int id = -1;
    protected char character;
    protected boolean unknown;
    protected boolean action;

    protected boolean isCharacter;

    protected Key(String name, char character) {
        if(!(this instanceof MouseButton)){
            this.id = currentId;
            currentId++;
        }
        this.character = character;
        this.name = name;
        isCharacter = !(character == KeyMapper.DEFAULT_CHAR);
    }

    protected Key action() {
        this.action = true;
        return this;
    }

    protected Key register() {
        KeyMapper.registerKey(this);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public char getCharacter() {
        return character;
    }

    public int getId() {
        return this.id;
    }

    public boolean isCharacter() {
        return this.isCharacter;
    }

    public boolean isAction() {
        return this.action;
    }

    public boolean isUnknown() {
        return this.unknown;
    }

    public boolean isPressed() {
        return KeyMapper.isPressed(this);
    }

    public static Key get(int keyId) {
        Key key = KeyMapper.getKey(keyId);
        if (key != null) {
            return key;
        }

        key = new Key("UNKNOWN_" + keyId, KeyMapper.DEFAULT_CHAR);
        key.unknown = true;
        key.id = keyId;
        KEY_MAPPER.register(key, keyId);
        return key;
    }

    @Nullable
    public static Key getByName(@NotNull String name) {
        return KeyMapper.getKey(name);
    }

    public static String concat(Collection<Key> keys) {
        List<Key> toSort = new ArrayList<>();
        toSort.addAll(keys);
        toSort.sort(Collections.reverseOrder(Comparator.comparing(Key::getId)));
        StringJoiner joiner = new StringJoiner(" + ");
        for (Key key : toSort) {
            if (key == null) {
                continue;
            }

            String keyName = key.getName();
            joiner.add(keyName);
        }
        return joiner.toString();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Key key = (Key) o;
        return this.name.equals(key.name) && this.action == key.action
                && this.getId() == key.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.action);
    }

    public static boolean isCharacter(char character) {
        return character != 167 && character >= ' ' && character != 127;
    }
}
