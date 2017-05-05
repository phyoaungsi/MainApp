package pas.com.mm.shoopingcart.image;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import pas.com.mm.shoopingcart.R;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.util.FontUtil;
import pas.com.mm.shoopingcart.util.ImageFetcher;
import pas.com.mm.shoopingcart.util.ImageWorker;

/**
 * Created by phyo on 22/07/2016.
 */
public class MobileImageAdapter extends BaseAdapter {
    private int mNumColumns = 1;
    protected Context mContext;
    private ImageFetcher mImageFetcher;
    protected List<Item> list;
    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public MobileImageAdapter(Context c, ImageFetcher fetcher) {
        mContext = c;
        mImageFetcher=fetcher;
        this.list=DbSupport.list;
    }

    @Override
    public int getCount() {
        // If columns have yet to be determined, return no items
        if (getNumColumns() == 0) {
            return 0;
        }

        // Size + number of columns for top empty row
        //return Images.imageThumbUrls.length + mNumColumns;
        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // ImageView imageView;
        View gridView;
        //    if (convertView == null) {
        // if it's not recycled, initialize some attributes

        // gridView = new View(mContext);
        gridView = getLayout(inflater);


        TextView textView = (TextView) gridView.findViewById(R.id.grid_caption);
       FontUtil.setZawGyiText(mContext,textView,getImageDescription( position));
        TextView textViewPrice = (TextView) gridView.findViewById(R.id.grid_price);
        textViewPrice.setText(getPrice( position)+" "+getmContext().getResources().getString(R.string.currency));

        if(list.get(position).getDiscount()>0 && list.get(position).getDiscount()<list.get(position).getAmount()) {
            TextView t = (TextView) gridView.findViewById(R.id.usualPrice);
            textViewPrice.setText(getDiscountAmount(position) + " " + getmContext().getResources().getString(R.string.currency));
            t.setText(getPrice(position) + " " + getmContext().getResources().getString(R.string.currency));
            t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            t.setVisibility(View.VISIBLE);
        }
        final ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_image);

      //  final ProgressBar pb=(ProgressBar) gridView.findViewById(R.id.progressbar_grid_img);
        ImageWorker.OnImageLoadedListener imageListener=new ImageWorker.OnImageLoadedListener() {
            @Override
            public void onImageLoaded(boolean success) {
          //      pb.setVisibility(View.GONE);
           //     imageView.setVisibility(View.VISIBLE);
            }
        };
        //imageView.setImageResource(mThumbIds[position]);
        if (convertView == null) { // if it's not recycled, instantiate and initialize
           //imageView = new RecyclingImageView(mContext);
        //    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setLayoutParams(gridView.getLayoutParams());

            Log.i("test","getview new"+position);
        } else { // Otherwise re-use the converted view
          //  gridView = (LinearLayout) convertView;
          //  AbsListView.LayoutParams layoutParms= new AbsListView.LayoutParams(400,600);
                 //   ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         //  imageView.setLayoutParams(layoutParms);

          //  mImageFetcher.loadImage(Images.imageThumbUrls[position], imageView);
          //  mImageFetcher.loadImage(url, imageView,imageListener);

            Log.i("test","getview***"+position+" and length "+list.size());
        }
        String url="https://drive.google.com/uc?export=download&id=0B_9ZBXw3kTLIN01ibXRqUHV5Umc";
        url=getImageUrl(position);
        ViewGroup.LayoutParams param= imageView.getLayoutParams();
//        pb.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        if(isOnline()) {
            Picasso.with(this.getmContext())
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                     .networkPolicy(NetworkPolicy.NO_STORE)
                    .resize(400, 400)
                    .centerCrop().transform(new RoundedCornersTransform())
                    .into(imageView);
        }else {
            Picasso.with(this.getmContext())
                    .load(url)
                    //  .placeholder(R.drawable.placeholder)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(400, 400)
                    .centerCrop().transform(new RoundedCornersTransform())
                    .into(imageView);
        }
      //  mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
       // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
     //   imageView.setPadding(0, 0, 0, 0);

        return gridView;
    }

    protected View getLayout(LayoutInflater inflater) {
        View gridView;
        gridView = inflater.inflate(R.layout.grid_item, null);
        return gridView;
    }


    public String getImageUrl(int position)
    {
       String[] urls= list.get(position).imgUrl.split(" ");
       return urls[0];
    }

    public String getImageDescription(int position)
    {
        Log.d("ADAPTER","Invalid Index?"+position);
        return list.get(position).getTitle();
    }

    public String getPrice(int position)
    {
        return list.get(position).getAmount().toString();
    }

    public String getDiscountAmount(int position)
    {

        return list.get(position).getDiscount().toString();
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) this.getmContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){

            return false;
        }
        return true;
    }
}