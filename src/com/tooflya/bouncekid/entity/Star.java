package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Star extends Entity {

	public Star(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.animate(70);
		this.setScaleCenter(0, 0);
		this.setScale(1.2f);
	}

	public Star(int x, int y, TiledTextureRegion pTiledTextureRegion) {
		super(x, y, pTiledTextureRegion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

	}

	@Override
	public Entity deepCopy() {
		return new Star(getTextureRegion());
	}
}
