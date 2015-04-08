/*package com.stream.consuming;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.stream.Credentials;
import com.stream.R;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class ConsumeActivity extends Activity {

    private String pathToFileOrUrl;// = /*"rtmp://52.10.144.216:1935/live/myStream"*/;
    private VideoView mVideoView;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        pathToFileOrUrl = intent.getStringExtra("videoUrl");
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (!LibsChecker.checkVitamioLibs(this))
            return;


        setContentView(R.layout.activity_video_stream);
        mVideoView = (VideoView) findViewById(R.id.surface_view);

        if (pathToFileOrUrl == "") {
            Toast.makeText(this, "Please set the video path for your media file", Toast.LENGTH_LONG).show();
            return;
        } else {
            //Alternatively,for streaming media you can use
            //mVideoView.setVideoURI(Uri.parse(URLstring));
            mVideoView.setVideoPath(pathToFileOrUrl);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
        }

    }

    public void startPlay(View view) {
        if (!TextUtils.isEmpty(pathToFileOrUrl)) {
            mVideoView.setVideoPath(pathToFileOrUrl);
        }
    }
}
*/
