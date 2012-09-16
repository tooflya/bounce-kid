package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
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
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.GameTimer;
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

	private final static BitmapTextureAtlas autoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	private final static BitmapTextureAtlas autoParallaxBackgroundTexture2 = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	private final static BitmapTextureAtlas texture = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	public final static AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(30f);

	private final static TextureRegion parallaxLayerWater = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "water.png", 0, 0);
	private final static TextureRegion parallaxLayerWay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "way.png", 0, 70);

	private final static ChangeableText fpsInfo = new ChangeableText(100, 160, Game.font, "xxxxxxxxx");
	private final static ChangeableText altInfo = new ChangeableText(100, 160, Game.font, "xxxxxxxxxxxxxxx");
	private final static ChangeableText resolutionInfo = new ChangeableText(100, 160, Game.font, "xxxxxxxxxxxxxxxxxxxxxxxxx");
	private final static ChangeableText cameraInfo = new ChangeableText(100, 160, Game.font, "xxxxxxxxxxxxxxxxxxxxx");

	private final static HUD hud = new HUD();

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
		this.setBackground(new ColorBackground(152f / 255f, 232f / 255f, 255f / 255f, 1f));

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.5f, 0.5f, new EntitySimple(0, (int) (Options.cameraHeight - parallaxLayerWater.getHeight() * Options.cameraRatioFactor - parallaxLayerWay.getHeight() * Options.cameraRatioFactor), parallaxLayerWay, false)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-1.5f, 1f, new EntitySimple(0, (int) (Options.cameraHeight - parallaxLayerWater.getHeight() * Options.cameraRatioFactor), parallaxLayerWater, false)));

		Game.loadTextures(autoParallaxBackgroundTexture, autoParallaxBackgroundTexture2, texture);

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

		this.setOnSceneTouchListener(this);
		this.setTouchAreaBindingEnabled(true);
		this.attachChild(autoParallaxBackground);

		hud.attachChild(fpsInfo);
		hud.attachChild(altInfo);
		hud.attachChild(resolutionInfo);
		hud.attachChild(cameraInfo);

		fpsInfo.setPosition(15, 15);
		altInfo.setPosition(15, 40);
		resolutionInfo.setPosition(15, 65);
		cameraInfo.setPosition(15, 90);

		final TextureRegion region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, Game.context, "reset.png", 0, 0);
		final EntitySimple reset = new EntitySimple((int) (Options.cameraWidth - region.getWidth() * Options.cameraRatioFactor) - (int) (10 * Options.cameraRatioFactor), (int) (10 * Options.cameraRatioFactor), region, false) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				GameTimer.world.reInit();

				return false;
			}
		};
		hud.registerTouchArea(reset);

		hud.attachChild(reset);
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

		fpsInfo.setText("FPS: " + FloatMath.floor(Game.fps));
		altInfo.setText("ALT: " + FloatMath.floor(Game.world.personage.getY()));
		resolutionInfo.setText("RES: " + Options.cameraWidth + " x " + Options.cameraHeight + " x " + Options.cameraRatioFactor);
		cameraInfo.setText("CAM: " + Game.camera.getCenterX() + " x " + Game.camera.getCenterY());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onAttached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();

		Game.camera.setHUD(hud);

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
