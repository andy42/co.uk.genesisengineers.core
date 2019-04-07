package input;

import clock.Clock;
import clock.ClockHandler;
import util.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

import static org.lwjgl.glfw.GLFW.*;

public class KeyMapper extends Input {

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;

    private boolean capsOn = false;

    private static KeyMapper s_instance = null;

    private static final double KEY_PRESSED_HOLD_TIME = 0.05;

    private Queue<KeyEvent> keyEventQueue = new LinkedList();
    private LinkedList<KeyDownData> currentPressedKeys = new LinkedList<KeyDownData>();

    public static KeyMapper getInstance () {
        if (s_instance == null) {
            s_instance = new KeyMapper();
        }
        return s_instance;
    }

    private KeyMapper () {
    }

    public void init (long windowLong) {
        glfwSetKeyCallback(windowLong, (window, key, scancode, action, mods) -> {
            setWindow(window);
            handleKeyPress(key, action);
        });
    }

    private boolean updateKey (int key, int action, int matchKey, boolean currentValue) {

        if (key == matchKey && action == GLFW_PRESS) {
            return true;
        } else if (key == matchKey && action == GLFW_RELEASE) {
            return false;
        } else {
            return currentValue;
        }
    }

    private void addCurrentPressedKeys(int key, double time){
        currentPressedKeys.addFirst(new KeyDownData(key, time));
    }

    private void removeCurrentPressedKeys(int key){
        for(int i=0; i< this.currentPressedKeys.size(); i++){
            if(this.currentPressedKeys.get(i).key == key){
                this.currentPressedKeys.remove(i);
            }
        }
    }

    public void handleKeyPress (int key, int action) {

        int newKey = key;
        if(key >= 65 && key <= 90){
            if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == 1 || glfwGetKey(window, GLFW_KEY_RIGHT_SHIFT) == 1){
                //do nothing
            } else {
                newKey = key+32;
            }
        }

        double currentTime = ClockHandler.getInstance().getClock(ClockHandler.Type.SYSTEM_CLOCK).getTime();

        if(action == GLFW_PRESS){
            this.keyEventQueue.add(new KeyEvent(newKey, KeyEvent.ACTION_DOWN));
            addCurrentPressedKeys(newKey, currentTime);
        }
        else if(action == GLFW_RELEASE) {
            if (key == GLFW_KEY_CAPS_LOCK) {

                this.capsOn = !this.capsOn;
            }
            this.keyEventQueue.add(new KeyEvent(newKey, KeyEvent.ACTION_UP));
            removeCurrentPressedKeys(newKey);
        }

        // handle escape key
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(this.window, true); // We will detect this in the rendering loop
        }
        this.moveUp = updateKey(key, action, GLFW_KEY_W, this.moveUp);
        this.moveLeft = updateKey(key, action, GLFW_KEY_A, this.moveLeft);
        this.moveDown = updateKey(key, action, GLFW_KEY_S, this.moveDown);
        this.moveRight = updateKey(key, action, GLFW_KEY_D, this.moveRight);
    }

    public void update(){
        double currentTime = ClockHandler.getInstance().getClock(ClockHandler.Type.SYSTEM_CLOCK).getTime();
        for(KeyDownData keyDownData : this.currentPressedKeys){
            if((keyDownData.lastUpdate + KEY_PRESSED_HOLD_TIME) < currentTime){
                this.keyEventQueue.add(new KeyEvent(keyDownData.key, KeyEvent.ACTION_HOLD));
                keyDownData.lastUpdate = currentTime;
            }
        }
    }

    public boolean isMoveLeft () {
        return this.moveLeft;
    }

    public boolean isMoveRight () {
        return this.moveRight;
    }

    public boolean isMoveUp () {
        return this.moveUp;
    }

    public boolean isMoveDown () {
        return this.moveDown;
    }

    public KeyEvent getNextKeyEvent(){
        return keyEventQueue.poll();
    }

    protected static class KeyDownData{
        private int key;
        private double lastUpdate;
        public KeyDownData(int key, double lastUpdate){
            this.key = key;
            this.lastUpdate = lastUpdate;
        }
    }
}
