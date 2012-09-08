package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

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
	private byte states;

	public float jumpPower;
	private float maxJumpPower;
	public boolean isJumping;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Personage(final TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.states = 2;

		this.jumpPower = 0;
		this.maxJumpPower = 40;
		this.isJumping = false;

		this.setFlippedHorizontal(true);
		this.setScale(0.5f);

		this.setVisible(true);
		this.setIgnoreUpdate(false);
	}

	public Personage(final int x, final int y, final TiledTextureRegion pTiledTextureRegion) {
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

		/**
		 * 
		 * If character has running status pvoide animation and if not stop
		 * animation if it's running
		 * 
		 */
		runningProceed();

		/**
		 * 
		 *
		 * 
		 */
		jumpProceed();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void runningProceed() {
		if ((this.states & 2) > 0) {
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
		if ((this.states & 4) > 0) {
			if (this.jumpPower < this.maxJumpPower) {
				this.jumpPower++;
				this.setPosition(this.getX(), this.getY() - 4f);
			} else {
				this.states = 2;
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

	public void setState(final byte on, final byte off) {
		this.states = on;
	}

}
