package pas.com.mm.shoopingcart.image;


import android.app.Activity;
import android.content.Context;
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

import com.google.gson.Gson;

import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.util.ImageFetcher;
import pas.com.mm.shoopingcart.util.ImageWorker;

/**
 * Created by phyo on 22/07/2016.
 */
public class MobileImageAdapter extends BaseAdapter {
    private int mNumColumns = 1;
    private Context mContext;
    private ImageFetcher mImageFetcher;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public MobileImageAdapter(Context c, ImageFetcher fetcher) {
        mContext = c;
        mImageFetcher=fetcher;
    }

    @Override
    public int getCount() {
        // If columns have yet to be determined, return no items
        if (getNumColumns() == 0) {
            return 0;
        }

        // Size + number of columns for top empty row
        //return Images.imageThumbUrls.length + mNumColumns;
        return DbSupport.list.size();
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
        gridView = inflater.inflate(R.layout.grid_item, null);


        TextView textView = (TextView) gridView.findViewById(R.id.grid_caption);
        textView.setText(getImageDescription( position));

        final ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_image);

        final ProgressBar pb=(ProgressBar) gridView.findViewById(R.id.progressbar_grid_img);
        ImageWorker.OnImageLoadedListener imageListener=new ImageWorker.OnImageLoadedListener() {
            @Override
            public void onImageLoaded(boolean success) {
                pb.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
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
          String url="https://drive.google.com/uc?export=download&id=0B_9ZBXw3kTLIN01ibXRqUHV5Umc";
            url=getImageUrl(position);
          //  mImageFetcher.loadImage(Images.imageThumbUrls[position], imageView);
            mImageFetcher.loadImage(url, imageView,imageListener);
            Log.i("test","getview***"+position);
        }


      //  mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
       // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
     //   imageView.setPadding(0, 0, 0, 0);

        return gridView;
    }


    public String getImageUrl(int position)
    {
       return DbSupport.list.get(position).imgUrl;
    }

    public String getImageDescription(int position)
    {
        return DbSupport.list.get(position).getDescription();
    }

    public int getNumColumns() {
        return mNumColumns;
    }
}