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

	public static World world;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GameTimer(final World world2) {
		world = world2;
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
		world.update();
	}

	public static void reset() {
	}
}
