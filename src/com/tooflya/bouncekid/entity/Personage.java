package com.tooflya.bouncekid.entity;

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

	private static BitmapTextureAtlas texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

	// ===========================================================
	// Fields
	// ===========================================================

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

		Game.loadTextures(texture);
		Game.camera.setBounds(0, Integer.MAX_VALUE, -Integer.MAX_VALUE, Options.cameraHeight);
		Game.camera.setBoundsEnabled(true);
	}

	public Personage(final float x, final float y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion);

		this.setPosition(x, y);
	}

	public Personage(final float x, final float y) {
		this(x, y, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "sprite_running.png", 0, 0, 6, 3));
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

		runningProceed();

		if (this.IsState(ActionHelper.Jump)) {
			jumpProceed();
		}
		if (this.IsState(ActionHelper.Fall)) {
			fallProceed();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Personage(getTextureRegion().deepCopy());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void runningProceed() {
		if (!this.IsState(ActionHelper.Jump) && !this.IsState(ActionHelper.Fall)) {
			if (!this.isAnimationRunning()) {
				this.animate(new long[] { 80, 80, 80, 80, 80, 80, 80, 80, 80, 80 }, 0, 9, true);
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

	public boolean IsState(byte state) {
		return (this.currentStates & state) == state;
	}

	public void ChangeStates(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentStates = (byte) ((this.currentStates | settingMaskActions) & ~unsettingMaskActions); // And what I need to do if I don't want to have operation with int?
	}
}
