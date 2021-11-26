package com.example.coincome.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.coincome.MainActivity;
import com.example.coincome.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "FirebaseMessagingService";
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        //token을 서버로 전송
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // 메시지 수신 시 실행되는 메소드
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
            Log.d(TAG, "onMessageReceived: " + remoteMessage);
        }
    }
    /**
     * 메시지가 수신되었을 때 실행되는 메소드
     * **/
    private void sendNotification(RemoteMessage remoteMessage) {


        // 수신되는 푸시 메시지
        Log.v(TAG, String.valueOf(remoteMessage.getData()));
        Log.v(TAG,remoteMessage.getData().get("title"));
        Log.v(TAG,remoteMessage.getData().get("id"));
        Log.v(TAG,"호출");
        // 구분자를 통해 어떤 종류의 알람인지를 구별합니다.
        String title = remoteMessage.getData().get("title");
        String id = remoteMessage.getData().get("id");



        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel = "coincome";
            String channel_nm = getString(R.string.app_name); // 앱 설정에서 알림 이름으로 뜸.

            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("채널에 대한 설명입니다.");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            notichannel.createNotificationChannel(channelMessage);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setChannelId(channel)
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#0ec874"))
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());


        } else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
//                            .setSmallIcon(R.drawable.maindriver)
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#0ec874")) // 푸시 색상
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

        }
    }
}