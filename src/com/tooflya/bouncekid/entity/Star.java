package com.tooflya.bouncekid.entity;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.MoveModifier;
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

	public void mo() {

		final MoveModifier right = new MoveModifier(100, this.getX(), this.getX() - 60, this.getY(), this.getY()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);

			}
		};

		MoveModifier left = new MoveModifier(100, this.getX(), this.getX() + 60, this.getY(), this.getY()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);

				registerEntityModifier(right);
			}
		};

		this.registerEntityModifier(left);

	}
}
