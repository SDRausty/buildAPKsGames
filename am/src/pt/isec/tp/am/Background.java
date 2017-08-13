package pt.isec.tp.am;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;

public class Background {

	Bitmap bitmap;
	Rect rectSrc;
	RectF rectDst;
	
	public Background() {
		bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		rectSrc = null;
		rectDst = new RectF(0, 0, 0, 0);
	}
	
	public Background(Bitmap _bitmap, float x, float y) {
		bitmap = _bitmap;
		rectSrc = new Rect(0, 0, (int) x, (int) y);
		rectDst = new RectF(0, 0, x, y);
	}
	
	public void setBitmap(Bitmap _bitmap) {
		bitmap = _bitmap;
	}
	
	public void scrollDown() {
		if ( bitmap != null ) {
			rectSrc.top += 2;
			rectSrc.bottom += 2;
			if ( rectSrc.bottom >= bitmap.getHeight() ) {
				rectSrc.bottom = (int) rectDst.bottom;
				rectSrc.top = 0;
				float dif = Math.abs(rectSrc.right - rectSrc.left);
				rectSrc.left += dif;
				rectSrc.right += dif;
				
				if ( rectSrc.right >= bitmap.getWidth()) {
					rectSrc.left = 0;
					rectSrc.right = (int) rectDst.right;
				}
			}
		}
	}
	
}
