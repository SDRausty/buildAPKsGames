package pt.isec.tp.am;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class BitmapCache {
	
	private HashMap<Integer, HashMap<Float, SoftReference<Bitmap>>>resourceToRotatedBitmap;
	private HashMap<Integer, Bitmap> resourceToBitmap;
	
	public BitmapCache()  {
		resourceToRotatedBitmap = new HashMap<Integer, HashMap<Float, SoftReference<Bitmap>>>();
		resourceToBitmap = new HashMap<Integer, Bitmap>();
	}
	
	public void addBitmap(int resource, Bitmap bitmap, float rotation) {
		float rot = Math.abs(rotation) % 360;
		if ( rotation < 0 )
			rot = 360 - rot;
		
		if ( rot == 0 ) {
			if ( ! resourceToBitmap.containsKey(resource) )
				resourceToBitmap.put(resource, bitmap);
		}
		
		SoftReference<Bitmap> softBitmap = new SoftReference(bitmap);
		if ( resourceToRotatedBitmap.containsKey(resource) ) {
			if ( resourceToRotatedBitmap.get(resource).containsKey(rot))
				return;
			resourceToRotatedBitmap.get(resource).put(rot, softBitmap);
		}
		else {
			HashMap<Float, SoftReference<Bitmap>> rotToBitmap = new HashMap<Float, SoftReference<Bitmap>>();
			rotToBitmap.put(rotation, softBitmap);
			resourceToRotatedBitmap.put(resource, rotToBitmap);
		}
		//Log.d("BITMAP_TAM", resourceToRotatedBitmap.get(resource).size() + " ");
			
	}
	
	public Bitmap getBitmap(int resource, float rotation) {
			float rot = Math.abs(rotation) % 360;
			if ( rotation < 0 )
				rot = 360 - rot;
			
			if ( rot == 0.0 && resourceToBitmap.containsKey(resource)) 
				return resourceToBitmap.get(resource);
			else if ( resourceToRotatedBitmap.containsKey(resource) && resourceToRotatedBitmap.get(resource).containsKey(rotation) )
				return resourceToRotatedBitmap.get(resource).get(rotation).get();

			/*Log.d("Resource", Integer.toString(resource));
			Log.d("Rotation", Float.toString(rot));
			if ( resourceToRotatedBitmap.containsKey(resource) )
				Log.d("Resource size", Integer.toString(resourceToRotatedBitmap.get(resource).size()));*/
			
			return null;
	}
	
	public void clear() {
		for(HashMap<Float, SoftReference<Bitmap>>  map : resourceToRotatedBitmap.values()) 
			map.clear();
		
		resourceToRotatedBitmap.clear();
	}
	
	public boolean contains(int resource) {
		return resourceToRotatedBitmap.containsKey(resource) && ( ! resourceToRotatedBitmap.get(resource).isEmpty());
	}
}
