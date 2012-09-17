package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.entity.Star;
import com.tooflya.bouncekid.entity.StarD;
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
	private EntityManager starsd;

	private Block tempBlock = null;
	private Block lastBlock = null;

	// ===========================================================
	// Fields
	// ===========================================================

	public Personage personage;

	// ===========================================================
	// Constructors
	// ===========================================================

	public World() {
		super();

		this.personage = new Personage(0, Options.cameraHeight - 200);
		this.personage.create();

		Game.screens.get(Screen.MAIN).attachChild(this);

		texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.blocks = new EntityManager(150, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "ground_down.png", 0, 0, 1, 1)));
		this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "stars.png", 65, 65, 1, 18)));
		this.starsd = new EntityManager(10, new StarD(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "obj_star_disappear.png", 300, 0, 1, 11)));

		Game.loadTextures(texture);

		this.GenerateStartBlocks();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void ClearItems() {
		// TODO: Clear all blocks.
		// TODO: Clear all stars.
	}
	
	private void GenerateStartBlocks() {
		this.lastBlock = (Block) this.blocks.create();
		this.lastBlock.setPosition(0, Options.cameraHeight - this.lastBlock.getHeight());
		float x = this.lastBlock.getX();
		float y = this.lastBlock.getY();
		while (x < Options.cameraWidth) {
			x += this.lastBlock.getWidth();
			this.lastBlock = (Block) this.blocks.create();
			this.lastBlock.setPosition(x, y);
		}
	}

	private void MoveStartBloks(Block block) {
		// TODO: If block hide by left side move it to right side and do it the last. What happen if use zoome?
		block.setPosition(this.lastBlock.getX() + this.lastBlock.getWidth(), this.lastBlock.getY());
		this.lastBlock = block;
	}
	
	private void GenerateNextBlock() {
		// Bottom blocks.
		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + this.lastBlock.getWidth(), this.lastBlock.getY());
		this.lastBlock = this.tempBlock;
	}
	
	private void GenerateNextRandomBlock(){
		// Random blocks.
		float offsetX = 3;
		float upY = Math.min(3, this.lastBlock.getY() - this.personage.getHeight());
		float downY = Options.cameraHeight - this.lastBlock.getY() - this.lastBlock.getHeight();
		float offsetY = 0;
		if (this.lastBlock.getY() - offsetY < this.personage.getHeight()) {
			offsetY = this.lastBlock.getY() - this.personage.getHeight();
		}

		offsetX = Game.random.nextFloat() * 44;
		offsetY = this.personage.getHeight() / 2 + Options.cameraHeight / 2 + this.personage.getY() - Game.random.nextFloat() * Options.cameraHeight - 44;

		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);
	}
	
	
	private void GenerateNextRandomBlock_v2(){
		// Random blocks.
		float offsetX = 3;
		float upY = Math.min(3, this.lastBlock.getY() - this.personage.getHeight());
		float downY = Options.cameraHeight - this.lastBlock.getY() - this.lastBlock.getHeight();
		float offsetY = 0;
		if (this.lastBlock.getY() - offsetY < this.personage.getHeight()) {
			offsetY = this.lastBlock.getY() - this.personage.getHeight();
		}

		offsetX = Game.random.nextFloat() * 44;
		offsetY = this.personage.getHeight() / 2 + Options.cameraHeight / 2 + this.personage.getY() - Game.random.nextFloat() * Options.cameraHeight - 44;

		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);
	}

	
	
	
	
	private void GenerateNextStar() {
		Star star = (Star) this.stars.create();

		float offsetX = Game.random.nextFloat() * 44;
		float offsetY = this.personage.getHeight() / 2 + Options.cameraHeight / 2 + this.personage.getY() - Game.random.nextFloat() * Options.cameraHeight - 44;

		star.setPosition(this.lastBlock.getX() + offsetX, offsetY - 80);
	}
	

	public void CheckCollision(Personage personage) {
		if (!personage.IsState(ActionHelper.Fly)) {
			personage.ChangeStates(ActionHelper.Fall, (byte) 0);
			for (int i = 0; i < this.blocks.getCount() && personage.IsState(ActionHelper.Fall); i++) {
				// TODO: Maybe need other function of correct collision detection.
				if (this.IsBottomCollide(personage, (Block) this.blocks.getByIndex(i))) {
					personage.setPosition(personage.getX(), this.blocks.getByIndex(i).getY() - personage.getHeight() + 1);
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
			}
		}
	}

	private boolean IsBottomCollide(Personage personage, Block block) {
		float pLeft = personage.getX();
		float pRight = pLeft + personage.getWidth();
		float pTop = personage.getY();
		float pBottom = pTop + personage.getHeight();

		float bLeft = block.getX();
		float bRight = bLeft + block.getWidth();
		float bTop = block.getY();
		float bBottom = bTop + block.getHeight();

		// TODO: Some stupid code. Correct this function.
		if (!(pRight <= bLeft || bRight <= pLeft) && !(pBottom <= bTop || bBottom <= pTop)) {
			if (personage.getY() + personage.getHeight() - 5 < block.getY()) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		this.personage.update();

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

		if (this.lastBlock.getX() + this.lastBlock.getWidth() < Options.cameraWidth + Game.camera.getCenterX()) {
			this.GenerateNextBlock();
			this.GenerateNextRandomBlock();
			this.GenerateNextStar();
		}

		this.CheckCollision(this.personage);
	}
}
