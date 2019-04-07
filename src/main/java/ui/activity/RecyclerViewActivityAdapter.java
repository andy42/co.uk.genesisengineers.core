package ui.activity;

import ui.view.*;

public class RecyclerViewActivityAdapter extends RecyclerView.Adapter {

    LayoutInflater layoutInflater = new LayoutInflater();

    public RecyclerViewActivityAdapter(){

    }

    @Override
    public RecyclerView.ViewHolder createViewHolder (ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate("item_view.xml",null));
    }

    @Override
    public void bindViewHolder (RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount () {
        return 100;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView valueTextView;

        public ViewHolder(View view){
            super(view);
            titleTextView = (TextView)view.findViewById("title");
            valueTextView = (TextView)view.findViewById("value");
        }
        public void bind(int position){
            titleTextView.setText("title : "+position);
            valueTextView.setText("");
        }
    }
}
