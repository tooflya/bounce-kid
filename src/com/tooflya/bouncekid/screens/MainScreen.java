package com.tooflya.bouncekid.screens;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.GameActivity;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.Screen;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.helpers.ActionHelper;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	public MainScreen() {
	}

	public void after() {
		heroTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		heroRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(heroTexture, GameActivity.context, "sprite_running.png", 0, 0, 5, 2);

		hero = new Personage(0, 0, heroRegion);
		hero.setPosition(0, Options.cameraHeight - hero.getHeightScaled());

		GameActivity.instance.getTextureManager().loadTextures(heroTexture);

		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {

				switch (arg1.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if (!hero.IsState(ActionHelper.Jump) && !hero.IsState(ActionHelper.Fall)) {
						hero.ChangeStates(ActionHelper.Jump, (byte) 0);
					}
					break;
				case TouchEvent.ACTION_UP:
					hero.ChangeStates((byte) 0, ActionHelper.Jump);
					break;
				}
				return false;
			}
		});
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

	}

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