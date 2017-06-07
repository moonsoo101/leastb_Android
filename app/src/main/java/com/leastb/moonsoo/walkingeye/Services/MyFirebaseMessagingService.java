package com.leastb.moonsoo.walkingeye.Services;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.leastb.moonsoo.walkingeye.MainActivity;
import com.leastb.moonsoo.walkingeye.R;

import java.net.URL;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    Bitmap bigPicture;

    // [START receive_message]
    @Override    public void onMessageReceived(RemoteMessage remoteMessage) {

        //추가한것
        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("imgurl"));
    }

    private void sendNotification(String messageBody, String imgurl) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        try {
            URL url = new URL(imgurl);
            bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("WalkingEyE")
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .setBigContentTitle("WalkingEyE 위험예보")
                        .setSummaryText(messageBody))
                .setContentIntent(pendingIntent);
        String text = null;
        if(messageBody.contains("차량"))
            text="차량이";
        else if(messageBody.contains("오토바이"))
            text = "오토바이가";
        else if(messageBody.contains("자전거"))
            text = "자전거가";
        else if(messageBody.contains("행단보도"))
            text = "행단보도가";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(
                getApplicationContext(),//현재제어권자
                VoiceService.class); // 이동할 컴포넌트
        intent1.putExtra("text",text+" 감지되었습니다. 조심하세요.");
        getApplicationContext().startService(intent1); // 서비스 시작
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}

