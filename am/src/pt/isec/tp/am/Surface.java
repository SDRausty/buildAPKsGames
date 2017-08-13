package pt.isec.tp.am;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Surface extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener, Runnable 
{
	
	Thread thread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean running = false;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random;
    private Model m_model;
    private Size m_screenSize;
	float left ;
	float top;
	float right;
	float bottom;
	Matrix m_matrix;
	Background m_bg;
	private SensorManager sensorManager ;
	HashMap<Integer, Integer> pointsToColor;
	RectF m_scoreRect;
	RectF m_rectF;
	int m_scoreTextSize;
	Paint fillPaint;
	Paint borderPaint;
	Paint textPaint;
	Paint defaultPaint;
	String m_scoreStr;
	private GameActivity m_gameActivity;
	Boolean sensor;
	BitmapCache bitmapCache;
	Canvas canvas = new Canvas();
	AlertDialog mPauseDialog;
	
    public Surface(Context context) {
    	super(context);
    	this.setKeepScreenOn(true);
    	m_gameActivity = null;
    	surfaceHolder = getHolder();
    	getHolder().addCallback(this);
    	random = new Random();
		m_screenSize = new Size();
		this.setFocusableInTouchMode(true);
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		m_matrix = new Matrix();
		bitmapCache = new BitmapCache();
		m_bg = new Background();
		m_scoreTextSize = 23;
		//addResourceAndBitmap(R.drawable.face);
		addResourceAndBitmap(R.drawable.bola_cores);
		addResourceAndBitmap(R.drawable.bomb);
		addResourceAndBitmap(R.drawable.time);
		addResourceAndBitmap(R.drawable.failbomb);
		
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStrokeWidth(3);
		borderPaint.setStyle(Style.STROKE);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(m_scoreTextSize);
		textPaint.setColor(Color.WHITE);
		
		defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		defaultPaint.setColor(Color.BLACK);
		defaultPaint.setAlpha(255);
		defaultPaint.setStyle(Style.FILL);
		sensor = false;
		m_scoreStr = "Score: ";
		m_rectF = new RectF(left, top, right, bottom);
		mPauseDialog = null;
    }
    
    public synchronized void addResourceAndBitmap(int resource) {
    	bitmapCache.addBitmap(resource, BitmapFactory.decodeResource(getResources(), resource), 0);
    }
	

	@Override
	protected void onDraw(Canvas canvas) {
		     
		boolean hasColor = Barrier.hasColor();
		Bitmap bitmap = null;
		int color = Color.LTGRAY;
		Point p;
		float radius;

		if ( m_bg.bitmap != null ) {
			canvas.drawBitmap(m_bg.bitmap, m_bg.rectSrc, m_bg.rectDst, defaultPaint);
			m_bg.scrollDown();
		}
		else
			canvas.drawColor(Color.WHITE);

		paint.setColor(color);
		paint.setStyle(Style.FILL);
		paint.setAlpha(200);
		
		synchronized (m_model.barriers()) {
			for(Barrier barrier : m_model.barriers() ){
				if (hasColor) {
					paint.setColor(barrier.color());
					
				}
					
				for(RectF rect : barrier.rects() ) { 
					canvas.drawRect(rect, paint);
					canvas.drawRect(rect, borderPaint);
				}
			}
		}
		
		
		m_matrix.reset();
		
		synchronized (m_model.balls()) {
			
			if ( ! m_model.balls().isEmpty() )
			{
				
				for(Ball ball : m_model.balls()) {
					radius = getNewRadius(ball.radius(), ball.getRotation());
					
					left = ball.center().x() - radius;
					top = ball.center().y() - radius;
					right = ball.center().x() + radius;
					bottom = ball.center().y() + radius;
					m_rectF.set(left,  top, right, bottom);
					
					bitmap =  bitmapCache.getBitmap(ball.resource, ball.rotation());

					if ( bitmap == null ) {
						bitmap = bitmapCache.getBitmap(ball.resource, 0);
						bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(radius * 2), Math.round(radius * 2), true);
						bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m_matrix, true);
						bitmapCache.addBitmap(ball.resource, bitmap, ball.getRotation());
					}
			
					canvas.drawBitmap(bitmap, null, m_rectF, defaultPaint);
				}
			}
		}
		
		synchronized(m_model.bonusBalls()) {
			for (BonusBall ball : m_model.bonusBalls()) {
				radius = getNewRadius(ball.radius(), ball.getRotation());
				left = ball.center().x() - radius;
				top = ball.center().y() - radius;
				right = ball.center().x() + radius;
				bottom = ball.center().y() + radius;
				m_rectF.set(left,  top, right, bottom);
				bitmap = bitmapCache.getBitmap(ball.resource, ball.rotation());
				
				if ( bitmap == null ) {
					bitmap = bitmapCache.getBitmap(ball.resource, 0);
					bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(radius * 2), Math.round(radius * 2), true);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m_matrix, true);
					bitmapCache.addBitmap(ball.resource, bitmap, ball.getRotation());
				}
				
				canvas.drawBitmap(bitmap, null, m_rectF, defaultPaint);
			}
		}
		
		paint.setTextSize(m_scoreTextSize);
		paint.setColor(ScorePoint.color);
		
		synchronized(m_model.getNewScorePoints()) {
			
			for( ScorePoint point : m_model.getNewScorePoints() )
			{
				paint.setAlpha(point.getAlpha());
				canvas.drawText(point.toString(), point.getPos().x() , point.getPos().y(), paint);
			}
		}
		
		//desenha pontuação
		String points = "Score: " + m_model.score();
		m_scoreRect.right = points.length() * m_scoreTextSize/2;
		
		paint.setColor(Color.BLACK);
		paint.setAlpha(155);
		canvas.drawRoundRect(m_scoreRect, 5, 5, paint);
		//canvas.drawText(points, 5, getHeight() - 20, textPaint);
		canvas.drawText(points, m_scoreRect.left + 2, (float) (getHeight() - (m_scoreRect.bottom-m_scoreRect.top)/4), textPaint);
		
	}
	 

	public float getNewRadius(float radius, float rot) {
		boolean checkXdist = true;
		
		m_matrix.setRotate(rot);
		
		float [] points = {-radius, radius, radius, -radius};
		m_matrix.mapPoints(points);
		
		Point p1 = new Point(points[0], points[1]);
		Point p2 = new Point(0,0);
		float m = points[1] / points[0];

		if ( Math.abs(points[1]) >= radius )
			checkXdist = false;
		
		if ( checkXdist){
			if ( points[0] < 0 )
				radius *= -1;
			p2 = new Point(radius, m*radius);

		}
		else {
			if ( points[1] < 0 )
				radius *= -1;
			p2 = new Point(radius/m, radius);
		}
		
		radius = Math.abs(radius);
		radius += p1.distance(p2);
		
		return radius;
		
	}

    
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
    	super.onSizeChanged(xNew, yNew, xOld, yOld);
    	m_screenSize = new Size(xNew, yNew);
    	m_scoreTextSize = yNew / 25;
    	textPaint.setTextSize(m_scoreTextSize);
    	m_bg = new Background( BitmapFactory.decodeResource(getResources(), R.drawable.dreamy_sky_bg), xNew, yNew);
    	m_scoreRect = new RectF(0, (float) (yNew - (m_scoreTextSize*1.3)), m_scoreTextSize*4, yNew);
    	m_model.updateSpaceBetweenRectangles(yNew);
    }
    

  	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		running = true;
		//thread = new Thread(this);
		//thread.start();
		sensorManager.registerListener((SensorEventListener)this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

		setWillNotDraw(false);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		running = false;
		bitmapCache.clear();
		m_bg = null;
		sensorManager.unregisterListener(this);

		while (retry) {
			try {
				if ( thread != null )
					thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
        }
		unbindDrawables(this);
		System.gc();

	}
	
	private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }


	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		
		float [] values = event.values;
		
		float x = values[0];
		
		if(x<0) {
			m_model.moveBalls(Ball.DIRECTION.RIGHT);
		}
		else {
			m_model.moveBalls(Ball.DIRECTION.LEFT);
		}
			
	}

	public void run() {
	   // TODO Auto-generated method stub
	   while(running){
	    if(surfaceHolder.getSurface().isValid()) {
	     Canvas canvas = surfaceHolder.lockCanvas();
	     synchronized(surfaceHolder) {
	    	 this.onDraw(canvas);
	     }
	     surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	   }

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		m_model.activateBonus(event.getX(), event.getY());
		return super.onTouchEvent(event);
	}
    
	public void addModel(Model m){
		m_model = m;
		m_gameActivity = m_model.gameActivity();
		//Log.d("gameActivity", " " + m_gameActivity);
	}
	
	public Size getScreenSize()
	{
		return m_screenSize;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ( keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
			m_model.moveBalls(Ball.DIRECTION.LEFT);
			return true;
		}
		else if ( keyCode == KeyEvent.KEYCODE_DPAD_RIGHT )
		{
			m_model.moveBalls(Ball.DIRECTION.RIGHT);
			return true;
		}
		
		else if ( keyCode == KeyEvent.KEYCODE_BACK ) 
		{
			
			m_model.pause();
			
	        m_gameActivity.runOnUiThread(new Runnable() {
	        	public void run() {
	        		
	        		mPauseDialog = new AlertDialog.Builder(m_gameActivity)
	    	        .setIcon(android.R.drawable.ic_dialog_alert)
	    	        .setTitle(getResources().getString(R.string.quitDialogTitle))
	    	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
	    	        {
	    	            public void onClick(DialogInterface dialog, int which) {
	    	            	mPauseDialog = null;
	    	            	m_model.terminate();
	    	            }
	    	        }
	    	        ).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							mPauseDialog = null;
							m_model.resume();
						}
					})
					.show();
	        	}
	        });
	        

		}
		return false;
		//return super.onKeyDown(keyCode, event);
	}
	
	
	
	public void showGameOverDialog(){
		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				m_model.stopGameOverSound();
				if ( !  m_gameActivity.isFinishing() )
					m_gameActivity.finish();
			}
		};
		
		final DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
			
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		
        m_gameActivity.runOnUiThread(new Runnable() {
        	public void run() {
        		Resources res = getResources();
        		String gameOver = res.getString(R.string.gameOver);
        		String score = res.getString(R.string.Score);
        		new AlertDialog.Builder(m_gameActivity).setPositiveButton("OK", listener).setOnKeyListener(keyListener).setMessage(gameOver+"\n"+score+": " + m_model.score()).show();
        	}
        });
	}
	
	public AlertDialog dialog() {
		return mPauseDialog;
	}
	
}
