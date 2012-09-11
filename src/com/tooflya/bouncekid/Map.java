package com.tooflya.bouncekid;

import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.Constants;

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

	private TMXTiledMap mTMXTiledMap;
	private TMXLayer tmxLayer;

	public Map() {
		super();

		this.blocks = new EntityManager(100, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesBitmapTexture, GameActivity.context, "block.jpg", 0, 0, 1, 3)));
		this.stars = new EntityManager(100, new Star(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesBitmapTexture, GameActivity.context, "stars.png", 65, 0, 1, 18)));
		this.starsd = new EntityManager(100, new StarD(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GameActivity.resourcesBitmapTexture, GameActivity.context, "obj_star_disappear.png", 100, 0, 1, 11)));

		// this.GenerateStartBlocks();

		this.reset();

		GameActivity.screens.get(Screen.MAIN).attachChild(this);

		heroTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
		heroRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(heroTexture, GameActivity.context, "sprite_running.png", 0, 0, 5, 2);

		GameActivity.instance.getTextureManager().loadTextures(heroTexture);

		this.hero = new Personage(Options.startX, Options.startY, heroRegion);
		hero.setPosition(0, Options.cameraHeight - hero.getHeightScaled() - 100);
		GameActivity.camera.setBounds(0, Integer.MAX_VALUE, -Integer.MAX_VALUE, Options.cameraHeight);
		GameActivity.camera.setBoundsEnabled(true);
		GameActivity.camera.setChaseEntity(hero);

		try {
			final TMXLoader tmxLoader = new TMXLoader(GameActivity.context, GameActivity.instance.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mTMXTiledMap = tmxLoader.loadFromAsset(GameActivity.context, "levels/map.tmx");

		} catch (final TMXLoadException tmxle) {
		}

		this.tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		this.attachChild(this.tmxLayer);

		/* Now we are going to create a rectangle that will always highlight the tile below the feet of the pEntity. */
		currentTileRectangle = new Rectangle(0, 0, 64, 64);
		currentTileRectangle.setColor(1, 0, 0, 0.25f);
		this.attachChild(currentTileRectangle);

	}

	Rectangle currentTileRectangle;

	public void update() {
		/*
		 * for (int i = 0; i < this.blocks.getCount(); i++) { Entity block = this.blocks.getByIndex(i); // block.setPosition(block.getX() - Options.blockStep, block.getY()); if (block.getX() + block.getWidth() < GameActivity.camera.getCenterX() - Options.cameraCenterX) { block.delete(); } }
		 * 
		 * for (int i = 0; i < this.stars.getCount(); i++) { Entity block = this.stars.getByIndex(i); // block.setPosition(block.getX() - Options.blockStep, block.getY()); if (block.getX() + block.getWidth() < GameActivity.camera.getCenterX() - Options.cameraCenterX) { block.delete(); } }
		 * 
		 * for (int i = 0; i < this.starsd.getCount(); i++) { Entity block = this.starsd.getByIndex(i); // block.setPosition(block.getX() - Options.blockStep, block.getY()); if (block.getX() + block.getWidth() < GameActivity.camera.getCenterX() - Options.cameraCenterX) { block.delete(); } }
		 * 
		 * if (this.lastBlock.getX() + this.lastBlock.getWidth() < Options.cameraWidth + GameActivity.camera.getCenterX()) { this.GenerateNextBlock(); }
		 */

		// this.CheckCollision(hero);

		this.AI(hero);

		/* Get the scene-coordinates of the players feet. */
		final float[] playerFootCordinates = hero.convertLocalToSceneCoordinates(12, 31);

		/* Get the tile the feet of the player are currently waking on. */
		final TMXTile tmxTile = tmxLayer.getTMXTileAt(playerFootCordinates[Constants.VERTEX_INDEX_X], playerFootCordinates[Constants.VERTEX_INDEX_Y] + hero.getWidth());
		if (tmxTile != null) {
			if (!hero.IsState(ActionHelper.Jump)) {
				hero.ChangeStates(ActionHelper.Fall, (byte) 0);
				if (tmxTile.getTextureRegion() != null) {
					if (this.IsBottomCollide(hero, tmxTile)) {
						currentTileRectangle.setPosition(tmxTile.getTileX(), tmxTile.getTileY());
						hero.setPosition(hero.getX(), hero.getY()- 4f);
						hero.ChangeStates((byte) 0, ActionHelper.Fall);
					}
				}
			}

		}

		// TODO: Translate camera up or down.
	}

	private void GenerateStartBlocks() {
		// TODO: Clear all blocks.
		this.lastBlock = (Block) this.blocks.create();
		this.lastBlock.setPosition(0, Options.cameraHeight - this.lastBlock.getHeight());
		this.lastBlock.setType(1);
		this.lastBlock.setCurrentTileIndex(1);
		float y = this.lastBlock.getY();
		float x = this.lastBlock.getX() + this.lastBlock.getWidth();
		while (x < Options.cameraWidth) {
			this.lastBlock = (Block) this.blocks.create();
			this.lastBlock.setPosition(x, y);
			this.lastBlock.setType(1);
			this.lastBlock.setCurrentTileIndex(1);
			x += this.lastBlock.getWidth();
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
		this.tempBlock.setPosition(this.lastBlock.getX() + this.lastBlock.getWidth() + offsetX - Options.blockStep, this.lastBlock.getY() + offsetY);
		this.tempBlock.setType(1);
		this.tempBlock.setCurrentTileIndex(1);

		this.lastBlock = this.tempBlock;

		offsetX = GameActivity.random.nextFloat() * this.lastBlock.getWidth();
		offsetY = this.hero.getHeight() / 2 + Options.cameraHeight / 2 + this.hero.getY() - GameActivity.random.nextFloat() * Options.cameraHeight - this.lastBlock.getHeight();

		if (offsetY > Options.cameraHeight - Options.blockStep) {
			offsetY = Options.cameraHeight - Options.blockStep - 20;
		}

		this.tempBlock = (Block) this.blocks.create();
		this.tempBlock.setPosition(this.lastBlock.getX() + offsetX, offsetY);

		if (GameActivity.random.nextInt(5) == 1) {
			this.tempBlock.setType(2);
			this.tempBlock.setCurrentTileIndex(2);
		} else {
			this.tempBlock.setType(0);
			this.tempBlock.setCurrentTileIndex(0);
		}

		star.setPosition(this.lastBlock.getX() + offsetX, offsetY - 60);
	}

	/*
	 * private boolean IsBottomCollide(Personage personage, TMXTile block) { float pLeft = personage.getX(); float pRight = pLeft + personage.getWidth(); float pTop = personage.getY(); float pBottom = pTop + personage.getHeight();
	 * 
	 * float bLeft = block.getTileX(); float bRight = bLeft + block.getTileWidth(); float bTop = block.getTileY(); float bBottom = bTop + block.getTileHeight();
	 * 
	 * // TODO: Some stupid code. Correct this function. if (!(pRight <= bLeft || bRight <= pLeft) && !(pBottom <= bTop || bBottom <= pTop)) { if (personage.getY() + personage.getHeight() - 5 < block.getTileY()) { return true; } } return false; }
	 */

	private boolean IsBottomCollide(Personage personage, TMXTile block) {
		float y = personage.getY() + personage.getHeight();
		System.out.println(block.getTileY() - y );
		if (block.getTileY() - y < 2 && block.getTileY() - y > -10) {
			return true;
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
				// if (this.IsBottomCollide(personage, (Block) this.blocks.getByIndex(i))) {
				personage.setPosition(personage.getX(), this.blocks.getByIndex(i).getY() - personage.getHeight() + 1);
				personage.ChangeStates((byte) 0, ActionHelper.Fall);
				// }
			}
		}

		for (int i = 0; i < this.blocks.getCount(); i++) {
			// TODO: Maybe need other function of correct collision detection.
			// if (this.IsBottomCollide(personage, (Block) this.blocks.getByIndex(i))) {
			if (this.blocks.getByIndex(i).getType() == 2) {
				Map.hero.jumpPower = 150;
				GameActivity.map.hero.ChangeStates(ActionHelper.Jump, ActionHelper.Running);
				GameActivity.camera.shake(3, 10);
			}
			// }
		}

		for (int i = 0; i < this.stars.getCount(); i++) {
			Entity block = this.stars.getByIndex(i);

			if (block.collidesWith(personage)) {
				block.delete();

				Entity a = this.starsd.create();
				a.setPosition(block.getX() - 15, block.getY() - 15);
			}
		}
	}
}
