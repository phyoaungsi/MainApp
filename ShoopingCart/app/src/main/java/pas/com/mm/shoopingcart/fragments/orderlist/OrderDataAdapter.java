package pas.com.mm.shoopingcart.fragments.orderlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.model.Item;

/**
 * Created by phyoa on 12/22/2016.
 */

public class OrderDataAdapter extends RecyclerView.Adapter<OrderDataAdapter.ViewHolder> {
    private List<Item> mDataset;



    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
       public TextView mTextView;
       public TextView priceTextView;
        public ImageView foodImageView;
        public ViewHolder(View v) {
        super(v);
         mTextView = (TextView)itemView.findViewById(R.id.info_text);;
            priceTextView = (TextView)itemView.findViewById(R.id.price_text);
            foodImageView = (ImageView)itemView.findViewById(R.id.foodImage);
       }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderDataAdapter(List<Item> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_orders, parent, false);
        // set the view's size, margins, paddings and layout parameters

        OrderDataAdapter.ViewHolder vh = new OrderDataAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTitle());
        holder.priceTextView.setText(mDataset.get(position).getAmount().toString());

        Picasso.with(this.getmContext())
                .load(mDataset.get(position).getImgUrl().toString())
                .resize(100, 100)
                .centerCrop()
                .into(holder.foodImageView);
        holder.priceTextView.setText(mDataset.get(position).getAmount().toString()+ " "+getmContext().getResources().getString(R.string.currency));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}

