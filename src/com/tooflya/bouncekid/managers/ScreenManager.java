package com.tooflya.bouncekid.managers;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.R;
import com.tooflya.bouncekid.screens.MainScreen;
import com.tooflya.bouncekid.screens.Screen;

/**
 * @author Tooflya.com
 * @since
 */
public class ScreenManager {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private RelativeLayout casper;

	private Animation animationIn, animationOut;

	/**
	 * 
	 * List of available screens
	 * 
	 */
	public Screen[] screens;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScreenManager() {
		screens = new Screen[Screen.SCREENS_COUNT];

		/**
		 * 
		 * Create all scenes
		 * 
		 */
		screens[Screen.MAIN] = new MainScreen();
		// screens[Screen.OPTIONS] = new OptionsScreen();
		// screens[Screen.BOXCHOISE] = new BoxChoiseScreen();
		// screens[Screen.LEVELCHOISE] = new LevelChoiseScreen();
		// screens[Screen.LEVEL] = new LevelScreen();
		// screens[Screen.LEVELLOSE] = new LevelLoseScreen();
		// screens[Screen.LEVELPREPARE] = new LevelPrepareScreen();
		// screens[Screen.MODECHOISE] = new ModeChoiseScreen();
		// screens[Screen.MULTIPLAYER] = new MultiplayerScreen();

		/**
		 * 
		 * 
		 * 
		 */
		casper = (RelativeLayout) Game.instance.findViewById(R.id.casper);

		/**
		 * 
		 * 
		 * 
		 * 
		 */
		animationIn = AnimationUtils.loadAnimation(Game.context, R.anim.fadein);
		animationOut = AnimationUtils.loadAnimation(Game.context, R.anim.fadeout);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void set(final int pScreen) {
		Game.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				casper.setVisibility(View.VISIBLE);

				animationIn.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {

						animationOut.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
								screens[pScreen].setScene(Game.engine);
								screens[pScreen].onAttached();
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								casper.setVisibility(View.INVISIBLE);

								Screen.screen = pScreen;
							}
						});

						casper.startAnimation(animationOut);
					}
				});

				casper.startAnimation(animationIn);
			}
		});
	}

	public Screen get(final int pScreen) {
		return screens[pScreen];
	}

	public Screen getCurrent() {
		return screens[Screen.screen];
	}
}
