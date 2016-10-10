package pas.com.mm.shoopingcart.database;

/**
 * Created by phyo on 08/10/2016.
 */
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pas.com.mm.shoopingcart.database.model.Item;

public class DbSupport {
    private static String TAG="DB";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message/items/");
    public static List<Item> list=new ArrayList<Item>();
    public void writeMessage()
    {


        myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    public void writeNewPost(String userId, String username, String title, double body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = myRef.child("posts").push().getKey();
        Item post = new Item(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/items/" + key, postValues);
        childUpdates.put("/user-items/" + userId + "/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }
    public void listenDataChange() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Item post = postSnapshot.getValue(Item.class);
                    // [START_EXCLUDE]
                    Log.d(TAG, "Value is: " + post.code);
                    Log.d(TAG, "Value is: " + post.description);
                    Log.d(TAG, "Value is: " + post.getAmount());
                }


                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        myRef.addValueEventListener(postListener);

    }

    public void loadItemList()
    {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

             //   for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                GenericTypeIndicator<Map<String,Item>> genericTypeIndicator = new GenericTypeIndicator<Map<String,Item>>() {};
                    Map<String,Item> post = dataSnapshot.getValue(genericTypeIndicator);
                  list= new ArrayList<Item>();
                  for(Item i:post.values())
                  {
                      list.add(i);
                  }
                    // [START_EXCLUDE]
                   //list.add(post);
             //   }


                // [END_EXCLUDE]
                database.goOffline();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                //        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        myRef.addValueEventListener(postListener);

    }

}
