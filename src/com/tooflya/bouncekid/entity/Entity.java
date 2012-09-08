package com.tooflya.bouncekid.entity;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.GameActivity;
import com.tooflya.bouncekid.Screen;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	public Entity(final TiledTextureRegion pTiledTextureRegion) {
		super(0, 0, pTiledTextureRegion.deepCopy());

		this.setVisible(false);
		this.setCullingEnabled(false);

		GameActivity.screens.get(Screen.MAIN).attachChild(this);
	}

	public Entity(final int x, final int y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion.deepCopy());

		this.setCenterPosition(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public Entity create() {
		this.setVisible(true);

		return this;
	}

	public void delete() {
		setVisible(false);
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

	// ===========================================================
	// Virtual methods
	// ===========================================================

	@Override
	public void reset() {
		this.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.Entity#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		this.setIgnoreUpdate(!visible);
	}

	// ===========================================================
	// Abstract methods
	// ===========================================================

	public abstract Entity deepCopy();
}
