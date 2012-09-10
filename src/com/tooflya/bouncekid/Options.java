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
	public static int cameraWidth;
	public static int cameraHeight;
	public static int cameraCenterX;
	public static int cameraCenterY;

	// ===========================================================
	// Some options which will be saved
	// ===========================================================

	public static boolean ENABLE_VIBRATION;
	public static boolean ENABLE_MUSIC;

	public static final int targerFPS = 120;

	// ===========================================================
	// Map options
	// ===========================================================

	public static final int maxDistanceBetweenBlocksX = 0;
	public static final int maxDistanceBetweenBlocksY = 3;

	// ===========================================================
	// Block options
	// ===========================================================

	public static final int blockHeight = 64;
	public static final int blockWidth = 64;
	public static final float blockStep = 5;

	// ===========================================================
	// Personage options
	// ===========================================================

	public static final int startX = 50;
	public static final int startY = 300;
	public static final int someDistance = 100;
}
