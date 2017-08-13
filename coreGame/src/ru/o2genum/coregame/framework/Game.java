package ru.o2genum.coregame.framework;

public interface Game
{
	public Input getInput();

	public Vibration getVibration();

	public Audio getAudio();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public void setScreen(Screen screen);

	public Screen getCurrentScreen();

	public Screen getStartScreen();
}
