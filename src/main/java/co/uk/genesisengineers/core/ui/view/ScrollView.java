package co.uk.genesisengineers.core.ui.view;

import com.sun.javafx.geom.Vec3f;
import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.input.MotionEvent;
import co.uk.genesisengineers.core.ui.util.AttributeSet;
import co.uk.genesisengineers.core.util.Logger;
import co.uk.genesisengineers.core.util.Vector2Df;
import co.uk.genesisengineers.core.visualisation.Visualisation;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class ScrollView extends ViewGroup {

    protected int orientation;

    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;

    protected float positionOffset = 0;

    public final static String HORIZONTAL_KEY = "horizontal";
    public final static String VERTICAL_KEY = "vertical";

    public ScrollView (Context context, AttributeSet attrs) {
        super(context, attrs);

        String orientationValue = attrs.getAttributeValue(null, "orientation");
        if (orientationValue != null && orientationValue.equalsIgnoreCase(HORIZONTAL_KEY)) {
            this.orientation = HORIZONTAL;
        } else {
            this.orientation = VERTICAL;
        }

        this.isClickable = true;
    }

    @Override
    public void render () {
        renderBackgound();

        if(this.children.size() == 0){
            return;
        }

        View child = this.children.get(0);

        glPushMatrix();
        glTranslatef(position.x, position.y, 0);

        glEnable(GL_STENCIL_TEST);

        Visualisation.getInstance().useColourProgram();
        glStencilMask(0xFF);
        glClear( GL_STENCIL_BUFFER_BIT);
        glStencilFunc(GL_EQUAL, 1, 0xFF);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);

        glColorMask(false, false, false, false);

        glPushMatrix();
        glScalef(dimensions.x, dimensions.y, 0);
        Visualisation.getInstance().drawColouredSquareTopLeft(new Vec3f(1,1,1));
        glPopMatrix();

        glStencilMask(0x00);

        glColorMask(true, true, true, true);

        child.render();

        glPopMatrix();
        glDisable(GL_STENCIL_TEST);
    }

    @Override
    public void addView (View view) {
        if(this.children.size() > 0){
            Logger.error("ScrollView can only have one child view");
            return;
        }
        super.addView(view);
    }

    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        if (layoutParams.width > 0) {
            width = layoutParams.width;
        } else if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            width = widthMeasureSpec;
        }

        //Height
        if (layoutParams.height > 0) {
            height = layoutParams.height;
        } else if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            height = heightMeasureSpec;
        }

        if(this.children.size() > 0) {
            View child = this.children.get(0);
            child.onMeasure(width, height);
            int childWidth = child.getMeasuredWidth() + child.getLeftMargin() + child.getRightMargin();
            int childHeight = child.getMeasuredHeight() + child.getTopMargin() + child.getBottomMargin();
            if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = childWidth;
            }
            if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = childHeight;
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public void onLayout (int width, int height, int x, int y) {

        this.dimensions.x = width;
        this.dimensions.y = height;
        this.position.x = x;
        this.position.y = y;

        if(this.children.size() > 0) {
            View child = this.children.get(0);
            child.onLayout(child.getMeasuredWidth(), child.getMeasuredHeight(),0, 0);
        }
    }

    protected void moveChild(View child, Vector2Df displacement){

        if(child == null){
            return;
        }

        Vector2Df newPosition;
        Vector2Df maxDisplacement = Vector2Df.sub( this.getDimensions(), child.getDimensions());
        if(maxDisplacement.x < 0)maxDisplacement.x = 0;
        if(maxDisplacement.y < 0)maxDisplacement.y= 0;

        if(this.orientation == VERTICAL){
            Vector2Df orientationDisplacement = new Vector2Df(0f, displacement.y);
            newPosition = Vector2Df.add(child.getPosition(), orientationDisplacement);
            if(newPosition.y < maxDisplacement.y){
                newPosition.y = maxDisplacement.y;
            }
            else if(newPosition.y > 0){
                newPosition.y = 0;
            }
        }
        else {
            Vector2Df orientationDisplacement = new Vector2Df(displacement.x, 0);
            newPosition = Vector2Df.add(child.getPosition(), orientationDisplacement);
            if(newPosition.x < maxDisplacement.x){
                newPosition.x = maxDisplacement.x;
            }
            else if(newPosition.x > 0){
                newPosition.x = 0;
            }
        }

        child.setPosition(newPosition);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        View child = this.children.get(0);
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE && child != null){
            moveChild(child, motionEvent.getMotion());
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_SCROLL && child != null){
            moveChild(child, motionEvent.getMotion().multiply(new Vector2Df(10, 10)));
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        return true;
    }
}
