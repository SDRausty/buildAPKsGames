package pt.isec.tp.am;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Paint;

public class Painter {
	
	private static Map<Barrier, Paint> barrierToPaint = Collections.synchronizedMap(new HashMap<Barrier, Paint>());
	private static Map<Barrier, Paint> barrierToDefaultColor = Collections.synchronizedMap(new HashMap<Barrier, Paint>());
	private static Paint defaultFillPaint;
	private static Paint defaultBorderPaint;
	
	public static synchronized void addBarrierAndColor(Barrier barrier, Paint paint) {
		barrierToPaint.put(barrier, paint);
	}
	
	public static synchronized Paint getFillPaint(Barrier barrier) {
		if ( Barrier.hasColor() )
			return barrierToPaint.get(barrier);
		
		return defaultFillPaint;
	}
	
	public static synchronized Paint getBorderPaint(Barrier barrier) {
		if ( Barrier.hasColor() )
			return barrierToPaint.get(barrier);
		
		return defaultBorderPaint;
	}

}
