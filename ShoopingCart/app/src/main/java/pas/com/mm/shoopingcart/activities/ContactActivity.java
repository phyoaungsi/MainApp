package pas.com.mm.shoopingcart.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.common.CommonActivity;
import pas.com.mm.shoopingcart.fragments.ContactFragment;

public class ContactActivity extends CommonActivity implements ContactFragment.OnFragmentInteractionListener {


   public  Context getActivityContext()
    {
        return this;
    }

   public int getViewId() {
        return R.layout.activity_contact;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
