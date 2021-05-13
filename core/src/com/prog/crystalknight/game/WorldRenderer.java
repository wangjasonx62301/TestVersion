package com.prog.crystalknight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.prog.crystalknight.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class WorldRenderer implements Disposable{
	// always calls dispose
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	
	
	// GUI camera
	private OrthographicCamera cameraGUI;
	
	
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}
	
	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update(); // when camera moves, stay the same screen height / width
	
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		
	}
	
	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}
	
	public void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	// for test
	/**
	private void renderTestObjects() {
		// follow objects
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.end();
	}
	*/
	
	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
		// GUI
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
		
	}
	
	@Override 
	public void dispose() {
		batch.dispose();
	}
	
	// GUI
	
	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		// coin to top left
		renderGuiScore(batch);
		// lives
		renderGuiLives(batch);
		// FPS
		renderGuiFpsCounter(batch);
		// gameOver
		renderGuiGameOver(batch);
		
		batch.end();
		
	}
	
	private void renderGuiScore(SpriteBatch batch) {
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.crystalCoin.crystalCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultNormal.draw(batch, "Score -  " + worldController.score, x + 70, y + 42.5f);
	}
	
	private void renderGuiLives(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		
		
		batch.draw(Assets.instance.knight.knight, x + 50f, y, 50, 50, 150, 120, 0.35f, -0.35f, 0);
		if(worldController.lives == -1) {
			Assets.instance.fonts.defaultNormal.draw(batch, "X " + "0", x + 140, y + 38f);
		} else {
			Assets.instance.fonts.defaultNormal.draw(batch, "X " + worldController.lives, x + 140, y + 38f);
		}
	}
	
	private void renderGuiFpsCounter(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultSmall;
		if(fps >= 45) {
			fpsFont.setColor(0, 1, 0, 1);
			
		} else if (fps >= 30 && fps < 45) {
			fpsFont.setColor(1, 1, 0, 1);
		
		} else if (fps < 30) {
			fpsFont.setColor(1, 0, 0, 1);
		}
		
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1);
		
	}
	
	// gameOver
	
	private void renderGuiGameOver(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		if(worldController.GameOver()) {
			BitmapFont gameover = Assets.instance.fonts.defaultBig;
			gameover.setColor(1, 1, 1, 1);
			gameover.draw(batch, "GAME OVER", x + 0.1f, y + 0.1f, 0, Align.center, true);
			gameover.setColor(1, 1, 1, 1);
		}
	}
}
