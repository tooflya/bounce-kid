package com.tooflya.bouncekid.ui;

import org.anddev.andengine.engine.camera.BoundCamera;

/**
 * @author Tooflya.com
 * @since
 */
public class CustomCamera extends BoundCamera {

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

	public CustomCamera(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
		mShaking = false;

		mX = this.getCenterX();
		mY = this.getCenterY();
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);

		if (mShaking) {
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
