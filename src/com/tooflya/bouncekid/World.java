package com.tooflya.bouncekid;

import com.tooflya.bouncekid.entity.Personage;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Personage personage;

	// ===========================================================
	// Constructors
	// ===========================================================

	public World() {
		super();

		this.reset();

		Game.screens.get(Screen.MAIN).attachChild(this);

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void update() {

	}
}
