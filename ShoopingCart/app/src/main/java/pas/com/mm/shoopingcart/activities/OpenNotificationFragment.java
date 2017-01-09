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

import com.squareup.picasso.Picasso;

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
        String imageUrl=getActivity().getIntent().getStringExtra("imageUrl");
        ImageView image=(ImageView) v.findViewById(R.id.notiImage);
        Picasso.with(this.getContext())
                .load(imageUrl).placeholder(R.drawable.placeholder)
        .into(image);
        image.setVisibility(View.VISIBLE);


        return v;
    }
}
