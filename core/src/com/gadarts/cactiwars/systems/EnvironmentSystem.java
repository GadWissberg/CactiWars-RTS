package com.gadarts.cactiwars.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.cactiwars.Direction;
import com.gadarts.cactiwars.EntityBuilder;
import com.gadarts.cactiwars.UnitTypes;
import com.gadarts.cactiwars.assets.Assets;
import com.gadarts.cactiwars.assets.GameAssetManager;

import java.util.List;

import static com.gadarts.cactiwars.SpriteType.IDLE;
import static com.gadarts.cactiwars.systems.SystemsGlobalData.TEMP_GROUND_SIZE;

public class EnvironmentSystem extends GameEntitySystem {
	public static final float BILLBOARD_Y = 0.7F;
	private Model tempGround;

	@Override
	public List<SystemEvents> getEventsListenList( ) {
		return null;
	}

	@Override
	public void onGlobalDataReady(GameAssetManager assetsManager) {
		ModelBuilder modelBuilder = new ModelBuilder();
		tempGround = modelBuilder.createRect(
				0F, 0F, 0F,
				0F, 0F, TEMP_GROUND_SIZE,
				TEMP_GROUND_SIZE, 0F, TEMP_GROUND_SIZE,
				TEMP_GROUND_SIZE, 0F, 0F,
				0F, TEMP_GROUND_SIZE, 0F,
				new Material(ColorAttribute.createDiffuse(Color.BROWN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		EntityBuilder.beginBuildingEntity(getEngine())
				.addModelInstanceComponent(new ModelInstance(tempGround))
				.finishAndAddToEngine();
		addRocks(assetsManager);
		Vector3 position = new Vector3(4F, BILLBOARD_Y, 4F);
		EntityBuilder.beginBuildingEntity(getEngine())
				.addCharacterDecalComponent(assetsManager.get(UnitTypes.TROOPER_RIFLE.getAtlas().name()), IDLE, position)
				.addCharacterComponent(Direction.EAST)
				.addAnimationComponent()
				.finishAndAddToEngine();
	}

	private void addRocks(GameAssetManager assetsManager) {
		int numberOfRocks = MathUtils.random(10, 20);
		for (int i = 0; i < numberOfRocks; i++) {
			ModelInstance modelInstance = new ModelInstance(assetsManager.getModel(Assets.Models.ROCK));
			modelInstance.transform.setToTranslation(
					MathUtils.random(TEMP_GROUND_SIZE) + 0.5F,
					0F,
					MathUtils.random(TEMP_GROUND_SIZE) + 0.5F);
			EntityBuilder.beginBuildingEntity(getEngine())
					.addModelInstanceComponent(modelInstance)
					.finishAndAddToEngine();

		}
	}

	@Override
	public void dispose( ) {
		tempGround.dispose();
	}
}
