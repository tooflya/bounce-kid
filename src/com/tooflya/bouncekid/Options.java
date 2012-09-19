package com.tooflya.bouncekid;

/**
 * @author Tooflya.com
 * @since
 */
public class Options {
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

	public static float cameraRatioFactor;
	public final static float cameraOriginRatio = 577.0f;

	public static final int fps = 120;

	public static final boolean zoomEnabled = true;
	public static final boolean scrollEnabled = true;

	// ===========================================================
	// Map options
	// ===========================================================

	public static final int maxDistanceBetweenBlocksX = 300;
	public static final int maxDistanceBetweenBlocksY = 100;

	public static final int mainStep = 4;
	public static final int babyStep = 1;
}
