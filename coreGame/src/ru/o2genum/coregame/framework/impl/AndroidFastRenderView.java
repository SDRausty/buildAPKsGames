package ru.o2genum.coregame.framework.impl;

import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable
{
	AndroidGame game;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(AndroidGame game, Bitmap frameBuffer)
	{
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		this.holder = getHolder();
		/*
		 * From email by Fivos Asimakop:
		 *
		 * Remove the wake lock. It's a buggy thing that can cause troubles on
		 * devices like Motorola, ZTE and potentially others. Also it will allow
		 * you to remove that permission. The best way to prevent device from
		 * going to sleep while playing is setting a "keepscreenon" attribute to
		 * ON with the "setKeepScreenOn(true)" method. You may be able to do 
		 * this in the SurfaceView that holds the canvas ( Not 100% sure about
		 * that, I haven't worked with a Canvas yet ).
		 */
		setKeepScreenOn(true);
	}
	
	public void resume()
	{
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

	public void run()
	{
		long startTime = System.nanoTime();
		while(running)
		{
			if(!holder.getSurface().isValid())
				continue;

			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0F;
			startTime = System.nanoTime();
			
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);

			Canvas canvas = holder.lockCanvas();
			canvas.drawBitmap(frameBuffer, 0, 0, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause()
	{
		running = false;
		while(true)
		{
			try
			{
				renderThread.join();
				break;
			}
			catch (InterruptedException ex)
			{
				// Failed to stop thread, try again
			}
		}
	}
}
