package com.tooflya.bouncekid;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

/**
 * @author Tooflya.com
 * @since
 */
public class GameTimer implements ITimerCallback {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private World map;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GameTimer(final World map) {
		this.map = map;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.engine.handler.timer.ITimerCallback#onTimePassed (org.anddev.andengine.engine.handler.timer.TimerHandler)
	 */
	@Override
	public void onTimePassed(final TimerHandler pTimerHandler) {
		this.map.update();
	}

	public static void reset() {
	}
}
