package com.tooflya.bouncekid.managers;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Berry;
import com.tooflya.bouncekid.entity.Entity;

public class ApplesManager extends EntityManager {

	private static final int PATTERNS_COUNT = 3;
	private static final float PADDING_BOTTOM = 50 * Options.CAMERA_RATIO_FACTOR;

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
		final float random = block.getX() + Game.random.nextInt((int) (block.getWidthScaled() - 10 * 3));
		float x = 0, y = 1;
		for (int i = 1; i < 5; i++) {
			final Berry apple = ((Berry) this.create());

			if (i > 2) {
				x = apple.getHeightScaled();
			}
			if (i % 2 == 0) {
				y = 2;
			} else {
				y = 1;
			}

			apple.setPosition(random + x, block.getY() - (apple.getHeightScaled() * y) - apple.getWidthScaled() - PADDING_BOTTOM);
		}
	}

	private void generatePattern2(final Entity block) {
		final float random = block.getX() + Game.random.nextInt((int) (block.getWidthScaled() - 10 * 6));
		for (int i = 1; i < 6; i++) {
			final Berry apple = ((Berry) this.create());

			apple.setPosition(random + (apple.getWidthScaled() * i), block.getY() - apple.getWidthScaled() * 2 - PADDING_BOTTOM);
		}
	}

	private void generatePattern3(final Entity block) {
		final float random = block.getX() + Game.random.nextInt((int) (block.getWidthScaled() - 10 * 6));
		for (int i = 1; i < 6; i++) {
			final Berry apple = ((Berry) this.create());

			apple.setPosition(random + (apple.getWidthScaled() * i), block.getY() - (apple.getWidthScaled() / 2 * i) - apple.getWidthScaled() - PADDING_BOTTOM);
		}
	}

}