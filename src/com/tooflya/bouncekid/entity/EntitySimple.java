package com.tooflya.bouncekid.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;

import com.tooflya.bouncekid.Options;

/**
 * @author Tooflya.com
 * @since
 */
public class EntitySimple extends org.anddev.andengine.entity.sprite.Sprite implements IOnAreaTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntitySimple(int x, int y, TextureRegion textureRegion, final boolean changePosition) {
		super(x, y, textureRegion);

		this.setScaleCenter(0, 0);
		this.setScaleY(Options.CAMERA_RATIO_FACTOR);
		this.setScaleX(Options.CAMERA_RATIO_FACTOR);

		if (changePosition) {
			this.setPosition(this.getX() - (this.getWidthScaled() - Options.cameraWidth) / 2, this.getY());
		}
	}

	public EntitySimple(int x, int y, TextureRegion textureRegion) {
		this(x, y, textureRegion, true);
	}

	public EntitySimple(TextureRegion textureRegion, final boolean changePosition) {
		this(0, 0, textureRegion, changePosition);
	}

	public EntitySimple(TextureRegion textureRegion) {
		this(0, 0, textureRegion, true);
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

		GLHelper.enableTextures(GL);
		GLHelper.enableTexCoordArray(GL);
		GLHelper.enableDither(GL);
	}

	@Override
	public boolean onAreaTouched(TouchEvent arg0, ITouchArea arg1, float arg2, float arg3) {
		return false;
	}
}
