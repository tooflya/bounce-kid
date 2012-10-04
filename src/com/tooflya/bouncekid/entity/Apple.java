package com.tooflya.bouncekid.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.ScaleAtModifier;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;

public class Apple extends Entity {

	public static float WIDTH;

	private final ScaleAtModifier scaleModifier = new ScaleAtModifier(0.1f, 1 * Options.cameraRatioFactor, 1.1f, this.getWidthScaled() / 2, this.getHeightScaled() / 2) {
		@Override
		protected void onModifierFinished(final IEntity apple)
		{
			((Apple) apple).setVisible(false);
		}
	};

	private final AlphaModifier alphaModifier = new AlphaModifier(0.1f, 1f, 0.2f);

	public boolean reverse = true;
	public float offsetY = -1.0f, maxOffsetY = 1.0f, minOffsetY = -1.0f;

	public boolean isFollow = false;

	public Apple(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		WIDTH = this.getWidthScaled();

		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		scaleModifier.setRemoveWhenFinished(false);
		alphaModifier.setRemoveWhenFinished(false);

		this.registerEntityModifier(scaleModifier);
		this.registerEntityModifier(alphaModifier);
	}

	public Apple(int x, int y, TiledTextureRegion pTiledTextureRegion) {
		super(x, y, pTiledTextureRegion);
	}

	@Override
	public Entity create() {
		this.reverse = true;
		this.offsetY = -1.0f;
		this.isFollow = false;

		this.setAlpha(1f);
		this.setScaleCenter(0, 0);
		this.setScaleY(Options.cameraRatioFactor);
		this.setScaleX(Options.cameraRatioFactor);

		return super.create();
	}

	public void remove() {
		if (scaleModifier.isFinished()) {
			scaleModifier.reset();
			alphaModifier.reset();
		}
	}

	public void follow() {
		if (!this.isFollow) {
			isFollow = true;
			this.registerEntityModifier(new MoveModifier(0.1f, this.getX(), Game.world.personage.getX(), this.getY(), Game.world.personage.getY()));
		}
	}

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.reverse) {
			this.offsetY += 0.02f;
			if (this.offsetY > this.maxOffsetY) {
				this.reverse = false;
			}
		} else {
			this.offsetY -= 0.02f;
			if (this.offsetY < this.minOffsetY) {
				this.reverse = true;
			}
		}

		this.setPosition(this.getX() - Game.world.personage.runStep * (Options.fps / Game.fps), this.getY() + offsetY);
		if (this.getX() + this.getWidthScaled() < 0) {
			this.destroy();
		}
	}

	@Override
	public Entity deepCopy() {
		return new Apple(getTextureRegion());
	}
}
