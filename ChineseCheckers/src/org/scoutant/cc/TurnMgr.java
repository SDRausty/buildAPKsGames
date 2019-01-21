package org.scoutant.cc;

import org.scoutant.cc.model.Peg;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class TurnMgr {
	private ImageView view;
	private BaseActivity activity;

	public TurnMgr(BaseActivity activity, ImageView turnView) {
		this.activity = activity;
		this.view = turnView;
		updateView();
	}

	private int turn = 0;
	private Animation myFadeInAnimation;
	private Animation cautionAnimation;
	
	public int player() { return turn;}
	
	/** @return true is current player is AI  */
	public boolean isAI() { return activity.ai(turn); }

	/** @return true is current player is human  */
	public boolean ishuman() { return !isAI(); }
	
	public boolean onlyAis() { return activity.onlyAI(); }
	
	public void update() {
		turn++;
		turn = turn%6;
		if (activity.playing(turn)){
			updateView();
		} else update();
	}

	private void updateView(){
		int resId = PegUI.icons[turn];
		view.setImageResource(resId);
		myFadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
		cautionAnimation = AnimationUtils.loadAnimation(activity, R.anim.shrink_fade_out_center);
		view.startAnimation(myFadeInAnimation);
	}

	public boolean allowed(Peg peg){
		return ( peg.color == turn);
	}

	public void pop() {
		turn+=5;
		turn = turn%6;
		if (activity.playing(turn)){
			Log.d("activity", "turn is now : " + turn);
			updateView();
		} else pop(); 
	}

	
	/**
	 * animates the peg view showing who is the turn, mostly to indicate the user pressed wrong color... 
	 */
	public void animate() {
		view.startAnimation(cautionAnimation);
	}
}
