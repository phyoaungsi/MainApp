package pas.com.mm.shoopingcart;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import pas.com.mm.shoopingcart.communication.DownloadTask;
import pas.com.mm.shoopingcart.logger.Log;
import pas.com.mm.shoopingcart.logger.LogWrapper;
import pas.com.mm.shoopingcart.logger.MessageOnlyLogFilter;

public class MainActivity extends AppCompatActivity {
    ListView list;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
     String[] datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final String[] web = {
                "Google Plus",
              "Twitter",
                "Windows",
                "Bing",
                "Itunes",
                "Wordpress",
                "Drupal"
        };

        Integer[] imageId = {
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp,
            //    R.drawable.ic_find_in_page_black_24dp,
              //  R.drawable.ic_find_in_page_black_24dp,
                //R.drawable.ic_find_in_page_black_24dp,
                R.drawable.ic_find_in_page_black_24dp

        };

        final Context context = this;
       CustomerList adapter = new
               CustomerList(MainActivity.this, web, imageId);
       list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);

                if (id == 1) {
                    Intent intent = new Intent(context, ItemGridView.class);
                    intent.putExtra("POSITION", id);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("POSITION", id);
                    startActivity(intent);
                }
            }
        });

       new DownloadJason().execute("https://d8bf89a819a1e81b101480aa48030698c6e0cdcd-www.googledrive.com/host/0B_9ZBXw3kTLIejZURnljVHlmNHM/");
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
        if (id == R.id.action_favorite) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://pas.com.mm.shoopingcart/http/host/path")
        );

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://pas.com.mm.shoopingcart/http/host/path")
        );


    }

    private class DownloadJason extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String text= loadFromNetwork(urls[0]);

                return text;
            } catch (IOException e) {
                return "Connection Error";
            }
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
            // Log.i(TAG, result);
        }


        /** Initiates the fetch operation. */
        private String loadFromNetwork(String urlString) throws IOException {
            InputStream stream = null;
            String str ="";

            try {
                stream = downloadUrl(urlString);
                str = readIt(stream, 500);
                Log.d("INFO",str);
               ArrayList<String> dataList= ParseJSON(str);
               datas = dataList.toArray(new String[dataList.size()]);

                runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CustomerList adapter = new
                                                            CustomerList(MainActivity.this, datas, null);
                list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
                                                }
                });
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return str;
        }

        /**
         * Given a string representation of a URL, sets up a connection and gets
         * an input stream.
         * @param urlString A string representation of a URL.
         * @return An InputStream retrieved from a successful HttpURLConnection.
         * @throws java.io.IOException
         */
        private InputStream downloadUrl(String urlString) throws IOException {
            // BEGIN_INCLUDE(get_inputstream)
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Start the query
            conn.connect();
            InputStream stream = conn.getInputStream();
            return stream;
            // END_INCLUDE(get_inputstream)
        }

        /** Reads an InputStream and converts it to a String.
         * @param stream InputStream containing HTML from targeted site.
         * @param len Length of string that this method returns.
         * @return String concatenated according to len parameter.
         * @throws java.io.IOException
         * @throws java.io.UnsupportedEncodingException
         */
        private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        /** Create a chain of targets that will receive log data */
        public void initializeLogging() {

            // Using Log, front-end to the logging chain, emulates
            // android.util.log method signatures.

            // Wraps Android's native log framework
            LogWrapper logWrapper = new LogWrapper();
            Log.setLogNode(logWrapper);

            // A filter that strips out everything except the message text.
            MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
            logWrapper.setNext(msgFilter);

            // On screen logging via a fragment with a TextView.
            //mLogFragment =
            //        (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
            // msgFilter.setNext(mLogFragment.getLogView());
        }

        private ArrayList<String> ParseJSON(String json) {
            if (json != null) {
                try {
                    // Hashmap for ListView
                    ArrayList<String> studentList = new ArrayList<String>();

                    JSONObject jsonObj = new JSONObject(json);

                    // Getting JSON Array node
                    JSONArray students = jsonObj.getJSONArray("category");

                    // looping through All Students
                    for (int i = 0; i < students.length(); i++) {
                        String c = students.getString(i);




                        studentList.add(c);
                    }
                    return studentList;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                return null;
            }
        }
    }
}
