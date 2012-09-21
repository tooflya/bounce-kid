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

	private EntitySimple m;

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

		this.texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		Game.loadTextures(texture);

		this.personage = new Personage();
		this.personage.create();

		this.m = new EntitySimple(BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, Game.context, "m.png", 0, 500), false);
		this.attachChild(this.m);
		this.setVisible(false);

		this.blocks = new EntityManager(50, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "ground.png", 0, 0, 1, 1)));

		this.brood = new BroodManager(4, new Baby());

		// this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "stars.png", 83, 0, 1, 18)));

		this.reInit();
	}

	public void reInit() {
		gg = 0;
		this.personage.setPosition(0, 0);
		this.personage.rx = (int) this.personage.getX();
		this.blocks.clear();
		this.generateStartBlocks(0);
		this.apt = 0;
		c = 0;

		this.brood.clear();
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

	private void generateNextBottomBlock() {
		final Block tempBlock = (Block) this.blocks.create();
		final float maxWidth = (this.personage.getFlyPower() / this.personage.flyStep + this.personage.getFlyPower() / this.personage.fallStep) * this.personage.runStep;
		final float maxHeight = this.personage.getFlyPower() * this.personage.flyStep;
		final float y = (Game.random.nextFloat() - 0.5f) * maxHeight;
		final float x = maxWidth * Game.random.nextFloat();
		tempBlock.setSize(Options.cameraWidth / 2, tempBlock.getHeight());
		tempBlock.setPosition(this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() + x, Math.min(Options.cameraHeight - tempBlock.getHeightScaled(), this.bottomBlock.getY() - y));

		// Using some percent we can find if next block sticks up or down.
		// final int maxPercent = 100;
		// final int isUpPercent = 60; // maxPercent * 2 / 3;
		// int k = 1;
		// // if (Game.random.nextInt(maxPercent) >= isUpPercent) {
		// // k = -1;
		// // }
		//
		// // Operations for up block.
		// // if(k > 0) {
		// final float bottomBlockWidthScaled = this.bottomBlock.getX() + this.bottomBlock.getWidthScaled();
		// System.out.println(bottomBlockWidthScaled);
		// final float bottomBlockWidthScaled_ = bottomBlockWidthScaled - this.personage.getWidthScaled();
		// System.out.println(bottomBlockWidthScaled_);
		// float y = this.personage.getMaxFlyHeight() * Game.random.nextFloat();
		// System.out.println(y);
		// final float flyDistance = y / this.personage.flyStep * this.personage.runStep;
		// System.out.println(flyDistance);
		// final float fallDistance = (this.personage.getMaxFlyHeight() - y) / this.personage.fallStep * this.personage.runStep;
		// System.out.println(fallDistance);
		// float leftXForRandom = Math.max(bottomBlockWidthScaled, this.bottomBlock.getX() + flyDistance);
		// System.out.println(leftXForRandom);
		// float rightXForRandom = bottomBlockWidthScaled_ + this.personage.getMaxFlyDistance() + fallDistance;
		// System.out.println("From " + rightXForRandom);
		//
		// final float minBlockWidth = 1 * this.bottomBlock.getWidth();
		// final float maxBlockWidth = 10 * this.bottomBlock.getWidth();
		// final float m_mBlockWidth = maxBlockWidth - minBlockWidth;
		//
		// float tempBlockWidth = m_mBlockWidth * Game.random.nextFloat() + minBlockWidth;
		// System.out.println(tempBlockWidth);
		//
		// float leftX = (rightXForRandom - bottomBlockWidthScaled) * Game.random.nextFloat() + bottomBlockWidthScaled;
		// System.out.println(leftX);
		// float rightX = Math.min(leftX + tempBlockWidth, leftXForRandom);
		// System.out.println(rightX);
		//
		// tempBlock.setSize((rightX - leftX) * Game.random.nextFloat() + leftX, tempBlock.getHeight());
		// System.out.println("From " + leftX + " to " + rightX);
		// tempBlock.setPosition(leftX, this.bottomBlock.getY() - k * y);
		// }
		// TODO: Add correct operations for down block.
		// End of some more clever code for generating various blocks.

		// TODO: Uncomment if don't work.
		// tempBlock.setScale(9 * Game.random.nextFloat() + 1, 1); // TODO: Magic numbers. Maximum and minimum generated width (it is 7+3 and 3 now).
		// * Start of randomization x and y of block.
		// float heightMax = this.personage.getMaxFlyHeight();
		// float height = heightMax * Game.random.nextFloat();
		// float xMin = (heightMax - height) * this.personage.runStep / this.personage.flyStep;
		// float xMax = (heightMax - height) * this.personage.runStep / this.personage.fallStep;
		// float width = (xMax - xMin) * Game.random.nextFloat() + xMin;
		// TODO: Code is wait for correction.
		// float correctY = this.bottomBlock.getY() - k * height;
		// if (correctY < 0) {
		// correctY = 0;
		// }
		// * End of randomization x and y of block.
		// tempBlock.setPosition(this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() + width, correctY);
		// TODO: End code for uncomment.

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
	public int gg = 0, c = 0, mc = 4;

	public void update() {
		Options.cameraWidth = (int) (Options.cameraWidthOrigin / Game.camera.getZoomFactor());
		Options.cameraHeight = (int) (Options.cameraHeightOrigin / Game.camera.getZoomFactor());

		Options.cameraRatioFactor = Options.cameraHeight / Options.cameraOriginRatio;

		this.m.setPosition(this.m.getX() - 1, this.m.getY());

		this.personage.update();

		this.apt += Options.mainStep;
		gg++;

		if (this.bottomBlock.getX() + this.bottomBlock.getWidthScaled() < Options.cameraWidth + Game.camera.getCenterX()) {
			this.generateNextBottomBlock();
		}
		for (int i = 0; i < this.blocks.getCount(); i++) {
			final Entity block = this.blocks.getByIndex(i);
			block.setPosition(block.getX() - Options.mainStep, block.getY());
			if (block.getX() + block.getWidthScaled() < 0) {
				block.destroy();
			}
		}

		for (int i = 0; i < this.brood.getCount(); i++) {
			final Baby baby = (Baby) this.brood.getByIndex(i);
			this.CheckCollision(baby);
			baby.update();

			for (ActionsList actions : this.personage.actions) {
				if (actions.apt <= baby.rx) {
					baby.currentStates = 0;
					baby.ChangeStates(actions.currentStates, (byte) 0);

					// this.personage.actions.remove(actions);
				}
			}
		}

		this.CheckCollision(this.personage);

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
	}
}
