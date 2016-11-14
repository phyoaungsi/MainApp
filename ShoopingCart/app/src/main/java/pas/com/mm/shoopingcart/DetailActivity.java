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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;

import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.fragments.DescriptionFragment;
import pas.com.mm.shoopingcart.image.Images;
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

      //  mDrawerLayout =new  DrawerLayout(this);
      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);
      //  mDrawerList =new ListView(this);
      //  mDrawerList = (ListView) findViewById(R.id.left_drawer1);
      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       // fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View view) {
         //       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});



        //drawer layout


        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_drawer, mPlanetTitles));
        // Set the list's click listener
       // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
/**
       // mTitle = mDrawerTitle = getTitle();
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);
       // mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            //public void onDrawerClosed(View view) {
                //super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
              //  invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            //}

            /** Called when a drawer has settled in a completely open state. */
          //  public void onDrawerOpened(View drawerView) {
        //        super.onDrawerOpened(drawerView);
               // getActionBar().setTitle(mDrawerTitle);
      //          invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    //        }
  //      };

        // Set the drawer toggle as the DrawerListener
//        mDrawerLayout.setDrawerListener(mDrawerToggle);



        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout1);
        //mDrawerToggle = new ActionBarDrawerToggle(
                //this,                  /* host Activity */
                //mDrawerLayout,         /* DrawerLayout object */
              //  R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
            //    R.string.drawer_open,  /* "open drawer" description */
          //      R.string.drawer_close  /* "close drawer" description */
        //) {

            /** Called when a drawer has settled in a completely closed state. */
        //    public void onDrawerClosed(View view) {
      //          super.onDrawerClosed(view);
    //            getActionBar().setTitle(mTitle);
  //          }
//
          //  /** Called when a drawer has settled in a completely open state. */
        //    public void onDrawerOpened(View drawerView) {
      //          super.onDrawerOpened(drawerView);
    //            getActionBar().setTitle(mDrawerTitle);
  //          }
//        };

        // Set the drawer toggle as the DrawerListener
      //  mDrawerLayout.setDrawerListener(mDrawerToggle);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



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
              NavUtils.navigateUpFromSameTask(this);
               return true;
           }

            return true;
        }
        if (id == R.id.action_contact) {


        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

     super.onBackPressed();
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

