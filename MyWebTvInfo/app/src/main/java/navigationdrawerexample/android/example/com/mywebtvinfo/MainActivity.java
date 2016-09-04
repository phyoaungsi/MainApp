package navigationdrawerexample.android.example.com.mywebtvinfo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WebView web=(WebView)this.findViewById(R.id.webView);
        web.loadUrl("https://c543fde58b360813cc5e9475ec2f728c04492180-www.googledrive.com/host/0B_9ZBXw3kTLIZHdQdVJ1SXBnVkE");
        Intent i;

        final Button b=(Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = "sop://broker.sopcast.com:3912/256241";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setAction("android.intent.action.VIEW");
              //  i.setAction("SopCast.VIDEO_PLAY_ACTION");
                i.addCategory( "android.intent.category.DEFAULT");
                i.addCategory( "android.intent.category.BROWSABLE");
                i.setData(Uri.parse(url));
                i.setType("video/*");
                startActivity(i);
            }
        });
    }
}
