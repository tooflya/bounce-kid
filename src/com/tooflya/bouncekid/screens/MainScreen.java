package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.EntitySimple;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.ui.parallax.AutoParallaxBackground;
import com.tooflya.bouncekid.ui.parallax.ParallaxBackground.ParallaxEntity;

/**
 * @author Tooflya.com
 * @since
 */
public class MainScreen extends Screen implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {

	// ===========================================================
	// Constants
	// ===========================================================

	private final static BitmapTextureAtlas autoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
	private final static BitmapTextureAtlas autoParallaxBackgroundTexture2 = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

	private final static AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(30f);

	private final static TextureRegion parallaxLayerBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture2, Game.context, "main_bg.png", 0, 0);
	private final static TextureRegion parallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "par_back.png", 0, 410);
	private final static TextureRegion parallaxLayerMiddle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "oblaka.png", 0, 710);
	private final static TextureRegion parallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "par_front.png", 0, 0);

	private final static ChangeableText fpsInfo = new ChangeableText(100, 160, Game.font, "Fps      ");
	private final static ChangeableText altnfo = new ChangeableText(100, 160, Game.font, "Altitude");

	// ===========================================================
	// Fields
	// ===========================================================

	private SurfaceScrollDetector scrollDetector;
	private PinchZoomDetector pinchZoomDetector;
	private float pinchZoomStartedCameraZoomFactor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MainScreen() {
		final EntitySimple sun;
		EntitySimple a = new EntitySimple(Options.cameraWidth - parallaxLayerBackground.getWidth(), 0, parallaxLayerBackground);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0,a));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0,  sun = new EntitySimple(Options.cameraWidth - 190, 14, BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "sun_flames.png", 0, 910))));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-6.0f, new EntitySimple(parallaxLayerMiddle)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.0f, 3.0f, new EntitySimple(0, Options.cameraHeight - parallaxLayerBack.getHeight() - 100, parallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-4.0f, 7.0f, new EntitySimple(0, Options.cameraHeight - parallaxLayerFront.getHeight(), parallaxLayerFront)));

		this.attachChild(autoParallaxBackground);
		
		final RotationModifier rotate = new RotationModifier(40000000, 0, Integer.MAX_VALUE);

		sun.registerEntityModifier(rotate);

		fpsInfo.setPosition(15, 15);
		altnfo.setPosition(15, 40);

		this.attachChild(fpsInfo);
		this.attachChild(altnfo);

		this.setOnSceneTouchListener(this);

		Game.loadTextures(autoParallaxBackgroundTexture, autoParallaxBackgroundTexture2);

		this.setOnAreaTouchTraversalFrontToBack();

		this.scrollDetector = new SurfaceScrollDetector(this);
		if (MultiTouch.isSupportedByAndroidVersion()) {
			try {
				this.pinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				this.pinchZoomDetector = null;
			}
		} else {
			this.pinchZoomDetector = null;
		}

		this.setOnSceneTouchListener(this);
		this.setTouchAreaBindingEnabled(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener#onPinchZoomStarted(org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector, org.anddev.andengine.input.touch.TouchEvent)
	 */
	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
		this.pinchZoomStartedCameraZoomFactor = Game.camera.getZoomFactor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener#onPinchZoom(org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector, org.anddev.andengine.input.touch.TouchEvent, float)
	 */
	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		Game.camera.setZoomFactor(this.pinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener#onPinchZoomFinished(org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector, org.anddev.andengine.input.touch.TouchEvent, float)
	 */
	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		Game.camera.setZoomFactor(this.pinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener#onScroll(org.anddev.andengine.input.touch.detector.ScrollDetector, org.anddev.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public void onScroll(ScrollDetector pScollDetector, TouchEvent pTouchEvent, float pDistanceX, float pDistanceY) {
		if (Options.scrollEnabled) {
			final float zoomFactor = Game.camera.getZoomFactor();
			Game.camera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener#onSceneTouchEvent(org.anddev.andengine.entity.scene.Scene, org.anddev.andengine.input.touch.TouchEvent)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (Options.zoomEnabled) {
			if (this.pinchZoomDetector != null) {
				this.pinchZoomDetector.onTouchEvent(pSceneTouchEvent);

				if (this.pinchZoomDetector.isZooming()) {
					this.scrollDetector.setEnabled(false);
				} else {
					if (pSceneTouchEvent.isActionDown()) {
						this.scrollDetector.setEnabled(true);
					}
					this.scrollDetector.onTouchEvent(pSceneTouchEvent);
				}
			} else {
				this.scrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		}

		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			Game.world.personage.ChangeStates(ActionHelper.WantToFly, (byte) 0);
			break;
		case TouchEvent.ACTION_UP:
			Game.world.personage.ChangeStates((byte) 0, ActionHelper.WantToFly);
			break;
		}

		return false;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.scene.Scene#onManagedUpdate(float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		fpsInfo.setText(FloatMath.floor(Game.fps) + "");
		altnfo.setText(FloatMath.floor(Game.world.personage.getY()) + "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onAttached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();

		registerUpdateHandler(Game.GameTimer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onDetached() {
		super.onDetached();

		unregisterUpdateHandler(Game.GameTimer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onBackPressed()
	 */
	@Override
	public boolean onBackPressed() {
		Game.close();
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}
