package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.tooflya.bouncekid.entity.Bird;
import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.entity.Star;
import com.tooflya.bouncekid.entity.StarD;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.EntityManager;
import com.tooflya.bouncekid.screens.MainScreen;
import com.tooflya.bouncekid.screens.Screen;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private BitmapTextureAtlas texture;

	private EntityManager blocks;
	private EntityManager stars;
	private EntityManager starsd;

	private Block tempBlock = null;
	private Block lastBlock = null;

	// ===========================================================
	// Fields
	// ===========================================================

	public Personage personage;
	private Bird bird;

	// ===========================================================
	// Constructors
	// ===========================================================

	public World() {
		super();

		this.personage = new Personage(0, Options.cameraHeight - 200);
		this.personage.create();

		this.bird = new Bird(0, Options.cameraHeight - 200);
		MainScreen.hud.attachChild(bird);
		this.bird.create();

		Game.screens.get(Screen.MAIN).attachChild(this);

		texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.blocks = new EntityManager(150, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "ground_down.png", 0, 0, 1, 1)));
		this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "stars.png", 83, 0, 1, 18)));
		this.starsd = new EntityManager(10, new StarD(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "obj_star_disappear.png", 140, 0, 1, 11)));

		Game.loadTextures(texture);

		this.GenerateStartBlocks();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void GenerateStartBlocks() {
		// TODO: Clear all blocks.
		this.lastBlock = (Block) this.blocks.create();
		this.lastBlock.setPosition(0, Options.cameraHeight - this.lastBlock.getHeightScaled());
		float x = this.lastBlock.getX();
		float y = this.lastBlock.getY();
		while (x < Options.cameraWidth) {
			x += this.lastBlock.getWidthScaled();
			this.lastBlock = (Block) this.blocks.create();
			this.lastBlock.setPosition(x, y);
		}
	}

	private void GenerateNextBlock() {
		// Bottom blocks.
		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + this.lastBlock.getWidthScaled(), this.lastBlock.getY());
		this.lastBlock = this.tempBlock;
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

		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);
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
				if (this.IsBottomCollide(personage, (Block) this.blocks.getByIndex(i))) {
					personage.setPosition(personage.getX(), this.blocks.getByIndex(i).getY() - personage.getHeightScaled() + 1);
					personage.ChangeStates(ActionHelper.Run, ActionHelper.Fall);
				}
			}
		}

		for (int i = 0; i < this.stars.getCount(); i++) {
			Entity block = this.stars.getByIndex(i);

			if (block.collidesWith(personage)) {
				block.destroy();

				Entity a = this.starsd.create();
				a.setPosition(block.getX(), block.getY());
				personage.flyPower += 3;
			}
		}
	}

	private boolean IsBottomCollide(Personage personage, Block block) {
		float pLeft = personage.getX();
		float pRight = pLeft + personage.getWidthScaled();
		float pTop = personage.getY();
		float pBottom = pTop + personage.getHeightScaled();

		float bLeft = block.getX();
		float bRight = bLeft + block.getWidthScaled();
		float bTop = block.getY();
		float bBottom = bTop + block.getHeightScaled();

		// TODO: Some stupid code. Correct this function.
		if (!(pRight <= bLeft || bRight <= pLeft) && !(pBottom <= bTop || bBottom <= pTop)) {
			if (personage.getY() + personage.getHeightScaled() - 5 < block.getY()) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		this.personage.update();
		this.bird.update();

		for (int i = 0; i < this.blocks.getCount(); i++) {
			Entity block = this.blocks.getByIndex(i);
			if (block.getX() + block.getWidth() < Game.camera.getCenterX() - Options.cameraCenterX) {
				block.destroy();
			}
		}

		for (int i = 0; i < this.stars.getCount(); i++) {
			Entity block = this.stars.getByIndex(i);
			// block.setPosition(block.getX() - Options.blockStep, block.getY());
			if (block.getX() + block.getWidth() < Game.camera.getCenterX() - Options.cameraCenterX) {
				block.destroy();
			}
		}

		for (int i = 0; i < this.starsd.getCount(); i++) {
			Entity block = this.starsd.getByIndex(i);
			// block.setPosition(block.getX() - Options.blockStep, block.getY());
			if (block.getX() + block.getWidth() < Game.camera.getCenterX() - Options.cameraCenterX) {
				block.destroy();
			}
		}

		if (this.lastBlock.getX() + this.lastBlock.getWidthScaled() < Options.cameraWidth + Game.camera.getCenterX()) {
			this.GenerateNextBlock();
			this.GenerateNextRandomBlock();
			this.GenerateNextStar();
		}

		this.CheckCollision(this.personage);
	}
}
