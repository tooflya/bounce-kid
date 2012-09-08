package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Options;

public class Box extends Entity {

	public Box(int x, int y, TiledTextureRegion pTiledTextureRegion) {
		super(x, y, pTiledTextureRegion);

		this.setVisible(true);
		this.setIgnoreUpdate(false);
		
		this.speed = 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate
	 * (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.setPosition(this.getX() - this.speed, this.getY());

		if (this.getX() <-110) {
			this.setPosition(Options.cameraWidth +264, this.getY());
		}
	}

}
