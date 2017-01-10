package pas.com.mm.shoopingcart.splash;

/**
 * Created by phyo on 10/01/2017.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.gson.Gson;

import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.model.Config;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.Model;

/**
 * Created by phyo on 10/01/2017.
 */

public class NotiPromoDbListener implements DBListenerCallback {
    private  SplashScreen mContext;

    public NotiPromoDbListener(SplashScreen splashScreen) {
        this.mContext=splashScreen;

    }

    @Override
    public void LoadCompleted(boolean b) {

    }

    @Override
    public void receiveResult(Model model) {
        Gson gson=new Gson();
        Config config=(Config) model;
        Intent intent = new Intent(mContext, OpenNotification.class);

        //intent.putExtra("notificationBodys",config.get);
        intent.putExtra("imageUrl",config.getPromotionImage());
        intent.putExtra("clicked_noti",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }
}