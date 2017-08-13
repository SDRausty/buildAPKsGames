package dev.emmaguy.audiora;

import dev.emmaguy.audiora.GameThread.ScoreChangedListener;
import android.content.Context;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AudioraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread m_gThread = null;

    public AudioraSurfaceView(Context context, AttributeSet attrs) {
	super(context, attrs);

	getHolder().addCallback(this);

	m_gThread = new GameThread(getResources(), getHolder(), (ScoreChangedListener) context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
	m_gThread.resumeGame();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
	m_gThread.pauseGame();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	m_gThread.setWidthAndHeight(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    m_gThread.onTouchEvent();
	}
	return true;
    }

    public void onSensorChanged(SensorEvent event) {
	m_gThread.onSensorChanged(event);
    }
}
