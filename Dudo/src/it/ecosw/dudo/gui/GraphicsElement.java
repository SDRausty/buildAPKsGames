package it.ecosw.dudo.gui;

import it.ecosw.dudo.R;
import it.ecosw.dudo.media.GenDiceImage;
import it.ecosw.dudo.settings.SettingsHelper;
import android.app.Activity;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphicsElement {
	
	private DiceGraphicObjects[] dgos;
	
	private Chronometer chrono;
	
	private TextView playername;
	
	private LinearLayout dieLayout;
	
	private ImageButton deleteLateral,rollLateral;
	
	/**
	 * Constructor
	 * @param act Activity
	 * @param gdi Dice image generator
	 * @param settings Application Settings Helper
	 */
	public GraphicsElement(Activity act, GenDiceImage gdi, SettingsHelper settings){
		
        // Generate static graphic object
        dgos = new DiceGraphicObjects[6];
        dgos[0] = new DiceGraphicObjects(1, 
        		(ImageView)act.findViewById(R.id.ImageButton01), 
        		(ViewGroup)act.findViewById(R.id.LayoutDice01),
        		gdi,settings.isAnimationActivated());
        dgos[1] = new DiceGraphicObjects(2, 
        		(ImageView)act.findViewById(R.id.ImageButton02), 
        		(ViewGroup)act.findViewById(R.id.LayoutDice02),
        		gdi,settings.isAnimationActivated());
        dgos[2] = new DiceGraphicObjects(3, 
        		(ImageView)act.findViewById(R.id.ImageButton03), 
        		(ViewGroup)act.findViewById(R.id.LayoutDice03),
        		gdi,settings.isAnimationActivated());
        dgos[3] = new DiceGraphicObjects(4, 
        		(ImageView)act.findViewById(R.id.ImageButton04), 
        		(ViewGroup)act.findViewById(R.id.LayoutDice04),
        		gdi,settings.isAnimationActivated());
        dgos[4] = new DiceGraphicObjects(5, 
        		(ImageView)act.findViewById(R.id.ImageButton05), 
        		(ViewGroup)act.findViewById(R.id.LayoutDice05),
        		gdi,settings.isAnimationActivated());
        dgos[5] = new DiceGraphicObjects(5, 
        		(ImageView)act.findViewById(R.id.ImageButton06), 
        		(ViewGroup)act.findViewById(R.id.LayoutDice06),
        		gdi,settings.isAnimationActivated());
        
        // Set chrono
        chrono = (Chronometer)act.findViewById(R.id.chronometer);
        chrono.setFormat("%s");
		chrono.setBase(SystemClock.elapsedRealtime()+settings.getChronoTime());
        chrono.start();
        
        // Set playerName
        playername = (TextView)act.findViewById(R.id.playernameTextView);
        
        // Set die Layout
        dieLayout = (LinearLayout)act.findViewById(R.id.dieLayout);
        
        // Lateral Button for single player game
        rollLateral = (ImageButton)act.findViewById(R.id.imageButtonRollLateral);
        deleteLateral = (ImageButton)act.findViewById(R.id.imageButtonDeleteLateral);
	}
	
	/**
	 * Return dicegrphicsobject
	 * @return the dgos
	 */
	public DiceGraphicObjects[] getDgos() {
		return dgos;
	}
	
	/**
	 * Return chronometer
	 * @return the chrono
	 */
	public Chronometer getChrono() {
		return chrono;
	}

	/**
	 * Return playername
	 * @return the playername
	 */
	public TextView getPlayername() {
		return playername;
	}

	/**
	 * Return Layout with die
	 * @return the dieLayout
	 */
	public LinearLayout getDieLayout() {
		return dieLayout;
	}
	
	/**
	 * @return the deleteLateral
	 */
	public ImageButton getDeleteLateral() {
		return deleteLateral;
	}

	/**
	 * @return the rollLateral
	 */
	public ImageButton getRollLateral() {
		return rollLateral;
	}

}
