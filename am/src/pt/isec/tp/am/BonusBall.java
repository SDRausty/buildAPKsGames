package pt.isec.tp.am;

import java.util.Random;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

class ColorBonusBall extends BonusBall
{
	
	private static Thread bonusThread = null;
	private static Thread newBonusThread = null;
	private static BonusBall bonusBall = null;
	
	public ColorBonusBall(Model model, float cx, float cy, float radius) {
		super(model, cx, cy, radius);
		// TODO Auto-generated constructor stub
		resource = R.drawable.bola_cores;
	}
	
	@Override
	public void execute() {
		m_triggered = false;
		newBonusThread = new Thread(this);
		newBonusThread.start();
	}
	
	@Override
	public void run() {
		
		m_model.playSound("color");
		Barrier.activateColor();
		
		if (bonusThread !=  null) {
			synchronized(bonusThread) {
				if (bonusThread.isAlive())
				{
					bonusBall.terminate();
					try {
						bonusThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				bonusThread = newBonusThread;
				bonusBall = this;
			}
		}
		else {
			bonusThread = newBonusThread;
			bonusBall = this;
		}
		

		
		sleep = 10000;
		super.run(); //dorme durante o valor do sleep, ou seja, 10s
		if ( m_reset )
			Barrier.deactivateColor();

		m_executed = true;
		
	}
	
	@Override
	public Thread getThread() {
		return bonusThread;
	}
}



class BombBonusBall extends BonusBall
{
	
	public BombBonusBall(Model model, float cx, float cy, float radius) {
		super(model, cx, cy, radius);
		// TODO Auto-generated constructor stub
		resource = R.drawable.bomb;
	}
	
	@Override
	public void execute() {
		m_triggered = false;
		m_model.playSound("bomb");
		m_model.addScore(this, m_model.barriers().size() * m_model.points());
		synchronized(m_model.barriers()) {
			m_model.barriers().clear();
		}
		m_executed = true;	
	}
}


class TimeBonusBall extends BonusBall {
	Model m_model;
	final int timepause = 5000;
	private static Thread bonusThread = null;
	private static Thread newBonusThread = null;
	private static BonusBall bonusBall = null;
	private static float mOriginalSpeed = -1;
	
	public TimeBonusBall(Model model, float cx, float cy, float radius) {
		super(model, cx, cy, radius);
		resource = R.drawable.time;
		this.m_model=model;
		
	}
	
	@Override
	public void execute() {	
		m_triggered = false;
		newBonusThread = new Thread(this);
		newBonusThread.start();
	}
	
	@Override
	public void run() {
		
		m_model.playSound("time");
		if (bonusThread !=  null) {
			synchronized(bonusThread) {
				if (bonusThread.isAlive())
				{
					bonusBall.terminate();
					try {
						bonusThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				bonusThread = newBonusThread;
				bonusBall = this;
			}
		}
		else {
			bonusThread = newBonusThread;
			bonusBall = this;
		}
		
		sleep = 5000;
		if ( mOriginalSpeed == -1 )
			mOriginalSpeed = m_model.getSpeed();
		
		float speed = m_model.getSpeed() / 2; 
		if ( speed > 0.0 ) {
			m_model.setSpeed(speed);
			
			super.run();//adormece durante <sleep> segundos
			
			if ( m_reset ) {
				m_model.setSpeed(mOriginalSpeed);
				mOriginalSpeed = -1;
			}
		}
		
		m_executed = true;
		
	}
	
	@Override
	public Thread getThread() {
		return bonusThread;
	}
	
}


class FailBonusball extends BonusBall{

	public FailBonusball(Model model, float cx, float cy, float radius) {
		super(model, cx, cy, radius);
		resource = R.drawable.failbomb;
	}
	
	public void execute() {
		m_triggered = false;
		m_model.terminate();
		m_executed = true;
	}
}


class BonusBall extends Ball implements Runnable{
	
	boolean m_triggered;
	boolean m_executed;
	public Integer resource;
	volatile  private int _sleep;
	protected int sleep;
	protected boolean m_terminated;
	protected boolean m_reset;
	public volatile static boolean runAll = true;
	private static Thread bonusThread = null;
	
	public BonusBall(Model model, float cx, float cy, float radius) {
		super(model, cx, cy, radius);
		m_triggered = false;
		m_executed = false;
		resource = new Integer(0);
		sleep = _sleep = 0;
		m_terminated = false;
		m_reset = true;
		runAll = true;
	}
	
	public void execute() {
		m_executed = true;
	}
	
	public void setTriggered(boolean trigger) {
		m_triggered = trigger;
	}
	
	public boolean isTriggered() {
		return m_triggered;
	}
	
	public boolean isExecuted() {
		return m_executed;
	}

	public void run() {
		while (_sleep <= sleep && runAll) {
			try {
				Thread.sleep(300);
				_sleep += 300;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				terminateAndReset();
			}
		}
	}
	
	public void terminate() {
		m_terminated = true;
		m_reset = false;
		_sleep = sleep + 1;
	}
	
	public void terminateAndReset() {
		m_terminated = true;
		m_reset = true;
		_sleep = sleep + 1;
	}
	
	public boolean terminated() {
		return m_terminated;
	}
	
	public Thread getThread() {
		return bonusThread;
	}
}
