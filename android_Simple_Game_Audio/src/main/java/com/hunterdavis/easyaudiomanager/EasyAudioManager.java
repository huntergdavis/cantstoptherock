package com.hunterdavis.easyaudiomanager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.FloatMath;
import android.util.SparseIntArray;

public class EasyAudioManager {
	//public static final int SOUND_SCRATCH = 1;
	//public static final int DRUM_ONE = 2;
	//public static final int DRUM_TWO = 3;
	//public static final int DRUM_THREE = 4;
	//public static final int DRUM_FOUR = 5;
	//public static final int DRUM_FIVE = 6;
	//public static final int DRUM_SIX = 7;

	private SoundPool soundPool;
	MediaPlayer mediaPlayer;
	///private SparseIntArray soundPoolMap;
	private int[] soundArray;
	private int songId = -1;

	public boolean songPlaying;

	public EasyAudioManager(Context context, int[] soundsToLoad) {
		initSoundPool(context, soundsToLoad);
		songPlaying = false;
	}
	
	public EasyAudioManager(Context context) {
		initSoundPool(context, null);
		songPlaying = false;
	}

	public void initSoundPool(Context context, int[] soundsToLoad) {
		if(soundsToLoad == null) {
			return;
		}
		if(soundsToLoad.length == 0) {
			return;
		}
		soundPool = new SoundPool(soundsToLoad.length, AudioManager.STREAM_MUSIC, 100);
		soundArray = new int[soundsToLoad.length];
		//soundPoolMap = new SparseIntArray();
		for(int i =0 ;i<soundsToLoad.length;i++) {
			soundArray[i] = soundPool.load(context,  soundsToLoad[i], 1);
		}
		
		// soundPoolMap.put(SOUND_SCRATCH,
		// soundPool.load(context, R.raw.drum1, 1));
		// soundPoolMap.put(DRUM_ONE, soundPool.load(context, R.raw.drum1, 2));
		// soundPoolMap.put(DRUM_TWO, soundPool.load(context, R.raw.drum2, 3));
		// soundPoolMap.put(DRUM_THREE, soundPool.load(context, R.raw.drum3,
		// 4));
		// soundPoolMap.put(DRUM_FOUR, soundPool.load(context, R.raw.drum4, 5));
		// soundPoolMap.put(DRUM_FIVE, soundPool.load(context, R.raw.drum5, 6));
		// soundPoolMap.put(DRUM_SIX, soundPool.load(context, R.raw.drum6, 7));

		// mediaPlayer = MediaPlayer.create(context, R.raw.who);
		// mediaPlayer.setOnCompletionListener(this);
		

	}
	
	public MediaPlayer.OnCompletionListener getDefaultOnComplete() {
		return new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				
			}
		};
		
	}
	
	public void setSong(Context context,int songReference) {
		songId = songReference;
		mediaPlayer = MediaPlayer.create(context, songId);
		mediaPlayer.setOnCompletionListener(getDefaultOnComplete());
	}
	
	public void setSongAndOnComplete(Context context, int songReference, MediaPlayer.OnCompletionListener onComplete) {
		songId = songReference;
		mediaPlayer = MediaPlayer.create(context, songId);
		mediaPlayer.setOnCompletionListener(onComplete);
	}

	public void playSong() {
		if (!mediaPlayer.isPlaying()) {
			songPlaying = true;
			mediaPlayer.start();
		}
	}

	public void pauseSong() {
		songPlaying = false;
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void reset() {
		songPlaying = false;
		mediaPlayer.seekTo(0);
	}

	public void playSound(int soundPositionInPool, Context context) {
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;

		soundPool.play(soundArray[soundPositionInPool], volume, volume, 1, 0, 1f);
	}

//	public void scratch(Context context) {
//		playSound(SOUND_SCRATCH, context);
//	}
//
//	public void playDrumNumber(int drumNumber, Context context) {
//		switch (drumNumber) {
//		case 1:
//			playSound(DRUM_ONE, context);
//			break;
//		case 2:
//			playSound(DRUM_TWO, context);
//			break;
//		case 3:
//			playSound(DRUM_THREE, context);
//			break;
//		case 4:
//			playSound(DRUM_FOUR, context);
//			break;
//		case 5:
//			playSound(DRUM_FIVE, context);
//			break;
//		case 6:
//			playSound(DRUM_SIX, context);
//			break;
//
//		default:
//			break;
//		}
//	}

	// functions related to waveform generation

	/*
	 * playFrequency plays the given frequency for the given duration of time
	 */
	public void playFrequency(float frequency, float duration) {
		new FrequencyThread().execute(frequency, duration);
	}

	/*
	 * playNote plays the given note for the given duration of time Note is from
	 * 0-11 0=C 1=C# 2=D 3=Eb 4=E 5=F 6=F# 7=G 8=G# 9=A 10=Bb 11=B
	 */
	public void playNote(float note, float duration) {
		int BaseNote = (int) FloatMath.floor(note);
		float frequency;
		switch (BaseNote) {
		case 0:
			frequency = 1047;
			break;
		case 1:
			frequency = 1109;
			break;
		case 2:
			frequency = 1175;
			break;
		case 3:
			frequency = 1245;
			break;
		case 4:
			frequency = 1319;
			break;
		case 5:
			frequency = 1397;
			break;
		case 6:
			frequency = 1480;
			break;
		case 7:
			frequency = 1568;
			break;
		case 8:
			frequency = 1661;
			break;
		case 9:
			frequency = 1760;
			break;
		case 10:
			frequency = 1865;
			break;
		case 11:
			frequency = 1976;
			break;
		default:
			// default to A440 octave 2
			frequency = 880;
			break;
		}
		playFrequency(frequency, duration);
	}

	/*
	 * playFrequency plays a random note for the given duration of time
	 */
	public void playRandomNote(float duration) {
		int note = (int) Math.floor(Math.random() * 12);
		playNote(note, duration);
	}
	
	public void stopPlayingFrequency() {
		FrequencyThread.KeepPlayingTheTrack = false;
	}

	// function to release the soundpool and stop the media player
	public void closeSoundPool() {
		
		// release all sounds from soundpool
		soundPool.release();
		
		// stop the media player
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		// stop any frequencies
		stopPlayingFrequency();
	}

}
