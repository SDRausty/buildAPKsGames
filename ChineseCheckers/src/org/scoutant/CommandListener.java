package org.scoutant;

import android.view.View;
import android.view.View.OnClickListener;

public class CommandListener implements OnClickListener {
	private Command command;
	public CommandListener(Command command) {
		this.command = command;
	}
	@Override
	public void onClick(View v) {
		command.execute();
	}
}
