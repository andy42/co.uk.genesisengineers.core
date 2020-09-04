package co.uk.genesisengineers.core.ui.activity;

import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.content.Resources;
import co.uk.genesisengineers.core.input.KeyEvent;
import co.uk.genesisengineers.core.input.MotionEvent;
import co.uk.genesisengineers.core.ui.view.View;
import co.uk.genesisengineers.core.util.CollisionBox;
import co.uk.genesisengineers.core.util.Vector2Df;

import static org.lwjgl.opengl.GL11.*;

public abstract class Activity extends Context {

    protected Context baseContext;

    protected FragmentManager fragmentManager;

    protected boolean open = false;
    protected View view = null;

    protected boolean hasDragBar = true;
    protected boolean isDragging = false;
    protected Vector2Df dragBarClickOffset = new Vector2Df(0, 0);

    protected Vector2Df activityDimensions = new Vector2Df(0, 0);
    CollisionBox activityCollisionBox = new CollisionBox();

    protected Vector2Df position = new Vector2Df(100, 100);

    public Activity () {
        fragmentManager = new FragmentManager(this);
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

    public void setView(View view) {
        this.view = view;
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
        this.view.onLayout(this.view.getMeasuredWidth(), this.view.getMeasuredHeight(), x, 0);

        activityDimensions.x = this.view.getMeasuredWidth();
        activityDimensions.y = this.view.getMeasuredHeight();
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

    public void onViewCreated(){

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

    public void setDragbar(int viewId){
        View dragBar = view.findViewById(viewId);
        if(dragBar == null){
            return;
        }

        hasDragBar = true;
        dragBar.setOnTouchListener((MotionEvent event, View v) -> {
            onDragBarMovement(event);
        });
    }

    private void onDragBarMovement(MotionEvent motionEvent){
        if ((motionEvent.getAction() == MotionEvent.ACTION_DOWN)) {
            isDragging = true;
        } else if (isDragging && (motionEvent.getAction() == MotionEvent.ACTION_UP)) {
            isDragging = false;
        }
    }

    public Boolean dispatchTouchEvent (MotionEvent motionEvent) {
        if (hasDragBar) {
            if(isDragging && dragBarClickOffset == null){
                dragBarClickOffset = Vector2Df.sub(motionEvent.getPosition(), this.position);
            }
            else if(isDragging){
                position = motionEvent.getPosition().sub(dragBarClickOffset);
            } else {
                dragBarClickOffset = null;
            }
        }

        if (this.view != null){
            return dispatchTransformedTouchEvent(motionEvent);
        }

        return false;
    }

    public View findViewById(int id){
        if(view == null){
            return null;
        }
        return view.findViewById(id);
    }
}
