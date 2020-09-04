package co.uk.genesisengineers.core.input;

import co.uk.genesisengineers.core.util.Vector2Df;

import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Danny Weatherley <danny.f.weatherley@gmail.com>
 */
public class Mouse extends Input {

    private static Mouse s_instance = null;
    private Vector2Df position = new Vector2Df();
    private Boolean buttonOneDown = false;

    private Queue<MotionEvent> motionEventQueue = new LinkedList();

    public static Mouse getInstance () {
        if (s_instance == null) {
            s_instance = new Mouse();
        }
        return s_instance;
    }

    private Mouse () {
    }

    public void init (long windowLong) {
        glfwSetCursorPosCallback(windowLong, (window, xpos, ypos) -> {
            this.setWindow(window);

            Vector2Df oldPosition = this.position;

            this.position = new Vector2Df((float) xpos, (float) ypos);

            if(buttonOneDown){
                motionEventQueue.add(new MotionEvent(MotionEvent.ACTION_MOVE, position, Vector2Df.sub(this.position, oldPosition)));
            }

            //Logger.debug("Mouse - x: %s, y: %s", Double.toString(xpos), Double.toString(ypos));
        });

        glfwSetMouseButtonCallback(windowLong, (long window, int button, int action, int mods) -> {
            //Logger.debug("Mouse - button: %s, action: %s", Integer.toString(button), Integer.toString(action));
            if (button == 0) {
                int eventAction = 0;
                if(action == 1){
                    buttonOneDown = true;
                    eventAction = MotionEvent.ACTION_DOWN;
                } else {
                    buttonOneDown = false;
                    eventAction = MotionEvent.ACTION_UP;
                }
                motionEventQueue.add(new MotionEvent(eventAction, position));
            }
        });

        glfwSetScrollCallback(windowLong, (long win, double dx, double dy) -> {
            motionEventQueue.add(new MotionEvent(MotionEvent.ACTION_SCROLL, position, new Vector2Df(dx, dy)));
        });
    }

    public MotionEvent getNextMotionEvent(){
        return motionEventQueue.poll();
    }

    public Vector2Df getPosition () {
        return this.position;
    }
}
