package com.gadarts.cactiwars.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.cactiwars.DebugSettings;

import java.util.List;

import static com.gadarts.cactiwars.CactiWars.*;

public class CameraSystem extends GameEntitySystem {
	private static final float FAR = 100f;
	private static final float NEAR = 0.01f;
	private CameraInputController processor;

	@Override
	public List<SystemEvents> getEventsListenList( ) {
		return null;
	}

	@Override
	public void createGlobalData(SystemsGlobalData systemsGlobalData) {
		super.createGlobalData(systemsGlobalData);
		int viewportWidth = (DebugSettings.FULL_SCREEN ? FULL_SCREEN_RESOLUTION_WIDTH : WINDOWED_RESOLUTION_WIDTH) / 75;
		int viewportHeight = (DebugSettings.FULL_SCREEN ? FULL_SCREEN_RESOLUTION_HEIGHT : WINDOWED_RESOLUTION_HEIGHT) / 75;
		OrthographicCamera cam = new OrthographicCamera(viewportWidth, viewportHeight);
		cam.near = NEAR;
		cam.far = FAR;
		cam.update();
		cam.position.set(1, 1, 1);
		cam.lookAt(Vector3.Zero);
		systemsGlobalData.setCamera(cam);
		processor = new CameraInputController(cam);
		processor.autoUpdate = true;
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	public void initialize( ) {
		dispatcher.dispatchMessage(SystemEvents.CAMERA_CREATED.ordinal());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		systemsGlobalData.getCamera().update();
		processor.update();
	}

	@Override
	public void dispose( ) {

	}

}
