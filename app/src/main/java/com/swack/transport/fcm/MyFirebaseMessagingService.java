package com.swack.transport.fcm;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swack.transport.activities.MainActivity;
import com.swack.transport.data.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String DATA = "data";
    private static final String TOPIC_GLOBAL = "global";
    public SharedPreferenceManager preferenceManager;


    @Override
    public void onNewToken(String s) {
        preferenceManager = new SharedPreferenceManager(this);
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println(TAG+" Refreshed token: " + refreshedToken);
        System.out.println(TAG+" Refreshed token: " + s);

        preferenceManager.connectDB();
        preferenceManager.setString("FCM_TOKEN",refreshedToken);
        preferenceManager.closeDB();


        preferenceManager.connectDB();
        System.out.println(TAG+" Refreshed token: " + preferenceManager.getString("FCM_TOKEN"));
        preferenceManager.closeDB();

        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println(TAG+" From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            System.out.println(TAG+" Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleData(json);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(TAG+" Message data payload Error: " + e.getMessage());
            }

        } else if (remoteMessage.getNotification() != null) {
            System.out.println(TAG+" Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        //notificationUtils.playNotificationSound();
    }

    private void handleData(JSONObject json) {
        System.out.println(TAG+" push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
            NotificationVO notificationVO = new NotificationVO();
            notificationVO.setTitle(title);
            notificationVO.setMessage(message);

            System.out.println(TAG+"title: " + title);
            System.out.println(TAG+"message: " + message);
            System.out.println(TAG+"isBackground: " + isBackground);
            System.out.println(TAG+"payload: " + payload.toString());
            System.out.println(TAG+"imageUrl: " + imageUrl);
            System.out.println(TAG+"timestamp: " + timestamp);

            System.out.println(TAG+"Message data payload data: " + data);
            System.out.println(TAG+"Message data payload title: " + title);
            System.out.println(TAG+"Message data payload message: " + message);

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.displayNotification(notificationVO, resultIntent);
        } catch (JSONException e) {
            System.out.println(TAG+"Json Exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(TAG+"Exception: " + e.getMessage());
        }

    }

}
