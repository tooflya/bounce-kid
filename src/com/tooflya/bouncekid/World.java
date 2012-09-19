package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.tooflya.bouncekid.entity.Baby;
import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.entity.Personage.ActionsList;
import com.tooflya.bouncekid.entity.Star;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.BroodManager;
import com.tooflya.bouncekid.managers.EntityManager;
import com.tooflya.bouncekid.screens.Screen;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private BitmapTextureAtlas texture;

	private EntityManager blocks;
	private EntityManager stars;
	private BroodManager brood;

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

		//this.personage = new Personage();
		//this.personage.create();

		//this.blocks = new EntityManager(50, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "ground.png", 0, 0, 1, 1)));

		//this.brood = new BroodManager(5, new Baby());
		for (int i = 0; i < 5; i++) {
			//brood.create();
		}

		// this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "stars.png", 83, 0, 1, 18)));

		//this.reInit();
	}

	public void reInit() {
		this.personage.setPosition(350 * Options.cameraRatioFactor, 0);
		this.personage.rx = (int) this.personage.getX();
		this.blocks.clear();
		this.generateStartBlocks(0);
		this.apt = 0;
		brood.getByIndex(0).setPosition(350 * Options.cameraRatioFactor - brood.getByIndex(0).getWidthScaled() - 5 * Options.cameraRatioFactor, 0);
		((Baby) brood.getByIndex(0)).rx = (int) ((Baby) brood.getByIndex(0)).getX();
		brood.getByIndex(1).setPosition(350 * Options.cameraRatioFactor - brood.getByIndex(1).getWidthScaled() * 2 - 5 * Options.cameraRatioFactor * 2, 0);
		((Baby) brood.getByIndex(1)).rx = (int) ((Baby) brood.getByIndex(1)).getX();
		brood.getByIndex(2).setPosition(350 * Options.cameraRatioFactor - brood.getByIndex(2).getWidthScaled() * 3 - 5 * Options.cameraRatioFactor * 3, 0);
		((Baby) brood.getByIndex(2)).rx = (int) ((Baby) brood.getByIndex(2)).getX();
		brood.getByIndex(3).setPosition(350 * Options.cameraRatioFactor - brood.getByIndex(3).getWidthScaled() * 4 - 5 * Options.cameraRatioFactor * 4, 0);
		((Baby) brood.getByIndex(3)).rx = (int) ((Baby) brood.getByIndex(3)).getX();
		brood.getByIndex(4).setPosition(350 * Options.cameraRatioFactor - brood.getByIndex(4).getWidthScaled() * 5 - 5 * Options.cameraRatioFactor * 5, 0);
		((Baby) brood.getByIndex(4)).rx = (int) ((Baby) brood.getByIndex(4)).getX();
		this.personage.actions.clear();
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
		// Using some percent we can find if next block sticks up or down.
		final int maxPercent = 100;
		final int isUpPercent = maxPercent * 2 / 3;
		int k = 1;
		if (Game.random.nextInt(maxPercent) >= isUpPercent) {
			k = -1;
		}
		
		// Operation for up block.
		float y = this.personage.getMaxFlyHeight() * Game.random.nextFloat();
		float leftX = this.lastBlock.getX() + y / this.personage.flyStep * this.personage.runStep;
		float rightX = 
				this.lastBlock.getX() + this.lastBlock.getWidthScaled() - this.personage.getWidthScaled() + 
				this.personage.getMaxFlyHeight() / this.personage.flyStep * this.personage.runStep + 
				(this.personage.getMaxFlyHeight() - y) / this.personage.fallStep * this.personage.runStep;
		
		// TODO: Add some more clever code for generating various blocks.
		tempBlock.setScale(9 * Game.random.nextFloat() + 1, 1); // TODO: Magic numbers. Maximum and minimum generated width (it is 7+3 and 3 now).
		// * Start of randomization x and y of block.
		// TODO: Add constants.
		float heightMax = this.personage.getMaxFlyHeight();
		float height = heightMax * Game.random.nextFloat();
		float xMin = (heightMax - height) * this.personage.runStep / this.personage.flyStep;
		float xMax = (heightMax - height) * this.personage.runStep / this.personage.fallStep;
		float width = (xMax - xMin) * Game.random.nextFloat() + xMin;

		// TODO: Code is wait for correction.
		float correctY = this.bottomBlock.getY() - k * height;
		if (correctY < 0) {
			correctY = 0;
		}
		// * End of randomization x and y of block.
		tempBlock.setPosition(this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() + width, correctY);
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
	}

	public void CheckCollision(Baby personage) {
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

	public int apt = 0;

	public void update() {
		Options.cameraWidth = (int) (Options.cameraWidthOrigin / Game.camera.getZoomFactor());
		Options.cameraHeight = (int) (Options.cameraHeightOrigin / Game.camera.getZoomFactor());

		Options.cameraRatioFactor = Options.cameraHeight / Options.cameraOriginRatio;

		//this.personage.update();

//		this.apt += Options.mainStep;
//
//		if (this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() < Options.cameraWidth + Game.camera.getCenterX()) {
//			this.GenerateNextBottomBlock();
//		}
//		for (int i = 0; i < this.blocks.getCount(); i++) {
//			final Entity block = this.blocks.getByIndex(i);
//			block.setPosition(block.getX() - Options.mainStep, block.getY());
//			if (block.getX() + block.getWidthScaled() < 0) {
//				block.destroy();
//			}
//		}
//
//		for (int i = 0; i < this.brood.getCount(); i++) {
//			final Baby baby = (Baby) this.brood.getByIndex(i);
//			this.CheckCollision(baby);
//			baby.update();
//
//			for (ActionsList actions : this.personage.actions) {
//				if (actions.apt <= baby.rx) {
//					baby.currentStates = 0;
//					baby.ChangeStates(actions.currentStates, (byte) 0);
//
//					// this.personage.actions.remove(actions);
//				}
//			}
//		}
//
//		this.CheckCollision(this.personage);
	}
}
