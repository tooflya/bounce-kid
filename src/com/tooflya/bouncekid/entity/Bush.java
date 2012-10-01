package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Bush extends Entity {

	public Bush(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion, false);

		// this.setScale(this.getScaleX() * 1.5f, this.getScaleY() * 1.5f);
	}

	@Override
	public Entity deepCopy() {
		return new Bush(getTextureRegion());
	}
}
