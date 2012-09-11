package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.text.ChangeableText;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Screen;

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

		Game.camera.setHUD(hud);
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

		// fpsInfo.setText(FloatMath.floor(Game.fps) + "");
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
		Game.activity.finish();
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}
