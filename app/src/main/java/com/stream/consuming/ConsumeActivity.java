package com.stream.consuming;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.stream.R;

import java.util.ArrayList;

import info.sodapanda.sodaplayer.FFmpegVideoView;
import info.sodapanda.sodaplayer.PlayCallback;

public class ConsumeActivity extends Activity {
    private FFmpegVideoView player_surface;

    int width;
    int height;

    Button button_start;
    Button button_stop;

    //EditText filename;

    FrameLayout surface_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //filename = (EditText) findViewById(R.id.filename);

        //surfaceView
        Display display = getWindowManager().getDefaultDisplay();
        int screen_width = display.getWidth();
        width=screen_width;
        height = (int) (screen_width * 0.75f);
        player_surface = new FFmpegVideoView(this,new PlayCallback() {
            @Override
            public void onConnecting() {

            }

            @Override
            public void onConnected() {

            }

            @Override
            public void onStop() {

            }
        },320,240);
        player_surface.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        surface_frame = (FrameLayout) findViewById(R.id.surface_frame);
        surface_frame.addView(player_surface);

        button_start = (Button) findViewById(R.id.button_start);
        button_stop = (Button) findViewById(R.id.button_stop);

        final ArrayList<String> rtmplist = new ArrayList<String>();
        rtmplist.add(0,"rtmp://52.10.144.216:1935/live/myStream");

        button_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //String rtmpurl = filename.getText().toString();
                /*if(rtmpurl != null && !(rtmpurl.equals(""))){
                    rtmplist.add(0, rtmpurl);
                }*/

                player_surface.startPlayer(rtmplist);
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                player_surface.stop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        player_surface.stop();
        super.onBackPressed();
    }

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("main");
    }
}
