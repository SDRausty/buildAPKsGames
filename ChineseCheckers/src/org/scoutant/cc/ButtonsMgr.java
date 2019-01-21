/*
* Copyright (C) 2011- stephane coutant
*
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package org.scoutant.cc;

import org.scoutant.Command;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class ButtonsMgr {

	protected static final String tag = "ui";
	private View cancel;
	private View ok;

	private GameView game;
	private Command startAI;

	public ButtonsMgr(GameView game, View ok, Command startAI, View cancel) {
		this.game = game;
		this.ok = ok;
		this.ok.setOnClickListener(doOk);
		this.startAI = startAI;
		this.cancel = cancel;
		this.cancel.setOnClickListener(doCancel);
		setVisibility(View.INVISIBLE);
		setOkState( false);
	}
	
	public void setVisibility(int visibility) {
		ok.setVisibility(visibility);
		cancel.setVisibility(visibility);
	}

	protected void setState( View btn, boolean state) {
		btn.setEnabled( state);
		btn.setAlpha( state ? 0.8f : 0.3f );
	}

	public void reset() {
		setOkState(false);
		setVisibility(View.INVISIBLE);
	}
	
	public void setOkState(boolean state) {
		setState(ok, state);
	}

	private OnClickListener doOk = new OnClickListener() {
		public void onClick(View v) {
			game.game.play( game.move);
			game.turnMgr.update();
			game.init();
			startAI.execute();
		}
	};
	private OnClickListener doCancel = new OnClickListener() {
		public void onClick(View v) {
			Log.d(tag, "cancel...");
			game.init();
		}
	};

	public void resize() {
		int h = game.height;
		Log.d(tag, "game height : " + h);
		if (h<=0) return;
		int m = (game.width - h)/2;
		if (m<=0) return;
		// let's the buttons be in middle of region with width m...
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(m,h, Gravity.LEFT);
		params.leftMargin=20;
		cancel.setLayoutParams(params);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(m,h, Gravity.RIGHT);
		layoutParams.rightMargin=20;
		ok.setLayoutParams(layoutParams);
	}
	
}