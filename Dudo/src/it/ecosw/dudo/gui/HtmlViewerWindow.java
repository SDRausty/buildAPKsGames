package it.ecosw.dudo.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.webkit.WebView;

/**
 * This class allow to show an html file inside the app
 * @author Enrico Strocchi
 *
 */
public class HtmlViewerWindow {

	public static boolean showWindow(Context context, String text, String title, int iconid){
	    // Create the webview
		WebView wv = new WebView (context);
		wv.loadData(text, "text/html", "utf-8");
		wv.setBackgroundColor(Color.WHITE);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		// Create the alert dialog
		new AlertDialog.Builder(context)
		.setIcon(iconid)
		.setTitle(title)
		.setView(wv)
		.show();
		return true;
	}
	
}
