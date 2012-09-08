package com.tooflya.bouncekid.entity;

import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;

import com.tooflya.bouncekid.helpers.ActionHelper;

public class Hero extends BaseSprite {

	public Hero(float pX, float pY, float pWidth, float pHeight,
			BaseTextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		// TODO Auto-generated constructor stub
	}

	private byte currentActions = 0;

	public void ChangeActions(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentActions = (byte) (this.currentActions | settingMaskActions
				& ~unsettingMaskActions); // And what I need to do if I don't
											// want to have operation with int?
	}

	public void Update() {
		if ((this.currentActions & ActionHelper.MoveLeft) == ActionHelper.MoveLeft) {
			// TODO: Logic of move left action.
		}
		if ((this.currentActions & ActionHelper.Jump) == ActionHelper.Jump) {
			// TODO: Logic of jump action.
		}
	}
}
