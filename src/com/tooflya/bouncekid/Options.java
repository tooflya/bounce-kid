package com.tooflya.bouncekid;

/**
 * @author Tooflya.com
 * @since
 */
public class Options {
	public static final boolean DEBUG = true;

	public static float PI = 3.14159265358979f;

	/** Camera parameters */
	public static int cameraWidth;
	public static int cameraHeight;
	public static int cameraCenterX;
	public static int cameraCenterY;

	public static int cameraWidthOrigin;
	public static int cameraHeightOrigin;
	public static int cameraCenterOriginX;
	public static int cameraCenterOriginY;

	public static float cameraMaxCenterX;
	public static float cameraMaxCenterY;

	public static float CAMERA_RATIO_FACTOR;
	public final static float CORX = 1024.0f;
	public final static float cameraOriginRatioY = 580.0f;

	public static final int fps = 80;

	public static final boolean zoomEnabled = true;
	public static final boolean scrollEnabled = true;

	// ===========================================================
	// Map options
	// ===========================================================

	public static final int maxDistanceBetweenBlocksX = 300;
	public static final int maxDistanceBetweenBlocksY = 100;

	public static float mainStep = 1f;
	public static final int babyStep = 1;

	public static boolean NEED_MUSIC = true;
	public static boolean NEED_SOUND = true;
}
