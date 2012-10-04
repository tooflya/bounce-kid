package com.tooflya.bouncekid.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;

/**
 * @author Tooflya.com
 * @since
 */
public class Berry extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private final static float TIME_TO_FOLLOW = 0.2f;
	private final static float TIME_TO_SCALE = 0.2f;
	private final static float TIME_TO_ALPHA = 0.2f;
	private final static float TIME_TO_ANIMATION = 0.02f;

	private final static float SCALE_FACTOR = -0.2f;
	private final static float ALPHA_FACTOR = 0.9f;

	// ===========================================================
	// Fields
	// ===========================================================

	private final ScaleModifier mScaleModifier = new ScaleModifier(
			TIME_TO_SCALE,
			1f * Options.CAMERA_RATIO_FACTOR,
			1f * Options.CAMERA_RATIO_FACTOR + SCALE_FACTOR * Options.CAMERA_RATIO_FACTOR) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.util.modifier.BaseModifier#onModifierFinished(java.lang.Object)
		 */
		@Override
		protected void onModifierFinished(final IEntity pEntity) {
			pEntity.setVisible(false);
		}
	};

	private final AlphaModifier mAlphaModifier = new AlphaModifier(TIME_TO_ALPHA, 1f, ALPHA_FACTOR);

	public boolean mIsAnimationReverse;
	public boolean mIsAlreadyFollow;

	public final float mMaxOffsetY = 1.0f, mMinOffsetY = -1.0f;
	public float mOffsetY;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTiledTextureRegion
	 */
	public Berry(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		mScaleModifier.setRemoveWhenFinished(false);
		mAlphaModifier.setRemoveWhenFinished(false);

		this.registerEntityModifier(mScaleModifier);
		this.registerEntityModifier(mAlphaModifier);
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pTiledTextureRegion
	 */
	public Berry(int pX, int pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * 
	 */
	public void remove() {
		if (mScaleModifier.isFinished()) {
			mScaleModifier.reset();
			mAlphaModifier.reset();
		}
	}

	/**
	 * 
	 */
	public void follow() {
		if (!this.mIsAlreadyFollow) {
			this.mIsAlreadyFollow = true;
			this.registerEntityModifier(new MoveModifier(TIME_TO_FOLLOW, this.mX, Game.world.personage.getX(), this.mY, Game.world.personage.getY()));
		}
	}

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
		this.mIsAnimationReverse = true;
		this.mIsAlreadyFollow = false;
		this.mOffsetY = -1.0f;

		this.setAlpha(1f);
		this.setScaleCenter(0, 0);
		this.setScaleY(Options.CAMERA_RATIO_FACTOR);
		this.setScaleX(Options.CAMERA_RATIO_FACTOR);

		return super.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.mIsAnimationReverse) {
			this.mOffsetY += TIME_TO_ANIMATION;
			if (this.mOffsetY > this.mMaxOffsetY) {
				this.mIsAnimationReverse = false;
			}
		} else {
			this.mOffsetY -= TIME_TO_ANIMATION;
			if (this.mOffsetY < this.mMinOffsetY) {
				this.mIsAnimationReverse = true;
			}
		}

		this.setPosition(this.mX - Game.world.personage.runStep * (Options.fps / Game.fps), this.mY + mOffsetY);
		if (this.mX + this.getWidthScaled() < 0) {
			this.destroy();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Berry(getTextureRegion());
	}
}