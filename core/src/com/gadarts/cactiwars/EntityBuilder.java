package com.gadarts.cactiwars;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.cactiwars.components.AnimationComponent;
import com.gadarts.cactiwars.components.CharacterComponent;
import com.gadarts.cactiwars.components.CharacterDecalComponent;
import com.gadarts.cactiwars.components.ModelInstanceComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class EntityBuilder {
	public static final String MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST = "Call beginBuildingEntity() first!";
	@Getter
	private static final EntityBuilder instance = new EntityBuilder();
	@Setter(AccessLevel.PRIVATE)
	private PooledEngine engine;
	@Getter
	private Entity currentEntity;

	public static EntityBuilder beginBuildingEntity(Engine engine) {
		instance.init((PooledEngine) engine);
		return instance;
	}

	public EntityBuilder addCharacterComponent(Direction direction) {
		if (currentEntity == null) throw new RuntimeException(MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST);
		CharacterComponent charComponent = engine.createComponent(CharacterComponent.class);
		charComponent.init(direction);
		currentEntity.add(charComponent);
		return instance;
	}

	public EntityBuilder addAnimationComponent( ) {
		if (currentEntity == null) throw new RuntimeException(MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST);
		AnimationComponent animComponent = engine.createComponent(AnimationComponent.class);
		currentEntity.add(animComponent);
		return this;
	}

	public EntityBuilder addCharacterDecalComponent(final CharacterAnimations animations,
													final SpriteType spriteType,
													final Vector3 position) {
		if (currentEntity == null) throw new RuntimeException(MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST);
		CharacterDecalComponent characterDecalComponent = engine.createComponent(CharacterDecalComponent.class);
		characterDecalComponent.init(animations, spriteType, position);
		currentEntity.add(characterDecalComponent);
		return instance;
	}

	public EntityBuilder addModelInstanceComponent(ModelInstance modelInstance) {
		if (currentEntity == null) throw new RuntimeException(MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST);
		ModelInstanceComponent component = engine.createComponent(ModelInstanceComponent.class);
		component.init(modelInstance);
		currentEntity.add(component);
		component.getModelInstance().userData = currentEntity;
		return instance;
	}

	public void finishAndAddToEngine( ) {
		if (currentEntity == null) throw new RuntimeException(MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST);
		engine.addEntity(currentEntity);
		finish();
	}

	public void finish( ) {
		if (currentEntity == null) throw new RuntimeException(MSG_FAIL_CALL_BEGIN_BUILDING_ENTITY_FIRST);
		instance.reset();
	}


	private void init(final PooledEngine engine) {
		this.engine = engine;
		this.currentEntity = engine.createEntity();
	}


	private void reset( ) {
		engine = null;
		currentEntity = null;
	}


}

