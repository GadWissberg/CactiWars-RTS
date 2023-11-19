package com.gadarts.cactiwars.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.gadarts.cactiwars.EntityBuilder;

import java.util.List;

import static com.gadarts.cactiwars.systems.SystemsGlobalData.TEMP_GROUND_SIZE;

public class EnvironmentSystem extends GameEntitySystem {
	private Model tempGround;

	@Override
	public List<SystemEvents> getEventsListenList( ) {
		return null;
	}

	@Override
	public void onGlobalDataReady( ) {
		ModelBuilder modelBuilder = new ModelBuilder();
		tempGround = modelBuilder.createRect(
				0F, 0F, 0F,
				0F, 0F, TEMP_GROUND_SIZE,
				TEMP_GROUND_SIZE, 0F, TEMP_GROUND_SIZE,
				TEMP_GROUND_SIZE, 0F, 0F,
				0F, TEMP_GROUND_SIZE, 0F,
				new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		EntityBuilder.beginBuildingEntity(getEngine())
				.addModelInstanceComponent(new ModelInstance(tempGround))
				.finishAndAddToEngine();
	}

	@Override
	public void dispose( ) {
		tempGround.dispose();
	}
}
