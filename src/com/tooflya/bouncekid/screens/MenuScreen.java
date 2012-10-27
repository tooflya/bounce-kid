package com.tooflya.bouncekid.screens;

import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.entity.Entity;
import com.tooflya.bouncekid.entity.EntitySimple;

/**
 * @author Tooflya.com
 * @since
 */
public class MenuScreen extends Screen {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final float ICONS_SIZE = 64 * Options.CAMERA_RATIO_FACTOR;
	private static final float ICONS_PADDING = 16 * Options.CAMERA_RATIO_FACTOR;
	private static final float ICONS_PADDING_BETWEEN = 8 * Options.CAMERA_RATIO_FACTOR;

	private final static BitmapTextureAtlas mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	private final static EntitySimple mTwitterIcon = new EntitySimple(BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, Game.context, "twitter-icon.png", 0, 580)) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
		 */
		@Override
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch (pAreaTouchEvent.getAction()) {
			case TouchEvent.ACTION_UP:
				break;
			}

			return super.onAreaTouched(pAreaTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	};

	private final static EntitySimple mFacebookIcon = new EntitySimple(BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, Game.context, "facebook-icon.png", 64, 580)) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
		 */
		@Override
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch (pAreaTouchEvent.getAction()) {
			case TouchEvent.ACTION_UP:
				break;
			}

			return super.onAreaTouched(pAreaTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	};

	private final static Entity mMusicIcon = new Entity(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, "music-icon.png", 128, 580, 3, 1), false) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
		 */
		@Override
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch (pAreaTouchEvent.getAction()) {

			case TouchEvent.ACTION_DOWN:
				this.setCurrentTileIndex(1);
				break;

			case TouchEvent.ACTION_UP:
				Options.NEED_MUSIC = !Options.NEED_MUSIC;

				if (Options.NEED_MUSIC) {
					this.setCurrentTileIndex(0);
				} else {
					this.setCurrentTileIndex(2);
				}
				break;
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.RectangularShape#contains(float, float)
		 */
		@Override
		public boolean contains(float pX, float pY) {
			if (!super.contains(pX, pY)) {
				if (Options.NEED_MUSIC) {
					this.setCurrentTileIndex(0);
				} else {
					this.setCurrentTileIndex(2);
				}

				return false;
			}

			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tooflya.bouncekid.entity.Entity#create()
		 */
		@Override
		public Entity create() {

			if (Options.NEED_MUSIC) {
				this.setCurrentTileIndex(0);
			} else {
				this.setCurrentTileIndex(2);
			}

			return super.create();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
		 */
		@Override
		public Entity deepCopy() {
			return null;
		}
	};

	private final static Entity mSoundIcon = new Entity(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, "sound-icon.png", 320, 580, 3, 1), false) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
		 */
		@Override
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch (pAreaTouchEvent.getAction()) {

			case TouchEvent.ACTION_DOWN:
				this.setCurrentTileIndex(1);
				break;

			case TouchEvent.ACTION_UP:
				Options.NEED_SOUND = !Options.NEED_SOUND;

				if (Options.NEED_SOUND) {
					this.setCurrentTileIndex(0);
				} else {
					this.setCurrentTileIndex(2);
				}
				break;
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.RectangularShape#contains(float, float)
		 */
		@Override
		public boolean contains(float pX, float pY) {
			if (!super.contains(pX, pY)) {
				if (Options.NEED_SOUND) {
					this.setCurrentTileIndex(0);
				} else {
					this.setCurrentTileIndex(2);
				}

				return false;
			}

			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tooflya.bouncekid.entity.Entity#create()
		 */
		@Override
		public Entity create() {

			if (Options.NEED_SOUND) {
				this.setCurrentTileIndex(0);
			} else {
				this.setCurrentTileIndex(2);
			}

			return super.create();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
		 */
		@Override
		public Entity deepCopy() {
			return null;
		}
	};

	private final static Entity mShareIcon = new Entity(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, "share-icon.png", 512, 580, 2, 1), false) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
		 */
		@Override
		public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			switch (pAreaTouchEvent.getAction()) {

			case TouchEvent.ACTION_DOWN:
				this.setCurrentTileIndex(1);
				break;

			case TouchEvent.ACTION_UP:
				this.setCurrentTileIndex(0);
				break;
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.anddev.andengine.entity.shape.RectangularShape#contains(float, float)
		 */
		@Override
		public boolean contains(float pX, float pY) {
			if (!super.contains(pX, pY)) {
				this.setCurrentTileIndex(0);

				return false;
			}

			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
		 */
		@Override
		public Entity deepCopy() {
			return null;
		}
	};

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuScreen() {
		Game.loadTextures(mBackgroundTextureAtlas);

		this.setBackground(new SpriteBackground(new EntitySimple(BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas, Game.context, "menu.png", 0, 0))));

		mTwitterIcon.setPosition(0 + ICONS_PADDING, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);
		mFacebookIcon.setPosition(0 + ICONS_PADDING + ICONS_PADDING_BETWEEN + ICONS_SIZE, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);

		mMusicIcon.create().setPosition(Options.cameraWidth - ICONS_PADDING * 3 - ICONS_PADDING_BETWEEN * 3 - ICONS_SIZE * 3, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);
		mSoundIcon.create().setPosition(Options.cameraWidth - ICONS_PADDING * 2 - ICONS_PADDING_BETWEEN * 2 - ICONS_SIZE * 2, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);
		mShareIcon.create().setPosition(Options.cameraWidth - ICONS_PADDING - ICONS_PADDING_BETWEEN - ICONS_SIZE, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);

		this.attachChild(mTwitterIcon);
		this.attachChild(mFacebookIcon);
		this.attachChild(mMusicIcon);
		this.attachChild(mSoundIcon);
		this.attachChild(mShareIcon);

		this.registerTouchArea(mTwitterIcon);
		this.registerTouchArea(mFacebookIcon);
		this.registerTouchArea(mMusicIcon);
		this.registerTouchArea(mSoundIcon);
		this.registerTouchArea(mShareIcon);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onDetached() {
		super.onDetached();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onBackPressed()
	 */
	@Override
	public boolean onBackPressed() {
		Game.close();
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

}