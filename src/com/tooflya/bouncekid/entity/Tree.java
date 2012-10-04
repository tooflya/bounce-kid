package com.tooflya.bouncekid.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;

/**
 * @author Tooflya.com
 * @since
 */
public class Tree extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public float drawCount = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTiledTextureRegion
	 */
	public Tree(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion, false);

		this.setScale(this.getScaleX() * 1.5f, this.getScaleY() * 1.5f);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#create()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Tree(getTextureRegion());
	}
}
