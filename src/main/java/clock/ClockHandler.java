package clock;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class ClockHandler {

    private static ClockHandler s_instance = null;

    public static ClockHandler getInstance () {
        if (s_instance == null) {
            s_instance = new ClockHandler();
        }
        return s_instance;
    }

    public enum Type {
        GAME_CLOCK,
        ANIMATION_CLOCK,
        SYSTEM_CLOCK
    }

    private HashMap<Type, Clock> clockMap = new HashMap<>();

    public Clock getClock (Type type) {
        return clockMap.get(type);
    }

    private ClockHandler () {
        clockMap.put(Type.GAME_CLOCK, new Clock());
        clockMap.put(Type.ANIMATION_CLOCK, new Clock());
        clockMap.put(Type.SYSTEM_CLOCK, new Clock());
    }

    public void update () {
        double time = GLFW.glfwGetTime();
        for (Clock clock : clockMap.values()) {
            clock.update(time);
        }
    }

}
