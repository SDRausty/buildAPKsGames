package dev.emmaguy.audiora;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread implements Runnable {
    private ShipManager m_shipMgr;
    private AsteroidManager m_asteroids;

    private boolean isRunning = false;
    private boolean isInitialised = false;

    private final LinkedList<ParticleExplosion> m_explosions = new LinkedList<ParticleExplosion>();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Resources m_resources;
    private final SurfaceHolder m_holder;
    private final ScoreChangedListener m_scoreChangedListener;

    @SuppressWarnings("unused")
    private volatile ScheduledFuture<?> self;

    public GameThread(Resources r, SurfaceHolder holder, ScoreChangedListener scoreChangedListener) {
	this.m_scoreChangedListener = scoreChangedListener;
	this.m_resources = r;
	this.m_holder = holder;
    }

    public void run() {
	Canvas canvas = null;

	if (m_shipMgr == null || m_asteroids == null) {
	    return;
	}

	if (isRunning) {

	    try {
		m_shipMgr.update();
		m_asteroids.update();
		updateParticleExplosions();
		m_asteroids.detectForCollisions(m_shipMgr.getProjectiles(), m_scoreChangedListener, m_explosions);

		canvas = m_holder.lockCanvas(null);
		draw(canvas);
	    } catch (Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		Log.e("Audiora", e.getMessage() + "\n" + sw.toString());
	    } finally {
		if (canvas != null) {
		    m_holder.unlockCanvasAndPost(canvas);
		}
	    }
	    Thread.yield();
	}
    }

    private void updateParticleExplosions() {
	synchronized (m_explosions) {
	    for (Iterator<ParticleExplosion> iter = m_explosions.iterator(); iter.hasNext();) {
		ParticleExplosion p = iter.next();
		p.update();
		
		if (p.isDead()) {
		    iter.remove();
		}
	    }
	}
    }

    private void draw(Canvas canvas) {
	if (canvas != null) {
	    canvas.drawARGB(255, 0, 0, 0);

	    m_shipMgr.draw(canvas);
	    m_asteroids.draw(canvas);

	    for (ParticleExplosion p : this.m_explosions) {
		p.draw(canvas);
	    }
	}
    }

    public void onSensorChanged(SensorEvent event) {
	if (m_shipMgr != null) {
	    m_shipMgr.updateLocation(event.values[1], event.values[0]);
	}
    }

    public void onTouchEvent() {
	m_shipMgr.fire();
    }

    public void setWidthAndHeight(int width, int height) {
	if (m_shipMgr == null) {
	    m_shipMgr = new ShipManager(m_resources, width, height);
	}

	if (m_asteroids == null) {
	    m_asteroids = new AsteroidManager(m_resources, width, height);
	}
    }

    public interface ScoreChangedListener {
	void onScoreChanged(int score);
    }

    public void resumeGame() {
	if (!isInitialised) {
	    isInitialised = true;
	    this.self = executor.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);
	}
	isRunning = true;
    }

    public void pauseGame() {
	isRunning = false;
    }
}