package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;

public class Tree extends Entity {

	public Tree(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion, false);

		this.setScale(this.getScaleX() * 1.5f, this.getScaleY() * 1.5f);
	}

	@Override
	public Entity create() {
		switch (Game.random.nextInt(2)) {
		case 0:
			this.getTextureRegion().setFlippedHorizontal(true);
			break;
		case 1:
			this.getTextureRegion().setFlippedHorizontal(false);
			break;
		}

		return super.create();
	}

	@Override
	public Entity deepCopy() {
		return new Tree(getTextureRegion());
	}
}
