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

	private int jumpPower;
	private int maxJumpPower;
	private float jumpStep;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Personage(final TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.currentStates = ActionHelper.Running;

		this.jumpPower = this.maxJumpPower = 40;
		this.jumpStep = 4f;

		this.setFlippedHorizontal(true);
		this.setScaleCenter(0, 0);
		this.setScale(0.5f);

		this.setVisible(true);
		this.setIgnoreUpdate(false);
	}

	public Personage(final float x, final float y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion);

		this.setPosition(x, y);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.IsState(ActionHelper.Running)) {
			runningProceed();
		}
		if (this.IsState(ActionHelper.Jump)) {
			jumpProceed();
		}
		if (this.IsState(ActionHelper.Fall)) {
			fallProceed();
		}
	}

	@Override
	public Entity deepCopy() {
		return null;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void runningProceed() {
		if (!this.IsState(ActionHelper.Jump) && !this.IsState(ActionHelper.Fall)) {
			if (!this.isAnimationRunning()) {
				this.animate(new long[] { 80, 80, 80, 80, 80, 80, 80, 80 }, 0, 7, true);
			}
		} else {
			if (this.isAnimationRunning()) {
				this.stopAnimation(2);
			}
		}
	}

	private void jumpProceed() {
		if (this.jumpPower > 0) {
			this.jumpPower--;
			this.setPosition(this.getX(), this.getY() - this.jumpStep);
		} else {
			this.jumpPower = this.maxJumpPower;
			this.currentStates = ActionHelper.Fall;
		}
	}

	private void fallProceed() {
		this.setPosition(this.getX(), this.getY() + this.jumpStep);
	}

	public boolean IsState(byte state) {
		return (this.state & state) == state;
	}

	public void ChangeStates(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentStates = (byte) (this.currentStates | settingMaskActions & ~unsettingMaskActions); // And what I need to do if I don't want to have operation with int?
	}
}
