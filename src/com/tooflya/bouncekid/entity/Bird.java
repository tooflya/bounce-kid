package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;

public class Bird extends Entity {

	private static final BitmapTextureAtlas texture = new BitmapTextureAtlas(256, 128, TextureOptions.NEAREST_PREMULTIPLYALPHA);

	public Bird(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion, false);

		Game.loadTextures(texture);

		this.animate(150);
	}

	public Bird(final float x, final float y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion);
	}

	public Bird(final float x, final float y) {
		this(x, y, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "bird.png", 0, 0, 4, 1));
	}

	@Override
	public void update() {
		System.out.println(this.getX());

		if (this.getTextureRegion().isFlippedHorizontal()) {
			this.setPosition(this.getX() - 1, this.getY());
			if (this.getX() + this.getWidthScaled() < 0) {
				this.getTextureRegion().setFlippedHorizontal(false);
				this.setPosition(this.getX(), Game.random.nextInt(300));
			}

		} else {
			this.setPosition(this.getX() + 1, this.getY());
			if (this.getX() > Options.cameraWidth) {
				this.getTextureRegion().setFlippedHorizontal(true);
				this.setPosition(this.getX(), Game.random.nextInt(300));
			}
		}
	}

	@Override
	public Entity deepCopy() {
		return new Bird(getTextureRegion());
	}

}
