package com.stream.playing;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AndroidAudioDevice
{
    AudioTrack track;
    short[] buffer = new short[1024];

    public AndroidAudioDevice(int sampleRate,int channels)
    {
        int minSize = AudioTrack.getMinBufferSize(sampleRate, channels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        track = new AudioTrack( AudioManager.STREAM_MUSIC, sampleRate,
                channels==1? AudioFormat.CHANNEL_OUT_MONO: AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                minSize, AudioTrack.MODE_STREAM);
        track.play();
    }

    public void writeSamples(float[] samples)
    {
        fillBuffer( samples );
        track.write( buffer, 0, samples.length );
    }

    private void fillBuffer( float[] samples )
    {
        if( buffer.length < samples.length )
            buffer = new short[samples.length];

        for( int i = 0; i < samples.length; i++ )
            buffer[i] = (short)(samples[i] * Short.MAX_VALUE);
    }
}