/*

package com.stream.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.stream.R;
import com.stream.consuming.ConsumeActivity;
import com.stream.util.ActivityUtil;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                // Post notification of received message.
                sendNotification("Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        Intent intent = new Intent(ActivityUtil.getMainActivity(), ConsumeActivity.class);
        intent.putExtra("videoUrl", msg);


        PendingIntent pendingIntent = PendingIntent.getActivity(ActivityUtil.getMainActivity(), 0, intent, 0);

        Notification notification = new Notification(R.drawable.image, "Live Stream Available", System.currentTimeMillis());
        notification.setLatestEventInfo(ActivityUtil.getMainActivity(), "See", "", pendingIntent);

        NotificationManager mgr = (NotificationManager)ActivityUtil.getMainActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(NOTIFICATION_ID, notification);
    }
}

*/
