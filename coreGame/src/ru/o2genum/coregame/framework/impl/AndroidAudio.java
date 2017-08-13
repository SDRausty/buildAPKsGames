package ru.o2genum.coregame.framework.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import ru.o2genum.coregame.framework.*;

public class AndroidAudio implements Audio
{
	AssetManager assets;
	SoundPool soundPool;

	public AndroidAudio(Activity activity)
	{
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	}

	@Override
	public Sound newSound(String filename)
	{
		try
		{
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		}
		catch (IOException ex)
		{
			throw new RuntimeException("Couldn't load sound \"" +
					filename + "\"");
		}
	}
}
