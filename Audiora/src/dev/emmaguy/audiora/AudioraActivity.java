package dev.emmaguy.audiora;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import dev.emmaguy.audiora.GameThread.ScoreChangedListener;

public class AudioraActivity extends Activity implements SensorEventListener, ScoreChangedListener {

    private AudioraSurfaceView m_audioraSurfaceView;
    private SensorManager m_sensorManager;
    private TextView m_scoreView;
    private Sensor m_accelerometer;
    private int m_score = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);

	m_audioraSurfaceView = (AudioraSurfaceView) findViewById(R.id.audiora_surfaceview);
	m_scoreView = (TextView) findViewById(R.id.score_textview);

	m_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	m_accelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPause() {
	super.onPause();

	m_sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
	super.onResume();

	m_sensorManager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	    m_audioraSurfaceView.onSensorChanged(event);
	}
    }

    @Override
    public void onScoreChanged(final int score) {
	m_score += score;
	
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		m_scoreView.setText("Score: " + m_score);
	    }
	});
    }
}
