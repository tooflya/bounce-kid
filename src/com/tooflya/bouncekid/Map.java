package com.tooflya.bouncekid;

import org.anddev.andengine.entity.Entity;

import com.tooflya.bouncekid.entity.Block;
import com.tooflya.bouncekid.entity.Personage;
import com.tooflya.bouncekid.helpers.ActionHelper;

public class Map extends Entity {

	private Block tempBlock = null; // TODO: Question(Igor): Micro-optimization. To do or not to do?
	private Block lastBlock = null;

	private int count = 100; // TODO: Correct count of block on screen.
	private Block[] blocks = new Block[count]; // TODO: Correct array of block on screen.

	public Map() {
		this.GenerateStartBlocks(); // TODO: Question(Igor): Is it place for this function.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		for (int i = 0; i < this.count; i++) {
			this.blocks[i].setPosition(this.blocks[i].getX() + Options.blockStep, this.blocks[i].getY());
			if (this.blocks[i].getX() + this.blocks[i].getWidth() < 0) {
				this.blocks[i].delete();
				// TODO: Is it need to do other actions? Delete from this.blocks?
			}
		}
		if (this.lastBlock.getX() + this.lastBlock.getWidth() < Options.cameraWidth) {
			this.GenerateNextBlock();
		}
	}

	// TODO: Delete or correct function.
	private Block GetBlock() {
		return null;
	}

	private void GenerateStartBlocks() {
		// TODO: Clear all blocks.
		this.lastBlock = this.GetBlock(); // TODO: Replace for right function.
		this.lastBlock.setPosition(0, Options.cameraHeight - Options.blockHeight); // TODO: Question(Igor): Do hero start from bottom of screen or middle?
		float y = this.lastBlock.getY();
		float x = this.lastBlock.getX() + Options.blockWidth;
		while (x < Options.cameraWidth) {
			this.lastBlock = this.GetBlock(); // TODO: Replace for right function.
			this.lastBlock.setPosition(x, y);
			x += Options.blockWidth;
		}
	}

	private void GenerateNextBlock() {
		this.tempBlock = this.GetBlock(); // TODO: Replace for right function.
		float offsetX = GameActivity.random.nextFloat() * Options.maxDistanceBetweenBlocksX;
		float offsetY = GameActivity.random.nextFloat() * Options.maxDistanceBetweenBlocksY; // TODO: Add negative offsetY.
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

		return !(pRight < bLeft || bRight < pLeft) &&
				!(pBottom < bTop || bBottom < pTop);
	}

	public void CheckCollision(Personage personage) {
		if (!personage.IsState(ActionHelper.Jump)) {
			personage.ChangeStates(ActionHelper.Fall, (byte) 0);
			for (int i = 0; i < this.count && personage.IsState(ActionHelper.Fall); i++) {
				// TODO: Maybe need other function of correct collision detection.
				if (this.IsBottomCollide(personage, blocks[i])) {
					personage.setPosition(personage.getX(), this.blocks[i].getY() - personage.getHeight());
					personage.ChangeStates((byte) 0, ActionHelper.Fall);
				}
			}
		}
	}
}
