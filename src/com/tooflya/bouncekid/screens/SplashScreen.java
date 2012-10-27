package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.entity.EntitySimple;

/**
 * @author Tooflya.com
 * @since
 */
public class SplashScreen extends Screen {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final static BitmapTextureAtlas mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	private final static TimerHandler mUpdateTimer = new TimerHandler(4f, true, new ITimerCallback() {
		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {
			Game.screens.set(Screen.LOADING);
		}
	});

	// ===========================================================
	// Constructors
	// ===========================================================

	public SplashScreen() {
		Game.loadTextures(mBackgroundTextureAtlas);

		this.setBackground(new SpriteBackground(new EntitySimple(BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, Game.context, "tooflya_hd.png", 0, 0))));

		registerUpdateHandler(mUpdateTimer);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onDetached() {
		super.onDetached();

		clearUpdateHandlers();

		Game.unloadTextures(mBackgroundTextureAtlas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onBackPressed()
	 */
	@Override
	public boolean onBackPressed() {
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}