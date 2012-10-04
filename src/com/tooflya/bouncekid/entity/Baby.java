package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.helpers.ActionHelper;

public class Baby extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================
	public int rx = 0;

	private final int maxFlyTime = 40;
	public final float runStep = Options.mainStep; // TODO: Make a getter and private.
	public final int flyStep = 2; // TODO: Make a getter and private.
	public final int fallStep = 2; // TODO: Make a getter and private.

	// ===========================================================
	// Fields
	// ===========================================================

	public  byte currentStates;

	private int flyTime;

	// ===========================================================
	// Constructors
	// ===========================================================

	// TODO: Need to make a better code.

	public Baby(final TiledTextureRegion pTiledTextureRegion) {
		super(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(null, Game.context, "hero.png", 100, 100, 2, 3));

		this.currentStates = ActionHelper.Fall;
		AnimateState.setFall(this);

		this.flyTime = this.maxFlyTime;
	}

	public Baby(final float x, final float y, final TiledTextureRegion pTiledTextureRegion) {
		this(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(null, Game.context, "hero.png", 100, 100, 2, 3));

		this.setPosition(x, y);
	}

	public Baby(final float x, final float y) {
		this(x, y, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(null, Game.context, "hero.png", 100, 100, 2, 3));
	}

	public Baby() {
		this(0, 0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean IsState(byte state) {
		return (this.currentStates & state) == state;
	}

	public void ChangeStates(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentStates = (byte) ((this.currentStates | settingMaskActions) & ~unsettingMaskActions); // And what I need to do if I don't want to have operation with int?
	}

	private static class AnimateState {
		public static boolean isRun = false;
		public static boolean isFall = false;
		public static boolean isFly = false;

		public static void setRun(final Baby personage) {
			isFall = false;
			isFly = false;
			isRun = true;
			personage.animate(new long[] { 80, 80 }, 0, 1, true);
		}

		public static void setFall(final Baby personage) {
			isFall = true;
			isFly = false;
			isRun = false;
			personage.animate(new long[] { 80, 80 }, 4, 5, true);
		}

		public static void setFly(final Baby personage) {
			isFall = false;
			isFly = true;
			isRun = false;
			personage.animate(new long[] { 80, 80 }, 2, 3, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	public void update() {
		super.update();
	this.rx += Options.mainStep;
		if (this.IsState(ActionHelper.Run) && !AnimateState.isRun) {
			AnimateState.setRun(this);
		}

		if (this.IsState(ActionHelper.Fly) && !AnimateState.isFly) {
			AnimateState.setFly(this);
		}

		if (this.IsState(ActionHelper.Fall) && !AnimateState.isFall) {
			AnimateState.setFall(this);
		}

		if (this.IsState(ActionHelper.Run) && this.IsState(ActionHelper.WantToFly)) {
			this.ChangeStates(ActionHelper.Fly, ActionHelper.Run);
		}

		if (this.IsState(ActionHelper.Fly)) {
			if (this.flyTime > 0 && this.IsState(ActionHelper.WantToFly)) {
				this.flyTime--;
				this.setPosition(this.getX(), this.getY() - this.flyStep);
			} else {
				this.flyTime = Math.max(this.maxFlyTime, this.flyTime);
				this.ChangeStates(ActionHelper.Fall, ActionHelper.Fly);
			}
		}

		if (this.IsState(ActionHelper.Fall)) {
			this.setPosition(this.getX(), this.getY() + this.fallStep);
		}
	}

	@Override
	public Entity deepCopy() {
		return new Baby(getTextureRegion());
	}

}
