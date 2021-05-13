package com.prog.crystalknight.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.prog.crystalknight.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.prog.crystalknight.game.objects.Rock;
import com.prog.crystalknight.util.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.prog.crystalknight.game.objects.Knight;
import com.prog.crystalknight.game.objects.Knight.JUMP_STATE;
import com.prog.crystalknight.game.objects.Rock;
import com.prog.crystalknight.game.objects.CrystalCoin;

public class WorldController extends InputAdapter{
	
	private static final String TAG = WorldController.class.getName();
	
	// for testing
	// public Sprite[] testSprites;
	// public int selectedSprite;
	
	
	// debugging camera
	public CameraHelper cameraHelper;
	
	// for level
	public Level level;
	public static int lives;
	public int score;
	
	// for collision
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	// for gameOver
	
	private float timeToGameOver;
	
	public WorldController() {
		init();
	}
	
	public void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.knight);
	}
	
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		timeToGameOver = 0;
		initLevel();
		// initTestObjects();
	}
	
	// for testing only
	/**
	private void initTestObjects() {
		// create new 5 sprites
		testSprites = new Sprite[5];
		
		// create texture regions for testing
		
		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.knight.knight);
		regions.add(Assets.instance.crystalCoin.crystalCoin);
		
		
		
		// create  new sprites using texture region
		for(int i = 0; i < testSprites.length; i++) {
			
			Sprite spr = new Sprite(regions.random());
			
			// define sprite size to be 1m x 1m 
			spr.setSize(1, 1);
			
			// set origin to sprite's center
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			
			// calculate random position for sprite
			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
			spr.setPosition(randomX, randomY);
			
			// put new sprite into array
			
			testSprites[i] = spr;
		}
		
		// set first sprite as selected one
		selectedSprite = 0;
		
	}
	*/
	
	// same for testing
	
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if(GameOver()) {
			timeToGameOver -= deltaTime;
			if(timeToGameOver < 0) {
				init();
			}
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		// updateTestObjects(deltaTime);
		collision();
		cameraHelper.update(deltaTime);
		
		if(!GameOver() && InWater()) {
			lives--;
			if(GameOver()) {
				timeToGameOver = Constants.TIME_TO_GAMEOVER;
				
			} else {
				initLevel();
			}
		}
		
	}
	
	// test
	/**
	private void updateTestObjects(float deltaTime) {
		// get current rotation from selected
		float rotation = testSprites[selectedSprite].getRotation();
		// rotate sprite by 90 degrees per second
		rotation += 90 * deltaTime;
		// wrap around at 360 degrees
		rotation %= 360;
		// set new rotation value to selected sprite
		testSprites[selectedSprite].setRotation(rotation);
	}
	*/
	
	// testing
	
	private void handleInputGame(float deltaTime) {
		// character move
		if(cameraHelper.hasTarget(level.knight)) {
			float characterMove = 5 * deltaTime;
			if(Gdx.input.isKeyPressed(Keys.A)) {
				level.knight.velocity.x = -level.knight.terminalVelocity.x;	
			} else if(Gdx.input.isKeyPressed(Keys.D)) {
				level.knight.velocity.x = level.knight.terminalVelocity.x;
			}
			
		}
		
		// jumping
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			level.knight.setJumping(true);
		} else {
			level.knight.setJumping(false);
		}
		
	}
	
	private void handleDebugInput(float deltaTime) {
		if(Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}
		
		float sprMoveSpeed = 5 * deltaTime;
		
		// camera move
		if(!cameraHelper.hasTarget(level.knight)) {
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				camMoveSpeed *= camMoveSpeedAccelerationFactor ;
			}
			if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				moveCamera(-camMoveSpeed, 0);
			}
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
				moveCamera(camMoveSpeed, 0);	
			}
			if(Gdx.input.isKeyPressed(Keys.UP)) {
				moveCamera(0, camMoveSpeed);		
			}
			if(Gdx.input.isKeyPressed(Keys.DOWN)) {
				moveCamera(0, -camMoveSpeed);		
			}
		
			if(Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
				cameraHelper.setPosition(0, 0);
			}
		
			// zoom in/out
		
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				camZoomSpeed *= camZoomSpeedAccelerationFactor;
			}
			if(Gdx.input.isKeyPressed(Keys.COMMA)) {
				cameraHelper.addZoom(camZoomSpeed);
			}
			if(Gdx.input.isKeyPressed(Keys.PERIOD)) {
				cameraHelper.addZoom(-camZoomSpeed);
			}
			if(Gdx.input.isKeyPressed(Keys.SLASH)) {
				cameraHelper.setZoom(1);
			}
		
		
		}
	}
	
	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	} 
	
	
	// test
	/**
	private void moveSelectedSprite(float x, float y) {
		testSprites[selectedSprite].translate(x, y);
	}
	*/
	
	@Override
	public boolean keyUp(int keycode) {
		
		// reset 
		if(keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "GAme reset");
		}
		
		
		
		// select next sprite
		/**
		else if(keycode == Keys.SPACE) {
			selectedSprite = (selectedSprite + 1) % testSprites.length;
			// update camera to follow
			if(cameraHelper.hasTarget()) {
				cameraHelper.setTarget(testSprites[selectedSprite]);
			}
			Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
		}
		*/
		
		// toggle camera
		
		else if (keycode == Keys.ENTER) {
			
			
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.knight);
			Gdx.app.debug(TAG, "Camera follow enabled : " + cameraHelper.hasTarget());
		} 
		
		
		
		return false;
	}
	
	
	// collision
	private void collisionToRock(Rock rock) {
		Knight knight = level.knight;
		float heightDifference = Math.abs(knight.position.y - (rock.position.y + rock.bounds.height));
		if(heightDifference > 0.25f) {
			boolean hitRightEdge = knight.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if(hitRightEdge) {
				knight.position.x = rock.position.x + rock.bounds.width + knight.bounds.width;
			} else {
				knight.position.x = rock.position.x - knight.bounds.width;
			}
			return;
		}
		
		switch(knight.jumpState) {
			case GROUNDED :
				break;
			case FALLING :
			case JUMP_FALLING :
				knight.position.y = rock.position.y + knight.bounds.height;
				knight.jumpState = JUMP_STATE.GROUNDED;
				break;
			case JUMP_RISING :
				knight.position.y = rock.position.y + knight.bounds.height + knight.origin.y - 0.555f;
				break;
		}
	}
	
	private void collisionToCrystal(CrystalCoin crystal) {
		
		crystal.collected = true;
		score += crystal.getScore();
		Gdx.app.log(TAG, "Got a crystal");
		
	}
	
	private void collision() {
		r1.set(level.knight.position.x, level.knight.position.y, level.knight.bounds.width, level.knight.bounds.height);
		
		// knight and rock
		for(Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if(!r1.overlaps(r2)) {
				continue;
			}
			collisionToRock(rock);
			// need to test all rock
			
		}
		
		// coin and knight
		for(CrystalCoin crystal : level.crystalCoins) {
			if(crystal.collected) {
				continue;
			}
			r2.set(crystal.position.x + 0.7f, crystal.position.y, crystal.bounds.width, crystal.bounds.height);
			if(!r1.overlaps(r2)) {
				continue;
			}
			collisionToCrystal(crystal);
			break;
		}
	}

	// gameOver
	public boolean GameOver() {
		return lives < 0;
	}
	
	public boolean InWater() {
		return level.knight.position.y < -0.9f;
	}
}
