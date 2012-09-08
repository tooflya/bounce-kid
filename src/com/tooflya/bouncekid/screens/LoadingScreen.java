package com.tooflya.bouncekid.screens;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.util.GLHelper;

import com.tooflya.bouncekid.GameActivity;
import com.tooflya.bouncekid.Screen;

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
		mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		mProgressBarTextureAtlas = new BitmapTextureAtlas(512, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		GameActivity.instance.getTextureManager().loadTextures(mBackgroundTextureAtlas, mProgressBarTextureAtlas);

		/**
		 * 
		 * Creating of sprites
		 * 
		 */
		Sprite background = new Sprite(0, 0, BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, GameActivity.context, "tooflya_hd.png", 0, 0)) {
			@Override
			protected void onInitDraw(final GL10 pGL) {
				super.onInitDraw(pGL);
				GLHelper.enableTextures(pGL);
				GLHelper.enableTexCoordArray(pGL);
				GLHelper.enableDither(pGL);
			}
		};
		setBackground(new SpriteBackground(background));

		progressBar = new Sprite(228, 421, BitmapTextureAtlasTextureRegionFactory.createFromAsset(mProgressBarTextureAtlas, GameActivity.context, "loaderbar_full_hd.png", 0, 0));
		progressBar.setWidth(1);
		progressBar.getTextureRegion().setWidth(1);
		attachChild(progressBar);

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
					GameActivity.isGameLoaded = true;
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

		GameActivity.instance.getTextureManager().unloadTextures(mBackgroundTextureAtlas, mProgressBarTextureAtlas);
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