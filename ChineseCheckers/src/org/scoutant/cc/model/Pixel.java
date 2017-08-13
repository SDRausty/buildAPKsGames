package org.scoutant.cc.model;

import android.graphics.Point;
import android.view.MotionEvent;

/** A true point on the screen. Is android.graphics.Point. Just not to make confusion with project board 'Point' class */ 
public class Pixel extends Point {

	public Pixel(int x, int y) {
		super(x,y);
	}
	public Pixel (MotionEvent event) {
		this(  Float.valueOf(event.getX()).intValue(), Float.valueOf(event.getY()).intValue() );
	}

	/** @return distance between Pixels @param a and @param z. As square value! */
	public static int distance(Pixel a, Pixel z) {
		return (a.x-z.x)*(a.x-z.x) + (a.y-z.y)*(a.y-z.y); 
	}
	
}
