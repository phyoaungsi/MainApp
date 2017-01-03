package pas.com.mm.shoopingcart.util;

import android.content.SharedPreferences;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.model.NotificationModel;

/**
 * Created by phyo on 12/12/2016.
 */

public class PreferenceUtil {


    public static void saveNoti(NotificationModel noti)
    {

    }


    public static NotificationModel retrieveNoti()
    {
            return null;
    }

    public static void saveLastAccessItem(SharedPreferences sharedPref,String key,String value)
    {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getLastAccessItem(SharedPreferences sharedPref,String key)
    {


       String defaultValue="not_found";
        return sharedPref.getString(key, defaultValue);
    }

}
