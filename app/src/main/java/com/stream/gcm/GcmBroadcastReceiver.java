
package com.stream.gcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.stream.R;
import com.stream.consuming.ConsumeActivity;
import com.stream.util.ActivityUtil;


public class GcmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.

        String newVideoPath = intent.getStringExtra("videoUrl");


        Intent streamIntent = new Intent(ActivityUtil.getMainActivity(), ConsumeActivity.class);
        intent.putExtra("videoUrl", newVideoPath);


        PendingIntent pendingIntent = PendingIntent.getActivity(ActivityUtil.getMainActivity(), 0, streamIntent, 0);

        Notification notification = new Notification(R.drawable.image, "Live Stream Available", System.currentTimeMillis());
        notification.setLatestEventInfo(ActivityUtil.getMainActivity(), "See", "", pendingIntent);

        NotificationManager mgr = (NotificationManager)ActivityUtil.getMainActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(1, notification);

        //startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
