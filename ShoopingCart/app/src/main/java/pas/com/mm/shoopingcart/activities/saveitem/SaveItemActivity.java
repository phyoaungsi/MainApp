package pas.com.mm.shoopingcart.activities.saveitem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.Model;
import pas.com.mm.shoopingcart.image.FavouritiesImageAdapter;
import pas.com.mm.shoopingcart.image.MobileImageAdapter;
import pas.com.mm.shoopingcart.util.FontUtil;

public class SaveItemActivity extends AppCompatActivity implements DBListenerCallback {
    private GridView gridview;
    private Context mContext;
    private DbSupport db=new DbSupport();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FontUtil.setText(this,  toolbar, true);
        toolbar.setTitle(this.getResources().getString(R.string.favourities));
        setSupportActionBar(toolbar);
        FontUtil.setText(this.getBaseContext(),toolbar,false);
        mContext=this;

        gridview = (GridView) findViewById(R.id.gridview_save);
        MobileImageAdapter imageAdapter=new FavouritiesImageAdapter(this,null);
        gridview.setAdapter(imageAdapter);
       final DBListenerCallback cb=this;
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("PAS", 0);
                Map<String,?> valMap= settings.getAll();
                Object[] keys=valMap.keySet().toArray();
                db.getItemById((String)keys[position],cb);
            }
        });
    }


    @Override
    public void LoadCompleted(boolean b) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        Item item= db.getResult();
        Gson gson=new Gson();
        String objStr= gson.toJson(item);
        intent.putExtra("DETAIL_ITEM",objStr);
        //intent.putExtra("POSITION", id);
        startActivity(intent);
    }

    @Override
    public void receiveResult(Model model) {

    }
}
