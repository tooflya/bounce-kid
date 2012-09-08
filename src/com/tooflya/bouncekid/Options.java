package com.tooflya.bouncekid;

/**
 * @author Tooflya.com
 * @since
 */
public class Options {
	public static float PI = 3.14159265358979f;

	/**
	 * 
	 * Camera parameters
	 * 
	 */
	public static int cameraWidth = 800;
	public static int cameraHeight = 480;
	public static int cameraCenterX = Options.cameraWidth / 2;
	public static int cameraCenterY = Options.cameraHeight / 2;

	// ===========================================================
	// Some options which will be saved
	// ===========================================================

	public static boolean ENABLE_VIBRATION;
	public static boolean ENABLE_MUSIC;

	public static final int targerFPS = 120;

	// ===========================================================
	// Map options
	// ===========================================================

	public static final int maxDistanceBetweenBlocksX = 100; // TODO: Question(Igor): Int or Float type?
	public static final int maxDistanceBetweenBlocksY = 100; // TODO: Question(Igor): Int or Float type?

	// ===========================================================
	// Map options
	// ===========================================================

	public static final int blockHeight = 64;
	public static final int blockWidth = 64;
	public static final float blockStep = 5;
}
