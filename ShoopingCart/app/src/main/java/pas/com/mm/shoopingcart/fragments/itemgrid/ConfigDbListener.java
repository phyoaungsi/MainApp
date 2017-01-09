package pas.com.mm.shoopingcart.fragments.itemgrid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.model.Config;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.Model;
import pas.com.mm.shoopingcart.image.RoundedCornersTransform;
import pas.com.mm.shoopingcart.util.PreferenceUtil;

/**
 * Created by phyo on 09/01/2017.
 */

public class ConfigDbListener implements DBListenerCallback {



    private View view;


    private Context mContext;

    @Override
    public void LoadCompleted(boolean b) {

    }

    @Override
    public void receiveResult(Model model) {

       final Config c=(Config) model;
        ImageView image = (ImageView) this.getView();
        if(c.getPromotionOn()==true) {

            image.setVisibility(View.VISIBLE);
            Picasso.with(this.getmContext())
                    .load(c.getPromotionBanner())
                    .into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OpenNotification.class);

                    //intent.putExtra("DETAIL_ITEM", "");
                    intent.putExtra("imageUrl", c.getPromotionImage());
                    Activity a=(Activity) mContext;
                    a.startActivity(intent);
                }
            });

        }
        else{
            image.setVisibility(View.GONE);
        }
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

}
