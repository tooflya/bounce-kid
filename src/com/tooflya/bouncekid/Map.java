package com.tooflya.bouncekid;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.helpers.ActionHelper;
import com.tooflya.bouncekid.managers.BlocksManager;

public class Map extends Entity {

	private BlocksManager blocks;

	private Block tempBlock = null;
	private Block lastBlock = null;

	public static Personage hero;
	private BitmapTextureAtlas heroTexture;
	private TiledTextureRegion heroRegion;

	public Map() {
		super();

		this.blocks = new BlocksManager(50, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesAtlas, GameActivity.context, "block.png", 0, 0, 1, 1)));

		this.GenerateStartBlocks();

		this.reset();

		GameActivity.screens.get(Screen.MAIN).attachChild(this);

		heroTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		heroRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(heroTexture, GameActivity.context, "sprite_running.png", 0, 0, 5, 2);

		GameActivity.instance.getTextureManager().loadTextures(heroTexture);

		hero = new Personage(0, 0, heroRegion);
		hero.setPosition(0, Options.cameraHeight - hero.getHeightScaled() - 100);
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
			this.blocks.getByIndex(i).setPosition(this.blocks.getByIndex(i).getX() - Options.blockStep, this.blocks.getByIndex(i).getY());
			if (this.blocks.getByIndex(i).getX() + this.blocks.getByIndex(i).getWidth() < 0) {
				this.blocks.delete(i);
				// TODO: Is it need to do other actions? Delete from this.blocks?
			}
		}
		if (this.lastBlock.getX() + this.lastBlock.getWidth() < Options.cameraWidth) {
			this.GenerateNextBlock();
		}
		this.CheckCollision(hero);
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
		float offsetX = GameActivity.random.nextFloat() * Options.maxDistanceBetweenBlocksX;
		float upY = Math.min(Options.maxDistanceBetweenBlocksY, this.lastBlock.getY() - hero.getHeight());
		float downY = Options.cameraHeight - this.lastBlock.getY() - this.lastBlock.getHeight();
		float offsetY = GameActivity.random.nextFloat() * (upY + downY) - upY;
		if (this.lastBlock.getY() - offsetY < hero.getHeight()) {
			offsetY = this.lastBlock.getY() - hero.getHeight();
		}
		this.tempBlock.setPosition(this.lastBlock.getX() + this.lastBlock.getHeight() + offsetX, this.lastBlock.getY() + offsetY);
		this.lastBlock = this.tempBlock;
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
}
