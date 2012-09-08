package com.tooflya.bouncekid.screens;

import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;

import android.widget.Toast;

import com.tooflya.bouncekid.GameActivity;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.Screen;
import com.tooflya.bouncekid.entity.Personage;

/**
 * @author Tooflya.com
 * @since
 */
public class MainScreen extends Screen {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Personage hero;
	private BitmapTextureAtlas heroTexture;
	private TiledTextureRegion heroRegion;

	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;

	private TextureRegion mParallaxLayerBack;
	private TextureRegion mParallaxLayerMid;
	private TextureRegion mParallaxLayerFront;

	private AutoParallaxBackground autoParallaxBackground;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MainScreen() {
		heroTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		heroRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(heroTexture, GameActivity.context, "sprite_running.png", 0, 0, 5, 2);

		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, GameActivity.context, "parallax_background_layer_front.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, GameActivity.context, "parallax_background_layer_back.png", 0, 188);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, GameActivity.context, "parallax_background_layer_mid.png", 0, 669);

		GameActivity.instance.getTextureManager().loadTextures(this.mAutoParallaxBackgroundTexture, heroTexture);

		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.0f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 80, this.mParallaxLayerMid)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront)));

		this.setBackground(autoParallaxBackground);

		autoParallaxBackground.setParallaxChangePerSecond(40);

		hero = new Personage(220, Options.cameraHeight - 285, heroRegion);
		this.attachChild(hero);

		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {

				switch (arg1.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if (!hero.isJumping) {
						hero.ChangeStates((byte) 4, (byte) 1);
					}
					break;
				case TouchEvent.ACTION_UP:
					hero.ChangeStates((byte) 2, (byte) 2);
					break;
				}
				return false;
			}

		});

		final TMXLoader tmxLoader = new TMXLoader(GameActivity.activity,
				GameActivity.instance.getTextureManager(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		TMXLayer tmxLayer;
		try {
			tmxLayer = tmxLoader.loadFromAsset(GameActivity.activity, "tmx/desert.tmx").getTMXLayers().get(0);
			this.attachChild(tmxLayer);
		} catch (TMXLoadException e) {
			e.printStackTrace();
		}

	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onAttached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();
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
		GameActivity.activity.finish();
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}