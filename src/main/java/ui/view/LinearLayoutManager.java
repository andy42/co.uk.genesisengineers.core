package ui.view;

import input.MotionEvent;
import util.CollisionBox;
import util.Logger;
import util.Vector2Df;

import java.util.LinkedList;

public class LinearLayoutManager extends RecyclerView.LayoutManager {

    protected int orientation = VERTICAL;
    protected boolean roomToScroll = false;

    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;

    protected RecyclerView.ViewHolder touchTarget = null;

    public LinearLayoutManager(){

    }

    public LinearLayoutManager(int orientation){
        this.orientation = orientation;
    }

    protected CollisionBox viewCollisionBox = new CollisionBox();

    protected LinkedList<RecyclerView.ViewHolder> currentViewHolders = new LinkedList<RecyclerView.ViewHolder>();

    protected boolean isRoomToScroll(RecyclerView recyclerView){
        if(this.currentViewHolders.size() > 0){
            float lowestPoint = geLowestPoint(this.currentViewHolders.getLast());
            if( lowestPoint < ((this.orientation == VERTICAL) ? recyclerView.dimensions.y : recyclerView.dimensions.x)){
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //moveAmount returns a adjusted move value to make sure you can not over scroll
    public Vector2Df moveAmount (RecyclerView recyclerView, RecyclerView.Adapter adapter, Vector2Df value) {
        if(this.roomToScroll == false){
            return new Vector2Df(0,0);
        }

        float orientationMove = (this.orientation == VERTICAL) ? value.y : value.x;
        orientationMove*=-1;

        if(this.currentViewHolders.size() > 0){
            if(this.currentViewHolders.getFirst().index == 0 && orientationMove < 0){
                RecyclerView.ViewHolder startViewHolders = this.currentViewHolders.getFirst();
                if(this.orientation == VERTICAL){
                    if( (recyclerView.positionOffset.y + orientationMove) <= startViewHolders.view.position.y){
                        return new Vector2Df(0, ((recyclerView.positionOffset.y)- startViewHolders.view.position.y)*-1);
                    }
                } else {
                    if( (recyclerView.positionOffset.x + orientationMove) < startViewHolders.view.position.x){
                        return new Vector2Df(((recyclerView.positionOffset.x)- startViewHolders.view.position.x)*-1, 0);
                    }
                }
            }
            else if(this.currentViewHolders.getLast().index == (adapter.getItemCount() -1) && orientationMove > 0){

                RecyclerView.ViewHolder endViewHolders = this.currentViewHolders.getLast();

                if(this.orientation == VERTICAL){
                    if((recyclerView.positionOffset.y + recyclerView.dimensions.y + orientationMove) > ( endViewHolders.view.position.y +  endViewHolders.view.dimensions.y)){
                        return   new Vector2Df(0, (endViewHolders.view.position.y +  endViewHolders.view.dimensions.y) - (recyclerView.positionOffset.y + recyclerView.dimensions.y));
                    }
                } else {
                    if((recyclerView.positionOffset.x + recyclerView.dimensions.x  + orientationMove) > ( endViewHolders.view.position.x +  endViewHolders.view.dimensions.x)){
                        return new Vector2Df( (endViewHolders.view.position.x +  endViewHolders.view.dimensions.x) - (recyclerView.positionOffset.x + recyclerView.dimensions.x), 0);
                    }
                }
            }
        }

        if(this.orientation == VERTICAL){
            return new Vector2Df(0, orientationMove);
        } else {
            return new Vector2Df(orientationMove,0);
        }
    }


    @Override
    public void onDataSetChange(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        if(currentViewHolders.size() == 0) {
            walkViewsToEnd(recyclerView, adapter);

            this.roomToScroll = isRoomToScroll(recyclerView);
        }

        for(RecyclerView.ViewHolder viewHolder : currentViewHolders){
            adapter.bindViewHolder(viewHolder, viewHolder.index);
        }
    }

    protected float getHighestPoint (RecyclerView.ViewHolder viewHolder){
        if(this.orientation == VERTICAL){
            return viewHolder.view.getPosition().y;
        } else {
            return viewHolder.view.getPosition().x;
        }
    }

    protected float geLowestPoint(RecyclerView.ViewHolder viewHolder){
        if(this.orientation == VERTICAL){
            return viewHolder.view.getPosition().y + viewHolder.view.getDimensions().y;
        } else {
            return viewHolder.view.getPosition().x  + viewHolder.view.getDimensions().x;
        }
    }

    protected RecyclerView.ViewHolder getNewViewHolder(RecyclerView recyclerView, RecyclerView.Adapter adapter, int type){
        RecyclerView.ViewHolder viewHolder = this.recycler.getViewHolder(type);
        if(viewHolder == null){
            viewHolder = adapter.createViewHolder(recyclerView, type);
        }
        viewHolder.itemViewType = type;
        return viewHolder;
    }

    //start from the lowest position to start of view
    protected void walkViewsToStart(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        float highestPoint = 0;
        RecyclerView.ViewHolder highestChild = null;
        if(this.currentViewHolders.size() > 0){
            highestChild = this.currentViewHolders.get(0);
            highestPoint = getHighestPoint(highestChild);
        }
        else {
            return;
        }

        float orientationOffset = (this.orientation == VERTICAL) ? recyclerView.positionOffset.y : recyclerView.positionOffset.x;

        while(highestPoint > orientationOffset){

            if(highestChild.index == 0){
                return;
            }
            int newIndex = highestChild.index -1;
            highestChild = getNewViewHolder(recyclerView, adapter, adapter.getItemViewType(newIndex));
            highestChild.index = newIndex;
            this.currentViewHolders.addFirst(highestChild);

            initChildViewStart(recyclerView, adapter, highestChild, highestPoint);
            highestPoint = getHighestPoint(highestChild);
        }
    }

    //start from the highest position to end of view
    protected void walkViewsToEnd(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        float lowestPoint = 0;
        RecyclerView.ViewHolder lowestChild = null;

        if(this.currentViewHolders.size() > 0){
            lowestChild = this.currentViewHolders.getLast();
            lowestPoint = geLowestPoint(lowestChild);
        }
        else if(adapter.getItemCount() > 0){
            lowestChild = getNewViewHolder(recyclerView, adapter, adapter.getItemViewType(0));
            lowestChild.index = 0;
            initChildViewEnd(recyclerView, adapter, lowestChild, lowestPoint);
            this.currentViewHolders.addLast(lowestChild);
            lowestPoint = geLowestPoint(lowestChild);

        } else {
            return;
        }

        float maxPosition;
        if(this.orientation == VERTICAL){
            maxPosition = recyclerView.dimensions.y + recyclerView.positionOffset.y;
        } else {
            maxPosition = recyclerView.dimensions.x + recyclerView.positionOffset.x;
        }


        while(lowestPoint < maxPosition){

            if(lowestChild.index >= (adapter.getItemCount() - 1)){
                return;
            }
            int newIndex = lowestChild.index +1;
            lowestChild = getNewViewHolder(recyclerView, adapter, adapter.getItemViewType(newIndex));
            lowestChild.index = newIndex;
            initChildViewEnd(recyclerView, adapter, lowestChild, lowestPoint);
            this.currentViewHolders.addLast(lowestChild);
            lowestPoint = geLowestPoint(lowestChild);
        }
        return;
    }

    protected void removeViewHolder(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        this.currentViewHolders.remove(viewHolder);
        this.recycler.addViewHolder(viewHolder);
        recyclerView.removeView(viewHolder.view);
    }

    protected void removeViewsOutOfBounds(RecyclerView recyclerView){

        viewCollisionBox.initTopLeft(recyclerView.positionOffset, recyclerView.dimensions);

        CollisionBox childCollisionBox = new CollisionBox();

        for(int i = 0; i < this.currentViewHolders.size(); i++){
            RecyclerView.ViewHolder child = this.currentViewHolders.get(i);
            childCollisionBox.initTopLeft(child.view.position, child.view.dimensions);
            if(this.viewCollisionBox.boxCollisionTest(childCollisionBox) == false){
                removeViewHolder(recyclerView, child);
                i--;
            }
        }
    }

    protected void onLayoutChild(RecyclerView recyclerView, View view, float top){
        view.onMeasure((int)recyclerView.dimensions.x, (int)recyclerView.dimensions.y);
        if(this.orientation == this.VERTICAL){
            view.onLayout((int)recyclerView.dimensions.x, view.getMeasuredHeight(), 0, (int)top);
        }
        else {
            view.onLayout(view.getMeasuredWidth(), (int)recyclerView.dimensions.y, (int)top, 0);
        }
    }

    protected void initChildViewEnd(RecyclerView recyclerView, RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, float top){
        adapter.bindViewHolder(viewHolder, viewHolder.index);
        onLayoutChild(recyclerView, viewHolder.view, top);
        recyclerView.addView(viewHolder.view);
    }

    protected void initChildViewStart(RecyclerView recyclerView, RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, float bottom){
        View newView = viewHolder.view;
        adapter.bindViewHolder(viewHolder, viewHolder.index);
        viewHolder.view.onMeasure((int)recyclerView.dimensions.x, (int)recyclerView.dimensions.y);

        if(this.orientation == this.VERTICAL){
            viewHolder.view.onLayout( (int)recyclerView.dimensions.x, newView.getMeasuredHeight(), 0, (int)bottom- newView.getMeasuredHeight());
        } else {
            viewHolder.view.onLayout(newView.getMeasuredWidth(), (int)recyclerView.dimensions.y, (int)bottom- newView.getMeasuredWidth(), 0);
        }
        recyclerView.addView(newView);
    }

    protected void scrollRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Vector2Df scrollAmount){
        recyclerView.positionOffset = recyclerView.positionOffset.add(scrollAmount);

        if( ((this.orientation == VERTICAL) ? scrollAmount.y : scrollAmount.x) < 0){
            walkViewsToStart(recyclerView, adapter);
        } else {
            walkViewsToEnd(recyclerView, adapter);
        }

        removeViewsOutOfBounds(recyclerView);
    }

    private boolean dispatchChildTouchEvent(MotionEvent motionEvent, View view){
        MotionEvent newMotionEvent = null;
        try {
            newMotionEvent = (MotionEvent) motionEvent.clone();
        }
        catch (CloneNotSupportedException e){
            return false;
        }
        return view.dispatchTouchEvent(newMotionEvent);
    }

    public boolean dispatchTouchEvent(RecyclerView recyclerView, RecyclerView.Adapter adapter, MotionEvent motionEvent){
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            this.touchTarget = null;
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            Vector2Df scrollAmount = moveAmount(recyclerView, adapter, motionEvent.getMotion());
            scrollRecyclerView(recyclerView, adapter, scrollAmount);
            this.touchTarget = null;
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_SCROLL){
            Vector2Df scrollAmount = moveAmount(recyclerView, adapter, motionEvent.getMotion()).multiply(new Vector2Df(10, 10));
            scrollRecyclerView(recyclerView, adapter, scrollAmount);
            this.touchTarget = null;
        }

        if(touchTarget != null){
            if(motionEvent.getAction() == MotionEvent.ACTION_UP &&  recyclerView.getOnItemClickListener() != null){
                recyclerView.getOnItemClickListener().onItemClick(this.touchTarget.index, this.touchTarget.view);
            }
            return touchTarget.view.dispatchTouchEvent(motionEvent);
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            for(RecyclerView.ViewHolder viewHolder : this.currentViewHolders){
                if(dispatchChildTouchEvent(motionEvent, viewHolder.view)){
                    this.touchTarget = viewHolder;
                }
            }
        }
        return true;
    }
}
