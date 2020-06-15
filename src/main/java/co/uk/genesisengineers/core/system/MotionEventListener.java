package co.uk.genesisengineers.core.system;

import co.uk.genesisengineers.core.input.MotionEvent;

public interface MotionEventListener {
    boolean dispatchTouchEvent(MotionEvent motionEvent);
}
