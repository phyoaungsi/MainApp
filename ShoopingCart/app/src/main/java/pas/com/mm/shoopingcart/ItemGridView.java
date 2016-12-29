package pas.com.mm.shoopingcart;

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
import android.widget.Toast;

import pas.com.mm.shoopingcart.activities.ContactActivity;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.common.ApplicationConfig;

public class ItemGridView extends AppCompatActivity implements ImageGridFragment.OnFragmentInteractionListener,DetailFragment.OnFragmentInteractionListener {
    private static final String TAG = "ImageGridActivity";
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      Log.i("ItemGridVIEW","oNCREATE----");



        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);


       // gridview.setAdapter(new ImageAdapter(this));



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
        MenuItem v= (MenuItem)menu.findItem(R.id.action_favorite);
       if(ApplicationConfig.isPromotionOn.equals("false")) {
            v.setVisible(false);
        }
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

            Intent intent = new Intent(this, OpenNotification.class);intent.putExtra("POSITION", id);
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


}
