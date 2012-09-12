package com.tooflya.bouncekid;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.LimitedFPSEngine;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.WakeLockOptions;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.tooflya.bouncekid.background.AsyncTaskLoader;
import com.tooflya.bouncekid.background.IAsyncCallback;
import com.tooflya.bouncekid.managers.ScreenManager;
import com.tooflya.bouncekid.screens.LoadingScreen;
import com.tooflya.bouncekid.screens.Screen;
import com.tooflya.bouncekid.ui.CustomCamera;

/**
 * @author Tooflya.com
 * @since
 */
public class Game extends LayoutGameActivity implements IAsyncCallback {

	// ===========================================================
	// Constants
	// ===========================================================

	/** Random instance for all application */
	public final static Random random = new Random();

	/** Instance of engine */
	private static Engine engine;

	/**  */
	private static Activity instance;

	/** Context of main activity */
	private static Context context;

	/** Camera of the game */
	private static CustomCamera camera;

	/**  */
	public static boolean isGameLoaded = false;

	/**  */
	public static Font font;

	/**  */
	public final static BitmapTextureAtlas resourcesBitmapTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	/**  */
	private final static BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	/**  */
	public static float fps;

	/**  */
	public static TimerHandler GameTimer;

	/** Accelerometer data */
	public static float accelerometerX = 0;
	public static float accelerometerY = 0;

	/**  */
	public static ScreenManager screens;

	// ===========================================================
	// Fields
	// ===========================================================

	/**  */
	private long screenChangeTime = 0;

	/**  */
	public static World world;

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadComplete()
	 */
	@Override
	public void onLoadComplete() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadEngine()
	 */
	@Override
	public Engine onLoadEngine() {

		/** Let's remember Context of this activity */
		context = getApplicationContext();

		/** Set the position and resolution of camera */
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		/** Initialize camera parameters */
		Options.cameraWidth = displayMetrics.widthPixels * 2;
		Options.cameraHeight = displayMetrics.heightPixels * 2;
		Options.cameraCenterX = Options.cameraWidth / 2;
		Options.cameraCenterY = Options.cameraHeight / 2;

		/** Initialize camera instance */
		camera = new CustomCamera(0, 0, Options.cameraWidth, Options.cameraHeight);

		/** Initialize the configuration of engine */
		final EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(displayMetrics.widthPixels, displayMetrics.heightPixels), camera)
				.setWakeLockOptions(WakeLockOptions.SCREEN_BRIGHT)
				.setWakeLockOptions(WakeLockOptions.SCREEN_ON)
				.setNeedsMusic(true)
				.setNeedsSound(true);

		/** Disable extension vertex buffer objects. This extension usually has a problems with HTC phones */
		options.getRenderOptions().disableExtensionVertexBufferObjects();

		/** Auto setRunOnUpdateThread for touch events */
		options.getTouchOptions().setRunOnUpdateThread(true);

		/** Try to init our engine */
		engine = new LimitedFPSEngine(options, Options.targerFPS);

		/** Trying to initialize multitouch */
		try {
			if (MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
			}
		} catch (final MultiTouchException e) {
		}

		/**  */
		instance = this;

		return engine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadResources()
	 */
	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");

		font = FontFactory.createFromAsset(fontTexture, getApplicationContext(), "casual.ttf", 14, true, Color.WHITE);

		this.getEngine().getFontManager().loadFont(font);
		this.getEngine().getTextureManager().loadTextures(fontTexture, resourcesBitmapTexture);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.background.IAsyncCallback#workToDo()
	 */
	@Override
	public void workToDo() {

		/** Create screen manager */
		screens = new ScreenManager();

		this.world = new World();

		/** Create game timer */
		GameTimer = new TimerHandler(0.02f, true, new GameTimer(this.world));

		/** White while progressbar is running */
		while (!isGameLoaded) {
		} // TODO: synchronized?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.background.IAsyncCallback#onComplete()
	 */
	@Override
	public void onComplete() {
		screens.set(Screen.MAIN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.IGameInterface#onLoadScene()
	 */
	@Override
	public Scene onLoadScene() {
		FPSCounter fpsCounter = new FPSCounter() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);

				fps = getFPS();
			}
		};
		this.getEngine().registerUpdateHandler(fpsCounter);

		/** Start background loader */
		new AsyncTaskLoader().execute(this);

		/** Create loading screen and return her scene for attaching to the activity */
		return new LoadingScreen();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.activity.BaseGameActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();

		// TODO: Here we need to correctly shutdown our application and unload all resources
		getTextureManager().unloadTextures();

		/** Notify the system to finalize and collect all objects of the application on exit so that the process running the application can be killed by the system without causing issues. NOTE: If this is set to true then the process will not be killed until all of its threads have closed. */
		System.runFinalizersOnExit(true);

		/** Force the system to close the application down completely instead of retaining it in the background. The process that runs the application will be killed. The application will be completely created as a new application in a new process if the user starts the application again. */
		System.exit(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.activity.BaseGameActivity#onResumeGame()
	 */
	@Override
	public void onResumeGame() {
		super.onResumeGame();

		// TODO: Release all music, update handlers and other active things

		// this.enableAccelerometerSensor(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.activity.BaseGameActivity#onPauseGame()
	 */
	@Override
	public void onPauseGame() {
		super.onPauseGame();

		// TODO: Stop all music, update handlers and other active things

		// this.disableAccelerometerSensor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (Screen.screen < 0) {
			return false;
		}

		if (System.currentTimeMillis() - screenChangeTime < 500) {
			return false;
		}

		screenChangeTime = System.currentTimeMillis();

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return screens.get(Screen.screen).onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.activity.LayoutGameActivity#getLayoutID()
	 */
	@Override
	protected int getLayoutID() {
		return R.layout.main;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.ui.activity.LayoutGameActivity#getRenderSurfaceViewID ()
	 */
	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.main_rendersurfaceview;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static void loadTextures(final BitmapTextureAtlas... textures) {
		engine.getTextureManager().loadTextures(textures);
	}

	public static void unloadTextures(final BitmapTextureAtlas... textures) {
		engine.getTextureManager().unloadTextures(textures);
	}

	public static void close() {
		instance.finish();
	}

	public static CustomCamera getCamera() {
		return camera;
	}

	public static Context getContext() {
		return context;
	}

	public static Activity getInstance() {
		return instance;
	}

	public static Engine getCore() {
		return engine;
	}
}
