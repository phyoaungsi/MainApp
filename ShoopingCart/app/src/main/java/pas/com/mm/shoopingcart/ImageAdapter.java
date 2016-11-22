package pas.com.mm.shoopingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by phyo on 30/07/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
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

            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_caption);
            textView.setText("Temporay position >"+position);

           ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_image);
           // imageView = new ImageView(mContext);
           // gridView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setImageResource(mThumbIds[position]);
      //  } else {
        //    gridView = (View) convertView;
       // }


        return gridView;
    }

    // references to our images
    private Integer[] mThumbIds = {

    };
}