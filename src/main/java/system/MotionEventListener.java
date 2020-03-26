package system;

import input.MotionEvent;

public interface MotionEventListener {
    boolean dispatchTouchEvent(MotionEvent motionEvent);
}
