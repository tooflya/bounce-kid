package com.tooflya.bouncekid.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;

public class Sprite extends org.anddev.andengine.entity.sprite.Sprite {

	public Sprite(int x, int y, TextureRegion pTextureRegion) {
		super(x, y, pTextureRegion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.BaseSprite#onInitDraw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);

		GLHelper.enableDither(pGL);
	}
}
