package com.tooflya.bouncekid.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;

import com.tooflya.bouncekid.Options;

/**
 * @author Tooflya.com
 * @since
 */
public class EntitySimple extends org.anddev.andengine.entity.sprite.Sprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntitySimple(int x, int y, TextureRegion textureRegion) {
		super(x, y, textureRegion);

		this.setScaleCenter(0, 0);
		this.setScaleY(Options.cameraRatioFactor);
		this.setScaleX(Options.cameraRatioFactor);

		this.setPosition(this.getX() - (this.getWidthScaled() - Options.cameraWidth) / 2, this.getY()); // TODO: YYYY

		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	public EntitySimple(TextureRegion textureRegion) {
		this(0, 0, textureRegion);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.BaseSprite#onInitDraw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	protected void onInitDraw(final GL10 GL) {
		super.onInitDraw(GL);

		GLHelper.enableDither(GL);
		GLHelper.enableCulling(GL);
		GLHelper.enableTextures(GL);
		GLHelper.enableTexCoordArray(GL);
	}
}
