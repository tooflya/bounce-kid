package com.tooflya.bouncekid.helpers;

public class ActionHelper {
	// Binary number system works only in JAVA SE 1.7.
	public static final byte None = 0; // 0b00000000;
	public static final byte Running = 1; // 0b00000001;
	public static final byte Jump = 2; // 0b00000010;
	public static final byte Fall = 4; // 0b00000100;
	public static final byte JumpOrFall = 6; // 0b00000110;
	public static final byte OnPressure = 8; // 0b00001000;
	
}
