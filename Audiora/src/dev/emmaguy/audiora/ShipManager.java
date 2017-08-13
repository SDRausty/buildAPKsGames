package dev.emmaguy.audiora;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class ShipManager {
    private static final int SENSOR_CHANGE_MIN = 1;
    private static final float SCALE_SHIP_SPEED_X = 0.3f;
    private static final float SCALE_SHIP_SPEED_Y = 0.4f;

    private final int m_canvasWidth;
    private final int m_canvasHeight;

    private float m_shipX = 0, m_shipY = 0;

    private Bitmap m_shipBitmap = null, m_projectile = null;
    private List<Projectile> m_projectiles = new ArrayList<Projectile>();
    private List<Projectile> m_newProjectiles = new ArrayList<Projectile>();

    public ShipManager(Resources r, int width, int height) {
	this.m_canvasWidth = width;
	this.m_canvasHeight = height;
	this.m_shipX = 30;
	this.m_shipY = m_canvasHeight / 2;
	this.m_projectile = BitmapFactory.decodeResource(r, R.drawable.bullet);
	this.m_shipBitmap = BitmapFactory.decodeResource(r, R.drawable.spaceship);
    }

    public void draw(Canvas c) {
	c.drawBitmap(m_shipBitmap, m_shipX, m_shipY, null);

	for (int i = 0; i < m_projectiles.size(); i++) {
	    Projectile prj = m_projectiles.get(i);
	    prj.draw(c);
	}
    }

    public void update() {
	for (int i = 0; i < m_projectiles.size(); i++) {
	    Projectile prj = m_projectiles.get(i);
	    prj.update(5);

	    if (prj.isOffScreen()) {
		m_projectiles.remove(prj);
	    }
	}

	addNewProjectiles();
    }

    public void updateLocation(float x, float y) {
	if (x > SENSOR_CHANGE_MIN && m_shipX < m_canvasWidth - m_shipBitmap.getWidth()) {
	    m_shipX += (x / SCALE_SHIP_SPEED_X);
	} else if (x < -SENSOR_CHANGE_MIN && m_shipX > 0) {
	    m_shipX -= (x / -SCALE_SHIP_SPEED_X);
	}

	if (y > SENSOR_CHANGE_MIN && m_shipY < m_canvasHeight - m_shipBitmap.getHeight()) {
	    m_shipY += (y / SCALE_SHIP_SPEED_Y);
	} else if (y < -SENSOR_CHANGE_MIN && m_shipY > 0) {
	    m_shipY -= (y / -SCALE_SHIP_SPEED_Y);
	}
    }

    public void fire() {
	Projectile pj = new Projectile(m_shipX + m_shipBitmap.getWidth(), (float)(m_shipY + 0.5 * m_shipBitmap.getHeight()));
	m_newProjectiles.add(pj);
    }

    private void addNewProjectiles() {
	m_projectiles.addAll(m_newProjectiles);
	m_newProjectiles.clear();
    }

    public List<Projectile> getProjectiles() {
	return m_projectiles;
    }
    
    class Projectile {
	private float xlocation, ylocation;
	
	public Projectile(float x, float y) {
	    this.xlocation = x;
	    this.ylocation = y;
	}

	public void update(int i) {
	    this.xlocation += i;
	}

	public boolean isOffScreen() {
	    return xlocation > m_canvasWidth;
	}

	public void draw(Canvas c) {
	    c.drawBitmap(m_projectile, xlocation, ylocation, null);
	}

	public float getX() {
	    return xlocation;
	}
	
	public float getY() {
	    return ylocation;
	}
    }
}
