package com.mobilepearls.react;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilepearls.react.ReactHighScoreDatabase.HighScoreEntry;

public class ReactHighScoresActivity extends ListActivity {

	public static String JUST_STORED = "com.mobilepearls.react.just_stored";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.mobilepearls.react.R.layout.highscorelist);

		ReactHighScoreDatabase db = ReactHighScoreDatabase.getDatabase(this);
		final List<String> list = new ArrayList<String>();
		for (HighScoreEntry entry : db.getSortedHighScores()) {
			list.add(entry.score / ReactView.NUMBER_OF_CLICKS + " ms - " + entry.name);
		}

		setListAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, list));
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		// Toast.makeText(this, "You have selected " + position, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(com.mobilepearls.react.R.menu.highscore_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == com.mobilepearls.react.R.id.shareHighScoreMenuItem) {
			share();
		}
		return super.onOptionsItemSelected(item);
	}

	private void share() {
		ReactHighScoreDatabase db = ReactHighScoreDatabase.getDatabase(this);
		List<HighScoreEntry> list = db.getSortedHighScores();
		if (list.isEmpty()) {
			Toast.makeText(this, "No high score to share yet!", Toast.LENGTH_SHORT).show();
			return;
		}
		StringBuilder buffer = new StringBuilder("My high score in React! (http://mobilepearls.com):\n\n");
		for (HighScoreEntry entry : list) {
			buffer.append(entry.score).append(" ms - ").append(entry.name).append('\n');
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		// sendIntent.putExtra("sms_body", "The SMS text");
		intent.putExtra(Intent.EXTRA_TEXT, buffer.toString());
		intent.putExtra(Intent.EXTRA_SUBJECT, "Memory high score");
		startActivity(Intent.createChooser(intent, "Share high score"));
	}

}
