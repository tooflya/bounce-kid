package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;

public class Block extends Entity {

	public Block(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);
	}

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.setPosition(this.getX() - Game.world.personage.runStep * (Options.fps / Game.fps), this.getY());
		if (this.getX() + this.getWidthScaled() < 0) {
			this.destroy();
		}
	}

	@Override
	public Entity deepCopy() {
		return new Block(getTextureRegion());
	}
}
