package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.helpers.ActionHelper;

public class Personage extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	/**
	 * 
	 * States of character
	 * 
	 * 0 - Is running? 1 - Is start jumping (power up)?
	 * 
	 */
	private byte currentStates;

	public float jumpPower;
	private float maxJumpPower;
	public boolean isJumping;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Personage(final TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.currentStates = 1;

		this.jumpPower = 0;
		this.maxJumpPower = 40;
		this.isJumping = false;

		this.setFlippedHorizontal(true);
		this.setScale(0.5f);

		this.setVisible(true);
		this.setIgnoreUpdate(false);
	}

	public Personage(final float x, final float  y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion);

		this.setPosition(x, y);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate
	 * (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if ((this.currentStates & ActionHelper.Running) == ActionHelper.Running) {
			runningProceed();
		}
		if ((this.currentStates & ActionHelper.Jump) == ActionHelper.Jump) {
			jumpProceed();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void runningProceed() {
		if ((this.currentStates & ActionHelper.Running) == ActionHelper.Running) {
			if (!this.isAnimationRunning() && this.jumpPower == 0) {
				this.animate(new long[] { 80, 80, 80, 80, 80, 80, 80, 80 }, 0, 7, true);
			}
		} else {
			if (this.isAnimationRunning()) {
				this.stopAnimation(2);
			}
		}
	}

	private void jumpProceed() {
		if ((this.currentStates & ActionHelper.Jump) == ActionHelper.Jump) {
			if (this.jumpPower < this.maxJumpPower) {
				this.jumpPower++;
				this.setPosition(this.getX(), this.getY() - 4f);
			} else {
				this.currentStates = 2;
			}
		} else {
			if (this.jumpPower > 0) {
				this.jumpPower--;
				this.setPosition(this.getX(), this.getY() + 4f);
			}
		}
		if (this.jumpPower == 0) {
			this.isJumping = false;
		} else {
			this.isJumping = true;
		}
	}

	public void ChangeStates(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentStates = (byte) (this.currentStates | settingMaskActions & ~unsettingMaskActions);
		// And what I need to do if I don't want to have operation with int?
	}

}
