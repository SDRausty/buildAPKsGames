package pt.isec.tp.am;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {
	
	private int mMaxValue      = 7;
	private int mMinValue      = 3;
	private int mCurrentValue = 3;
	private SeekBar mSeekBar = null;
	private TextView mStatusText = null;

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected View onCreateView(ViewGroup parent){
		setPersistent(true);
		
		LinearLayout layout =  null;
		
		try {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = (LinearLayout) inflater.inflate(R.layout.preferences_seekbar, parent, false);
		}
		catch(Exception e)
		{
			Log.e(" " + this.getClass(), "Error creating seek bar preference", e);
			return layout;
		}
		
		mSeekBar = (SeekBar) layout.findViewById(R.id.seekBar1);
		
		if ( mSeekBar != null ) {
			mSeekBar.setMax(mMaxValue);
			mSeekBar.setProgress(mCurrentValue);
			mSeekBar.setOnSeekBarChangeListener(this);
		}
		
		mStatusText = (TextView) layout.findViewById(R.id.textView3);
		if ( mStatusText != null )
			mStatusText.setText(Integer.toString(mSeekBar.getProgress()));

		return layout;
		
	}
    
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(progress < mMinValue)
			progress = mMinValue;
		
		// change rejected, revert to the previous value
		if(!callChangeListener(progress)){
			seekBar.setProgress(mCurrentValue); 
			return; 
		}

		// change accepted, store it
		mCurrentValue = progress;
		String text = "";
		if ( mCurrentValue == mMinValue)
			text += "Min: ";
		else if ( mCurrentValue == mMaxValue )
			text += "Max: ";
		text += String.valueOf(progress);
		
		mStatusText.setText(text);
		seekBar.setProgress(progress);
		persistInt(mCurrentValue);
		notifyChanged();
	}

	public void onStartTrackingTouch(SeekBar seekBar) 
	{	
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override 
	protected Object onGetDefaultValue(TypedArray ta, int index){
		
		int defaultValue = ta.getInt(index, mMinValue);
		return defaultValue;
		
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		
		if(restoreValue) {
			//Log.d("Restore", "restore");
			
			mCurrentValue = getPersistedInt(mMinValue);
		}
		else {
			int temp = 1;
			try {
				temp = (Integer)defaultValue;
			}
			catch(Exception ex) {
				Log.e(this.getClass().toString(), "Invalid default value: " + defaultValue.toString());
			}
			
			persistInt(temp);
			mCurrentValue = temp;
			
		}
		
	}
	
}
