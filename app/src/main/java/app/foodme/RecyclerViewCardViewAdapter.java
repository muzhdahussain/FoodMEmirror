package app.foodme;

/**
 *  Code adapted from: https://androidjson.com/recyclerview-json-listview-example/
 *
 *  Sets up the adapter for the RecyclerView, which creates the display for the retrieved database items,
 *  and sets layout options using the cardview layout.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import java.util.List;

public class RecyclerViewCardViewAdapter extends RecyclerView.Adapter<RecyclerViewCardViewAdapter.ViewHolder> {

    Context context;
    List<Item> itemList;


    public RecyclerViewCardViewAdapter(List<Item> getDataAdapter, Context context){

        super();
        this.itemList = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item getDataAdapter1 =  itemList.get(position);
        holder.itemName.setText(getDataAdapter1.getItemName());



    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName;

        public ViewHolder(View itemView) {

            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.TextViewCard);
        }
    }
}
