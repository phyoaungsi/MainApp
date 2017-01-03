package pas.com.mm.shoopingcart.activities.saveitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import pas.com.mm.shoopingcart.DetailActivity;
import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.image.FavouritiesImageAdapter;
import pas.com.mm.shoopingcart.image.MobileImageAdapter;

public class SaveItemActivity extends AppCompatActivity {
    private GridView gridview;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext=this;

        gridview = (GridView) findViewById(R.id.gridview_save);
        MobileImageAdapter imageAdapter=new FavouritiesImageAdapter(this,null);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //   Toast.makeText(getActivity(), "" + position,
                //         Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                Item item= DbSupport.list.get(position);
                Gson gson=new Gson();
                String objStr= gson.toJson(item);
                intent.putExtra("DETAIL_ITEM",objStr);
                //intent.putExtra("POSITION", id);
                startActivity(intent);
                //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new DetailFragment()).commit();

            }
        });
    }



}
