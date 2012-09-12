package com.tooflya.bouncekid;

import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.screens.Screen;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public Personage personage;

	// ===========================================================
	// Constructors
	// ===========================================================

	public World() {
		super();

		this.personage = new Personage(0, Options.cameraHeight - 120);
		this.personage.create();

		Game.screens.get(Screen.MAIN).attachChild(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void update() {
		this.personage.update();
	}
}
