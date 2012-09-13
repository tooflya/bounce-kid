package com.tooflya.bouncekid.ui.parallax;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Tooflya.com
 * @since
 */
public class ParallaxBackground extends Entity {

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
		final ArrayList<ParallaxEntity> parallaxEntities = this.parallaxEntities;

		for (int i = 0; i < this.parallaxEntityCount; i++) {
			parallaxEntities.get(i).onDraw(GL, parallaxValue, camera);
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

		private float xParallaxFactor;
		private float yParallaxFactor;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntity(final float xParallaxFactor, final Shape shape) {
			this.shape = shape;

			this.xParallaxFactor = xParallaxFactor;
			this.yParallaxFactor = 0;
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
				final float cameraWidth = camera.getWidth();
				final float shapeWidthScaled = this.shape.getWidth();
				float baseOffsetX = (parallaxValue * this.xParallaxFactor) % shapeWidthScaled;
				float baseOffsetY = 0;

				if (this.yParallaxFactor > 0) {
					baseOffsetY = -(camera.getCenterY() - camera.getHeight() / 2) * this.yParallaxFactor / 10;
				}

				while (baseOffsetX > 0) {
					baseOffsetX -= shapeWidthScaled;
				}

				GL.glTranslatef(baseOffsetX, baseOffsetY, 0);

				float currentMaxX = baseOffsetX;

				do {
					this.shape.onDraw(GL, camera);
					GL.glTranslatef(shapeWidthScaled, 0, 0);
					currentMaxX += shapeWidthScaled;
				} while (currentMaxX < cameraWidth);
			}
			GL.glPopMatrix();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}