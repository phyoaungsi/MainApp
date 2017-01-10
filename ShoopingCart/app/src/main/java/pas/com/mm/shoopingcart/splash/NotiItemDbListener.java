package pas.com.mm.shoopingcart.splash;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.ItemGridView;
import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.Model;

/**
 * Created by phyo on 10/01/2017.
 */

public class NotiItemDbListener implements DBListenerCallback {
    private  SplashScreen mContext;

    public NotiItemDbListener(SplashScreen splashScreen) {
        this.mContext=splashScreen;
    }

    @Override
    public void LoadCompleted(boolean b) {

    }

    @Override
    public void receiveResult(Model model) {
        Gson gson=new Gson();
        Item item=(Item) model;
        String objStr= gson.toJson(item);
        Activity a=(Activity) mContext;
        Intent intent1 = new Intent(a, DetailActivity.class);
       // intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK );

        intent1.putExtra("DETAIL_ITEM",objStr);
        intent1.putExtra("clicked_noti",true);
      // a.startActivity(intent1);
        TaskStackBuilder.create(a)
                .addParentStack(ItemGridView.class)
                .addNextIntent(intent1)
                .startActivities();

    }
}
