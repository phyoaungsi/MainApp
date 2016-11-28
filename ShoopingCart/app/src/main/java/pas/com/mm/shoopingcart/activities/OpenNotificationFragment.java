package pas.com.mm.shoopingcart.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
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
        TextView tv=(TextView) v.findViewById(R.id.notiTitle);
        tv.setText(this.getActivity().getIntent().getStringExtra("notificationBodys"));

        ImageView image=(ImageView) v.findViewById(R.id.notiImage);
       image.setVisibility(View.VISIBLE);

        WebView web=(WebView) v.findViewById(R.id.notiWebView);
        web.setVisibility(View.VISIBLE);
        web.loadData("hello","text/html","UTF-8");
        return v;
    }
}
