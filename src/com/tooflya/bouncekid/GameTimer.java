package com.tooflya.bouncekid;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

public class GameTimer implements ITimerCallback {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.engine.handler.timer.ITimerCallback#onTimePassed (org.anddev.andengine.engine.handler.timer.TimerHandler)
	 */
	@Override
	public void onTimePassed(final TimerHandler pTimerHandler) {
		GameActivity.map.hero.update();
		GameActivity.map.update();
	}

	public static void reset() {
	}
}
