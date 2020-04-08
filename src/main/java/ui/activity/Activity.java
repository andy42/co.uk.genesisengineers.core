package ui.activity;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import content.Resources;
import input.KeyEvent;
import input.MotionEvent;
import ui.util.AttributeParser;
import ui.view.View;
import util.CollisionBox;
import util.Vector2Df;
import visualisation.Visualisation;

import static org.lwjgl.opengl.GL11.*;

public abstract class Activity extends Context {

    protected Context baseContext;


    protected boolean open = false;
    protected View view = null;

    protected boolean hasDragBar = true;
    protected boolean isDragging = false;
    protected int dragBarHeight = 15;
    protected Vector2Df dragBarDimensions = new Vector2Df(0, 0);
    protected Vector2Df dragBarClickOffset = new Vector2Df(0, 0);
    CollisionBox dragBarCollisionBox = new CollisionBox();
    protected Vec3f dragBarColor = null;

    protected Vector2Df activityDimensions = new Vector2Df(0, 0);
    CollisionBox activityCollisionBox = new CollisionBox();

    protected Vector2Df position = new Vector2Df(100, 100);

    public Activity () {
        //baseContext = context;
        dragBarColor = AttributeParser.colorFromString("#8e8e8e");
    }

    public void setBaseContext(Context context){
        this.baseContext = context;
    }

    @Override
    public Resources getResources(){
        return baseContext.getResources();
    }

    public boolean isOpen () {
        return this.open;
    }

    public void setOpen (boolean open) {
        this.open = open;
    }

    public View getView () {
        return this.view;
    }

    public void update (double time) {

    }

    public void render () {
        if (isOpen() == false) {
            return;
        }
        if (view == null) {
            return;
        }

        glPushMatrix();
        glTranslatef(position.x, position.y, 0);
        glScalef(dragBarDimensions.x, dragBarDimensions.y, 0);
        Visualisation.getInstance().drawColouredSquareTopLeft(dragBarColor);
        glPopMatrix();

        glPushMatrix();
        glTranslatef(position.x, position.y, 0);
        view.render();
        glPopMatrix();

    }

    public void onCreate () {

    }

    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        if (this.view == null) {
            return;
        }
        this.view.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onLayout (int width, int height, int x, int y) {
        if (this.view == null) {
            return;
        }
        dragBarDimensions = new Vector2Df(this.view.getMeasuredWidth(), dragBarHeight);
        this.view.onLayout(this.view.getMeasuredWidth(), this.view.getMeasuredHeight(), x, ((hasDragBar) ? dragBarHeight : 0));

        activityDimensions.x = this.view.getMeasuredWidth();
        activityDimensions.y = this.view.getMeasuredHeight() + ((hasDragBar) ? dragBarHeight : 0);
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent motionEvent){
        MotionEvent newMotionEvent = null;
        try {
            newMotionEvent = (MotionEvent) motionEvent.clone();
        }
        catch (CloneNotSupportedException e){
            return false;
        }

        newMotionEvent.transformPosition(this.position);
        return view.dispatchTouchEvent(newMotionEvent);
    }

    public boolean pointCollisionTest(MotionEvent motionEvent){
        activityCollisionBox.initTopLeft(this.position, activityDimensions);
        return activityCollisionBox.pointCollisionTest(motionEvent.getPosition());
    }

    public Boolean dispatchKeyEvent (KeyEvent keyEvent){

        if(this.view == null){
            return false;
        }
        return this.view.dispatchKeyEvent(keyEvent);
    }

    public Boolean dispatchTouchEvent (MotionEvent motionEvent) {
        //Logger.info("touchEvent DragBar "+point.toString());
        if (hasDragBar) {
            dragBarCollisionBox.initTopLeft(position, dragBarDimensions);

            Vector2Df mousePos = motionEvent.getPosition();
            if ((motionEvent.getAction() == MotionEvent.ACTION_DOWN) && dragBarCollisionBox.pointCollisionTest(motionEvent.getPosition()) && isDragging == false) {
                isDragging = true;
                dragBarClickOffset = Vector2Df.sub(mousePos, this.position);
                return true;
            } else if (isDragging && (motionEvent.getAction() == MotionEvent.ACTION_UP)) {
                isDragging = false;
                return true;
            } else if (isDragging && (motionEvent.getAction() == MotionEvent.ACTION_MOVE)) {
                position = mousePos.sub(dragBarClickOffset);
                return true;
            }
        }

        if (this.view != null){
            return dispatchTransformedTouchEvent(motionEvent);
        }

        return false;
    }
}
