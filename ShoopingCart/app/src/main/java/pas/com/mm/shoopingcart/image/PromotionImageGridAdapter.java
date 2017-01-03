package pas.com.mm.shoopingcart.image;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.util.ImageFetcher;
import pas.com.mm.shoopingcart.util.ImageWorker;

/**
 * Created by phyoa on 12/10/2016.
 */

public class PromotionImageGridAdapter extends MobileImageAdapter  {


    public PromotionImageGridAdapter(Context c, ImageFetcher fetcher,List<Item> list) {
        super(c, fetcher);
        super.list=list;
    }
}
