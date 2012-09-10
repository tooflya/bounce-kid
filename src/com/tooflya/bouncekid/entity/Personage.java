package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.GameActivity;
import com.tooflya.bouncekid.Options;
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

	public int jumpPower;
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

		this.reset();

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
	public void update() {

		runningProceed();

		if (this.IsState(ActionHelper.Jump)) {
			jumpProceed();
		}
		if (this.IsState(ActionHelper.Fall)) {
			fallProceed();
		}

		dieProceed();

		System.out.println("X: " + this.getX() + " /  Y: " + this.getY());
	}

	@Override
	public Entity deepCopy() {
		return null;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int a = 0;

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

		this.setPosition(this.getX() + 5, this.getY());
	}

	private void jumpProceed() {
		if (this.jumpPower > 0) {
			this.jumpPower--;
			this.setPosition(this.getX(), this.getY() - this.jumpStep);
		} else {
			this.jumpPower = this.maxJumpPower;
			this.ChangeStates(ActionHelper.Fall, ActionHelper.Jump);
		}
	}

	private void fallProceed() {
		this.jumpPower = 40;

		this.setPosition(this.getX(), this.getY() + this.jumpStep);
	}

	private void dieProceed() {
		if (this.getY() > Options.cameraHeight) {
			this.setPosition(this.getX(), 0);
			this.ChangeStates(ActionHelper.Fall, ActionHelper.Jump);
			this.ChangeStates(ActionHelper.Fall, ActionHelper.Running);
		}
	}

	public boolean IsState(byte state) {
		return (this.currentStates & state) == state;
	}

	public void ChangeStates(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentStates = (byte) ((this.currentStates | settingMaskActions) & ~unsettingMaskActions); // And what I need to do if I don't want to have operation with int?
	}
}
