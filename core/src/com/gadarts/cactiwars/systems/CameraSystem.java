package com.gadarts.cactiwars.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.gadarts.cactiwars.DebugSettings;
import com.gadarts.cactiwars.GameAssetManager;

import java.util.List;

import static com.gadarts.cactiwars.CactiWars.*;

public class CameraSystem extends GameEntitySystem implements InputProcessor {
	private static final float FAR = 100f;
	private static final float NEAR = 0.01f;
	private static final int SCROLL_PADDING = 100;
	public static final float SCREEN_SCROLL_SPEED = 0.2F;
	public static final int SCALE_FACTOR = 25;
	private static final float PADDING_EDGE = 15F;
	private final Vector2 lastMousePosition = new Vector2();

	@Override
	public List<SystemEvents> getEventsListenList( ) {
		return null;
	}

	@Override
	public void createGlobalData(SystemsGlobalData systemsGlobalData) {
		super.createGlobalData(systemsGlobalData);
		int viewportWidth = (DebugSettings.FULL_SCREEN ? FULL_SCREEN_RESOLUTION_WIDTH : WINDOWED_RESOLUTION_WIDTH) / SCALE_FACTOR;
		int viewportHeight = (DebugSettings.FULL_SCREEN ? FULL_SCREEN_RESOLUTION_HEIGHT : WINDOWED_RESOLUTION_HEIGHT) / SCALE_FACTOR;
		OrthographicCamera cam = new OrthographicCamera(viewportWidth, viewportHeight);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		cam.position.set(2, 8, 4);
		cam.lookAt(2, 0, 0);
		systemsGlobalData.setCamera(cam);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void onGlobalDataReady(GameAssetManager assetsManager) {
		dispatcher.dispatchMessage(SystemEvents.CAMERA_CREATED.ordinal());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		OrthographicCamera camera = systemsGlobalData.getCamera();
		moveCameraAlongAxis(camera, lastMousePosition.x, Gdx.graphics.getWidth(), camera.position.x, Vector2.X);
		moveCameraAlongAxis(camera, lastMousePosition.y, Gdx.graphics.getHeight(), camera.position.z, Vector2.Y);
		camera.update();
	}

	private void moveCameraAlongAxis(OrthographicCamera camera,
									 float mousePositionAlongAxis,
									 int screenSizeAlongAxis,
									 float cameraPositionAlongAxis,
									 Vector2 axis) {
		if (mousePositionAlongAxis >= screenSizeAlongAxis - SCROLL_PADDING
				&& cameraPositionAlongAxis < SystemsGlobalData.TEMP_GROUND_SIZE - PADDING_EDGE) {
			camera.position.add(axis.x * SCREEN_SCROLL_SPEED, 0F, axis.y * SCREEN_SCROLL_SPEED);
		} else if (mousePositionAlongAxis <= SCROLL_PADDING
				&& cameraPositionAlongAxis > PADDING_EDGE) {
			camera.position.add(-1F * axis.x * SCREEN_SCROLL_SPEED, 0F, -1F * axis.y * SCREEN_SCROLL_SPEED);
		}
	}

	@Override
	public void dispose( ) {

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		lastMousePosition.set(screenX, screenY);
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
