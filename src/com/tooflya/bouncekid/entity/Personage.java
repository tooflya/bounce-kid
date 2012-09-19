package com.tooflya.bouncekid.entity;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.helpers.ActionHelper;

/**
 * @author Tooflya.com
 * @since
 */
public class Personage extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================
	public int rx = Options.babyStep * 150;
	public ArrayList<ActionsList> actions = new ArrayList<ActionsList>();

	private static final BitmapTextureAtlas texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

	private final int maxFlyTime = 40;
	public final int runStep = Options.mainStep; // TODO: Make a getter and private.
	public final int flyStep = 2; // TODO: Make a getter and private.
	public final int fallStep = 2; // TODO: Make a getter and private.

	// ===========================================================
	// Fields
	// ===========================================================

	private byte currentStates;

	private int flyTime;

	// ===========================================================
	// Constructors
	// ===========================================================

	// TODO: Need to make a better code.

	public Personage(final TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.currentStates = ActionHelper.Fall;
		AnimateState.setFall(this);

		this.flyTime = this.maxFlyTime;

		Game.loadTextures(texture);
		Game.camera.setBounds(0, Integer.MAX_VALUE, -Integer.MAX_VALUE, Options.cameraHeightOrigin);
		Game.camera.setBoundsEnabled(true);
		Game.camera.setChaseEntity(this);
	}

	public Personage(final float x, final float y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion);

		this.setPosition(x, y);
	}

	public Personage(final float x, final float y) {
		this(x, y, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "hero.png", 0, 0, 2, 3));
	}

	public Personage() {
		this(0, 0);
	}

	// ===========================================================
	// Get and set
	// ===========================================================

	public int getFlyPower() {
		return this.flyTime;
	}

	public void setFlyPower(int value) {
		this.flyTime = value;
	}

	public int getMaxFlyTime() {
		return this.maxFlyTime;
	}

	public float getMaxFlyHeight() {
		return this.maxFlyTime * this.flyStep;
	}

	public float getMaxFlyDistance() {
		return this.maxFlyTime * this.runStep;
	}

	public float getMaxFallDistance() {
		return (float) this.getMaxFlyHeight() / this.fallStep * this.runStep;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Personage(getTextureRegion());
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

		public static void setRun(final Personage personage) {
			isFall = false;
			isFly = false;
			isRun = true;
			personage.animate(new long[] { 80, 80 }, 0, 1, true);
			try {
				personage.actions.add(new ActionsList(personage.currentStates, personage.rx));
			} catch (NullPointerException e) {
			}
		}

		public static void setFall(final Personage personage) {
			isFall = true;
			isFly = false;
			isRun = false;
			personage.animate(new long[] { 80, 80 }, 4, 5, true);
			try {
				personage.actions.add(new ActionsList(personage.currentStates, personage.rx));
			} catch (NullPointerException e) {
			}
		}

		public static void setFly(final Personage personage) {
			isFall = false;
			isFly = true;
			isRun = false;
			personage.animate(new long[] { 80, 80 }, 2, 3, true);
			try {
				personage.actions.add(new ActionsList(personage.currentStates, personage.rx));
			} catch (NullPointerException e) {
			}
		}
	}

	public static class ActionsList {
		public byte currentStates;
		public int apt;

		public ActionsList(final byte currentStates, final int apt) {
			this.currentStates = currentStates;
			this.apt = apt;
		}
	}
}
