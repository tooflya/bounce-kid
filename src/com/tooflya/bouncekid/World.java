package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.tooflya.bouncekid.entity.Bird;
import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.entity.Star;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.EntityManager;
import com.tooflya.bouncekid.screens.Screen;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private BitmapTextureAtlas texture;

	private EntityManager blocks;
	private EntityManager stars;

	private Block lastBlock = null;
	private Block bottomBlock = null;

	// ===========================================================
	// Fields
	// ===========================================================

	public Personage personage;

	// ===========================================================
	// Constructors
	// ===========================================================

	public World() {
		super();

		Game.screens.get(Screen.MAIN).attachChild(this);

		this.texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		Game.loadTextures(texture);

		this.personage = new Personage();
		this.personage.create();

		this.blocks = new EntityManager(150, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "ground_down.png", 0, 0, 1, 1)));

		// this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "stars.png", 83, 0, 1, 18)));

		this.reInit();
	}

	public void reInit() {
		this.personage.setPosition(0, 0);
		this.blocks.clear();
		this.generateStartBlocks(0);

		// TODO: reInit messages on screen.
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void generateStartBlocks(final float startY) {
		this.bottomBlock = (Block) this.blocks.create();
		this.bottomBlock.setPosition(startY, Options.cameraHeight - this.bottomBlock.getHeightScaled());
		float x = this.bottomBlock.getX();
		float y = this.bottomBlock.getY();
		while (x < Options.cameraWidth) {
			x += this.bottomBlock.getWidthScaled();
			this.bottomBlock = (Block) this.blocks.create();
			this.bottomBlock.setPosition(x, y);
		}
	}

	private void GenerateNextBottomBlock() {
		final Block tempBlock = (Block) this.blocks.create();
		// TODO: Add some more clever code for generating various blocks.
		tempBlock.setScale(7 * Game.random.nextFloat() + 3, 1); // TODO: Magic numbers. Maximum and minimum generated width (it is 7+3 and 3 now).
		// * Start of randomization x and y of block.
		float heightMax = this.personage.getMaxFlyHeight();
		float height = heightMax * Game.random.nextFloat();
		float xMin = (heightMax - height) * this.personage.runStep / this.personage.flyStep;
		float xMax = (heightMax - height) * this.personage.runStep / this.personage.fallStep;
		float width = (xMax - xMin) * Game.random.nextFloat() + xMin;
		// * End of randomization x and y of block.
		tempBlock.setPosition(this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() + width, this.bottomBlock.getY() - height);
		this.bottomBlock = tempBlock;
	}

	private void GenerateNextRandomBlock() {
		// Random blocks.
		float offsetX = 3;
		float upY = Math.min(3, this.lastBlock.getY() - this.personage.getHeight());
		float downY = Options.cameraHeight - this.lastBlock.getY() - this.lastBlock.getHeight();
		float offsetY = 0;
		if (this.lastBlock.getY() - offsetY < this.personage.getHeight()) {
			offsetY = this.lastBlock.getY() - this.personage.getHeight();
		}

		offsetX = Game.random.nextFloat() * 82 * Options.cameraRatioFactor;
		offsetY = this.personage.getHeight() / 2 + Options.cameraHeight / 2 + this.personage.getY() - Game.random.nextFloat() * Options.cameraHeight - 82 * Options.cameraRatioFactor;

		final Block tempBlock = (Block) this.blocks.create();
		tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);
	}

	private void GenerateNextStar() {
		Star star = (Star) this.stars.create();

		float offsetX = Game.random.nextFloat() * 82 * Options.cameraRatioFactor;
		float offsetY = this.personage.getHeight() / 2 + Options.cameraHeight / 2 + this.personage.getY() - Game.random.nextFloat() * Options.cameraHeight - 82 * Options.cameraRatioFactor;

		star.setPosition(this.lastBlock.getX() + offsetX, offsetY - 80);
	}

	public void CheckCollision(Personage personage) {
		if (!personage.IsState(ActionHelper.Fly)) {
			personage.ChangeStates(ActionHelper.Fall, (byte) 0);
			for (int i = 0; i < this.blocks.getCount() && personage.IsState(ActionHelper.Fall); i++) {
				// TODO: Maybe need other function of correct collision detection.
				final Entity block = this.blocks.getByIndex(i);
				if (this.isUpBottomCollide(personage, block)) {
					personage.setPosition(personage.getX(), block.getY() - personage.getHeightScaled() + 1);
					personage.ChangeStates(ActionHelper.Run, ActionHelper.Fall);
				}
			}
		}

		// for (int i = 0; i < this.stars.getCount(); i++) {
		// Entity block = this.stars.getByIndex(i);
		//
		// if (block.collidesWith(personage)) {
		// block.destroy();
		//
		// Entity a = this.starsd.create();
		// a.setPosition(block.getX(), block.getY());
		// personage.SetFlyPower(personage.GetFlyPower() + 3); // TODO: Make a constant.
		// }
		// }
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
		this.personage.update();

		// * Start of bottom blocks logic.
		// ! Delete after add block. We can delete last block.
		if (this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() < Options.cameraWidth + Game.camera.getCenterX()) {
			this.GenerateNextBottomBlock();
		}
		for (int i = 0; i < this.blocks.getCount(); i++) {
			final Entity block = this.blocks.getByIndex(i);
			block.setPosition(block.getX() - Options.mainStep, block.getY());
			if (block.getX() + block.getWidthScaled() < Game.camera.getCenterX() - Options.cameraCenterX) {
				block.destroy();
			}
		}
		// * End of bottom blocks logic.

		// this.bird.update();

		// for (int i = 0; i < this.stars.getCount(); i++) {
		// Entity block = this.stars.getByIndex(i);
		// // block.setPosition(block.getX() - Options.blockStep, block.getY());
		// if (block.getX() + block.getWidth() < Game.camera.getCenterX() - Options.cameraCenterX) {
		// block.destroy();
		// }
		// }
		//
		// for (int i = 0; i < this.starsd.getCount(); i++) {
		// Entity block = this.starsd.getByIndex(i);
		// // block.setPosition(block.getX() - Options.blockStep, block.getY());
		// if (block.getX() + block.getWidth() < Game.camera.getCenterX() - Options.cameraCenterX) {
		// block.destroy();
		// }
		// }
		//
		// this.GenerateNextRandomBlock();
		// this.GenerateNextStar();

		this.CheckCollision(this.personage);
	}
}
