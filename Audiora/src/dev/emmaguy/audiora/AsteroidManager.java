package dev.emmaguy.audiora;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import dev.emmaguy.audiora.GameThread.ScoreChangedListener;
import dev.emmaguy.audiora.ShipManager.Projectile;

public class AsteroidManager {
    private static final int MIN_NUM_ASTEROIDS = 10;
    private static final int NUM_PARTICLES = 300;

    private final List<Asteroid> m_asteroids = new ArrayList<Asteroid>();
    private final Random m_random = new Random();
    private final Bitmap m_asteroid;

    private final int m_canvasWidth;
    private final int m_canvasHeight;

    public AsteroidManager(Resources resource, int width, int height) {
	this.m_canvasWidth = width;
	this.m_canvasHeight = height;
	this.m_asteroid = BitmapFactory.decodeResource(resource, R.drawable.asteroid);
    }

    public void draw(Canvas canvas) {
	for (Asteroid a : m_asteroids) {
	    a.draw(canvas);
	}
    }

    public void update() {
	if (m_asteroids.size() < MIN_NUM_ASTEROIDS || m_random.nextInt(10) == 0) {
	    Asteroid a = new Asteroid(m_canvasWidth / 2 + m_random.nextInt(m_canvasWidth), m_random.nextInt(m_canvasHeight),
		    m_asteroid.getWidth(), m_asteroid.getHeight(), 1 + m_random.nextInt(2));

	    m_asteroids.add(a);
	}

	for (Iterator<Asteroid> iter = m_asteroids.iterator(); iter.hasNext();) {
	    Asteroid a = iter.next();
	    a.update();

	    if (a.isAsteroidOffScreen()) {
		iter.remove();
	    }
	}
    }

    public void detectForCollisions(List<Projectile> projectiles, ScoreChangedListener scoreChangedListener,
	    List<ParticleExplosion> explosions) {
	List<Asteroid> astsToDelete = new ArrayList<Asteroid>();
	List<Projectile> projsToDelete = new ArrayList<Projectile>();

	for (Projectile p : projectiles) {
	    for (Asteroid a : m_asteroids) {
		if (a != null && p != null && a.hasBeenHitBy(p)) {

		    explosions.add(new ParticleExplosion(NUM_PARTICLES, p.getX(), p.getY()));
		    scoreChangedListener.onScoreChanged(a.getSpeed() * 10);

		    astsToDelete.add(a);
		    projsToDelete.add(p);
		}
	    }
	}

	synchronized (m_asteroids) {
	    for (Asteroid a : astsToDelete) {
		m_asteroids.remove(a);
	    }
	}

	synchronized (projectiles) {
	    for (Projectile p : projsToDelete) {
		projectiles.remove(p);
	    }
	}

    }

    class Asteroid {
	private int xlocation;
	private final int ylocation;
	private final int speed;
	private final int width;
	private final int height;

	public Asteroid(int x, int y, int width, int height, int speed) {
	    this.xlocation = x;
	    this.ylocation = y;
	    this.speed = speed;
	    this.width = width;
	    this.height = height;
	}

	public boolean isAsteroidOffScreen() {
	    return xlocation < -width || ylocation < -height || xlocation > m_canvasWidth || ylocation > m_canvasHeight;
	}

	public void draw(Canvas canvas) {
	    canvas.drawBitmap(m_asteroid, xlocation, ylocation, null);
	}

	public void update() {
	    this.xlocation -= speed;
	}

	public int getSpeed() {
	    return speed;
	}

	public boolean hasBeenHitBy(Projectile p) {
	    return p.getX() >= xlocation && p.getX() < xlocation + width && p.getY() >= ylocation
		    && p.getY() < ylocation + height;
	}
    }
}
