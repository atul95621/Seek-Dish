package com.dish.seekdish.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.dish.seekdish.R;
import com.dish.seekdish.ui.home.HomeActivity;
import com.dish.seekdish.util.Global;
import com.dish.seekdish.util.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


        Log.e("onNewToken",s);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.setValues(SessionManager.FCM_TOKEN, s);


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        try {
           // Map<String,String> datamap=remoteMessage.getData();
            //Log.d("Notify", "MyFirebaseMessagingService: " + remoteMessage.getData());


            //getting the title and the body
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            //Log.d("Notify title", "MyFirebaseMessagingService: " + title);
            //Log.d("Notify body", "MyFirebaseMessagingService: " + body);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Global.CHANNEL_ID, Global.CHANNEL_NAME, importance);
                mChannel.setDescription(Global.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200/*, 300, 400, 500, 400, 300, 200, 400*/});
                mNotificationManager.createNotificationChannel(mChannel);
            }

            /*
            * Displaying a notification locally
            */

            sendNotification(title, body);
            // MyNotificationManager.getInstance(this).displayNotification(title, body);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title ,String messageBody) {


        try {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

           // Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.D);
            Uri defaultSoundUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.hangouts_message);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Global.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setContentTitle(title)//"Seekdish title"
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setVibrate((new long[]{100, 200}))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}