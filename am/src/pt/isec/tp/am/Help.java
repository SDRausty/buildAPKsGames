package pt.isec.tp.am;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class Help extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		final TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();
		tabhost.addTab(tabhost.newTabSpec("tab_help").setIndicator(getResources().getString(R.string.Help, (Object[])null)).setContent(R.id.tab1));
		tabhost.addTab(tabhost.newTabSpec("tab_help_bonus").setIndicator(getResources().getString(R.string.Bonus, (Object[])null)).setContent(R.id.tab2));
		tabhost.setCurrentTab(0);
	}

	
	
	public void onBack(View v)
	{
		finish();
	}
	
}
