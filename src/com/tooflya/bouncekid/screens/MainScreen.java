package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.util.FloatMath;

import com.tooflya.bouncekid.GameActivity;
import com.tooflya.bouncekid.Map;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.Screen;
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

	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;

	private TextureRegion mParallaxLayerBack;
	private TextureRegion mParallaxLayerMid;
	private TextureRegion mParallaxLayerFront;

	private AutoParallaxBackground autoParallaxBackground;

	private ChangeableText fpsInfo, altitudeInfo;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MainScreen() {
	}

	public void after() {
		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);

		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, GameActivity.context, "parallax_background_layer_front.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, GameActivity.context, "parallax_background_layer_back.png", 0, 188);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, GameActivity.context, "parallax_background_layer_mid.png", 0, 669);

		GameActivity.instance.getTextureManager().loadTextures(this.mAutoParallaxBackgroundTexture);

		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.3f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 80, this.mParallaxLayerMid)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, Options.cameraHeight - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront)));

		this.setBackground(autoParallaxBackground);

		autoParallaxBackground.setParallaxChangePerSecond(40);

		GameActivity.instance.getTextureManager().loadTextures(mAutoParallaxBackgroundTexture);

		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {

				switch (arg1.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if (!GameActivity.map.hero.IsState(ActionHelper.Jump) && !GameActivity.map.hero.IsState(ActionHelper.Fall)) {
						GameActivity.map.hero.ChangeStates(ActionHelper.Jump, ActionHelper.Running);
					}
					break;
				case TouchEvent.ACTION_UP:
					GameActivity.map.hero.ChangeStates(ActionHelper.Fall, ActionHelper.Jump);
					break;
				}
				return false;
			}
		});

		fpsInfo = new ChangeableText(100, 160, GameActivity.font, "Fps      ");
		fpsInfo.setPosition(15, 15);

		altitudeInfo = new ChangeableText(100, 160, GameActivity.font, "Altitude");
		altitudeInfo.setPosition(15, 40);

		HUD hud = new HUD();
		hud.attachChild(fpsInfo);
		hud.attachChild(altitudeInfo);

		GameActivity.camera.setHUD(hud);
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

		fpsInfo.setText(FloatMath.floor(GameActivity.fps) + "");
		altitudeInfo.setText(FloatMath.floor(Map.hero.getY()) + "");
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
