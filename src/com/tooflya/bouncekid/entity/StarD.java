package com.tooflya.bouncekid.entity;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class StarD extends Entity {

	public StarD(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);
	}

	@Override
	public Entity create() {

		this.animate(70, false, new IAnimationListener() {
			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
				destroy();
			}
		});

		return super.create();
	}

	@Override
	public Entity deepCopy() {
		return new StarD(getTextureRegion());
	}
}
