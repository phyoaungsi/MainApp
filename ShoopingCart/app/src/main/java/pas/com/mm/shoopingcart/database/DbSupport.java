package pas.com.mm.shoopingcart.database;

/**
 * Created by phyo on 08/10/2016.
 */
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pas.com.mm.shoopingcart.database.model.Item;


public class DbSupport {
    private static String TAG="DB";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static List<Item> list=new ArrayList<Item>();
    public static List<Item> favList=new ArrayList<Item>();
    public void writeMessage()
    {

        DatabaseReference myRef = database.getReference("message/items/");
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
        DatabaseReference myRef = database.getReference("message/items/");
        String key = myRef.child("posts").push().getKey();
        Item post = new Item(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/items/" + key, postValues);
        childUpdates.put("/user-items/" + userId + "/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }
    public void listenDataChange() {
        DatabaseReference myRef = database.getReference("message/items/");
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

    public void loadItemList(DBListenerCallback cb)
    {
      try {
          database.setPersistenceEnabled(true);

      }catch(Exception e)
        {
            e.printStackTrace();
        }

       // database.goOnline();


                final DBListenerCallback cb1=cb;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

             //   for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                GenericTypeIndicator<Map<String,Item>> genericTypeIndicator = new GenericTypeIndicator<Map<String,Item>>() {};
                    Map<String,Item> post = dataSnapshot.getValue(genericTypeIndicator);
                  list= new ArrayList<Item>();
                if(post!=null) {
                    for (Item i : post.values()) {
                        Log.d("DBSupport", i.getDescription());
                        list.add(i);
                    }
                }
                   Collections.sort(list);

                    // [START_EXCLUDE]
                   //list.add(post);
             //   }


                // [END_EXCLUDE]
               //database.goOffline();
               if(cb1!=null) {
                   cb1.LoadCompleted(true);
               };
                Log.d("DBSupport", "---------LOADED--------");
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
        try {
            DatabaseReference myRef = database.getReference("message/items/");
            myRef.orderByChild("type").equalTo("eat").addValueEventListener(postListener);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public  Item getItemById(String id,DBListenerCallback cb)
    {
        DatabaseReference myRef = database.getReference("message/items/"+id.trim());


        final Item r=new Item();
        final DBListenerCallback callback=cb;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                //   for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                // TODO: handle the post

             Item dbResult  = dataSnapshot.getValue(Item.class);
                r.setAmount(dbResult.getAmount());
                r.setCode(dbResult.getCode());
                r.setDescription(dbResult.getDescription());
                r.setDiscount(dbResult.getDiscount());
                r.setHtmlDetail(dbResult.getHtmlDetail());
                r.setImgUrl(dbResult.getImgUrl());
                r.setTitle(dbResult.getTitle());
                item=r;
               callback.LoadCompleted(true);
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
        return r;
    }




    public static Item item;




    public  void getItemsByType(String type,DBListenerCallback cb)
    {
        final Item r=new Item();
        final DBListenerCallback callback=cb;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Item>> genericTypeIndicator = new GenericTypeIndicator<Map<String,Item>>() {};
                Map<String,Item> post = dataSnapshot.getValue(genericTypeIndicator);
               List list2= new ArrayList<Item>();
                if(post!=null) {
                    for (Item i : post.values()) {
                        Log.d("DBSupport", i.getDescription());
                        list.add(i);
                    }
                }
                Collections.sort(list);

                // [START_EXCLUDE]
                //list.add(post);
                //   }


                // [END_EXCLUDE]
                //database.goOffline();
                if(callback!=null) {

                    callback.LoadCompleted(true);
                    resultList=list2;
                }
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

        try {
            DatabaseReference myRef = database.getReference("message/items/");
            myRef.orderByChild("type").equalTo("eat").addValueEventListener(postListener);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    private List resultList;
}
