package ui.view;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import input.MotionEvent;
import ui.util.AttributeSet;
import util.Logger;
import util.Vector2Df;
import visualisation.Visualisation;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class RecyclerView extends ViewGroup{

    private Adapter adapter = null;

    protected Vector2Df positionOffset = new Vector2Df(0,0);
    private OnItemClickListener onItemClickListener = null;

    public static final int NO_INDEX = -1;
    public static final long NO_ID = -1;
    public static final int INVALID_TYPE = -1;

    protected LayoutManager layoutManager;

    public RecyclerView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isClickable = true;
    }

    @Override
    public void render () {
        renderBackgound();

        glPushMatrix();
        glTranslatef(position.x, position.y, 0);

        glEnable(GL_STENCIL_TEST);

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

        glPushMatrix();
        glTranslatef(this.positionOffset.x*-1, this.positionOffset.y*-1, 0);

        for(View child : this.children){
            child.render();
        }
        glPopMatrix();

        glPopMatrix();
        glDisable(GL_STENCIL_TEST);

    }

    @Override
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {

        if(visibility == View.GONE){
            setMeasuredDimension(0, 0);
            return;
        }

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

        setMeasuredDimension(width, height);
    }

    @Override
    public void onLayout (int width, int height, int x, int y) {

        this.dimensions.x = width;
        this.dimensions.y = height;
        this.position.x = x;
        this.position.y = y;

        if(this.adapter != null && this.layoutManager != null){
            //this.createViewsDown();
            this.layoutManager.onDataSetChange(this,this.adapter);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        //boolean returnValue = super.dispatchTouchEvent(motionEvent);

        MotionEvent newMotionEvent = null;
        try {
            newMotionEvent = (MotionEvent) motionEvent.clone();
        }
        catch (CloneNotSupportedException e){
            return false;
        }
        newMotionEvent.transformPosition(Vector2Df.multiply(this.positionOffset, -1f).add(position));

        if(this.layoutManager != null && this.adapter != null){
            return this.layoutManager.dispatchTouchEvent(this, adapter, newMotionEvent);
        }
        return false;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        this.adapter.setDataSetChangeListener(this);
    }

    public void onDataSetChange () {

        if(this.layoutManager == null){
            return;
        }
        this.layoutManager.onDataSetChange(this,this.adapter);
    }

    public void setLayoutManager(LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    public Vector2Df getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(Vector2Df positionOffset) {
        this.positionOffset = positionOffset;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        return true;
    }

    public static final class Recycler {

        protected HashMap<Integer, LinkedList<ViewHolder>> viewHoldersTypes = new HashMap<Integer, LinkedList<ViewHolder>>();

        public ViewHolder getViewHolder(int type){
            if(viewHoldersTypes.containsKey(type) == false){
                return null;
            }
            if(viewHoldersTypes.get(type).size() > 0){
                return viewHoldersTypes.get(type).pop();
            } else {
                Logger.info("getViewHolder view not found, type = "+type );
                return null;
            }
        }

        public void addViewHolder(ViewHolder viewHolder){
            if(viewHoldersTypes.containsKey(viewHolder.itemViewType) == false){
                viewHoldersTypes.put(viewHolder.itemViewType, new LinkedList<ViewHolder>());
            }
            viewHoldersTypes.get(viewHolder.itemViewType).push(viewHolder);
        }

        public int getSize(){
            int count = 0;
            Set<Integer> keySet = viewHoldersTypes.keySet();
            for(Integer i : keySet){
                count += viewHoldersTypes.get(i).size();
            }
            return count;
        }
    }

    public static abstract class LayoutManager {

        protected Recycler recycler = new Recycler();

        public abstract void onDataSetChange(RecyclerView recyclerView, Adapter adapter);

        public abstract boolean dispatchTouchEvent(RecyclerView recyclerView, Adapter adapter, MotionEvent motionEvent);
    }

    public static abstract class ViewHolder{
        public View view;
        public int index = NO_INDEX;
        public long itemId = NO_ID;
        public int itemViewType = INVALID_TYPE;

        public ViewHolder(){
        }
        public ViewHolder(View view){
            this.view = view;
        }
    }

    public static abstract class Adapter<T>{
        private RecyclerView listener = null;
        public abstract ViewHolder createViewHolder(ViewGroup parent, int viewType);
        public abstract void bindViewHolder(T holder, int position);
        public abstract int getItemCount();

        public Adapter(){

        }
        public void notifyDataSetChanged() {
            if(this.listener != null){
                listener.onDataSetChange();
            }
        }
        public int getItemViewType(int position) {
            return 0;
        }

        public void setDataSetChangeListener(RecyclerView listener ){
            this.listener = listener;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void removeOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return this.onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int index, View view);
    }

}
