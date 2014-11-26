package com.hunterdavis.easyaudiomanager;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.FloatMath;

public class FrequencyThread extends AsyncTask<Float, Integer, Integer> {
	// our audio device
	private AudioTrack track;

	public static boolean KeepPlayingTheTrack = true;

	protected Integer doInBackground(Float... freqParams) {
		int count = freqParams.length;
		if (count != 2) {
			return -1;
		}

		// what to play
		Float frequency = freqParams[0];

		// how long to play
		Float duration = freqParams[1];

		// our integer playback buffer
		short[] buffer = new short[1024];

		// our float sample buffer
		float samples[] = new float[1024];

		int minSize = AudioTrack.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, minSize, AudioTrack.MODE_STREAM);
		try {
			track.play();
		} catch (IllegalStateException e) {
			return 1;
		}

		// we'll increment our wave angle by the frequency over the sample rate
		float increment = (float) (2 * Math.PI) * frequency / 44100;

		// our current angle for the sine wave
		float angle = 0;

		// loop over our duration (40 ~ samplerate/buffersize)
		for (int j = 0; j < (duration * 40); j++) {
			for (int i = 0; i < samples.length; i++) {
				buffer[i] = (short) (FloatMath.sin(angle) * Short.MAX_VALUE);
				angle += increment;
			}
			if (KeepPlayingTheTrack == false) {
				KeepPlayingTheTrack = true;
				track.stop();
				return 1;
			}
			track.write(buffer, 0, samples.length);
		}

		track.stop();
		return 1;
	}

	protected void onProgressUpdate(Integer... progress) {

	}

	protected void onPostExecute(Long result) {

	}
}
