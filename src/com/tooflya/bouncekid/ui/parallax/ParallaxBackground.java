package com.tooflya.bouncekid.ui.parallax;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.Shape;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.managers.EntityManager;

/**
 * @author Tooflya.com
 * @since
 */
public class ParallaxBackground extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ParallaxEntity> parallaxEntities = new ArrayList<ParallaxEntity>();
	private int parallaxEntityCount;

	protected float parallaxValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxValue(final float parallaxValue) {
		this.parallaxValue = parallaxValue;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.Entity#onManagedDraw(javax.microedition.khronos.opengles.GL10, org.anddev.andengine.engine.camera.Camera)
	 */
	@Override
	public void onManagedDraw(final GL10 GL, final Camera camera) {
		super.onManagedDraw(GL, camera);

		final float parallaxValue = this.parallaxValue;

		for (int i = 0; i < this.parallaxEntityCount; i++) {
			this.parallaxEntities.get(i).onDraw(GL, parallaxValue, camera);
		}
	}

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		for (int i = 0; i < this.parallaxEntityCount; i++) {
			this.parallaxEntities.get(i).onManagedUpdate(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void attachParallaxEntity(final ParallaxEntity parallaxEntity) {
		this.parallaxEntities.add(parallaxEntity);
		this.parallaxEntityCount++;
	}

	public boolean detachParallaxEntity(final ParallaxEntity parallaxEntity) {
		this.parallaxEntityCount--;

		final boolean success = this.parallaxEntities.remove(parallaxEntity);

		if (!success) {
			this.parallaxEntityCount++;
		}

		return success;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ParallaxEntity {

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		protected final Shape shape;

		protected float xParallaxFactor;
		protected float yParallaxFactor;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntity(final float xParallaxFactor, final Shape shape) {
			this.shape = shape;

			this.xParallaxFactor = xParallaxFactor;
			this.yParallaxFactor = 0;
		}

		public ParallaxEntity(final Shape shape) {
			this(0, 0, shape);
		}

		public ParallaxEntity(final float xParallaxFactor, final float yParallaxFactor, final Shape shape) {
			this(xParallaxFactor, shape);

			this.yParallaxFactor = yParallaxFactor;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onDraw(final GL10 GL, final float parallaxValue, final Camera camera) {
			GL.glPushMatrix();
			{
				final float shapeWidthScaled = FloatMath.floor(this.shape.getWidthScaled());
				float baseOffsetX = (parallaxValue * this.xParallaxFactor) % shapeWidthScaled;
				float baseOffsetY = 0;

				if (this.yParallaxFactor != 0) {
					baseOffsetY = (camera.getCenterY() - Options.cameraHeight / 2) - (Game.camera.getCenterY() - Options.cameraCenterOriginY) * this.yParallaxFactor + (Options.cameraHeight - Options.cameraHeightOrigin);
				} else {
					baseOffsetY = (camera.getCenterY() - camera.getHeight() / 2) + (Options.cameraHeight - Options.cameraHeightOrigin);
				}

				while (baseOffsetX > 0) {
					baseOffsetX -= shapeWidthScaled;
				}

				GL.glTranslatef(baseOffsetX, baseOffsetY, 0);

				float currentMaxX = baseOffsetX;

				while (currentMaxX * Options.cameraRatioFactor < Options.cameraWidth + this.shape.getWidthScaled()) {
					this.shape.onDraw(GL, camera);
					GL.glTranslatef(shapeWidthScaled, 0, 0);
					currentMaxX += shapeWidthScaled;
				}
			}
			GL.glPopMatrix();
		}

		public void onManagedUpdate(final float pSecondsEllapsed) {

		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class ParallaxEntityTree extends ParallaxEntity {

		// ===========================================================
		// Constants
		// ===========================================================

		private final static int mShapesCount = 150;

		// ===========================================================
		// Fields
		// ===========================================================

		private EntityManager trees, bush;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntityTree(final float xParallaxFactor, final Shape shape) {
			super(xParallaxFactor, shape);
		}

		public ParallaxEntityTree(final float xParallaxFactor, final float yParallaxFactor, final Shape shape, final Entity trees, final Entity bush) {
			super(xParallaxFactor, yParallaxFactor, shape);

			this.trees = new EntityManager(mShapesCount, trees);
			this.bush = new EntityManager(mShapesCount, bush);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		private final int minTreeDistance = 200, minBushDistance = 200;
		private int treeDistance = 0, bushDistance = 0;

		@Override
		public void onManagedUpdate(final float pSecondsElapsed) {
			final int chance = Game.random.nextInt(100);

			treeDistance++;
			bushDistance++;

			if (chance > 50 && chance < 55 && treeDistance > this.minTreeDistance) {
				treeDistance = 0;
				Entity shape = this.trees.create();
				if (shape != null) {
					shape.setCurrentTileIndex(Game.random.nextInt(3));
					shape.setPosition(Options.cameraWidth + shape.getWidthScaled(), 0);
				}
			}

			if (chance > 50 && chance < 55 && bushDistance > this.minBushDistance) {
				bushDistance = 0;
				Entity shape = this.bush.create();
				if (shape != null) {
					shape.setCurrentTileIndex(Game.random.nextInt(3));
					shape.setPosition(Options.cameraWidth + shape.getWidthScaled(), 170);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tooflya.bouncekid.ui.parallax.ParallaxBackground.ParallaxEntity#onDraw(javax.microedition.khronos.opengles.GL10, float, org.anddev.andengine.engine.camera.Camera)
		 */
		@Override
		public void onDraw(final GL10 GL, final float parallaxValue, final Camera camera) {
			for (int i = 0; i < this.trees.getCount(); i++) {
				final Entity shape = this.trees.getByIndex(i);

				if (shape.getX() + shape.getWidthScaled() < 0) {
					shape.destroy();
					i--;
				} else {
					float baseOffsetX = this.xParallaxFactor / 2f;
					float baseOffsetY = 0;

					if (this.yParallaxFactor != 0) {
						baseOffsetY = (camera.getCenterY() - Options.cameraHeight / 2) - (Game.camera.getCenterY() - Options.cameraCenterOriginY) * this.yParallaxFactor + (Options.cameraHeight - Options.cameraHeightOrigin);
					} else {
						baseOffsetY = (camera.getCenterY() - camera.getHeight() / 2) + (Options.cameraHeight - Options.cameraHeightOrigin);
					}

					GL.glPushMatrix();
					{
						shape.setPosition(shape.getX() + baseOffsetX, shape.getY());
						GL.glTranslatef(baseOffsetX, baseOffsetY, 0);
						shape.onDraw(GL, camera);
					}
					GL.glPopMatrix();

				}
			}

			for (int i = 0; i < this.bush.getCount(); i++) {
				final Entity shape = this.bush.getByIndex(i);

				if (shape.getX() + shape.getWidthScaled() < 0) {
					shape.destroy();
				} else {
					float baseOffsetX = this.xParallaxFactor / 2f;
					float baseOffsetY = 0;

					if (this.yParallaxFactor != 0) {
						baseOffsetY = (camera.getCenterY() - Options.cameraHeight / 2) - (Game.camera.getCenterY() - Options.cameraCenterOriginY) * this.yParallaxFactor + (Options.cameraHeight - Options.cameraHeightOrigin);
					} else {
						baseOffsetY = (camera.getCenterY() - camera.getHeight() / 2) + (Options.cameraHeight - Options.cameraHeightOrigin);
					}

					GL.glPushMatrix();
					{
						shape.setPosition(shape.getX() + baseOffsetX, shape.getY());
						GL.glTranslatef(baseOffsetX, baseOffsetY, 0);
						shape.onDraw(GL, camera);
					}
					GL.glPopMatrix();

				}
			}

			super.onDraw(GL, parallaxValue, camera);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}