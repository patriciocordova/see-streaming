package com.stream.playing;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.Frame;
import com.googlecode.javacv.FrameGrabber;

public class PlayStream extends Activity {

    static Frame captured_frame;
    private String url = Credentials.url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        playFrame();
    }

    public void playFrame()
    {
        new Thread(new Runnable() {

            public void run() {
                FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(url);
                AudioReader audioReader = new AudioReader(frameGrabber.getSampleRate(), frameGrabber.getAudioChannels());
                try {
                    frameGrabber.start();
                    audioReader.start();
                    while (true) {
                        try {
                            captured_frame = frameGrabber.grabFrame();
                            if (captured_frame == null) {
                                System.out.println("!!! Failed cvQueryFrame");
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
