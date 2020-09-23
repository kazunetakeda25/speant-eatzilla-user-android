package com.speant.user.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.speant.user.R;
import com.speant.user.ui.DiningTrackActivity;
import com.speant.user.ui.InboxActivity;
import com.speant.user.ui.SplashActivity;
import com.speant.user.ui.TrackingActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Map;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class Fcm extends FirebaseMessagingService {

    private static final String GROUP_KEY = "com.speant.user.GROUP_KEY";
    private String notifyTitle;
    private String notifyMessage;
    private String notifyRequestId;
    private static final String TAG = "FCM";
    private String notifyImage;
    private String notifyDeliveryType;
    private String notifyFromType;
    private String receiverType;
//    Uri sound =  Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sien);
//    Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.sien);
   /* @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }*/

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.saveDeviceToken(refreshedToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT", object.toString());

        try {
            notifyTitle = object.getString("title");
            notifyMessage = object.getString("message");
            notifyRequestId = object.getString("request_id");
            notifyDeliveryType = object.getString("delivery_type");
            notifyImage = object.getString("image");
            notifyFromType = object.getString("provider_type");
            if (notifyFromType.equalsIgnoreCase("1")) {
                receiverType = "DeliveryBoy";
            }else{
                receiverType = "Admin";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onMessageReceived:JSONException "+e.getMessage() );
        }

        String NOTIFICATION_CHANNEL_ID = "eatzilla_channel";

        long pattern[] = {0, 1000, 500, 1000};

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            notificationChannel.setDescription(" ");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
//            notificationChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        Intent intent = null;

        /*if (notifyRequestId != null && !notifyRequestId.isEmpty()) {
            intent = new Intent(this, TrackingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
        } else {
            intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }*/
        if(notifyTitle.contains("New chat received")){
            intent = new Intent(this, InboxActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(Global.FROM_TYPE, receiverType);
            intent.putExtra(Global.DELIVERYBOY_ID, "0");
            intent.putExtra(Global.ORDER_ID, notifyRequestId);

        } else if(notifyRequestId != null && !notifyRequestId.isEmpty() && !notifyRequestId.equals("0")) {
            if (notifyDeliveryType.equals(DINING)) {

                intent = new Intent(this, DiningTrackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(CONST.REQUEST_ID, notifyRequestId);

            } else if (notifyDeliveryType.equals(DOOR_DELIVERY) ||
                    notifyDeliveryType.equals(PICKUP_RESTAURANT)) {

                intent = new Intent(this, TrackingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
                intent.putExtra(CONST.DELIVERY_TYPE, notifyDeliveryType);
            }

        }else{
            intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

//        Log.e(TAG, "onMessageReceived:sound "+sound );
        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notifyMessage))
                .setContentTitle(getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setGroup(GROUP_KEY)
//                .setSound(sound)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);
        if (notifyImage != null && !notifyImage.isEmpty()) {
            try {
                URL url = new URL(notifyImage);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image));
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        mNotificationManager.notify(1000, notificationBuilder.build());


    }


}
