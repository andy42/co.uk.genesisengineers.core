package co.uk.genesisengineers.core.ui.view.recyclerLayoutManager;

import co.uk.genesisengineers.core.ui.view.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class GridLayoutManager extends LinearLayoutManager {

    protected ArrayList<LinkedList<RecyclerView.ViewHolder>> currentViewHoldersColumns = new ArrayList<LinkedList<RecyclerView.ViewHolder>>();
    protected ArrayList<RecyclerView.ViewHolder> viewHolderToAdd = new ArrayList<RecyclerView.ViewHolder>();


    protected int columnCount = 1;

    public GridLayoutManager(int orientation, int columnCount){
        super(orientation);
        this.columnCount = columnCount;

        for(int i=0; i< columnCount; i++){
            currentViewHoldersColumns.add(new LinkedList<RecyclerView.ViewHolder>());
        }
    }

    protected RecyclerView.ViewHolder addRowToStart(RecyclerView recyclerView, RecyclerView.Adapter adapter, int startIndex, float lowestPoint){
        RecyclerView.ViewHolder lastChildAdded = null;
        float maxDimension = 0;

        float columnWidth = (this.orientation == this.VERTICAL) ? recyclerView.getDimensions().x/columnCount : recyclerView.getDimensions().y/columnCount;
        viewHolderToAdd.clear();

        for(int i=0; i< columnCount; i++){
            int newIndex = startIndex - i;
            if(newIndex < 0){
                break;
            }

            if( (startIndex + i) < adapter.getItemCount()){
                lastChildAdded = getNewViewHolder(recyclerView, adapter, adapter.getItemViewType(newIndex));
                lastChildAdded.index = newIndex;

                adapter.bindViewHolder(lastChildAdded, newIndex);

                if(this.orientation == this.VERTICAL){
                    lastChildAdded.view.onMeasure((int)columnWidth, (int)recyclerView.getDimensions().y);
                    if(lastChildAdded.view.getMeasuredHeight() > maxDimension){
                        maxDimension = lastChildAdded.view.getMeasuredHeight();
                    }
                } else {
                    lastChildAdded.view.onMeasure((int)recyclerView.getDimensions().x, (int)columnWidth);
                    if(lastChildAdded.view.getMeasuredWidth() > maxDimension){
                        maxDimension = lastChildAdded.view.getMeasuredWidth();
                    }
                }
                viewHolderToAdd.add(lastChildAdded);
            }
        }

        for(int i=0; i< viewHolderToAdd.size(); i++){
            RecyclerView.ViewHolder viewHolder = viewHolderToAdd.get(i);
            if(this.orientation == this.VERTICAL){
                viewHolder.view.onLayout( (int)columnWidth, (int)maxDimension,(int)columnWidth*(columnCount -i -1), (int)(lowestPoint - maxDimension) );
            } else {
                viewHolder.view.onLayout((int)maxDimension, (int)columnWidth, (int)(lowestPoint - maxDimension), (int)columnWidth*(columnCount -i -1));
            }

            recyclerView.addView(viewHolder.view);
            this.currentViewHolders.addFirst(viewHolder);
        }

        return lastChildAdded;
    }

    protected RecyclerView.ViewHolder addRowToEnd(RecyclerView recyclerView, RecyclerView.Adapter adapter, int startIndex, float highestPoint){
        RecyclerView.ViewHolder lastChildAdded = null;
        float maxDimension = 0;

        float columnWidth = (this.orientation == this.VERTICAL) ? recyclerView.getDimensions().x/columnCount : recyclerView.getDimensions().y/columnCount;
        viewHolderToAdd.clear();

        for(int i=0; i< columnCount; i++){
            int newIndex = i + startIndex;
            if( (startIndex + i) < adapter.getItemCount()){
                lastChildAdded = getNewViewHolder(recyclerView, adapter, adapter.getItemViewType(newIndex));
                lastChildAdded.index = newIndex;

                adapter.bindViewHolder(lastChildAdded, newIndex);

                if(this.orientation == this.VERTICAL){
                    lastChildAdded.view.onMeasure((int)columnWidth, (int)recyclerView.getDimensions().y);
                    if(lastChildAdded.view.getMeasuredHeight() > maxDimension){
                        maxDimension = lastChildAdded.view.getMeasuredHeight();
                    }
                } else {
                    lastChildAdded.view.onMeasure((int)recyclerView.getDimensions().x, (int)columnWidth);
                    if(lastChildAdded.view.getMeasuredWidth() > maxDimension){
                        maxDimension = lastChildAdded.view.getMeasuredWidth();
                    }
                }
                viewHolderToAdd.add(lastChildAdded);
            }
        }

        for(int i=0; i< viewHolderToAdd.size(); i++){
            RecyclerView.ViewHolder viewHolder = viewHolderToAdd.get(i);
            if(this.orientation == this.VERTICAL){
                viewHolder.view.onLayout( (int)columnWidth, (int)maxDimension,(int)columnWidth*i, (int)highestPoint);
            } else {
                viewHolder.view.onLayout((int)maxDimension, (int)columnWidth, (int)highestPoint, (int)columnWidth*i);
            }

            recyclerView.addView(viewHolder.view);
            this.currentViewHolders.addLast(viewHolder);
        }

        return lastChildAdded;
    }

    //start from the highest position to end of view
    @Override
    protected void walkViewsToEnd(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        float lowestPoint = 0;
        RecyclerView.ViewHolder lowestChild = null;

        if(this.currentViewHolders.size() > 0){
            lowestChild = this.currentViewHolders.getLast();
            lowestPoint = geLowestPoint(lowestChild);
        }
        else if(adapter.getItemCount() > 0){
            lowestChild = addRowToEnd(recyclerView,adapter,0,0);
            lowestPoint = geLowestPoint(lowestChild);

        } else {
            return;
        }

        float maxPosition;
        if(this.orientation == VERTICAL){
            maxPosition = recyclerView.getDimensions().y + recyclerView.getPositionOffset().y;
        } else {
            maxPosition = recyclerView.getDimensions().x + recyclerView.getPositionOffset().x;
        }


        while(lowestPoint < maxPosition){

            if(lowestChild.index >= (adapter.getItemCount() - 1)){
                return;
            }
            lowestChild = addRowToEnd(recyclerView,adapter,lowestChild.index +1,lowestPoint);
            lowestPoint = geLowestPoint(lowestChild);
        }
        return;
    }

    //start from the lowest position to start of view
    @Override
    protected void walkViewsToStart(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        float highestPoint = 0;
        RecyclerView.ViewHolder highestChild = null;
        if(this.currentViewHolders.size() > 0){
            highestChild = this.currentViewHolders.getFirst();
            highestPoint = getHighestPoint(highestChild);
        }
        else {
            return;
        }

        float orientationOffset = (this.orientation == VERTICAL) ? recyclerView.getPositionOffset().y : recyclerView.getPositionOffset().x;

        while(highestPoint > orientationOffset){

            if(highestChild.index == 0){
                return;
            }
            int newIndex = highestChild.index -1;
            highestChild = addRowToStart(recyclerView,adapter, newIndex, highestPoint);
            if(highestChild != null){
                highestPoint = getHighestPoint(highestChild);
                return;
            }

        }
    }
}
