package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
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
public class LoadingScreen extends Screen {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBackgroundTextureAtlas;
	private BitmapTextureAtlas mProgressBarTextureAtlas;

	private Sprite progressBar;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoadingScreen() {

		/**
		 * 
		 * Creating of texture atlases
		 * 
		 */
		mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mProgressBarTextureAtlas = new BitmapTextureAtlas(512, 512, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		Game.loadTextures(mBackgroundTextureAtlas, mProgressBarTextureAtlas);

		/**
		 * 
		 * Creating of sprites
		 * 
		 */
		SpriteBackground background = new SpriteBackground(new EntitySimple(BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, Game.context, "tooflya_hd.png", 0, 0)));

		this.setBackground(background);

		progressBar = new Sprite(228, 421, BitmapTextureAtlasTextureRegionFactory.createFromAsset(mProgressBarTextureAtlas, Game.context, "loaderbar_full_hd.png", 0, 0));
		progressBar.setWidth(1);
		progressBar.getTextureRegion().setWidth(1);
		// attachChild(progressBar);

		/**
		 * 
		 * Register timer of loading progressbar changes
		 * 
		 */
		registerUpdateHandler(new TimerHandler(1f / 15.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {

				/**
				 * 
				 * Changing size of progressbar
				 * 
				 */
				if (progressBar.getWidth() < 337) {

					progressBar.getTextureRegion().setWidth(progressBar.getTextureRegion().getWidth() + 3);

					progressBar.setWidth(progressBar.getTextureRegion().getWidth() + 3);
				} else {

					/**
					 * 
					 * if progressbar is full
					 * 
					 */
					Game.isGameLoaded = true;
				}
			}
		}));
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

		Game.unloadTextures(mBackgroundTextureAtlas, mProgressBarTextureAtlas);
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