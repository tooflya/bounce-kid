package com.tooflya.bouncekid;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import com.tooflya.bouncekid.screens.MainScreen;

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

	private int update = -120;
	private int updateCount = 0;
	private int parallaxUpdateCount = 0;
	private final int updateNecessity = Options.fps * 2;

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
		this.update++;

		if (this.update % this.updateNecessity == 0 && this.update > 0) {
			this.updateCount++;

			world.personage.runStep += 0.1f;

			if (this.updateCount % 2 == 0) {
				this.parallaxUpdateCount++;
				MainScreen.autoParallaxBackground.accelerate(this.parallaxUpdateCount);
			}
		}

		GameTimer.world.update();
	}

	public static void reset() {
	}
}
