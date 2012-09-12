package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Block extends Entity {

	public Block(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);
	}

	public Block(int x, int y, TiledTextureRegion pTiledTextureRegion) {
		super(x, y, pTiledTextureRegion);
	}

	@Override
	public Entity deepCopy() {
		return new Block(getTextureRegion());
	}
}
