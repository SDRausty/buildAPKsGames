package com.mobilepearls.react;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class ReactSoundManager {

	private static AudioManager mAudioManager;
	private static SoundPool mSoundPool;
	private static int SOUND_ID_CORRECT;
	private static int SOUND_ID_GAME_DONE;
	private static int SOUND_ID_INCORRECT;

	public static synchronized void initSounds(Context context) {
		if (mAudioManager != null)
			return;

		mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		SOUND_ID_INCORRECT = mSoundPool.load(context, R.raw.incorrect, 1);
		SOUND_ID_CORRECT = mSoundPool.load(context, R.raw.correct, 1);
		SOUND_ID_GAME_DONE = mSoundPool.load(context, R.raw.game_done, 1);
	}

	public static void playCorrect() {
		ReactSoundManager.playSoundOnce(ReactSoundManager.SOUND_ID_CORRECT);
	}

	public static void playGameDone() {
		ReactSoundManager.playSoundOnce(ReactSoundManager.SOUND_ID_GAME_DONE);
	}

	public static void playIncorrect() {
		ReactSoundManager.playSoundOnce(ReactSoundManager.SOUND_ID_INCORRECT);
	}

	private static int playSoundOnce(int soundId) {
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		return mSoundPool.play(soundId, streamVolume, streamVolume, 1, 0, 1f);
	}

}
