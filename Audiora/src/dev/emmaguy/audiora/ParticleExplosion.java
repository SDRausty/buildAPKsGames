package dev.emmaguy.audiora;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ParticleExplosion {
    private List<Particle> m_particles;
    private final Random m_random = new Random();

    public ParticleExplosion(int numberOfParticles, float xLocation, float yLocation) {
	this.m_particles = new ArrayList<Particle>(numberOfParticles);
	int colour = Color.argb(255, m_random.nextInt(255), m_random.nextInt(255), m_random.nextInt(255));

	for (int i = 0; i < numberOfParticles; i++) {
	    this.m_particles.add(new Particle(xLocation, yLocation, colour));
	}
    }

    public void draw(Canvas canvas) {
	for (ParticleExplosion.Particle x : this.m_particles) {
	    x.draw(canvas);
	}
    }

    public void update() {
	synchronized (m_particles) {
	    for (Iterator<ParticleExplosion.Particle> iter = m_particles.iterator(); iter.hasNext();) {
		
		Particle p = iter.next();
		p.update();
		
		if(!p.isAlive()){
		    iter.remove();
		}
	    }
	}
    }

    public boolean isDead() {
	return m_particles.isEmpty();
    }

    class Particle {
	private static final int DEFAULT_LIFETIME = 150;
	private static final int NUM_FADE = 10;
	private static final int MAX_DIMENSION = 3;
	private static final int MAX_SPEED = 4;

	private float m_xLoc, m_yLoc;
	private double m_xVel, m_yVel;
	private int m_age;
	private int m_colour;

	private final float m_width;
	private final float m_height;
	private final Paint m_paint;
	
	private boolean isAlive = true;

	public Particle(float xLocation, float yLocation, int colour) {
	    this.m_xLoc = xLocation;
	    this.m_yLoc = yLocation;
	    this.m_width = 1 + m_random.nextInt(MAX_DIMENSION);
	    this.m_height = this.m_width;
	    this.m_age = 0;
	    this.m_xVel = m_random.nextDouble() * (MAX_SPEED * 2) - MAX_SPEED;
	    this.m_yVel = m_random.nextDouble() * (MAX_SPEED * 2) - MAX_SPEED;

	    if (m_xVel * m_xVel + m_yVel * m_yVel > MAX_SPEED * MAX_SPEED) {
		m_xVel *= 0.7;
		m_yVel *= 0.7;
	    }
	    this.m_colour = colour;
	    this.m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    this.m_paint.setColor(this.m_colour);
	}
	
	public void update() {
	    
	    if(!isAlive())
		return;

	    this.m_xLoc += this.m_xVel;
	    this.m_yLoc += this.m_yVel;

	    int a = this.m_colour >>> 24;
	    a -= NUM_FADE;

	    if (a <= 0) {
		isAlive = false;
	    } else {
		this.m_colour = (this.m_colour & 0x00ffffff) + (a << 24);
		this.m_paint.setAlpha(a);

		this.m_age++;
	    }

	    if (this.m_age >= DEFAULT_LIFETIME) {
		isAlive = false;
	    }

	}

	private boolean isAlive() {
	    return isAlive;
	}

	public void draw(Canvas canvas) {
	    canvas.drawRect(this.m_xLoc, this.m_yLoc, this.m_xLoc + this.m_width, this.m_yLoc + this.m_height, m_paint);
	}
    }
}