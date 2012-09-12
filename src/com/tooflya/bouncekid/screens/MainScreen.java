package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Sprite;

/**
 * @author Tooflya.com
 * @since
 */
public class MainScreen extends Screen implements IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	private BitmapTextureAtlas mAutoParallaxBackgroundTexture, mAutoParallaxBackgroundTexture2;

	private TextureRegion mParallaxLayerBackground;
	private TextureRegion mParallaxLayerBack;
	private TextureRegion mParallaxLayerBack2;
	private TextureRegion mParallaxLayerMid;
	private TextureRegion mParallaxLayerFront;

	private AutoParallaxBackground autoParallaxBackground;

	// ===========================================================
	// Fields
	// ===========================================================

	private ChangeableText fpsInfo, altnfo;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MainScreen() {

		fpsInfo = new ChangeableText(100, 160, Game.font, "Fps      ");
		fpsInfo.setPosition(15, 15);

		altnfo = new ChangeableText(100, 160, Game.font, "Altitude");
		altnfo.setPosition(15, 40);

		HUD hud = new HUD();
		hud.attachChild(fpsInfo);
		hud.attachChild(altnfo);

		Game.getCamera().setHUD(hud);

		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.mAutoParallaxBackgroundTexture2 = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

		this.mParallaxLayerBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture2, Game.getContext(), "main_bg.png", 0, 0);
		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "par_front.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "par_back.png", 0, 410);
		this.mParallaxLayerBack2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "oblaka.png", 0, 710);

		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 30f);

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0, this.mParallaxLayerBackground)));

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-6.0f, new Sprite(0, 0, this.mParallaxLayerBack2)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.0f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-4.0f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront)));

		this.setBackground(autoParallaxBackground);

		final Sprite sun = new Sprite(Options.cameraWidth - 190, 14, BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "sun_flames.png", 0, 910));
		final RotationModifier rotate = new RotationModifier(400000000, 0, Integer.MAX_VALUE);

		sun.registerEntityModifier(rotate);

		hud.attachChild(sun);

		this.setOnSceneTouchListener(this);

		Game.loadTextures(this.mAutoParallaxBackgroundTexture, this.mAutoParallaxBackgroundTexture2);
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
		// altnfo.setText(FloatMath.floor(Map.hero.getY()) + "");
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

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			break;
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}
