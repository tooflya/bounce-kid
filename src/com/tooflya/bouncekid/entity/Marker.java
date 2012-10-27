package com.tooflya.bouncekid.entity;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.screens.Screen;

/**
 * @author Tooflya.com
 * @since
 */
public class Marker extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final float PADDING_BOTTOM = 50 * Options.CAMERA_RATIO_FACTOR;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private MarkerText text = new MarkerText(0, 0, Game.font2, "xxxxx");

	public Marker(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Virtual methods
	// ===========================================================

	public void setFlag(final Block block, final int meters) {
		this.mX = block.getX() + Game.random.nextInt((int) block.getWidthScaled());
		this.mY = block.getY() - this.getHeightScaled() - PADDING_BOTTOM;

		this.text.setText(meters + " m");
		this.text.setPosition(this.mX, this.mY - this.text.getHeightScaled() / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.mX + this.getWidthScaled() > 0) {
			this.mX = this.mX - Game.world.personage.runStep * (Options.fps / Game.fps);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Marker(getTextureRegion());
	}

	/**
	 * @author Tooflya.com
	 * @since
	 */
	public static class MarkerText extends ChangeableText {

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public MarkerText(float pX, float pY, Font pFont, String pText) {
			super(pX, pY, pFont, pText);

			Game.screens.get(Screen.LEVEL).attachChild(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.Entity#onManagedUpdate(float)
		 */
		@Override
		public void onManagedUpdate(final float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);

			if (this.getX() + this.getWidthScaled() > 0) {
				this.setPosition(this.getX() - Game.world.personage.runStep * (Options.fps / Game.fps), this.getY());
			}
		}
	}
}
