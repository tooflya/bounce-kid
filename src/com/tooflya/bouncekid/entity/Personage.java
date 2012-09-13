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

	private static final BitmapTextureAtlas texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
	private final int maxFlyPower = 50;
	private final int runStep = 4;
	private final int flyStep = 4;
	private final int fallStep = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private byte currentStates;

	private int flyPower;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Personage(final TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.currentStates = ActionHelper.Run;

		this.flyPower = this.maxFlyPower;

		Game.loadTextures(texture);
		Game.camera.setBounds(0, Integer.MAX_VALUE, -Integer.MAX_VALUE, Options.cameraHeight);
		Game.camera.setBoundsEnabled(true);
		Game.camera.setChaseEntity(this);
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

		this.setPosition(this.getX() + this.runStep, this.getY());

		if (this.IsState(ActionHelper.Run) && !this.isAnimationRunning()) {
			this.animate(new long[] { 80, 80, 80, 80, 80 }, 0, 4, true);
		}

		if (!this.IsState(ActionHelper.Run) && this.isAnimationRunning()) {
			this.stopAnimation(2);
		}

		if (this.IsState(ActionHelper.Run) && this.IsState(ActionHelper.WantToFly)) {
			this.ChangeStates(ActionHelper.Fly, ActionHelper.Run);
		}

		if (this.IsState(ActionHelper.Fly)) {
			if (this.flyPower > 0 &&  this.IsState(ActionHelper.WantToFly)) {
				this.flyPower--;
				this.setPosition(this.getX(), this.getY() - this.flyStep);
			} else {
				this.flyPower = this.maxFlyPower;
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
		return new Personage(getTextureRegion().deepCopy());
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
}
