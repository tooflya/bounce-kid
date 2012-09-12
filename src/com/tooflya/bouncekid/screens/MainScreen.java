package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Debug;

import android.util.FloatMath;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;

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
	private TextureRegion mParallaxLayerMid;
	private TextureRegion mParallaxLayerFront;

	private AutoParallaxBackground autoParallaxBackground;

	public static Sprite top, bottom, center;

	private TMXTiledMap mTMXTiledMap;

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

		this.mParallaxLayerBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture2, Game.getContext(), "bg_static.png", 0, 0);
		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "par_up.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "par_down.png", 0, 310);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, Game.getContext(), "par_centre.png", 0, 620);

		Game.loadTextures(this.mAutoParallaxBackgroundTexture, this.mAutoParallaxBackgroundTexture2);

		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0, this.mParallaxLayerBackground)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.3f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerBack.getHeight() / 2, this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-3.0f, new Sprite(0, -this.mParallaxLayerFront.getHeight() / 2, this.mParallaxLayerFront)));

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(5.0f, new Sprite(0, (Options.cameraHeight - this.mParallaxLayerMid.getHeight()) / 2, this.mParallaxLayerMid)));

		this.setBackground(autoParallaxBackground);

		autoParallaxBackground.setParallaxChangePerSecond(20);

		this.setOnSceneTouchListener(this);

		try {
			final TMXLoader tmxLoader = new TMXLoader(Game.getInstance(), Game.getCore().getTextureManager(), TextureOptions.NEAREST, null);
			this.mTMXTiledMap = tmxLoader.loadFromAsset(Game.getInstance(), "levels/map.tmx");
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}

		// Add the non-object layers to the scene
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++) {
			TMXLayer layer = this.mTMXTiledMap.getTMXLayers().get(i);
			this.attachChild(layer);
		}
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
			Game.getCamera().setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			break;
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}
