package navigationdrawerexample.android.example.com.mywebtvinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WebView web=(WebView)this.findViewById(R.id.webView);
        web.loadUrl("https://c543fde58b360813cc5e9475ec2f728c04492180-www.googledrive.com/host/0B_9ZBXw3kTLIZHdQdVJ1SXBnVkE");
    }
}
