package pt.isec.tp.am;

public class Size extends Object {
	
	float m_width = 0;
	float m_height = 0;
	
	public Size(float width, float height) {
		m_width = width;
		m_height = height;
	}
	
	public Size(int width, int height) {
		this((float) width, (float) height);
	}
	
    public Size(double width, double height) {
    	this((float) width, (float) height);
    }
	
	public Size(Size s) {
		this(s.width(), s.height());
	}
	
	public Size() {
		this(0, 0);
	}

	public float width(){
		return m_width;
	}
	
	public void setWidth(float width){
		m_width = width;
	}
	
	public void addWidth(float width){
		m_width += width;
	}
	
	public float height(){
		return m_height;
	}
	
	public void setHeight (float height){
		m_height = height;
	}
	
	public void addHeight (float height){
		m_height += height;
	}
	
	public boolean equals(Object o){
		if(o == null || o.getClass() != this.getClass() )
			return false;
		
		Size size = (Size) o;
		
		if ( size.width() == this.width() && size.height() == this.height())
			return true;
			
		return false;
	}

}
