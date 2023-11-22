package com.gadarts.cactiwars.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gadarts.cactiwars.GameAssetManager;
import com.gadarts.cactiwars.components.ComponentsMappers;
import com.gadarts.cactiwars.components.ModelInstanceComponent;
import com.gadarts.cactiwars.systems.GameEntitySystem;
import com.gadarts.cactiwars.systems.SystemEvents;
import com.gadarts.cactiwars.systems.SystemsGlobalData;

import java.util.List;

public class RenderSystem extends GameEntitySystem {
	private AxisModelHandler axisModelHandler;
	private ModelBatch modelBatch;
	private ImmutableArray<Entity> modelEntities;

	@Override
	public List<SystemEvents> getEventsListenList( ) {
		return List.of(SystemEvents.CAMERA_CREATED);
	}

	@Override
	public void createGlobalData(SystemsGlobalData globalData) {
		super.createGlobalData(globalData);
		modelEntities = getEngine().getEntitiesFor(Family.all(ModelInstanceComponent.class).get());
		axisModelHandler = new AxisModelHandler();
		axisModelHandler.addAxis(getEngine());
		this.modelBatch = new ModelBatch();
	}

	@Override
	public void onGlobalDataReady(GameAssetManager assetsManager) {
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		if (msg.message == SystemEvents.CAMERA_CREATED.ordinal()) {
			Gdx.app.log("!!!!", "!!!");
			return true;
		}
		return false;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ScreenUtils.clear(Color.BLACK, true);
		modelBatch.begin(systemsGlobalData.getCamera());
		for (int i = 0; i < modelEntities.size(); i++) {
			modelBatch.render(ComponentsMappers.modelInstance.get(modelEntities.get(i)).getModelInstance());
		}
		modelBatch.end();
	}

	@Override
	public void dispose( ) {
		axisModelHandler.dispose();
	}
}
