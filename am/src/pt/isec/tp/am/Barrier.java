package pt.isec.tp.am;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;

class Barrier
{
	Model m_model;
	float top, bottom;
	RectF m_rect;
	ArrayList<RectF> m_rects;
	boolean m_outOfScreen;
	boolean m_passed;
	/*static int pink = Color.rgb(255, 0, 255);
	static int purple = Color.rgb(128, 0, 128);
	static int orange = Color.rgb(255, 127, 0);
	static int white = 	Color.rgb(248, 248, 255);*/
	static int [] m_colors = {Color.GREEN, Color.RED, Color.BLACK, Color.BLUE};
	int m_color;
	private volatile static boolean m_hasColor = false;
	private float mPassingPointX1 = 0;
	private float mPassingPointX2 = 0;
	
	public Barrier(Model m) {
		this(m, 0, 0, 100, 20);
	}

	public Barrier(Model m, RectF rect) {
		this (m, rect.left, rect.top, rect.right, rect.bottom);
	}
	
	public Barrier(Model m, float left, float top, float right, float bottom)
	{
		m_model = m;
		m_rect = new RectF(left, top, right, bottom);
		m_outOfScreen = false;
		m_rects = new ArrayList<RectF>();
		Random rand = new Random();
		m_color = m_colors[rand.nextInt(m_colors.length)];
		m_passed = false;
	}
	
	public Barrier(Model m, float top) {
		this(m, 0, 0, 0, 0);
		
		float screenWidth = m.getScreenSize().width();
		float height = m.getScreenSize().height() / 30;
		int availableWidth = (int) (screenWidth - m.spaceBetweenBarriers());
		Random random = new Random();
		int width = random.nextInt(availableWidth);
		mPassingPointX1 = width;
		m_rects.add(new RectF(0, top, width, top+height));
		
		width = availableWidth - width;
		mPassingPointX2 = screenWidth - width;
		m_rects.add(new RectF(screenWidth - width, top, screenWidth, top+height));
		
	}
	
	
	public ArrayList<RectF> rects() {
		return m_rects;
	}
	
	public void goUp() {
		float speed = m_model.getSpeed();
		for(RectF rect : m_rects) {
			rect.top -= speed;
			rect.bottom -= speed;
		}
		
		if ( rect().bottom <= 0 )
			m_outOfScreen = true;	
	}
	
	public boolean isOut(){
		return m_outOfScreen;
	}
	

	public RectF rect() {
		return m_rects.get(0);
	}
	
	public ArrayList<Point> getPoints(){
		ArrayList<Point> sizes = new ArrayList<Point>();
		int i = 0;
		
		//percorre a base e o topo (comprimento)
		for(i=(int) m_rect.left; i <= m_rect.right ; i++){
			sizes.add(new Point(i, m_rect.top));
			sizes.add(new Point(i, m_rect.bottom));
		}
		
		//percorre os lados (largura)
		for(i=(int) m_rect.top; i <= m_rect.bottom ; i++){
			sizes.add(new Point(m_rect.left, i));
			sizes.add(new Point(m_rect.right, i));
		}
		
		return sizes;
	}
	
	public boolean containsPoint(Point point) {
		for(RectF rect : m_rects)
			if ( rect.top <= point.y() && rect.bottom >= point.y() && rect.left < point.x() && rect.right > point.x() )
				return true;
		
		return false;
	}
	
	public boolean containsAnyPoint(ArrayList<Point> points) {
		
		for(Point point : points)
			if ( m_rect.top <= point.y() && m_rect.bottom >= point.y() && m_rect.left < point.x() && m_rect.right > point.x() )
				return true;
		
		return false;
	}
	
	public Point getMatchingPoint(ArrayList<Point> points) {
		for(Point point : points)
			if ( point.y() >= m_rect.top &&  point.y() <= m_rect.bottom && point.x() >= m_rect.left && point.x() <= m_rect.right )
				return point;
		
		return null;
	}
	
	public ArrayList<Point> getMatchingPoints(ArrayList<Point> points){
		ArrayList<Point> matchPoints = new ArrayList<Point>();
		int i =0;
		
		for(Point point : points){
			if ( m_rect.top <= point.y() && m_rect.bottom >= point.y() && m_rect.left <= point.x() && m_rect.right >= point.x() )
				matchPoints.add(new Point(point));
		}
		
		return matchPoints;
	}
	
	public boolean isPassed() {
		return m_passed;
	}
	
	public void setPassed(boolean passed) {
		m_passed = passed;
	}
	
	public synchronized static void activateColor() {
		m_hasColor = true;
	}
	
	public synchronized static void deactivateColor() {
		m_hasColor = false;
	}
	
	public synchronized static boolean hasColor() {
		return m_hasColor;
	}
	
	public int color() {
		return m_color;
	}
	
	public float getOverlapedDist(float left, float top, float right, float bottom) {	
		if ( (left >= mPassingPointX1 && right <= mPassingPointX2) || bottom < rect().top || top > rect().bottom ) 
			return 0;
		
		if ( bottom >= m_rects.get(0).bottom )  {
			if (left <= mPassingPointX1 )
				return Math.abs( m_rects.get(0).right - left);
			else if ( right >=  mPassingPointX2 )
				return Math.abs( right - m_rects.get(1).left);
		}
			
		
		return 0;
	}
	
	public RectF getOverlapedRect(float left, float top, float right, float bottom) {		
		if ( (left >= mPassingPointX1 && right <= mPassingPointX2) || bottom < rect().top || top > rect().bottom ) 
			return null;
		
		if ( left < mPassingPointX1 ) 
			return m_rects.get(0);
	
		return m_rects.get(1);
		
	}
}