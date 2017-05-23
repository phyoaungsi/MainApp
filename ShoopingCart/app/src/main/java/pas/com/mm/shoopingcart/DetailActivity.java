package pas.com.mm.shoopingcart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;

import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.fragments.DescriptionFragment;
import pas.com.mm.shoopingcart.image.Images;
import pas.com.mm.shoopingcart.image.ZoomImageView;
import pas.com.mm.shoopingcart.util.FontUtil;
import pas.com.mm.shoopingcart.util.ImageCache;
import pas.com.mm.shoopingcart.util.ImageFetcher;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener, DescriptionFragment.OnFragmentInteractionListener {
    private static final String TAG = "DetailActivity";
    // Button button;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       context=this;
      String object= this.getIntent().getStringExtra("DETAIL_ITEM");
      Gson gson=new Gson();
      this.setItem((Item) gson.fromJson(object,Item.class));
       DetailFragment df= new DetailFragment();
        df.setItem(this.getItem());
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, df, TAG);

            ft.commit();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("DetailActivity","Destory");
    }
/**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
**/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== android.R.id.home) {


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            DescriptionFragment f = (DescriptionFragment) getSupportFragmentManager().findFragmentByTag("DESC");
           if(f!=null) {
               ft.setCustomAnimations(R.anim.exit_slide_out_up, R.anim.exit_slide_in_up);

               ft.remove(f);
               ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

               ft.commit();
               getSupportFragmentManager().popBackStack();
               try {
                  this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                   this.getSupportActionBar().setDisplayShowHomeEnabled(true);
               }catch(Exception e)
               {
                   Log.d("--------",e.getMessage());
               }
           }
            else{
            //  NavUtils.navigateUpFromSameTask(this);
               super.onBackPressed();
               return true;
           }

            return true;
        }
        if (id == R.id.action_contact) {


        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

      if(this.getIntent().getStringExtra("ZOOMED")!=null&&this.getIntent().getStringExtra("ZOOMED").equals("y"))
      {
         ImageView expandedImageView = (ImageView ) findViewById(R.id.expanded_image);
         expandedImageView.setVisibility(View.GONE);
          ImageView slideImageView = (ImageView) findViewById(R.id.slide1);
          slideImageView.setAlpha(1f);
          getIntent().putExtra("ZOOMED","n");
      }else {
          super.onBackPressed();
      }
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    private Item item;
}

