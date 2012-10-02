package com.tooflya.bouncekid.ui;

import org.anddev.andengine.engine.camera.ZoomCamera;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.helpers.ActionHelper;

/**
 * @author Tooflya.com
 * @since
 */
public class Camera extends ZoomCamera {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mShaking;
	private float mDuration;
	private float mIntensity;
	private float mCurrentDuration;

	private float mX;
	private float mY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Camera(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);

		this.mShaking = false;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	// private final float maxA = 100;
	// private final float countT = 40;
	// private final float maxT = (float) (2 * Math.PI * 3); // Where 3 is count of wavering.
	// private final float stepT = maxT / countT;
	// private float t = 0;

	private int oldYNumber = 3;
	private float[] oldY = new float[oldYNumber];

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// float yOld = this.getCenterY();

		super.onUpdate(pSecondsElapsed);

		for (int i = oldYNumber - 1; i > 0; i--) {
			oldY[i] = oldY[i - 1];
		}
		oldY[0] = this.getCenterY();
		this.setCenter(this.getCenterX(), oldY[oldYNumber - 1]);

		// final float distance = this.getCenterY() - yOld;
		// if (distance < -this.maxA) {
		// yOld = this.getCenterY() + this.maxA;
		// t = (float) Math.PI;
		// }
		// else if (this.maxA < distance) {
		// yOld = this.getCenterY() - this.maxA;
		// t = 0;
		// }
		// else if (t < maxT && Game.world != null && Game.world.personage != null) {
		// if (!Game.world.personage.IsState(ActionHelper.Fly) && !Game.world.personage.IsState(ActionHelper.Fall)) {
		// yOld += (float) ((1 - t / maxT) * maxA * Math.sin(t));
		// t += stepT;
		// }
		// }
		//
		// this.setCenter(this.getCenterX(), yOld);

		if (mShaking) {
			mX = this.getCenterX();
			mY = this.getCenterY();

			mCurrentDuration += pSecondsElapsed;
			if (mCurrentDuration > mDuration) {
				mShaking = false;
				mCurrentDuration = 0;
				this.setCenter(mX, mY);
			} else {
				int sentitX = 1;
				int sentitY = 1;
				if (Math.random() < 0.5)
					sentitX = -1;
				if (Math.random() < 0.5)
					sentitY = -1;
				this.setCenter((float) (mX + Math.random() * mIntensity * sentitX), (float) (mY + Math.random() * mIntensity * sentitY));
			}
		}

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void shake(float d, float i) {
		if (mShaking) {
			return;
		}

		mShaking = true;
		mDuration = d;
		mIntensity = i;
		mCurrentDuration = 0;
	}
}
