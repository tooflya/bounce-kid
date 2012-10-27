package com.tooflya.bouncekid.managers;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.screens.LevelScreen;
import com.tooflya.bouncekid.screens.LoadingScreen;
import com.tooflya.bouncekid.screens.MenuScreen;
import com.tooflya.bouncekid.screens.Screen;
import com.tooflya.bouncekid.screens.SplashScreen;

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
		screens[Screen.SPLASH] = new SplashScreen();
		screens[Screen.LOADING] = new LoadingScreen();
		screens[Screen.MENU] = new MenuScreen();
		screens[Screen.LEVEL] = new LevelScreen();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void set(final int pScreen) {
		screens[pScreen].setScene(Game.engine);
		screens[pScreen].onAttached();
		Screen.screen = pScreen;
	}

	public Screen get(final int pScreen) {
		return screens[pScreen];
	}

	public Screen getCurrent() {
		return screens[Screen.screen];
	}
}
