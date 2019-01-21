package pt.isec.tp.am;

public class Point {
	float m_x = 0;
	float m_y = 0;
	
	public Point(float x, float y) {
		m_x = x;
		m_y = y;
	}
	
	public Point(int x, int y) {
		this((float) x, (float) y);
	}
	
    public Point(double x, double y) {
    	this((float) x, (float) y);
    }
	
	public Point(Point s) {
		this(s.x(), s.y());
	}
	
	public Point() {
		this(0, 0);
	}

	public float x(){
		return m_x;
	}
	
	public void setX(float x){
		m_x = x;
	}
	
	public void addX(float x){
		m_x += x;
	}
	
	public float y(){
		return m_y;
	}
	
	public void setY (float y){
		m_y = y;
	}
	
	public void addY (float y){
		m_y += y;
	}
	
	public boolean equals(Object o){
		if(o == null || o.getClass() != this.getClass() )
			return false;
		
		Point point = (Point) o;
		
		if ( point.x() == this.x() && point.y() == this.y())
			return true;
			
		return false;
	}
	
	public float distance (Point p) {
		return(float) Math.sqrt(Math.pow(this.x() - p.x(), 2) + Math.pow(this.y() - p.y(), 2) );
	}
	
	public float distance (float x, float y) {
		return distance (new Point(x, y));
	}
	
	public String toString(){
		return Float.toString(m_x) + ", " + Float.toString(m_y);
	}
}
