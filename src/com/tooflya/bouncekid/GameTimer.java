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

	private World world;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GameTimer(final World world) {
		this.world = world;
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
		this.world.update();
	}

	public static void reset() {
	}
}
