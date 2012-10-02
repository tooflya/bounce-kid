package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;

public class Tree extends Entity {

	public float drawCount = 0;

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

		this.drawCount = 0;

		return super.create();
	}

	@Override
	public Entity deepCopy() {
		return new Tree(getTextureRegion());
	}
}
