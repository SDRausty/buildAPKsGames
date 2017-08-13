package pt.isec.tp.am;

import android.graphics.Color;
import android.graphics.Paint;

public class ScorePoint {
	
	int m_alpha;
	Point m_pos;
	int m_points;
	static int color = Color.GREEN;
	
	public int getAlpha() {
		return m_alpha;
	}

	public void setAlpha(int m_alpha) {
		this.m_alpha = m_alpha;
	}
	
	public ScorePoint(Point p) {
		this(p, 2);
	}
	
	public ScorePoint(Point p, int points) {
		m_alpha = 255;
		m_points = points;
		m_pos = new Point(p);
	}
	
	public Point getPos() {
		return m_pos;
	}
	
	public void reduceAlpha() {
		m_alpha -= 2;
	}
	
	public String toString() {
		return "+" + Integer.toString(m_points);
	}
	
	

}
