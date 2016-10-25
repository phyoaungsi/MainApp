package pas.com.mm.shoopingcart.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pas.com.mm.shoopingcart.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OpenNotificationFragment extends Fragment {

    public OpenNotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_open_notification, container, false);
        TextView tv=(TextView) v.findViewById(R.id.notiTextBody);
        tv.setText(this.getActivity().getIntent().getStringExtra("notificationBodys"));
        return v;
    }
}
