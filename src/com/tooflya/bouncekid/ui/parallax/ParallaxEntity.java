package com.tooflya.bouncekid.ui.parallax;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.Shape;

public class ParallaxEntity extends org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity {

	private float yParallaxFactor = 0;
	private float xParallaxFactor = 0;
	
	private Shape shape;

	public ParallaxEntity(final float pParallaxFactor, final Shape shape) {
		super(pParallaxFactor, shape);

		this.shape = shape;
		this.xParallaxFactor = pParallaxFactor;
	}

	public ParallaxEntity(final float xParallaxFactor, final float yParallaxFactor, final Shape pShape) {
		super(xParallaxFactor, pShape);

		this.xParallaxFactor = xParallaxFactor;
		this.yParallaxFactor = yParallaxFactor;
	}

	@Override
	public void onDraw(final GL10 pGL, final float pParallaxValue, final Camera pCamera) {
		pGL.glPushMatrix();
		{
			final float cameraWidth = pCamera.getWidth();
			final float shapeWidthScaled = this.shape.getWidth();
			float baseOffsetX = (pParallaxValue * this.xParallaxFactor) % shapeWidthScaled;
			float baseOffsetY = 0;

			if (this.yParallaxFactor > 0) {
				baseOffsetY = -(pCamera.getCenterY() - pCamera.getHeight() / 2) * this.yParallaxFactor / 10;
			}

			while (baseOffsetX > 0) {
				baseOffsetX -= shapeWidthScaled;
			}

			pGL.glTranslatef(baseOffsetX, baseOffsetY, 0);

			float currentMaxX = baseOffsetX;

			do {
				this.shape.onDraw(pGL, pCamera);
				pGL.glTranslatef(shapeWidthScaled, 0, 0);
				currentMaxX += shapeWidthScaled;
			} while (currentMaxX < cameraWidth);
		}
		pGL.glPopMatrix();
	}
}
