package com.prog.crystalknight.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.prog.crystalknight.game.Assets;
import com.badlogic.gdx.math.MathUtils;

public class CrystalCoin extends AbstractGameObject{
	
	private TextureRegion regCrystalCoin;
	
	public boolean collected;
	
	public CrystalCoin() {
		init();
	}
	
	private void init() {
		dimension.set(0.5f, 0.5f);
		
		setAnimation(Assets.instance.crystalCoin.anim_coin);
		
		
		// set bounds when touching
		bounds.set(0, 0, dimension.x, dimension.y);
		
		// while touching, crystal disappear
		collected = false;
	}
	
	public void render(SpriteBatch batch) {
		// while collecting, crystal disappear
		if(collected) {
			return;
		}
		
		TextureRegion reg = null;
		reg = animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(), position.x + 0.3f, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
	
	public int getScore() {
		return 500;
	}
	
}
