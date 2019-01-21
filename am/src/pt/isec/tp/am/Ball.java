package pt.isec.tp.am;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.RectF;
import android.util.Log;



public class Ball {

	Point m_center;
	float m_radius;
	protected Model m_model;
	private float m_rotation;
	private float mMoves;
	String m_image;
	boolean m_out;
	int m_color;
	RectF m_rectCollided;
	boolean m_playable;
	ArrayList<RectF> m_rectsAux;
	public Integer resource;
	
	static enum DIRECTION {
	    LEFT, TOP, RIGHT, BOTTOM;
	}
	
	public Ball(Model model, float cx, float cy, float radius) {
		this(model, cx, cy, radius, false, 0);
	}
	
	public Ball(Model model, float cx, float cy, float radius, boolean playable) {
		this(model, cx, cy, radius, playable, 0);
	}
	
	public Ball(Model model, float cx, float cy, float radius,boolean playable, int color) {
		if ( cx < radius+1 )
			cx = radius + 1;
		m_center = new Point(cx, cy);
		m_radius = radius;
		m_model = model;
		m_rotation = 0;
		m_out = false;
		m_color = color;
		m_playable = playable;
		m_rectsAux = new ArrayList<RectF>();
		mMoves = 0;
	}
	
	public String image() {
		return m_image;
	}
	
	public Point center() {
		return m_center;
	}
	
	public float radius() {
		return m_radius;
	}
	
	public void move(int steps, DIRECTION dir) {
		float rotate = 10;
		
		
			switch(dir)
			{
			case LEFT:
				steps = -steps;
				rotate *= -1;
			case RIGHT:
				float unitsTomove = move(steps);
				
				if ( unitsTomove <= 0 )
					return;
			
				if ( steps < 0 ) 
					unitsTomove *= -1;
			
				synchronized(m_center){
					m_center.addX(unitsTomove);
					m_rotation += rotate;
				}
				
				/*if ( moved > 0 )
					m_rotation += rotate;
				
				if ( canMove(m_center.x()+steps, m_center.y()) ){
					m_center.addX(steps);
					m_rotation += rotate;
				}*/
			
		}
		
		
	}
	
	private float move(int steps) {
		float cx = m_center.x()+steps;
		float absSteps = Math.abs(steps);
		float cy = m_center.y();
		float left = cx - m_radius;
		float right = cx + m_radius;
		float top = cy - m_radius;
		float bottom = cy + m_radius;
		float overlapedDist = 0;
		Barrier b = null;
		
		if ( right > m_model.getScreenSize().width() ) 
			return absSteps - (right - m_model.getScreenSize().width());
		
		if ( left < 0 )
			return absSteps - Math.abs(left);
		
		synchronized(m_model.barriers()) {
			for(Barrier barrier : m_model.barriers()) {
				overlapedDist = barrier.getOverlapedDist(left, top, right, bottom);
				if ( overlapedDist != 0) {
					if ( m_playable && Barrier.hasColor() && barrier.color() == m_color )
						return absSteps;
					b = barrier;
					break;
				}
				//if ( barrier.getOverlapedDist(left, top, right, bottom) != 0 )
				//	return false;
			}
		}
		
		if ( b != null && overlapedDist > 0) {
			if ( cy >= b.rect().top && cy <= b.rect().bottom )
				return absSteps - overlapedDist;
			else if ( cy >= b.rect().bottom && top <= b.rect().bottom) {
				setCenterX(cx);
				moveOnOverlap(b);
				return 0;
			}
		}
		
		return absSteps;
	}
	
	public void drop() {
		float cy = m_center.y() + 1;
		
		//se a bola estiver no topo do ecrã, o jogador perdeu
		if ( cy <= 0 || cy > m_model.getScreenSize().height() + m_model.spaceBetweenBarriers()) {
			m_out = true;
			return;
		}
		

		
		//se a bola estiver no fundo do ecrã e for uma bola jogável não faz nada
		if ( cy + m_radius  >= m_model.getScreenSize().height() && m_playable ){
				//mete o modelo mais rápido se for uma bola "jogável", isto porque as bolas bónus também executam esta função 
				//e não devem pôr o modelo mais rápido
				m_model.faster();
				return;
		} else if ( m_playable )
			m_model.slower();
		

		setCenterY(cy);
		
		/*synchronized(m_model.barriers()) {
			for(Barrier barrier: m_model.barriers()) {
				if ( m_playable && barrier.isPassed() )
					continue;
				//se for de encontro a uma barreira, actualiza a posição para evitar a colisão e sai (não cai)
				moveOnOverlap(barrier);
			}
		}*/
			
		/*synchronized(m_model.barriers()) {
			for(Barrier barrier: m_model.barriers()) {
				if (barrier.isPassed()) continue;
				//se for de encontro a uma barreira, actualiza a posição para evitar a colisão e sai (não cai)
				if ( moveOnOverlap(barrier) )
					return;
			}
		}*/
		
	}
	
	
	public void setCenterXY(float x, float y) {
		synchronized(m_center) {
			m_center.setX(x);
			m_center.setY(y);
		}
	}
	
	public void setCenterX(float x) {
		synchronized(m_center) {
			m_center.setX(x);
		}
	}
	
	public void setCenterY(float y) {
		synchronized(m_center) {
			m_center.setY(y);
		}
	}
	
	public void addCenterY(float y) {
		synchronized(m_center) {
			m_center.addY(y);
		}
	}
	
	public void addCenterX(float x) {
		synchronized(m_center) {
			m_center.addX(x);
		}
	}
	
	public boolean moveOnOverlap(List<BonusBall> balls) {
		return moveOnOverlap(balls, 0, balls.size());
	}
	
	public boolean moveOnOverlap(List<BonusBall> balls, int start, int end) {
		float m, overlapDist, distx, disty, dist;
		double angle;
		int i = 0;
		Ball b;
		
		synchronized(balls) {
			Iterator<BonusBall> it = balls.iterator();
			while(it.hasNext()) {
				b = it.next();
				
				if ( i < start ) {
					i++;
					continue;
				}
				
				if ( i >= end )
					break;
				
				dist = m_center.distance(b.center());
				if( dist < m_radius + b.radius()) {
					overlapDist = (m_radius + b.radius()) - dist;
					m = ( b.center().y() -m_center.y()) / (b.center().x() - m_center.x() );
					angle = Math.atan((double)m);
					
					if ( b.center().x() > m_center.x() )
						i = -1;
					else 
						i = 1;
					
					distx =  (float) (i * overlapDist * Math.cos(angle));
					disty =  (float) (i * overlapDist * Math.sin(angle));
					//boolean ballValidPos = b.validPosition(b.center().x()-distx, b.center().y()-disty, b.radius() );
					if ( validPosition(m_center.x()+distx, m_center.y()+disty , m_radius )  ) {
						m_center.addX(distx);
						m_center.addY(disty);
					}
					else{
						b.center().addX(-distx);
						b.center().addY(-disty);
					}
						
				}
				
				i++;
			}
			/*for(i=start; i < end; i++) {
				b = balls.get(i);
				dist = m_center.distance(b.center());
				if( dist < m_radius + b.radius()) {
					overlapDist = (m_radius + b.radius()) - dist;
					m = ( b.center().y() -m_center.y()) / (b.center().x() - m_center.x() );
					angle = Math.atan((double)m);
					
					if ( b.center().x() > m_center.x() )
						i = -1;
					
					m_center.addX((float) ( i * overlapDist * Math.cos(angle)));
					m_center.addY((float) ( i * overlapDist * Math.sin(angle)));
				}
			}*/
		}
		return true;
	}
    
    public boolean moveOnOverlap(Barrier barrier) {
    	
		float cy = m_center.y();
		float cx = m_center.x();
		RectF rectCollided = null;
		float bbottom = cy + m_radius;
		float btop = cy - m_radius;
		float bleft = cx - m_radius;
		float bright = cx + m_radius;
		
		rectCollided = barrier.getOverlapedRect(bleft, btop, bright, bbottom);
		
		if ( rectCollided == null )
			return false;
		
		float m, rx, ry, overlapDist;
		double angle;
		float dist = m_center.distance(rectCollided.left, rectCollided.top);
		int i = 1;
		rx = rectCollided.left;
		ry = rectCollided.top;
		
		if ( dist >= m_radius ) {
			dist = m_center.distance(rectCollided.left, rectCollided.bottom);
			ry = rectCollided.bottom;
		}
	
		if ( dist >= m_radius ){
			dist = m_center.distance(rectCollided.right, rectCollided.top);
			rx = rectCollided.right;
			ry = rectCollided.top;
		}
				
		if ( dist >= m_radius ) {
			dist = m_center.distance(rectCollided.right, rectCollided.bottom);
			rx = rectCollided.right;
			ry = rectCollided.bottom;
		}
		
		if ( dist < m_radius ) {

			overlapDist = m_radius - dist;
			m = ( ry -m_center.y()) / (rx - m_center.x() );
			angle = Math.atan((double)m);
			
			if ( rx > m_center.x() )
				i = -1;

			m_center.addX((float) ( i * overlapDist * Math.cos(angle)));
			m_center.addY((float) ( i * overlapDist * Math.sin(angle)));
			
		}
		

		
		if ( bbottom >= rectCollided.top &&  btop <= rectCollided.top && m_center.x() >= rectCollided.left && m_center.x() <= rectCollided.right)
			m_center.setY(m_center.y() - Math.abs(bbottom - rectCollided.top));
		
		//if (bbottom >= bottom &&  btop <= bottom)
		//	m_center.setY(m_center.y() - Math.abs(bbottom-top));
	
		/*if ( bbottom >= top &&  bbottom <= bottom && m_center.x() >= left && m_center.x() <= right ) 
			m_center.setY(m_center.y() - (bbottom-top));
		
		if ( btop >= top &&  btop <= bottom && m_center.x() >= left && m_center.x() <= right ) 
			m_center.setY(m_center.y() + (bottom - btop));*/
		
		if ( m_center.y() >= rectCollided.top &&  m_center.y() <= rectCollided.bottom && bleft >= rectCollided.left && bleft <= rectCollided.right ) 
			m_center.setX(m_center.x() + Math.abs((rectCollided.right - bleft)));
		
		if ( m_center.y() >= rectCollided.top &&  m_center.y() <= rectCollided.bottom && bright >= rectCollided.left && bright <= rectCollided.right ) 
			m_center.setX(m_center.x() - Math.abs((bright - rectCollided.left)));
		
		return true;
	}
	
	public float getRotation() {
		return m_rotation;
	}
	
	public boolean validPosition(float x, float y, float radius) {
		
		if ( x - radius < 0 || y - radius < 0 || x + radius > m_model.getScreenSize().width() || y + radius > m_model.getScreenSize().height() )
			return false;
		
		return true;
	}
	
	public boolean contains(float x, float y) {
		float cx = m_center.x();
		float cy = m_center.y();
		float radius = m_radius * 2;
		if ( x >= cx - radius && x <= cx + radius && y >= cy - radius && y <= cy + radius ) 
			return true;
		
		return false;
	}
	
	public boolean isOut() {
		return m_out; 
	}
	
	public float rotation() {
		return m_rotation;
	}
	
	public void setOut(boolean out) {
		m_out = out;
	}
	
	public int color() {
		return m_color;
	}
}
