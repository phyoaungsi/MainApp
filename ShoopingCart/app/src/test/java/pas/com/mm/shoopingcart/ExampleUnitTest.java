package pas.com.mm.shoopingcart;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import pas.com.mm.shoopingcart.logger.Log;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

       // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        assertEquals(4, 2 + 2);
        ArrayList<String> ar=ParseJSON("category:{\"cat1\",\"cat2\",\"cat3\",\"cat4\"}");
        System.out.println(ar.size());
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
                    String c = students.getString(0);




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