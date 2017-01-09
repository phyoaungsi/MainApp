package pas.com.mm.shoopingcart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import pas.com.mm.shoopingcart.activities.ContactActivity;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.activities.saveitem.SaveItemActivity;
//import pas.com.mm.shoopingcart.common.ApplicationConfig;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.fragments.itemgrid.ConfigDbListener;
import pas.com.mm.shoopingcart.util.FontUtil;
import android.support.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class ItemGridView extends AppCompatActivity implements ImageGridFragment.OnFragmentInteractionListener,DetailFragment.OnFragmentInteractionListener {
    private static final String TAG = "ImageGridActivity";
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    Context mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String IS_PROMOTION_CONFIG_KEY = "is_promotion_on";
    private String isPromotionOn="invalid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        mContext=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FontUtil.setText(this.getBaseContext(),toolbar,true);
      Log.i("ItemGridVIEW","oNCREATE----");

       initRemoteConfig();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);


       // gridview.setAdapter(new ImageAdapter(this));
        DbSupport db=new DbSupport();
        ConfigDbListener configListener=new ConfigDbListener();
        View v=this.findViewById(R.id.banner);
        configListener.setView(v);
        configListener.setmContext(this);
        db.getConfig(configListener);


    }

    public void onResume()
    {
        super.onResume();
        Log.i("ItemGridVIEW","RESUME ]]]]]]]]]]]]]]]]]----");
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
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
        if (id == R.id.action_contact) {

            Intent intent = new Intent(this, ContactActivity.class);intent.putExtra("POSITION", id);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_favorite) {

            Intent intent = new Intent(this, SaveItemActivity.class);intent.putExtra("POSITION", id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ImageGridFragment();
            Bundle args=new Bundle();
            args.putInt("PANEL",i);
            fragment.setArguments(args);

            // Our object is just an integer :-P
           // args.putInt(ImageGridFragment.ARG_OBJECT, i + 1);
           // fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return getResources().getString(R.string.title_tab1);
                case 2:
                    return getResources().getString(R.string.title_tab2);
               default:
                    return getResources().getString(R.string.title_tab3);

            }
        }
    }

 private void initRemoteConfig(){
     // Get Remote Config instance.
     // [START get_remote_config_instance]
     mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
     // [END get_remote_config_instance]

     // Create Remote Config Setting to enable developer mode.
     // Fetching configs from the server is normally limited to 5 requests per hour.
     // Enabling developer mode allows many more requests to be made per hour, so developers
     // can test different config values during development.
     // [START enable_dev_mode]
     FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
             .setDeveloperModeEnabled(BuildConfig.DEBUG)
             .build();
     mFirebaseRemoteConfig.setConfigSettings(configSettings);
     // [END enable_dev_mode]

     // Set default Remote Config values. In general you should have in app defaults for all
     // values that you may configure using Remote Config later on. The idea is that you
     // use the in app defaults and when you need to adjust those defaults, you set an updated
     // value in the App Manager console. Then the next time you application fetches from the
     // server, the updated value will be used. You can set defaults via an xml file like done
     // here or you can set defaults inline by using one of the other setDefaults methods.S
     // [START set_default_values]
     mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
     // [END set_default_values]

     fetchWelcome();
 }

    private void fetchWelcome() {
        isPromotionOn=mFirebaseRemoteConfig.getString(IS_PROMOTION_CONFIG_KEY);

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ItemGridView.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(ItemGridView.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        readConfig();
                    }
                });
        // [END fetch_config_with_callback]
    }

    private void readConfig() {
        // [START get_config_values]
        String isPromotionOn = mFirebaseRemoteConfig.getString(IS_PROMOTION_CONFIG_KEY);
        // [END get_config_values]
        Log.i("ItemGridView","PromotionStatus="+isPromotionOn);
        if(isPromotionOn.equals("true")) {
            ImageView imv = (ImageView) findViewById(R.id.banner);
            imv.setVisibility(View.VISIBLE);
            imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OpenNotification.class);
                    startActivity(intent);
                }
            });
        }
    }

}
