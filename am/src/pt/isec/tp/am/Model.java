package pt.isec.tp.am;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Model implements Runnable
{
	Surface m_view;
	List<Barrier> m_barriers;
	List<Ball> m_balls;
	List<BonusBall> m_bonusBalls;
	List<ScorePoint> m_newScorePoints;
	volatile boolean m_run;
	float m_speed;
	float m_defaultSpeed;
	int m_spaceBetweenRectangles;
	long m_score;
	GameActivity m_gameActivity;
	int m_sleep;
	int m_defaultSleep;
	int m_points, mStartScore;
	boolean m_bombactive = false;
	Map<String, MediaPlayer> m_nameToMediaPlayer;
	String m_dificulty;
	boolean m_playSounds;
	int m_ballSpeed;
	int m_defBallSpeed;
	Random random = new Random();
	boolean mPaused = false;
	
	public Model(GameActivity gameActivity, Surface view)
	{
		m_view = view;
		m_gameActivity = gameActivity;
		m_view.addModel(this);
		m_barriers = Collections.synchronizedList(new ArrayList<Barrier>());
		m_balls = Collections.synchronizedList(new ArrayList<Ball>());
		m_bonusBalls = Collections.synchronizedList(new ArrayList<BonusBall>());
		m_newScorePoints = Collections.synchronizedList(new ArrayList<ScorePoint>());
		m_run = true;
		m_speed = m_defaultSpeed = 1;
		m_spaceBetweenRectangles = 0;
		m_score = 0;
		m_sleep = m_defaultSleep = 10;
		m_points = 2;
		m_nameToMediaPlayer = Collections.synchronizedMap(new HashMap<String, MediaPlayer>());
		mStartScore = 0;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(m_gameActivity.getApplicationContext());
		m_playSounds = ! prefs.getBoolean("MuteSound", true);
		
		if (m_playSounds) {
			m_nameToMediaPlayer.put("bomb", MediaPlayer.create(m_gameActivity.getBaseContext(), R.raw.bomb_bonus));
			m_nameToMediaPlayer.put("color", MediaPlayer.create(m_gameActivity.getBaseContext(), R.raw.color_bonus));
			m_nameToMediaPlayer.put("gameover", MediaPlayer.create(m_gameActivity.getBaseContext(), R.raw.gameover));
			m_nameToMediaPlayer.put("points", MediaPlayer.create(m_gameActivity.getBaseContext(), R.raw.new_point));
			m_nameToMediaPlayer.put("time", MediaPlayer.create(m_gameActivity.getBaseContext(), R.raw.time_bonus));
			m_nameToMediaPlayer.put("win", MediaPlayer.create(m_gameActivity.getBaseContext(), R.raw.win));
		}
		
		prefs = PreferenceManager.getDefaultSharedPreferences(gameActivity.getApplicationContext());
		m_dificulty = prefs.getString("Dificulty", "Easy");
		
		if(m_dificulty.equalsIgnoreCase("Normal"))
		{
			m_points = 2;
			mStartScore = 701;
		}
		else if(m_dificulty.equalsIgnoreCase("Hard"))
		{
			m_points = 3;
			mStartScore = 1401;
		}	

		m_defBallSpeed = m_ballSpeed = prefs.getInt("BallSpeed", 3);
	}
	
	public synchronized void waitOnPause() {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void run()
	{
		
		//game loop
		while(m_run)
		{
			try {
				Thread.sleep(m_sleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//if view isn't loaded yet, go to beginning
			if ( m_spaceBetweenRectangles == 0)
				continue;
			
			if ( mPaused )
				waitOnPause();
			
			synchronized(m_balls) {
				updateBalls();
			}

			synchronized(m_bonusBalls) {
				updateBonusBalls();
			}
			
			moveScenario();
			fixCollisions();
			updateScorePoints();
			
			m_view.postInvalidate();

		}
		
		Score.onSave(m_gameActivity.getBaseContext(), (int) m_score);
		
		if ( Score.score.get(0) == m_score ) 
			playSound("win");
		else
			playSound("gameover");
		
		synchronized(m_barriers) {
			m_barriers.clear();
		}
		synchronized(m_balls) {
			m_balls.clear();
		}
		synchronized(m_bonusBalls) {
			BonusBall.runAll = false;
			
			for(BonusBall ball : m_bonusBalls) {
				if ( ball.getThread() != null && ball.getThread().isAlive() ) {
					ball.terminateAndReset();
					try {
						ball.getThread().join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			m_bonusBalls.clear();
		}
		
		synchronized(m_newScorePoints) {
			m_bonusBalls.clear();
		}
		
		synchronized(m_nameToMediaPlayer) {
			
			Iterator<Entry<String, MediaPlayer>> it = m_nameToMediaPlayer.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry<String, MediaPlayer> entry = (Entry<String, MediaPlayer>) it.next();
				if ( ! entry.getValue().isPlaying() ) {
					entry.getValue().release();
					it.remove();
				}
			}
			
		}
		
		System.gc();
		
		m_view.showGameOverDialog();
		
	}
	
	public void stopGameOverSound() {
		synchronized(m_nameToMediaPlayer) {
			
			Iterator<Entry<String, MediaPlayer>> it = m_nameToMediaPlayer.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry<String, MediaPlayer> entry = (Entry<String, MediaPlayer>) it.next();
				if (entry.getValue().isPlaying() )
					entry.getValue().stop();
				it.remove();
			}
			
			m_nameToMediaPlayer.clear();
		}
	}
	
	private void updateBalls() {
			Iterator<Ball> it = m_balls.iterator();
			Ball b;
			
			while(it.hasNext()) {
				b = it.next();
				b.drop();
				
				if (b.isOut())
					it.remove();
			}
			
			if ( m_balls.isEmpty() ) 
				m_run = false;
		
	}
	
	private void updateBonusBalls() {
		Iterator<BonusBall> it = m_bonusBalls.iterator();
		BonusBall b;
		while(it.hasNext()) {
			b = it.next();
						
			if (b.isExecuted() || b.isOut() || b.isTriggered() || b == null) {
				it.remove();
			}
			
			if ( b.isTriggered() && (! b.isExecuted()) ){
				b.execute();
				continue;
			}
				
			b.drop();
		}
		
	}
	
	private void updateScorePoints() {
		Ball b;
		Barrier barrier;
		long score = 0;
		
		synchronized(m_newScorePoints) {
			Iterator<ScorePoint> it = m_newScorePoints.iterator();
			ScorePoint sp;
			while(it.hasNext()) {
				sp = it.next();
				sp.reduceAlpha();
				
				if ( sp.getAlpha() <= 0 )
					it.remove();
			}
		}
		
		synchronized(m_balls) {
			for(int i=0; i < m_balls.size(); i++) {
				b = m_balls.get(i);
				synchronized(m_barriers) {
					for(int j=0; j < m_barriers.size(); j++) {
						barrier = m_barriers.get(j);
						if (barrier.isPassed() ) continue;
						if ( b.center().y() >= barrier.rect().top ) {
							addScore(b, m_points);
							barrier.setPassed(true);
							playSound("points");
							break;
						}
					}
				}
			}
		}
		
		score = m_score + mStartScore;
		
		//verifica nivel
		if(score > 700 && m_points == 2)
		{
			//m_sleep = m_sleep + 30;
			ScorePoint.color = Color.YELLOW;
			m_defaultSpeed = m_speed = 2;
			m_points = 3;
		}
		else if(score > 1400 && m_points == 3)
		{
			//m_sleep = m_sleep + 20;
			ScorePoint.color = Color.RED;
			m_defaultSpeed = m_speed = 3;
			m_points = 4;
		}
		else if(score > 2800 && m_points == 4)
		{
			ScorePoint.color = Color.DKGRAY;
			//m_sleep = m_sleep + 20;
			m_defaultSpeed = m_speed = 4;
			m_points = 5;
		}
		else if(score > 5600 && m_points == 5 )
		{
			ScorePoint.color = Color.BLACK;
			//m_sleep = m_sleep + 10;
			m_defaultSpeed = m_speed = 5;
			m_points = 6;
		}
	}
	
	public synchronized void addScore(Ball b, int s) {
		m_score += s;
		m_newScorePoints.add(new ScorePoint(b.center(), s));
	}
	
	public synchronized void addScore(int s) {
		m_score += s;
	}
	
	public void terminate()
	{
		m_run = false;
		if (m_view.dialog() != null)
			m_view.dialog().cancel();
		
		if (mPaused)
			resume();
	}
	
	public void moveScenario()
	{
		float height = getScreenSize().height() + m_spaceBetweenRectangles;
		float minBottom = 0;
		Barrier barrier;
		
		synchronized(m_barriers) {
			Iterator<Barrier> it = m_barriers.iterator();
			
			while(it.hasNext()){
				barrier = it.next();
				barrier.goUp();
				
				if ( barrier.isOut()) { 
					it.remove();
					continue;
				}
				
				if ( barrier.rect().bottom > minBottom )
					minBottom = barrier.rect().bottom;
			}
			
			
		}
		
		//cria novas barreiras
		if ( Math.abs(height - minBottom) >= m_spaceBetweenRectangles ){
			
			synchronized(m_barriers) {
				m_barriers.add(new Barrier(this, height));
			}
			
			float bonus_release = (float) (0.1 * m_points);
			
			if(random.nextFloat() < bonus_release)
			{
				BonusBall bonusBall = null;
				float rand = random.nextFloat();
				float radius = m_spaceBetweenRectangles / 5;
				int cx = (int) (random.nextInt((int) (m_view.getWidth()-(radius+1))));
				if ( cx < radius+1 )
					cx = (int) radius+1;
				int cy = (int) (height - radius);
				
				if(rand < 0.2){
					bonusBall = new ColorBonusBall(this, cx, cy, radius);
				}
				else if(rand >=0.2 && rand<0.55){
					bonusBall = new BombBonusBall(this, cx, cy, radius);
				}
				else if(rand >=0.55 && rand<0.95){
					bonusBall = new TimeBonusBall(this, cx, cy, radius);
				}

				else if(rand >=0.95)
				{
					bonusBall = new FailBonusball(this, cx, cy, radius);
				}
				
				if ( bonusBall != null) {
					synchronized(m_bonusBalls) {
						m_bonusBalls.add(bonusBall);
					}
				}
			}
				
		}
	}
	
	public synchronized void playSound(String name) {
		if (! m_playSounds)
			return;
		
		synchronized(m_nameToMediaPlayer)  {
			if (m_nameToMediaPlayer.containsKey(name)) {
				MediaPlayer player = m_nameToMediaPlayer.get(name);
				if ( player.isPlaying() ) {
					player.pause();
					player.seekTo(0);
				}
				
				player.start();
			}
		}
		
	}
	
	public Size getScreenSize()
	{
		return m_view.getScreenSize();
	}
	
	public float getSpeed(){
		return m_speed;
	}
	
	public List<Barrier> barriers (){
		return m_barriers;
	}
	
	public void updateSpaceBetweenRectangles(int screenHeight){
		int spaceBetweenRectangles = screenHeight / 6;
		int radius = spaceBetweenRectangles / 3;
		addNewBall(random.nextInt((int) (getScreenSize().width()-(radius+1))), radius, radius);
		m_spaceBetweenRectangles = spaceBetweenRectangles;
	}
	
	
	public void addNewBall(float x, float y, float radius){
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(m_gameActivity.getApplicationContext());
		String colorName = prefs.getString("Color", "Red");

		int resource = m_gameActivity.getResources().getIdentifier("face_"+colorName.toLowerCase(), "drawable", "pt.isec.tp.am");
		Ball b = new Ball(this, x, y, radius, true, Color.parseColor(colorName));
		b.resource = resource;
		m_view.addResourceAndBitmap(resource);
		m_balls.add(b);
	}
	
	public List<Ball> balls(){
		return m_balls;
	}
	
	public List<BonusBall> bonusBalls(){
		return m_bonusBalls;
	}
	
	public void fixCollisions() {
	
		synchronized(m_bonusBalls) {
			for(int i=0; i < m_bonusBalls.size()-1; i++) {
				m_bonusBalls.get(i).moveOnOverlap(m_bonusBalls, i+1, m_bonusBalls.size());
			}
			
			synchronized(m_balls) {
				for(Ball ball : m_balls) {
					ball.moveOnOverlap(m_bonusBalls, 0, m_bonusBalls.size());
				}
			}
		}
				

				synchronized(m_bonusBalls) {
					for(BonusBall ball: m_bonusBalls) {
						synchronized(m_barriers) {
							for(Barrier barrier :  m_barriers) {
								if ( ball.moveOnOverlap(barrier) )
									break;
							}
						}
					}
				}
					
				synchronized(m_balls) {
					for(Ball ball : m_balls) {
						synchronized(m_barriers) {
							for(Barrier barrier :  m_barriers) {
								if ( barrier.isPassed())
									continue;
								
								if ( ! (Barrier.hasColor() && barrier.color() == ball.color()) ) {
									if ( ball.moveOnOverlap(barrier) )
										break;
								}
							}
						}

					}
				
			}
	}
	
	/*public void moveObjectsAt(Barrier barrier) {
		/*for(int i=0; i < m_bonusBalls.size(); i++) {
			BonusBall ball = m_bonusBalls.get(i);
			for(int j=0; j < m_bonusBalls.size(); j++) {
				if ( j != i )
					ball.moveOnOverlap(balls)
			}
		}
		
		for(BonusBall ball: m_bonusBalls) {
			ball.moveOnOverlap(barrier);
			ball.moveOnOverlap(m_bonusBalls);
		}
		
		
		
		for(Ball ball : m_balls) {
			if ( ! barrier.isPassed() )
				if ( ! (Barrier.hasColor() && barrier.color() == ball.color()) )
					ball.moveOnOverlap(barrier);
			ball.moveOnOverlap(m_bonusBalls);
		}
		
		
	}*/
	
	public void moveBalls(Ball.DIRECTION dir) {
		if ( mPaused )
			return;
		
		synchronized(m_balls) {
			for(Ball b : m_balls)
				b.move(m_ballSpeed, dir);
		}
		
		synchronized(m_bonusBalls) {
			for(Ball b : m_bonusBalls)
				b.move(m_ballSpeed/2, dir);
		}
	}
	
	public List<ScorePoint> getNewScorePoints() {
		return m_newScorePoints;
	}
	
	public void clearNewScorePoints() {
		m_newScorePoints.clear();
	}
	
	public long score() {
		return m_score;
	}
	
	public int spaceBetweenBarriers() {
		return m_spaceBetweenRectangles;
	}
	
	public synchronized void setSleep(int s) {
		m_sleep = s;
	}
	
	public synchronized void activateBonus(float x, float y){
		synchronized(m_bonusBalls) {
			for(BonusBall b : m_bonusBalls) {
				if (b.contains(x, y)) {
					b.setTriggered(true);
					break;
				}
			}
		}
	}
	
	public int points() {
		return m_points;
	}
	
	public void setSpeed(int v) {
		m_speed = v;
	}
	
	public void setDefaultSpeed() {
		m_speed = m_defaultSpeed;
	}
	
	public synchronized void faster() {
		if (m_sleep > 0)
			m_sleep /= 2;
	}
	
	public int sleep() {
		return m_sleep;
	}
	
	public synchronized void slower() {
		m_sleep = m_defaultSleep;
	}
	
	public GameActivity gameActivity() {
		return m_gameActivity;
	}
	
	public int getBallSpeed(){
		return m_ballSpeed;
	}
	
	public void setBallSpeed(int s) {
		if ( s >= m_defBallSpeed && s < 8 )
			m_ballSpeed = s;
	}
	
	public synchronized void setSpeed(float s) {
		m_speed = s;
	}
	
	public void pause() {
		mPaused = true;
	}
	
	public synchronized void resume() {
		mPaused = false;
		this.notify();
	}
	
}

