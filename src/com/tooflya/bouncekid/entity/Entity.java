package com.tooflya.bouncekid.entity;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Screen;
import com.tooflya.bouncekid.managers.EntityManager;

/**
 * @author Tooflya.com
 * @since
 */
public abstract class Entity extends AnimatedSprite {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int id;

	protected int health;
	protected int state;
	protected int type;

	protected float speed;

	protected EntityManager manager;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Entity(final TiledTextureRegion pTiledTextureRegion) {
		super(0, 0, pTiledTextureRegion.deepCopy());

		this.hide();

		Game.screens.get(Screen.MAIN).attachChild(this);
	}

	public Entity(final int x, final int y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion.deepCopy());

		this.setCenterPosition(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public Entity create() {
		this.show();

		return this;
	}

	public void destroy() {
		if (this.isManagerExist()) {
			this.manager.destroy(this.id);
		}

		this.hide();
	}

	public void update() {
	}

	public void show() {
		this.setVisible(true);
		this.setIgnoreUpdate(false);
		this.setCullingEnabled(true);
	}

	public void hide() {
		this.setVisible(false);
		this.setIgnoreUpdate(true);
		this.setCullingEnabled(false);
	}

	// ===========================================================
	// Validate methods
	// ===========================================================

	public boolean isManagerExist() {
		if (this.manager != null) {
			return false;
		}

		return false;
	}

	// ===========================================================
	// Setters
	// ===========================================================

	public void setID(final int id) {
		this.id = id;
	}

	public void setState(final int state) {
		this.state = state;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public void setSpeed(final float speed) {
		this.speed = speed;
	}

	public void setHealth(final int health) {
		this.health = health;
	}

	public void setCenterX(final float centerX) {
		this.setPosition(centerX - getWidth() / 2, getY());
	}

	public void setCenterY(final float centerY) {
		this.setPosition(getX(), centerY - getHeight() / 2);
	}

	public void setCenterPosition(final float centerX, final float centerY) {
		this.setPosition(centerX - getWidth() / 2, centerY - getHeight() / 2);
	}

	public void setManager(final EntityManager manager) {
		this.manager = manager;
	}

	// ===========================================================
	// Getters
	// ===========================================================

	public int getID() {
		return id;
	}

	public int getState() {
		return state;
	}

	public int getType() {
		return type;
	}

	public float getSpeed() {
		return speed;
	}

	public int getHealth() {
		return health;
	}

	public float getCenterX() {
		return getX() + getWidth() / 2;
	}

	public float getCenterY() {
		return getY() + getHeight() / 2;
	}

	public EntityManager getManager(final EntityManager manager) {
		return manager;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

	// ===========================================================
	// Abstract methods
	// ===========================================================

	public abstract Entity deepCopy();
}
