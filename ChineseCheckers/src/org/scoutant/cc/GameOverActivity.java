package org.scoutant.cc;

import java.util.List;

import org.scoutant.cc.model.Game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class GameOverActivity extends BaseActivity {

	private static final String tag = "activity";
	private LinearLayout container;
	private int rank=0;
	private LayoutInflater inflater;
	private List<Integer> ranking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		container = (LinearLayout) findViewById(R.id.container);
		inflater = getLayoutInflater();
		bind( R.id.play, new Finish());
		processRankingOrder();
		for (int player=0; player<6; player++) {
			if (playing(player)) addRanking();
		}
	}

	private void processRankingOrder() {
		int[] overs = getIntent().getIntArrayExtra("overs");
		ranking = Game.ranking(overs);
	}
	
	private void addRanking() {
		try {
			View view = inflater.inflate(R.layout.ranking_item, container, false);
			TextView tv = (TextView) view.findViewById(R.id.rank);
			tv.setText(""+(rank+1));
			ImageView iv = (ImageView) view.findViewById(R.id.peg);
			Integer player = ranking.get(rank);
			iv.setImageResource( PegUI.icons[ player]);
			rank++;
			container.addView(view);
		} catch(Exception e) {
			Log.e(tag, "BAD ranking!", e);
		}
	}
}
