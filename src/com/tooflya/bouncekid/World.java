package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.EntityManager;
import com.tooflya.bouncekid.screens.Screen;

public class World extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private BitmapTextureAtlas texture;

	private EntityManager blocks;

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

		this.personage = new Personage(0, Options.cameraHeight - 120);
		this.personage.create();

		Game.screens.get(Screen.MAIN).attachChild(this);

		texture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.blocks = new EntityManager(50, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.getContext(), "ground_down.png", 0, 0, 1, 1)));

		Game.loadTextures(texture);

		this.GenerateStartBlocks();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void GenerateStartBlocks() {
		// TODO: Clear all blocks.
		this.lastBlock = (Block) this.blocks.create();
		this.lastBlock.setPosition(0, Options.cameraHeight - 44);
		float y = this.lastBlock.getY();
		float x = this.lastBlock.getX() + 44;
		while (x < Options.cameraWidth) {
			this.lastBlock = (Block) this.blocks.create();
			this.lastBlock.setPosition(x, y);
			x += 44;
		}
	}

	private void GenerateNextBlock() {
		this.tempBlock = (Block) this.blocks.create();
		// Star star = (Star) this.stars.create();

		float offsetX = 3;
		float upY = Math.min(3, this.lastBlock.getY() - this.personage.getHeight());
		float downY = Options.cameraHeight - this.lastBlock.getY() - this.lastBlock.getHeight();
		float offsetY = 0;
		if (this.lastBlock.getY() - offsetY < this.personage.getHeight()) {
			offsetY = this.lastBlock.getY() - this.personage.getHeight();
		}
		this.tempBlock.setPosition(this.lastBlock.getX() + 44 + offsetX - 3, this.lastBlock.getY() + offsetY);
		this.lastBlock = this.tempBlock;

		offsetX = Game.random.nextFloat() * 44;
		offsetY = this.personage.getHeight() / 2 + Options.cameraHeight / 2 + this.personage.getY() - Game.random.nextFloat() * Options.cameraHeight - 44;

		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);

		//star.setPosition(this.lastBlock.getX() + offsetX, offsetY - 80);
	}

	public void CheckCollision(Personage personage) {
		if (!personage.IsState(ActionHelper.Jump)) {
			personage.ChangeStates(ActionHelper.Fall, (byte) 0);
			for (int i = 0; i < this.blocks.getCount() && personage.IsState(ActionHelper.Fall); i++) {
				// TODO: Maybe need other function of correct collision detection.
				if (this.IsBottomCollide(personage, (Block) this.blocks.getByIndex(i))) {
					personage.setPosition(personage.getX(), this.blocks.getByIndex(i).getY() - personage.getHeight() + 1);
					personage.ChangeStates((byte) 0, ActionHelper.Fall);
				}
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
			if (block.getX() + block.getWidth() < Game.getCamera().getCenterX() - Options.cameraCenterX) {
				block.destroy();
			}
		}

		if (this.lastBlock.getX() + this.lastBlock.getWidth() < Options.cameraWidth) {
			this.GenerateNextBlock();
			System.out.println("GENERATE NEW BLOCK!!!");
		}

		this.CheckCollision(this.personage);
	}
}
