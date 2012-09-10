package com.tooflya.bouncekid;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.entity.Star;
import com.tooflya.bouncekid.entity.StarD;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.EntityManager;

public class Map extends org.anddev.andengine.entity.Entity {

	private EntityManager blocks;
	private EntityManager stars;
	private EntityManager starsd;

	private Block tempBlock = null;
	private Block lastBlock = null;

	public static Personage hero;
	private BitmapTextureAtlas heroTexture;
	private TiledTextureRegion heroRegion;

	public Map() {
		super();

		this.blocks = new EntityManager(50, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesAtlas, GameActivity.context, "block.jpg", 0, 0, 1, 1)));
		this.stars = new EntityManager(50, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesAtlas, GameActivity.context, "stars.png", 65, 65, 1, 18)));
		this.starsd = new EntityManager(10, new StarD(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesAtlas, GameActivity.context, "obj_star_disappear.png", 300, 0, 1, 10)));

		this.GenerateStartBlocks();

		this.reset();

		GameActivity.screens.get(Screen.MAIN).attachChild(this);

		heroTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		heroRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(heroTexture, GameActivity.context, "sprite_running.png", 0, 0, 5, 2);

		GameActivity.instance.getTextureManager().loadTextures(heroTexture);

		this.hero = new Personage(Options.startX, Options.startY, heroRegion);
		hero.setPosition(100, Options.cameraHeight - hero.getHeightScaled() - 100);
		GameActivity.camera.setBounds(0, Options.cameraWidth * 100, -10000, 480);
		GameActivity.camera.setBoundsEnabled(true);
		GameActivity.camera.setChaseEntity(hero);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		for (int i = 0; i < this.blocks.getCount(); i++) {
			Entity block = this.blocks.getByIndex(i);
			// block.setPosition(block.getX() - Options.blockStep, block.getY());
			if (block.getX() + block.getWidth() < GameActivity.camera.getCenterX() - Options.cameraCenterX) {
				block.delete();
			}
		}

		for (int i = 0; i < this.stars.getCount(); i++) {
			Entity block = this.stars.getByIndex(i);
			// block.setPosition(block.getX() - Options.blockStep, block.getY());
			if (block.getX() + block.getWidth() < GameActivity.camera.getCenterX() - Options.cameraCenterX) {
				block.delete();
			}
		}

		for (int i = 0; i < this.starsd.getCount(); i++) {
			Entity block = this.starsd.getByIndex(i);
			// block.setPosition(block.getX() - Options.blockStep, block.getY());
			if (block.getX() + block.getWidth() < GameActivity.camera.getCenterX() - Options.cameraCenterX) {
				block.delete();
			}
		}

		if (this.lastBlock.getX() + this.lastBlock.getWidth() < Options.cameraWidth + GameActivity.camera.getCenterX()) {
			this.GenerateNextBlock();
		}

		this.CheckCollision(this.hero);

		this.AI(this.hero);

		// TODO: Translate camera up or down.
	}

	private void GenerateStartBlocks() {
		// TODO: Clear all blocks.
		this.lastBlock = (Block) this.blocks.create();
		this.lastBlock.setPosition(0, Options.cameraHeight - Options.blockHeight);
		float y = this.lastBlock.getY();
		float x = this.lastBlock.getX() + Options.blockWidth;
		while (x < Options.cameraWidth) {
			this.lastBlock = (Block) this.blocks.create();
			this.lastBlock.setPosition(x, y);
			x += Options.blockWidth;
		}
	}

	private void GenerateNextBlock() {
		this.tempBlock = (Block) this.blocks.create();
		Star star = (Star) this.stars.create();

		float offsetX = Options.maxDistanceBetweenBlocksX;
		float upY = Math.min(Options.maxDistanceBetweenBlocksY, this.lastBlock.getY() - hero.getHeight());
		float downY = Options.cameraHeight - this.lastBlock.getY() - this.lastBlock.getHeight();
		float offsetY = 0;
		if (this.lastBlock.getY() - offsetY < hero.getHeight()) {
			offsetY = this.lastBlock.getY() - hero.getHeight();
		}
		this.tempBlock.setPosition(this.lastBlock.getX() + Options.blockWidth + offsetX - Options.blockStep, this.lastBlock.getY() + offsetY);
		this.lastBlock = this.tempBlock;

		offsetX = GameActivity.random.nextFloat() * Options.blockWidth;
		offsetY = this.hero.getHeight() / 2 + Options.cameraHeight / 2 + this.hero.getY() - GameActivity.random.nextFloat() * Options.cameraHeight - Options.blockHeight;

		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);

		star.setPosition(this.lastBlock.getX() + offsetX, offsetY - 80);
		// star.mo();

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

	private void AI(Personage personage) {
		for (int i = 0; i < this.blocks.getCount() && personage.IsState(ActionHelper.Fall); i++) {
			// TODO: AI: If personage can land at block that situated upper then personage should jump.
		}
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

		for (int i = 0; i < this.stars.getCount(); i++) {
			Entity block = this.stars.getByIndex(i);

			if (block.collidesWith(personage)) {
				block.delete();

				Entity a = this.starsd.create();
				a.setPosition(block.getX(), block.getY());
			}
		}
	}
}
