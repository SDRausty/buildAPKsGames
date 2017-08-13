package org.scoutant.cc.model;
import java.util.List;

public class Log {
	
	public static void e(String tag, String msg) {
		if (Game.LOG) d(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (Game.LOG) android.util.Log.d(tag, msg);
	}

	public static void d(String tag, Object o) {
		if (Game.LOG) d(tag, ""+o);
	}

	public static void d(String tag, List<?> list) {
		if(!Game.LOG) return;
		String str = "";
		for (Object o : list) {
			str += o + ", ";
		}
		str += "\n";
		d(tag, str);
	}
	
}
