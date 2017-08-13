package ru.o2genum.coregame.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import ru.o2genum.coregame.framework.*;

public abstract class AndroidGame extends Activity implements Game
{
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Vibration vibration;
	Screen screen;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bitmap frameBuffer = 
			Bitmap.createBitmap(
					getWindowManager().getDefaultDisplay().getWidth(),
					getWindowManager().getDefaultDisplay().getHeight(),
					Config.RGB_565);
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView);
		vibration = new AndroidVibration(this);
		screen = getStartScreen();
		setContentView(renderView);

		PowerManager powerManager = 
			(PowerManager) getSystemService(Context.POWER_SERVICE);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		renderView.pause();
		screen.pause();

		if(isFinishing())
			screen.dispose();
	}

	@Override
	public Input getInput()
	{
		return input;
	}

	@Override
	public FileIO getFileIO()
	{
		return fileIO;
	}

	@Override
	public Graphics getGraphics()
	{
		return graphics;
	}

	@Override
	public Audio getAudio()
	{
		return audio;
	}

	@Override
	public void setScreen(Screen screen)
	{
		if(screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen()
	{
		return screen;
	}

	public Vibration getVibration()
	{
		return vibration;
	}
}
