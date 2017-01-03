package pas.com.mm.shoopingcart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import pas.com.mm.shoopingcart.activities.ContactActivity;
import pas.com.mm.shoopingcart.activities.OpenNotification;
import pas.com.mm.shoopingcart.fragments.orderconfirm.OrderConfirmPopupFragment;
import pas.com.mm.shoopingcart.fragments.orderlist.OrderListFragment;

public class ItemGridView extends AppCompatActivity implements ImageGridFragment.OnFragmentInteractionListener,DetailFragment.OnFragmentInteractionListener,OrderListFragment.OnFragmentInteractionListener,OrderConfirmPopupFragment.OnFragmentInteractionListener {
    private static final String TAG = "ImageGridActivity";
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        MenuItem menu=(MenuItem)toolbar.findViewById(R.id.sign_in);
      //  menu.setTitle("0.00");
      Log.i("ItemGridVIEW","oNCREATE----");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



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
         menu.getItem(0).setTitle(OrderListFragment.getTotalText());
      //  View count = menu.getItem(2).getActionView();//.findItem(R.id.action_favorite).getActionView();
        menu.findItem(R.id.action_favorite).setIcon(buildCounterDrawable(9, R.drawable.common_google_signin_btn_icon_light_normal));
        return super.onCreateOptionsMenu(menu);

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

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.icon_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void doIncrease() {
        //count=9;
        invalidateOptionsMenu();
    }

}
