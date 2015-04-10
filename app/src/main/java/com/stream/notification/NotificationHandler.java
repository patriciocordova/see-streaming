/*
package com.stream.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.stream.R;
import com.stream.consuming.ConsumeActivity;
import com.stream.publishing.PublishActivity;
import com.stream.service.request.Poll;
import com.stream.service.response.PollResponse;
import com.stream.util.ActivityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


*/
/**
 * Created by home on 2015-03-23.
 *//*

public final class NotificationHandler {

    private static NotificationHandler INSTANCE = new NotificationHandler();

    private final int NOTIFY_ME_ID = 1;
    private String newVideoPath = "";


    private NotificationHandler() {
    }

    public static NotificationHandler getInstance() {
        return INSTANCE;
    }


    public void start() {
        Poll poll = new Poll();
        poll.run();

        PollResponse response = (PollResponse) poll.getResponse();


        if (response.isSuccess()) {
            newVideoPath = response.getVideoUrl();
            if (newVideoPath != null && !newVideoPath.isEmpty()) {
                System.out.println(newVideoPath);
                createNotification();
            }
        }
    }

    private void createNotification() {

        try {
            Intent intent = new Intent(ActivityUtil.getMainActivity(), ConsumeActivity.class);
            intent.putExtra("videoUrl", newVideoPath);


            PendingIntent pendingIntent = PendingIntent.getActivity(ActivityUtil.getMainActivity(), 0, intent, 0);

            Notification notification = new Notification(R.drawable.image, "Android Example Status message!", System.currentTimeMillis());
            notification.setLatestEventInfo(ActivityUtil.getMainActivity(), "Example", "Example", pendingIntent);

            NotificationManager mgr = (NotificationManager) ActivityUtil.getMainActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.notify(NOTIFY_ME_ID, notification);


        } catch (Exception e) {
            Toast.makeText(ActivityUtil.getMainActivity(), "Could not play video : " + newVideoPath, Toast.LENGTH_SHORT).show();
        }
    }

}
*/
