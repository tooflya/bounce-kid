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
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.GameTimer;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Bush;
import com.tooflya.bouncekid.entity.EntitySimple;
import com.tooflya.bouncekid.entity.Tree;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.ui.parallax.AutoParallaxBackground;
import com.tooflya.bouncekid.ui.parallax.ParallaxBackground.ParallaxEntity;
import com.tooflya.bouncekid.ui.parallax.ParallaxBackground.ParallaxEntityTree;

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

	public final static AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(Options.fps);

	private final static TextureRegion parallaxTopLayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture2, Game.context, "oblaka.png", 0, 0);
	private final static TextureRegion parallaxBackLayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "back_par.png", 0, 0);
	private final static TextureRegion parallaxFrontLayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture, Game.context, "mid_par.png", 0, 445);
	private final static TiledTextureRegion parallaxMiddleLayerTrees = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(autoParallaxBackgroundTexture, Game.context, "tree_one.png", 0, 690, 3, 1);
	private final static TiledTextureRegion parallaxMiddleLayerBush = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(autoParallaxBackgroundTexture2, Game.context, "bush.png", 0, 200, 3, 1);
	private final static TextureRegion parallaxBushLayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture2, Game.context, "bush_par.png", 0, 400);
	private final static TextureRegion parallaxBushLayer2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxBackgroundTexture2, Game.context, "bush_par2.png", 0, 600);

	private final static ChangeableText fpsInfo = new ChangeableText(100, 160, Game.font, "xxxxxxxxx");
	private final static ChangeableText altInfo = new ChangeableText(100, 160, Game.font, "xxxxxxxxxxxxxxxxxxxxxx");
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
		this.setBackground(new ColorBackground(21f / 255f, 209f / 255f, 255f / 255f, 1f));

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.2f, 0f, new EntitySimple(0, 0, parallaxTopLayer)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.2f, 0.1f, new EntitySimple(0, (int) ((Options.cameraHeight - parallaxBackLayer.getHeight() * Options.cameraRatioFactor) - parallaxMiddleLayerTrees.getHeight() / 2 * Options.cameraRatioFactor + 100 * Options.cameraRatioFactor), parallaxBackLayer)));
		//autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.3f, 0.2f, new EntitySimple(0, (int) ((Options.cameraHeight - parallaxFrontLayer.getHeight() * Options.cameraRatioFactor - parallaxBushLayer2.getHeight() * Options.cameraRatioFactor + 90 * Options.cameraRatioFactor)), parallaxBushLayer2)));
		//autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.4f, 0.2f, new EntitySimple(0, (int) ((Options.cameraHeight - parallaxFrontLayer.getHeight() * Options.cameraRatioFactor - parallaxBushLayer.getHeight() * Options.cameraRatioFactor + 90 * Options.cameraRatioFactor)), parallaxBushLayer)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntityTree(-0.7f, 0.2f, new EntitySimple(0, (int) (Options.cameraHeight - parallaxFrontLayer.getHeight() * Options.cameraRatioFactor + 10 * Options.cameraRatioFactor), parallaxFrontLayer), new Tree(parallaxMiddleLayerTrees), new Bush(parallaxMiddleLayerBush)));

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

		if (Options.DEBUG) {
			hud.attachChild(fpsInfo);
			hud.attachChild(altInfo);
			hud.attachChild(resolutionInfo);
			hud.attachChild(cameraInfo);

			fpsInfo.setPosition(15, 15);
			altInfo.setPosition(15, 40);
			resolutionInfo.setPosition(15, 65);
			cameraInfo.setPosition(15, 90);
		}

		final TextureRegion region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, Game.context, "reset.png", 0, 0);
		final EntitySimple reset = new EntitySimple((int) (Options.cameraWidth - region.getWidth() * Options.cameraRatioFactor) - (int) (10 * Options.cameraRatioFactor), (int) (10 * Options.cameraRatioFactor), region, false) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				GameTimer.world.init();

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
		if (this.pinchZoomStartedCameraZoomFactor * pZoomFactor < 1.2f && this.pinchZoomStartedCameraZoomFactor * pZoomFactor > 0.5f) {
			Game.camera.setZoomFactor(this.pinchZoomStartedCameraZoomFactor * pZoomFactor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener#onPinchZoomFinished(org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector, org.anddev.andengine.input.touch.TouchEvent, float)
	 */
	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		System.out.println(this.pinchZoomStartedCameraZoomFactor * pZoomFactor);
		if (this.pinchZoomStartedCameraZoomFactor * pZoomFactor < 1.2f && this.pinchZoomStartedCameraZoomFactor * pZoomFactor > 0.5f) {
			Game.camera.setZoomFactor(this.pinchZoomStartedCameraZoomFactor * pZoomFactor);
		}
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

			float distanceX = 0;
			float distanceY = 0;

			if (Options.cameraCenterOriginX <= Game.camera.getCenterX() + (-pDistanceX / zoomFactor) && Options.cameraMaxCenterX >= Game.camera.getCenterX() + (-pDistanceX / zoomFactor)) {
				distanceX = FloatMath.floor(-pDistanceX / zoomFactor);
			}

			if (Options.cameraCenterOriginY >= Game.camera.getCenterY() + (-pDistanceY / zoomFactor) && -Options.cameraMaxCenterY <= Game.camera.getCenterY() + (-pDistanceY / zoomFactor)) {
				distanceY = FloatMath.floor(-pDistanceY / zoomFactor);
			}

			Game.camera.offsetCenter(distanceX, distanceY);
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
			if (!Game.world.personage.IsState(ActionHelper.Fall))
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

		if (Options.DEBUG) {
			fpsInfo.setText("FPS: " + FloatMath.floor(Game.fps));
			altInfo.setText("DST: " + FloatMath.floor(Game.world.personage.getY()) + " x " + FloatMath.floor(Game.world.apt));
			resolutionInfo.setText("RES: " + FloatMath.floor(Game.camera.getWidth()) + " x " + FloatMath.floor(Game.camera.getHeight()) + " x " + Options.cameraRatioFactor);
			cameraInfo.setText("CAM: " + Game.camera.getCenterX() + " x " + Game.camera.getCenterY());
		}
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

		/*
		 * this.registerUpdateHandler(new IUpdateHandler() {
		 * 
		 * @Override public void onUpdate(float arg0) { GameTimer.world.update(); }
		 * 
		 * @Override public void reset() { }
		 * 
		 * });
		 */
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
