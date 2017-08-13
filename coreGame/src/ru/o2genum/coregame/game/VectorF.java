package ru.o2genum.coregame.game;

import android.graphics.PointF;
import android.util.Log;

public class VectorF extends PointF
{
	public VectorF(float x, float y)
	{
		super(x, y);
	}
	// Not modifying methods. Bad for GC.
	public VectorF add(VectorF vector)
	{
		return new VectorF(this.x + vector.x, this.y + vector.y);
	}

	public VectorF multiply(float factor)
	{
		return new VectorF(this.x * factor, this.y * factor);
	}

	public VectorF divide(float factor)
	{
		return multiply(1.0F / factor);
	}

	public VectorF subtract(VectorF vector)
	{
		return new VectorF(this.x - vector.x, this.y - vector.y);
	}
	// These methods modify vector. Good for GC.
	public void multiplyThis(float factor)
	{
		this.x *= factor;
		this.y *= factor;
	}

	public void divideThis(float factor)
	{
		multiplyThis(1.0F / factor);
	}

	public void addToThis(VectorF vector)
	{
		this.x += vector.x;
		this.y += vector.y;
	}

	public void addToThis(float x, float y)
	{
		this.x += x;
		this.y += y;
	}

	public void subtractFromThis(VectorF vector)
	{
		this.x -= vector.x;
		this.y -= vector.y;
	}
}
