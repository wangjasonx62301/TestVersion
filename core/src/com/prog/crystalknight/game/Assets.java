package com.prog.crystalknight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.prog.crystalknight.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets implements Disposable, AssetErrorListener{
	
	public static final String TAG = Assets.class.getName();
	
	// objects calling
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	// prevent instantiation from  other class
	private Assets() {
		
	}
	
	// object 
	public AssetKnight knight;
	public AssetRock rock;
	public AssetCoin crystalCoin;
	public AssetLevelDecoration levelDecoration;
	
	// Font
	public AssetFonts fonts;
	
	public class AssetFonts{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts() {
			// create font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/BebasNeue.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/BebasNeue.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/BebasNeue.fnt"), true);
			
			// size
			defaultSmall.getData().setScale(0.5f);
			defaultNormal.getData().setScale(0.85f);
			defaultBig.getData().setScale(2.0f);
			
			// set filter
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		}
		
	}
	
	
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		
		// set manager error handler
		assetManager.setErrorListener(this);
		
		// loading atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		
		// start loading and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets load : " + assetManager.getAssetNames().size);
		
		for(String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset : " + a);
		}
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		// enable filtering for pixel smoothing
		for(Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		// create objects
		
		// fonts
		fonts = new AssetFonts();
		
		// objects
		knight = new AssetKnight(atlas);
		rock = new AssetRock(atlas);
		crystalCoin = new AssetCoin(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		
	}
	
	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}
	
	// debugging
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't loadd asset '" + asset.fileName + "'", (Exception)throwable);
	}

	// All objects
	
	public class AssetKnight{
		public final AtlasRegion knight;
		
		public final Animation<TextureRegion> anim_idle;
		public final Animation<TextureRegion> anim_walk;
		public final Animation<TextureRegion> dead_anim;
		public final Animation<TextureRegion> anim_attack;
		
		public AssetKnight(TextureAtlas atlas) {
			knight = atlas.findRegion("knight");
			
			Array<AtlasRegion> regions = null;
			AtlasRegion region = null;
			
			// idle
			regions = atlas.findRegions("idle");
			anim_idle = new Animation<TextureRegion>(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
		
			// walking
			regions = atlas.findRegions("anim_walk");
			anim_walk = new Animation<TextureRegion>(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
			
			// attack
			regions = atlas.findRegions("anim_attack");
			anim_attack =  new Animation<TextureRegion>(0.3f / 10.f, regions, Animation.PlayMode.NORMAL);
			
			// dead
			regions = atlas.findRegions("dead_anim");
			dead_anim = new Animation<TextureRegion>(0.185f / 10.f, regions, Animation.PlayMode.NORMAL);
			
		}
	}

	public class AssetRock{
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		public AssetRock(TextureAtlas atlas) {
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}
	
	public class AssetCoin{
		public final AtlasRegion crystalCoin;
		public final Animation<TextureRegion> anim_coin;
		
		public AssetCoin(TextureAtlas atlas) {
			crystalCoin = atlas.findRegion("coin");
			
			// animation
			Array<AtlasRegion> regions = atlas.findRegions("anim_coin");
			AtlasRegion region = regions.first();
			for(int i = 0; i < 10; i++) {
				regions.insert(0, region);
			}
			anim_coin = new Animation<TextureRegion>(1.0f / 20.0f, regions, Animation.PlayMode.LOOP);
		}
	}
	
	public class AssetLevelDecoration{
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion cloud04;
		public final AtlasRegion mountain01;
		public final AtlasRegion mountain02;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration(TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			cloud04 = atlas.findRegion("cloud04");
			mountain01 = atlas.findRegion("Wasteland_Mountains01");
			mountain02 = atlas.findRegion("Wasteland_Mountains02");
			waterOverlay = atlas.findRegion("waterOverFlow");
		}
	}
	
}
