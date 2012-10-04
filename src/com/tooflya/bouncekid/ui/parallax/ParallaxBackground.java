package com.tooflya.bouncekid.ui.parallax;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.Shape;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Tree;
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

	protected float parallaxChangePerSecond;

	protected final ArrayList<ParallaxEntity> parallaxEntities = new ArrayList<ParallaxEntity>();
	protected int parallaxEntityCount;

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
			this.parallaxEntities.get(i).onDraw(GL, parallaxValue, this.parallaxChangePerSecond, camera);
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

	public static class ParallaxEntity extends org.anddev.andengine.entity.Entity {

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

		public void onDraw(final GL10 GL, final float parallaxValue, final float parallaxChangePerSecond, final Camera camera) {
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

				while (currentMaxX * Options.CAMERA_RATIO_FACTOR < Options.cameraWidth + this.shape.getWidthScaled()) {
					this.shape.onDraw(GL, camera);
					GL.glTranslatef(shapeWidthScaled, 0, 0);
					currentMaxX += shapeWidthScaled;
				}
			}
			GL.glPopMatrix();
		}

		@Override
		public void onManagedUpdate(final float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);
		}

		public void accelerate(final float accelerateFactor) {
			this.xParallaxFactor += accelerateFactor;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class ParallaxEntityTree extends ParallaxEntity {

		// ===========================================================
		// Constants
		// ===========================================================

		private final static int mShapesCount = 5;

		// ===========================================================
		// Fields
		// ===========================================================

		private int minTreeDistance = 1000, minTreeDistanceOrigin = 4, minBushDistance = 200;
		private int treeDistance = minTreeDistance - 1, bushDistance = 0;

		private EntityManager trees, bush;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntityTree(final float xParallaxFactor, final Shape shape) {
			super(xParallaxFactor, shape);
		}

		public ParallaxEntityTree(final float xParallaxFactor, final float yParallaxFactor, final Shape shape, final Entity trees) {
			super(xParallaxFactor, yParallaxFactor, shape);

			this.trees = new EntityManager(mShapesCount, trees);

			this.generateTree();
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

		private void generateTree() {
			Entity shape = this.trees.create();
			if (shape != null) {
				if (!shape.hasParent()) {
					this.attachChild(shape);
				}
				shape.setCurrentTileIndex(Game.random.nextInt(3));
				shape.setPosition(Options.cameraWidth, 0);
			}
		}

		public void onDraw(final GL10 GL, final float parallaxValue, final float parallaxChangePerSecond, final Camera camera) {
			for (int i = 0; i < this.trees.getCount(); i++) {
				final Tree shape = ((Tree) this.trees.getByIndex(i));

				shape.drawCount += parallaxChangePerSecond / Options.fps;

				final float shapeWidthScaled = Options.CORX + shape.getWidthScaled();
				final float shapeWidthScaledFalse = Options.cameraWidth + shape.getWidthScaled();
				float baseOffsetX = (shape.drawCount * this.xParallaxFactor) % shapeWidthScaled;
				float baseOffsetY = 0;

				if (baseOffsetX <= -shapeWidthScaledFalse) {
					this.generateTree();
					shape.destroy();
					i--;
				} else {
					if (this.yParallaxFactor != 0) {
						baseOffsetY = (camera.getCenterY() - Options.cameraHeight / 2) - (Game.camera.getCenterY() - Options.cameraCenterOriginY) * this.yParallaxFactor + (Options.cameraHeight - Options.cameraHeightOrigin);
					} else {
						baseOffsetY = (camera.getCenterY() - camera.getHeight() / 2) + (Options.cameraHeight - Options.cameraHeightOrigin);
					}

					GL.glPushMatrix();
					{
						GL.glTranslatef(baseOffsetX, baseOffsetY, 0);
						shape.onDraw(GL, camera);
					}
					GL.glPopMatrix();
				}
			}

			/*
			 * for (int i = 0; i < this.bush.getCount(); i++) { final Entity shape = this.bush.getByIndex(i);
			 * 
			 * if (shape.getX() + shape.getWidthScaled() < 0) { shape.destroy(); i--; } else { float baseOffsetX = this.xParallaxFactor * 2; float baseOffsetY = 0;
			 * 
			 * if (this.yParallaxFactor != 0) { baseOffsetY = (camera.getCenterY() - Options.cameraHeight / 2) - (Game.camera.getCenterY() - Options.cameraCenterOriginY) * this.yParallaxFactor + (Options.cameraHeight - Options.cameraHeightOrigin); } else { baseOffsetY = (camera.getCenterY() - camera.getHeight() / 2) + (Options.cameraHeight - Options.cameraHeightOrigin); }
			 * 
			 * GL.glPushMatrix(); { shape.setPosition(shape.getX() + baseOffsetX, shape.getY()); GL.glTranslatef(0, baseOffsetY, 0); shape.onDraw(GL, camera); } GL.glPopMatrix();
			 * 
			 * } }
			 */

			super.onDraw(GL, parallaxValue, parallaxChangePerSecond, camera);
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}