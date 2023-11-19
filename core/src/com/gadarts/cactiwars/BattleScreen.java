package com.gadarts.cactiwars;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.gadarts.cactiwars.systems.GameEntitySystem;
import com.gadarts.cactiwars.systems.Systems;
import com.gadarts.cactiwars.systems.SystemsGlobalData;

import java.util.Arrays;

public class BattleScreen implements Screen {
	private final PooledEngine engine;

	public BattleScreen( ) {
		engine = new PooledEngine();
		SystemsGlobalData systemsGlobalData = new SystemsGlobalData();
		Arrays.stream(Systems.values()).forEach(system -> engine.addSystem(system.getInstance()));
		Arrays.stream(Systems.values()).forEach(dispatcher -> Arrays.stream(Systems.values())
				.map(Systems::getInstance)
				.forEach(instance -> dispatcher.getInstance().addListener(instance)));
		Arrays.stream(Systems.values()).forEach(system -> system.getInstance().createGlobalData(systemsGlobalData));
		Arrays.stream(Systems.values()).forEach(system -> system.getInstance().onGlobalDataReady());
	}

	@Override
	public void show( ) {

	}

	@Override
	public void render(float delta) {
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause( ) {

	}

	@Override
	public void resume( ) {

	}

	@Override
	public void hide( ) {

	}

	@Override
	public void dispose( ) {
		engine.getSystems().forEach(entitySystem -> ((GameEntitySystem) entitySystem).dispose());
	}
}
