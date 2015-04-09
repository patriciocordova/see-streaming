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
//import com.stream.consuming.ConsumeActivity;
import com.stream.publishing.PublishActivity;
import com.stream.service.request.Poll;
import com.stream.service.response.PollResponse;
import com.stream.util.ActivityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by home on 2015-03-23.
 */
public final class NotificationHandler {

    private static NotificationHandler INSTANCE = new NotificationHandler();

    private final int NOTIFY_ME_ID = 1337;
    private List<VideoInfo> videos;
    private Timer timer;

    private boolean isPaused;

    private Cursor cursor;
    private String newVideoPath = "";


    private NotificationHandler(){
        videos = loadVideos();
    }

    public static NotificationHandler getInstance(){
        return INSTANCE;
    }


    public void start(){
        // Poll
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(!isPaused) {
                    Poll poll = new Poll();
                    poll.run();

                    PollResponse response = (PollResponse)poll.getResponse();
                    if (response.isSuccess()) {
                        newVideoPath = response.getVideoUrl();
                    }
                    else {
                        // Just for testing play video from phone when no streams are found
                        if (!videos.isEmpty()) {
                            newVideoPath = videos.get(0).filePath;
                        }
                    }

                    createNotification();
                }
            }
        }, 1000, 30000);
    }

    public boolean hasStarted(){
        return (timer != null);
    }

    public void pause(){
        isPaused = true;
    }

    public void resume(){
        isPaused = false;
    }






    private void createNotification(){

        try{
            //Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            //File file = new File(newVideoPath);
            //String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            //String mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //intent.setDataAndType(Uri.fromFile(file), mimeType);


            /*
            Intent intent = new Intent(ActivityUtil.getMainActivity(), ConsumeActivity.class);
            intent.putExtra("videoUrl", newVideoPath);
            

            PendingIntent pendingIntent = PendingIntent.getActivity(ActivityUtil.getMainActivity(), 0, intent, 0);

            Notification notification = new Notification(R.drawable.image, "Android Example Status message!", System.currentTimeMillis());
            notification.setLatestEventInfo(ActivityUtil.getMainActivity(), "Example", "Example", pendingIntent);

            //notification.number = 3;

            NotificationManager mgr = (NotificationManager)ActivityUtil.getMainActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.notify(NOTIFY_ME_ID, notification);

            */
        }
        catch (Exception e){
            Toast.makeText(ActivityUtil.getMainActivity(), "Could not play video : " + newVideoPath, Toast.LENGTH_SHORT).show();
        }
    }

    // Test method to load videos from the phone
    private List<VideoInfo> loadVideos(){

        List<VideoInfo> videos = new ArrayList<VideoInfo>();

        String[] thumbColumns = {
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID,
        };

        String[] mediaColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE };

        cursor = ActivityUtil.getMainActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                VideoInfo videoInfo = new VideoInfo();

                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));

                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                videoInfo.displayName = displayName;
                videoInfo.filePath = filePath;

                Cursor thumbCursor = ActivityUtil.getMainActivity().managedQuery(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);

                if (thumbCursor.moveToFirst()) {
                    videoInfo.thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    System.out.println(videoInfo.thumbPath);
                }


                videos.add(videoInfo);
            } while (cursor.moveToNext());
        }

        return videos;
    }
}

class VideoInfo {
    String displayName;
    String filePath;
    String thumbPath;
}
