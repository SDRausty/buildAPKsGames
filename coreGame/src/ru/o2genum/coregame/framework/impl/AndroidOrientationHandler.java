package ru.o2genum.coregame.framework.impl;

import android.hardware.*;
import android.content.Context;

public class AndroidOrientationHandler implements OrientationHandler,
		  SensorEventListener
{
	SensorManager manager;
	Sensor sensor;
	float azimuth = 0.0F;

	public AndroidOrientationHandler(Context context)
	{
		manager = 
			(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// Nothing to do
	}

	public void onSensorChanged(SensorEvent event)
	{
		/* Now it works using accelerometer, not orientation
		 * sensor. Because:
		 *
		 * Fivos Asimakop wrote:
		 *
		 * Try an alternative of the controls with the azimuth. In my
		 * experience most devices lose their calibration easily and the azimuth
		 * is not to be trusted. Especially after deprecating the orientation
		 * sensor. How about using an accelerometer reading? Or the rotation
		 * vector when android version is 2.3 and above?
		 */

		azimuth = (float) (Math.atan2((double) event.values[1],
				(double) event.values[0]) / (Math.PI *2) * 360);
	}

	@Override
	public float getAzimuth()
	{
		return azimuth;
	}
}
