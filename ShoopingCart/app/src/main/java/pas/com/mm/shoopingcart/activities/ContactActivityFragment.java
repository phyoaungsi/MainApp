package pas.com.mm.shoopingcart.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import pas.com.mm.shoopingcart.R;
import pas.com.mm.shoopingcart.database.model.Item;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactActivityFragment extends Fragment {

    public ContactActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_contact2, container, false);


    }
}
