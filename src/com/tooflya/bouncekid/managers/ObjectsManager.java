package com.tooflya.bouncekid.managers;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.screens.MainScreen;
import com.tooflya.bouncekid.screens.Screen;

/**
 * @author Tooflya.com
 * @since
 */
public class ObjectsManager {

	// ===========================================================
	// Constants
	// ===========================================================

	private final static BitmapTextureAtlas mObjectsTexture = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	private static EntityManager[] treesPool;
	private static EntityManager reeds;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Virtual methods
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public ObjectsManager() {
		Game.loadTextures(mObjectsTexture);

		treesPool = new EntityManager[] {
				new EntityManager(5, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mObjectsTexture, Game.context, "tree_one.png", 0, 0, 1, 1))),
				new EntityManager(5, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mObjectsTexture, Game.context, "tree_tri.png", 305, 0, 1, 1))),
				new EntityManager(5, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mObjectsTexture, Game.context, "tree_two.png", 600, 0, 1, 1)))
		};

		Game.screens.get(Screen.MAIN).attachChild(MainScreen.autoParallaxBackground2); // LOL!!!

		reeds = new EntityManager(5, new Block(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mObjectsTexture, Game.context, "reed.png", 0, 300, 1, 1)));
	}

	public void decorate() {

	}

	public void decorate(final Block block) {
		final Entity reed = reeds.create();
		if (reed != null)
			reed.setPosition(block.getX() + Game.random.nextInt((int) block.getWidthScaled()), block.getY() - reed.getHeightScaled() + 5);
	}

	public void decorate(final Personage presonage) {

	}

	// ===========================================================
	// Events
	// ===========================================================

	public void update() {
		this.updateTrees();
		this.updateReeds();

		this.generateTree();
	}

	// ===========================================================
	// Generators
	// ===========================================================

	private int minTreeDistance = 100;
	private int curTreeDistance = 0;

	private void generateTree() {
		curTreeDistance++;

		final int chance = Game.random.nextInt(100);

		if (chance > 44 && chance < 60 && curTreeDistance > minTreeDistance) {
			curTreeDistance = 0;

			final Entity tree = treesPool[Game.random.nextInt(2)].create();
			if (tree != null) {
				tree.setPosition(Options.cameraWidth + tree.getWidthScaled(), Options.cameraHeight - (190 * Options.cameraRatioFactor) - tree.getHeightScaled());
				if (Game.random.nextInt(2) == 1) {
					tree.getTextureRegion().setFlippedHorizontal(true);
				} else {
					tree.getTextureRegion().setFlippedHorizontal(false);
				}
			}
		}
	}

	// ===========================================================
	// Updates
	// ===========================================================

	private void updateTrees() {
		for (int i = 0; i < treesPool.length; i++) {
			for (int j = 0; j < treesPool[i].getCount(); j++) {
				final Entity block = treesPool[i].getByIndex(j);
				block.setPosition(block.getX() - 2f, block.getY());
				if (block.getX() + block.getWidthScaled() < 0) {
					block.destroy();
				}
			}
		}
	}

	private void updateReeds() {
		for (int j = 0; j < reeds.getCount(); j++) {
			final Entity block = reeds.getByIndex(j);
			block.setPosition(block.getX() - Options.mainStep, block.getY());
			if (block.getX() + block.getWidthScaled() < 0) {
				block.destroy();
			}
		}
	}
}
