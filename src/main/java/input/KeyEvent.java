package input;

public class KeyEvent {

    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_HOLD = 3;

    public int keyValue;
    public int action;

    public KeyEvent(int keyValue, int action){
        this.keyValue = keyValue;
        this.action = action;
    }
}
