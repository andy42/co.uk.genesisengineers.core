package ui.view;

import input.KeyEvent;
import ui.Context;
import ui.util.AttributeParser;
import ui.util.AttributeSet;
import input.MotionEvent;
import util.CollisionBox;
import util.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class ViewGroup extends View {

    protected List<View> children = new ArrayList<>();
    protected View touchTarget = null;
    protected boolean touchIntercept = false;
    private View currentFocused = null;

    public ViewGroup (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup () {
        super();
    }


    public void addView (View view) {
        view.setParent(this);
        this.children.add(view);
    }
    public void removeView (View view) {
        this.children.remove(view);
        view.removeParent();
    }


    /*
    //this is a work in progress need to work out how to do nested clipping
    //here are soom articls about what needs to be done
    //http://cranialburnout.blogspot.com/2014/03/nesting-and-overlapping-translucent.html
    //https://en.wikibooks.org/wiki/OpenGL_Programming/Stencil_buffer
    @Override
    public void render () {
        super.render();

        glPushMatrix();
        glTranslatef(position.x, position.y, 0);

        glEnable(GL_STENCIL_TEST);


        for (View view : children) {

            glStencilMask(0xFF); // set Stencil_buffer to write
            glClear( GL_STENCIL_BUFFER_BIT);
            glStencilFunc(GL_EQUAL, 1, 0xFF);
            glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);

            glColorMask(false, false, false, false); //turn off writing to the color buffer

            glPushMatrix();
            glScalef(dimensions.x, dimensions.y, 0);
            Visualisation.getInstance().drawColouredSquareTopLeft(new Vec3f(1,1,1)); //draw to Stencil_buffer
            glPopMatrix();

            glStencilMask(0x00); set Stencil_buffer to read-only

            glColorMask(true, true, true, true);//turn on writing to the color buffer

            view.render();
        }
        glPopMatrix();
        glDisable(GL_STENCIL_TEST);
    }
    */

    public View getCurrentFocused(){
        return this.currentFocused;
    }

    @Override
    public void unFocus(){
        super.unFocus();
        if(this.currentFocused != null){
            this.currentFocused.unFocus();
        }
    }

    public void requestChildFocus(View child, View focused){
        if(this.currentFocused != child && this.currentFocused != null){
            this.currentFocused.unFocus();
        }

        this.currentFocused = child;
        if(parent != null){
            parent.requestChildFocus(this, focused);
        }
    }


    @Override
    public void render () {
        super.render();
        glPushMatrix();
        glTranslatef(position.x, position.y, 0);
        for (View view : children) {
            view.render();
        }
        glPopMatrix();
    }

    public LayoutParams generateLayoutParams (AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams {

        public LayoutParams (Context context, AttributeSet attrs) {
            width = AttributeParser.getLayoutDimension(attrs, "layout_width");
            height = AttributeParser.getLayoutDimension(attrs, "layout_height");
        }

        public LayoutParams () {
            this.width = FILL_PARENT;
            this.height = FILL_PARENT;
        }

        public LayoutParams (int with, int height) {
            this.width = with;
            this.height = height;
        }

        public int width;
        public int height;

        public final static int FILL_PARENT = -1;
        public final static int MATCH_PARENT = -2;
        public final static int WRAP_CONTENT = -3;
    }

    public View findViewById(String id){
        for(View view : children){
            if(view.getId().equalsIgnoreCase(id)){
                return view;
            }

            View foundView = view.findViewById(id);
            if(foundView != null){
                return foundView;
            }
        }
        return null;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        return false;
    }

    public boolean dispatchTransformedTouchEvent(MotionEvent motionEvent, boolean cancel,  View view){

        MotionEvent newMotionEvent = null;
        try {
            newMotionEvent = (MotionEvent) motionEvent.clone();
        }
        catch (CloneNotSupportedException e){
            return false;
        }

        if(cancel){
            newMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
        }

        newMotionEvent.transformPosition(position);

        return view.dispatchTouchEvent(newMotionEvent);
    }

    @Override
    public Boolean dispatchKeyEvent (KeyEvent keyEvent){
        if(this.currentFocused == null){
            return false;
        }
        return this.currentFocused.dispatchKeyEvent(keyEvent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){

        //reset for new Action
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            this.touchIntercept = false;
            this.touchTarget = null;
        }

        if(onInterceptTouchEvent(motionEvent)){
            touchIntercept = true;
        }

        if(touchIntercept == true && touchTarget != null){
            dispatchTransformedTouchEvent(motionEvent, true, touchTarget);
            touchTarget = null;
            return this.onTouchEvent(motionEvent);
        }

        if(touchTarget != null){
            return dispatchTransformedTouchEvent(motionEvent, false, touchTarget);
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            CollisionBox collisionBox = new CollisionBox();
            collisionBox.initTopLeft(this.position, this.dimensions);
            if(collisionBox.pointCollisionTest(motionEvent.getPosition())){

                for (View view : children) {
                    if (dispatchTransformedTouchEvent(motionEvent, false, view)) {
                        touchTarget = view;
                        return true;
                    }
                }

                return onTouchEvent(motionEvent);
            }
            else {
                return false;
            }
        } else {
            return onTouchEvent(motionEvent);
        }
    }
}
