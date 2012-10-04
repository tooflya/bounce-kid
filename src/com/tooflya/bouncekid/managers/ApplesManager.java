package com.tooflya.bouncekid.managers;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Apple;
import com.tooflya.bouncekid.entity.Entity;

public class ApplesManager extends EntityManager {

	private static final int PATTERNS_COUNT = 3;
	private static final float PADDING_BOTTOM = 50 * Options.cameraRatioFactor;

	public ApplesManager(int capacity, Entity element) {
		super(capacity, element);
	}

	public void generate(final Entity block) {
		switch (Game.random.nextInt(PATTERNS_COUNT + 1)) {
		case 1:
			this.generatePattern1(block);
			break;
		case 2:
			this.generatePattern2(block);
			break;
		case 3:
			this.generatePattern3(block);
			break;
		}
	}

	private void generatePattern1(final Entity block) {
		final float random = block.getX() + Game.random.nextInt((int) (block.getWidthScaled() - Apple.WIDTH * 3));
		final int tile = Game.random.nextInt(3);
		float x = 0, y = 1;
		for (int i = 1; i < 5; i++) {
			final Apple apple = ((Apple) this.create());
			// apple.setCurrentTileIndex(tile);

			if (i > 2) {
				x = apple.getHeightScaled();
			}
			if (i % 2 == 0) {
				y = 2;
			} else {
				y = 1;
			}

			apple.setPosition(random + x, block.getY() - (Apple.WIDTH * y) - Apple.WIDTH - PADDING_BOTTOM);
		}
	}

	private void generatePattern2(final Entity block) {
		final float random = block.getX() + Game.random.nextInt((int) (block.getWidthScaled() - Apple.WIDTH * 6));
		final int tile = Game.random.nextInt(3);
		for (int i = 1; i < 6; i++) {
			final Apple apple = ((Apple) this.create());
			// apple.setCurrentTileIndex(tile);

			apple.setPosition(random + (Apple.WIDTH * i), block.getY() - Apple.WIDTH * 2 - PADDING_BOTTOM);
		}
	}

	private void generatePattern3(final Entity block) {
		final float random = block.getX() + Game.random.nextInt((int) (block.getWidthScaled() - Apple.WIDTH * 6));
		final int tile = Game.random.nextInt(3);
		for (int i = 1; i < 6; i++) {
			final Apple apple = ((Apple) this.create());
			// apple.setCurrentTileIndex(tile);

			apple.setPosition(random + (Apple.WIDTH * i), block.getY() - (Apple.WIDTH / 2 * i) - Apple.WIDTH - PADDING_BOTTOM);
		}
	}

}
