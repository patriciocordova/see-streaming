package com.stream.playing;

import com.googlecode.javacv.Frame;

import java.nio.Buffer;
import java.nio.FloatBuffer;

public class AudioReader extends Thread {

    int sampleRate,channels;

    public AudioReader(int sampleRate,int channels){
        this.sampleRate = sampleRate;
        this.channels = channels;
    }

    public void run(){
        AndroidAudioDevice aaD=new AndroidAudioDevice(sampleRate,channels);
        Buffer[] samples;
        float[] smpls = new float[0];
        Buffer b;
        FloatBuffer fb,b1,b2;
        Frame captured_frame;

        while(true) {
            captured_frame = PlayStream.captured_frame;
            if (captured_frame != null && captured_frame.samples != null) {
                try {
                    samples = captured_frame.samples.clone();

                    if (aaD.track.getChannelCount() == 1)//For using with mono track
                    {
                        b = samples[0];
                        fb = (FloatBuffer) b;
                        fb.rewind();
                        smpls = new float[fb.capacity()];
                        fb.get(smpls);
                    } else if (aaD.track.getChannelCount() == 2)//For using with stereo track
                    {
                        b1 = (FloatBuffer) samples[0];
                        b2 = (FloatBuffer) samples[1];
                        smpls = new float[b1.capacity() + b2.capacity()];
                        for (int i = 0; i < b1.capacity(); i++) {
                            smpls[2 * i] = b1.get(i);
                            smpls[2 * i + 1] = b2.get(i);
                        }
                    }
                    aaD.writeSamples(smpls);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
