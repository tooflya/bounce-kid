package com.tooflya.bouncekid.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.managers.EntityManager;
import com.tooflya.bouncekid.screens.Screen;

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

	public Entity(final TiledTextureRegion pTiledTextureRegion, final boolean needParent) {
		super(0, 0, pTiledTextureRegion.deepCopy());

		this.hide();

		this.setScaleCenter(0, 0);
		this.setScaleY(Options.cameraRatioFactor);
		this.setScaleX(Options.cameraRatioFactor);

		this.setPosition(this.getX() - (this.getWidthScaled() - Options.cameraWidth) / 2, this.getY()); // TODO: YYY

		if (needParent)
			Game.screens.get(Screen.MAIN).attachChild(this);
	}

	public Entity(final int x, final int y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion, true);

		this.setCenterPosition(x, y);
	}

	public Entity(final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion, true);
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
		this.setCullingEnabled(false);
	}

	public void hide() {
		this.setVisible(false);
		this.setIgnoreUpdate(true);
		this.setCullingEnabled(true);
	}

	// ===========================================================
	// Validate methods
	// ===========================================================

	public boolean isManagerExist() {
		if (this.manager != null) {
			return true;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.BaseSprite#onInitDraw(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);

		GLHelper.enableDither(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
	}

	// ===========================================================
	// Abstract methods
	// ===========================================================

	public abstract Entity deepCopy();
}
