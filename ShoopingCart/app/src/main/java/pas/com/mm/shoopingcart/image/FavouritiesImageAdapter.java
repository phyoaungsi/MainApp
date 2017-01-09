package pas.com.mm.shoopingcart.image;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.util.ImageFetcher;

/**
 * Created by phyo on 14/10/2016.
 */
public class FavouritiesImageAdapter extends MobileImageAdapter {

    private static final String PREFS_NAME ="FAVLIST" ;
    List<String> values;
    public FavouritiesImageAdapter(Context c, ImageFetcher fetcher) {
       super(c,fetcher);
        values=new ArrayList<String>();
        SharedPreferences settings = c.getSharedPreferences("PAS", 0);
       Map<String,?> valMap= settings.getAll();

        for(String s:valMap.keySet()) {
          values.add( (String)valMap.get(s));
        }

    }

    @Override
    public int getCount() {
        // If columns have yet to be determined, return no items



      return values.size();
    }

    public String getImageUrl(int position)
    {
       Object[] string= values.toArray();
        String jsonString=(String)string[position];
        Gson json=new Gson();
        Item obj = json.fromJson(jsonString, Item.class);
        String[] urls=obj.imgUrl.split(" ");
        return urls[0];
    }

    public String getImageDescription(int position)
    {
        Object[] string= values.toArray();
        String jsonString=(String)string[position];
        Gson json=new Gson();
        Item obj = json.fromJson(jsonString, Item.class);
        return obj.getDescription();
    }

    public String getPrice(int position)
    {
        Object[] string= values.toArray();
        String jsonString=(String)string[position];
        Gson json=new Gson();
        Item obj = json.fromJson(jsonString, Item.class);
        return obj.getAmount().toString();
    }

    public String getDiscountAmount(int position)
    {

        Object[] string= values.toArray();
        String jsonString=(String)string[position];
        Gson json=new Gson();
        Item obj = json.fromJson(jsonString, Item.class);
        return obj.getAmount().toString();
    }

    @Override
    protected View getLayout(LayoutInflater inflater) {
        View gridView;
        gridView = inflater.inflate(R.layout.grid_item_horizontal, null);
        return gridView;
    }
}
