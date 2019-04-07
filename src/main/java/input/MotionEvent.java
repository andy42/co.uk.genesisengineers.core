package input;

import util.Vector2Df;

public class MotionEvent implements Cloneable{
    private int action = 0;
    private Vector2Df position = new Vector2Df();

    private Vector2Df motion = new Vector2Df();

    public static final int ACTION_DOWN = 1;
    public static final int ACTION_UP = 2;
    public static final int ACTION_MOVE = 3;
    public static final int ACTION_CANCEL = 4;

    public MotionEvent(int action, Vector2Df position, Vector2Df motion){
        this.action = action;
        this.position = position;
        this.motion = motion;
    }

    public MotionEvent(int action, Vector2Df position){
        this.action = action;
        this.position = position;
        this.motion = new Vector2Df();
    }

    public int getAction () {
        return action;
    }

    public void setAction (int action) {
        this.action = action;
    }

    public Vector2Df getPosition () {
        return position;
    }

    public void setPosition (Vector2Df position) {
        this.position = position;
    }

    public Vector2Df getMotion () {
        return motion;
    }

    public void setMotion (Vector2Df motion) {
        this.motion = motion;
    }

    public void transformPosition (Vector2Df transform){
        this.position = Vector2Df.sub(this.position, transform);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MotionEvent motionEvent = (MotionEvent)super.clone();
        motionEvent.setPosition( (Vector2Df)this.position.clone() );
        return motionEvent;
    }
}
