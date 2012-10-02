package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.tooflya.bouncekid.entity.Baby;
import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.EntitySimple;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.entity.Personage.ActionsList;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.BroodManager;
import com.tooflya.bouncekid.managers.EntityManager;
import com.tooflya.bouncekid.screens.Screen;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// public ObjectsManager mObjectsManager;

	private BitmapTextureAtlas texture;

	private EntityManager blocks;
	// private EntityManager stars;
	private BroodManager brood;

	private Block bottomBlock = null;

	private int widthCoef = 0;
	private int widthCoefStep = 10;

	// TODO: Strange name of variable.
	public int apt = 0, gg = 0, c = 0, mc = 0;

	// TODO: Use get or private?
	public Personage personage;

	// ===========================================================
	// Constructors
	// ===========================================================

	public World() {
		super();
		// mObjectsManager = new ObjectsManager();
		Game.screens.get(Screen.MAIN).attachChild(this);

		this.texture = new BitmapTextureAtlas(1024, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Game.loadTextures(texture);

		this.personage = new Personage();
		this.personage.create();

		this.brood = new BroodManager(4, new Baby());

		this.blocks = new EntityManager(10, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "platform.png", 0, 0, 1, 1)));

		// this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "stars.png", 83, 0, 1, 18)));

		this.init();
	}

	public void init() {
		this.blocks.clear(); // TODO: It seems that method don't work.
		this.generateStartBlocks(0, Options.cameraWidth);

		gg = 0;
		this.personage.setPosition(0, this.bottomBlock.getY() - this.bottomBlock.getHeightScaled());
		this.personage.rx = (int) this.personage.getX();
		this.apt = 0;
		c = 0;
		this.brood.clear();
		this.personage.actions.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void generateStartBlocks(final float startY, final float width) {
		this.bottomBlock = (Block) this.blocks.create();
		this.bottomBlock.setPosition(startY, Options.cameraHeight - this.bottomBlock.getHeightScaled());
		// this.bottomBlock.setScale(width / this.bottomBlock.getWidthScaled(), 1);
	}

	private void generateNextBottomBlock() {
		this.widthCoef += widthCoefStep;
		final Block tempBlock = (Block) this.blocks.create();

		final float maxWidth = (this.personage.getFlyPower() / this.personage.flyStep + this.personage.getFlyPower() / this.personage.fallStep) * this.personage.runStep;
		final float maxHeight = this.personage.getFlyPower() * this.personage.flyStep;

		final int maxPercent = 100;
		final int isUpPercent = 90;// (int) (maxPercent * 1); // TODO: Set a percent.
		int k = -1;
		if (Game.random.nextInt(maxPercent) >= isUpPercent) {
			k = 1;
		}
		float y = maxHeight * Game.random.nextFloat();

		float flyDistanceFromBlock = y / this.personage.flyStep * this.personage.runStep;
		float fallDistanceFromBlock = y / this.personage.fallStep * this.personage.runStep;
		float fallDistance = (maxHeight - y) / this.personage.fallStep * this.personage.runStep;
		float maxFlyDistance = this.personage.getFlyPower() / this.personage.flyStep * this.personage.runStep;

		if (k >= 0) {
			float xStart = this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() + fallDistanceFromBlock;
			float xFinish = xStart + maxWidth;
			float width = Math.max(tempBlock.getWidth(), Options.cameraWidth - widthCoef); // maxFlyDistance; // xFinish - xStart;
			// tempBlock.setScale(width / tempBlock.getWidth(), 1);
			tempBlock.setPosition(xStart + (xFinish - xStart) * Game.random.nextFloat(), Math.min(Options.cameraHeight - tempBlock.getHeightScaled(), this.bottomBlock.getY() + k * y));
		}
		else {
			float xStart = this.bottomBlock.getX() + Math.max(this.bottomBlock.getWidthScaled(), flyDistanceFromBlock);
			float xFinish = this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() - this.personage.getWidthScaled() + maxFlyDistance + fallDistance;
			float width = Math.max(tempBlock.getWidth(), Options.cameraWidth - widthCoef); // maxFlyDistance; // xFinish - xStart;
			// tempBlock.setScale(width / tempBlock.getWidth(), 1);
			tempBlock.setPosition(xStart + (xFinish - xStart) * Game.random.nextFloat(), this.bottomBlock.getY() + k * y);
		}

		this.bottomBlock = tempBlock;
		// this.mObjectsManager.decorate(this.bottomBlock);
	}

	private void checkCollision(Personage personage) {
		if (!personage.IsState(ActionHelper.Fly)) {
			personage.ChangeStates(ActionHelper.Fall, (byte) 0);
			for (int i = 0; i < this.blocks.getCount() && personage.IsState(ActionHelper.Fall); i++) {
				// TODO: Maybe need other function of correct collision detection.
				final Entity block = this.blocks.getByIndex(i);
				if (this.isUpBottomCollide(personage, block)) {
					personage.setPosition(personage.getX(), block.getY() - personage.getHeightScaled() + 3);
					personage.ChangeStates(ActionHelper.Run, ActionHelper.Fall);
				}
			}
		}
	}

	// TODO: Delete method and use history. Baby doesn't need check collision.
	private void checkCollision(Baby personage) {
		if (!personage.IsState(ActionHelper.Fly)) {
			personage.ChangeStates(ActionHelper.Fall, (byte) 0);
			for (int i = 0; i < this.blocks.getCount() && personage.IsState(ActionHelper.Fall); i++) {
				// TODO: Maybe need other function of correct collision detection.
				final Entity block = this.blocks.getByIndex(i);
				if (this.isUpBottomCollide(personage, block)) {
					personage.setPosition(personage.getX(), block.getY() - personage.getHeightScaled() + 3);
					personage.ChangeStates(ActionHelper.Run, ActionHelper.Fall);
				}
			}
		}
	}

	private boolean isUpBottomCollide(Entity upEntity, Entity downEntity) {
		final float pLeft = upEntity.getX();
		final float pRight = pLeft + upEntity.getWidthScaled();
		final float pTop = upEntity.getY();
		final float pBottom = pTop + upEntity.getHeightScaled();

		final float bLeft = downEntity.getX();
		final float bRight = bLeft + downEntity.getWidthScaled();
		final float bTop = downEntity.getY();
		final float bBottom = bTop + downEntity.getHeightScaled();

		// TODO: Some stupid code. Correct this function.
		if (!(pRight <= bLeft || bRight <= pLeft) && !(pBottom <= bTop || bBottom <= pTop)) {
			if (upEntity.getY() + upEntity.getHeightScaled() - 5 < downEntity.getY()) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		Options.cameraWidth = (int) (Options.cameraWidthOrigin / Game.camera.getZoomFactor());
		Options.cameraHeight = (int) (Options.cameraHeightOrigin / Game.camera.getZoomFactor());

		Options.cameraRatioFactor = Options.cameraHeight / Options.cameraOriginRatioY;

		this.personage.update();

		this.apt += Options.mainStep;
		gg++;

		if (this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() < Options.cameraWidth + Game.camera.getCenterX()) {
			this.generateNextBottomBlock();
		}

		this.personage.setPosition(this.personage.getX() - this.personage.getFreeX(), this.personage.getY());

		for (int i = 0; i < this.brood.getCount(); i++) {
			final Baby baby = (Baby) this.brood.getByIndex(i);
			this.checkCollision(baby);
			baby.update();

			for (ActionsList actions : this.personage.actions) {
				if (actions.apt <= baby.rx) {
					baby.currentStates = 0;
					baby.ChangeStates(actions.currentStates, (byte) 0);

					// this.personage.actions.remove(actions);
				}
			}
		}

		this.checkCollision(this.personage);

		if (this.gg % 150 == 0 && c < mc) {
			c++;

			Baby baby = (Baby) this.brood.create();

			this.personage.setPosition(baby.getWidthScaled() * c + 5 * Options.cameraRatioFactor * c, this.personage.getY());
			this.personage.rx = (int) this.personage.getX() + this.apt;

			for (int i = 0; i < this.brood.getCount() - 1; i++) {
				this.brood.getByIndex(i).setPosition(this.personage.getX() - baby.getWidthScaled() * (i + 1) - 5 * Options.cameraRatioFactor * (i + 1), this.brood.getByIndex(i).getY());
				((Baby) this.brood.getByIndex(i)).rx = (int) this.brood.getByIndex(i).getX() + this.apt;
			}

			baby.setPosition(this.personage.getX() - baby.getWidthScaled() * c - 5 * Options.cameraRatioFactor * c, this.personage.getY());
			baby.rx = (int) baby.getX() + this.apt;
		}

		// mObjectsManager.update();
	}
}
