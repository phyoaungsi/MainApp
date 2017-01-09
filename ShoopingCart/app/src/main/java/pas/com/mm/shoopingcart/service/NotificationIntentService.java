/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

        package pas.com.mm.shoopingcart.service;

        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;

        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;
        import com.google.gson.Gson;

        import java.util.Map;

        import pas.com.mm.shoopingcart.DetailActivity;
        import pas.com.mm.shoopingcart.MainActivity;
        import pas.com.mm.shoopingcart.R;
        import pas.com.mm.shoopingcart.activities.OpenNotification;
        import pas.com.mm.shoopingcart.database.DBListenerCallback;
        import pas.com.mm.shoopingcart.database.DbSupport;
        import pas.com.mm.shoopingcart.database.model.Item;
        import pas.com.mm.shoopingcart.database.model.Model;
        import pas.com.mm.shoopingcart.database.model.NotificationModel;
        import pas.com.mm.shoopingcart.util.PreferenceUtil;

public class NotificationIntentService extends FirebaseMessagingService implements DBListenerCallback {

    private static final String TAG = "MyFirebaseMsgService";



    private boolean messageReceived;




    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        sendNotification(remoteMessage);
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {

        String messageBody=remoteMessage.getNotification().getBody();

       Map<String,String> data= remoteMessage.getData();
       NotificationModel noti=new NotificationModel();


        noti.setTitle( remoteMessage.getNotification().getTitle());
        noti.setMessage(messageBody);
        noti.setContent(data.get("CONTENT"));
        noti.setType(data.get("TYPE"));
        noti.setMainImage(data.get("MAIN_IMAGE"));





        PendingIntent pendingIntent = null;

        if(noti.getType().equals("ITEM")){
            Intent intent1 = new Intent(this, DetailActivity.class);
            Item item=null;
            DbSupport db=new DbSupport();
            db.getItemById(noti.getContent(),this);
            while(!this.isMessageReceived()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Gson gson=new Gson();
            String objStr= gson.toJson(DbSupport.item);
            intent1.putExtra("DETAIL_ITEM",objStr);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent1,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        }else {
            Intent intent = new Intent(this, OpenNotification.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notificationBodys",noti.getMessage());
            intent.putExtra("imageUrl",noti.getMainImage());
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        }
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.samoa)
                .setContentTitle(noti.getTitle())
                .setContentText(noti.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public boolean isMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(boolean messageReceived) {
        this.messageReceived = messageReceived;
    }

    @Override
    public void LoadCompleted(boolean b) {
        setMessageReceived(true);
    }

    @Override
    public void receiveResult(Model model) {

    }
}


