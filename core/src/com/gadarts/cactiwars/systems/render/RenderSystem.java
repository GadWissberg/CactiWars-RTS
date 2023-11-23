package com.gadarts.cactiwars.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gadarts.cactiwars.CharacterAnimation;
import com.gadarts.cactiwars.CharacterAnimations;
import com.gadarts.cactiwars.Direction;
import com.gadarts.cactiwars.SpriteType;
import com.gadarts.cactiwars.assets.GameAssetManager;
import com.gadarts.cactiwars.components.*;
import com.gadarts.cactiwars.systems.GameEntitySystem;
import com.gadarts.cactiwars.systems.SystemEvents;
import com.gadarts.cactiwars.systems.SystemsGlobalData;

import java.util.List;

import static com.gadarts.cactiwars.SpriteType.IDLE;

public class RenderSystem extends GameEntitySystem {
	private static final int DECALS_POOL_SIZE = 200;
	private static final Vector3 auxVector3_1 = new Vector3();
	private static final Vector2 auxVector2_1 = new Vector2();
	private static final Vector2 auxVector2_2 = new Vector2();
	private static final Vector2 auxVector2_3 = new Vector2();
	private final static Plane auxPlane = new Plane();
	private AxisModelHandler axisModelHandler;
	private ModelBatch modelBatch;
	private ImmutableArray<Entity> modelEntities;
	private DecalBatch decalBatch;
	private ImmutableArray<Entity> characterDecalsEntities;

	private static CharacterAnimation fetchCharacterAnimationByDirectionAndType(Entity entity,
																				Direction direction,
																				SpriteType sprType) {
		int randomIndex = MathUtils.random(sprType.getVariations() - 1);
		CharacterAnimation animation = null;
		CharacterAnimations animations = ComponentsMappers.characterDecal.get(entity).getAnimations();
		if (animations.contains(sprType)) {
			animation = animations.get(sprType, randomIndex, direction);
		}
		return animation;
	}

	private static Direction forceDirectionForAnimationInitialization(Direction direction,
																	  SpriteType spriteType,
																	  AnimationComponent animationComponent) {
		if (spriteType.isSingleDirection()) {
			Animation<TextureAtlas.AtlasRegion> animation = animationComponent.getAnimation();
			if (animation == null || !animation.isAnimationFinished(animationComponent.getStateTime())) {
				direction = Direction.SOUTH;
			}
		}
		return direction;
	}

	private Direction calculateDirectionSeenFromCamera(Camera camera, Direction facingDirection) {
		auxVector2_3.set(1.0F, 0.0F);
		float playerAngle = facingDirection.getDirection(auxVector2_1).angleDeg();
		Ray ray = camera.getPickRay((float) Gdx.graphics.getWidth() / 2.0F, (float) Gdx.graphics.getHeight() / 2.0F);
		Intersector.intersectRayPlane(ray, auxPlane, auxVector3_1);
		float cameraAngle = auxVector2_2.set(camera.position.x, camera.position.z).sub(auxVector3_1.x, auxVector3_1.z).angleDeg();
		auxVector2_3.setAngleDeg(playerAngle - cameraAngle);
		float angleDiff = auxVector2_3.angleDeg();
		Direction direction;
		if (angleDiff >= 0.0F && (double) angleDiff <= 22.5 || angleDiff > 337.5F && angleDiff <= 360.0F) {
			direction = Direction.SOUTH;
		} else if ((double) angleDiff > 22.5 && (double) angleDiff <= 67.5) {
			direction = Direction.SOUTH_WEST;
		} else if ((double) angleDiff > 67.5 && (double) angleDiff <= 112.5) {
			direction = Direction.WEST;
		} else if ((double) angleDiff > 112.5 && (double) angleDiff <= 157.5) {
			direction = Direction.NORTH_WEST;
		} else if ((double) angleDiff > 157.5 && (double) angleDiff <= 202.5) {
			direction = Direction.NORTH;
		} else if ((double) angleDiff > 202.5 && (double) angleDiff <= 247.5) {
			direction = Direction.NORTH_EAST;
		} else if ((double) angleDiff > 247.5 && (double) angleDiff <= 292.5) {
			direction = Direction.EAST;
		} else {
			direction = Direction.SOUTH_EAST;
		}

		return direction;
	}

	@Override
	public List<SystemEvents> getEventsListenList( ) {
		return List.of(SystemEvents.CAMERA_CREATED);
	}

	@Override
	public void createGlobalData(SystemsGlobalData globalData) {
		super.createGlobalData(globalData);
		characterDecalsEntities = getEngine().getEntitiesFor(Family.all(CharacterDecalComponent.class).get());
		modelEntities = getEngine().getEntitiesFor(Family.all(ModelInstanceComponent.class).get());
		axisModelHandler = new AxisModelHandler();
		axisModelHandler.addAxis(getEngine());
		this.modelBatch = new ModelBatch();
	}

	@Override
	public void onGlobalDataReady(GameAssetManager assetsManager) {
		this.decalBatch = new DecalBatch(DECALS_POOL_SIZE, new GameCameraGroupStrategy(systemsGlobalData.getCamera(), assetsManager));
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
		renderDecals();
	}

	private void renderCharacters( ) {
		for (Entity entity : characterDecalsEntities) {
			updateCharacterDecal(entity);
			renderCharacterDecal(entity);
		}
	}

	private void renderCharacterDecal(Entity entity) {
		Decal decal = ComponentsMappers.characterDecal.get(entity).getDecal();
		Vector3 decalPosition = decal.getPosition();
		Camera camera = systemsGlobalData.getCamera();
		decal.lookAt(auxVector3_1.set(decalPosition).sub(camera.direction), camera.up);
		decalBatch.add(decal);
	}

	private void updateCharacterDecal(Entity entity) {
		Camera camera = systemsGlobalData.getCamera();
		CharacterComponent characterComponent = ComponentsMappers.character.get(entity);
		Direction direction = calculateDirectionSeenFromCamera(camera, characterComponent.getFacingDirection());
		SpriteType spriteType = characterComponent.getSpriteType();
		boolean sameSpriteType = spriteType.equals(ComponentsMappers.characterDecal.get(entity).getSpriteType());
		Direction characterFacingDirection = ComponentsMappers.characterDecal.get(entity).getDirection();
		if ((!sameSpriteType || !characterFacingDirection.equals(direction))) {
			updateCharacterDecalSprite(entity, direction, spriteType, sameSpriteType);
		} else {
			updateCharacterDecalFrame(entity, spriteType);
		}
	}

	private void updateCharacterDecalFrame(Entity entity,
										   SpriteType spriteType) {
		CharacterDecalComponent characterDecalComponent = ComponentsMappers.characterDecal.get(entity);
		AnimationComponent aniComp = ComponentsMappers.animation.get(entity);
		Animation<TextureAtlas.AtlasRegion> anim = aniComp.getAnimation();
		if (ComponentsMappers.animation.has(entity) && anim != null) {
			TextureAtlas.AtlasRegion currentFrame = (TextureAtlas.AtlasRegion) characterDecalComponent.getDecal().getTextureRegion();
			TextureAtlas.AtlasRegion newFrame = ComponentsMappers.animation.get(entity).calculateFrame();
			if (characterDecalComponent.getSpriteType() == spriteType && currentFrame != newFrame) {
				Decal decal = characterDecalComponent.getDecal();
				decal.setTextureRegion(newFrame);
			}
		}
	}

	private void updateCharacterDecalSprite(Entity entity,
											Direction direction,
											SpriteType spriteType,
											boolean sameSpriteType) {
		CharacterDecalComponent characterDecalComponent = ComponentsMappers.characterDecal.get(entity);
		characterDecalComponent.initializeSprite(spriteType, direction);
		if (ComponentsMappers.animation.has(entity)) {
			initializeCharacterAnimationBySpriteType(entity, direction, spriteType, sameSpriteType);
		}
	}

	private void initializeCharacterAnimationBySpriteType(Entity entity,
														  Direction direction,
														  SpriteType spriteType,
														  boolean sameSpriteType) {
		AnimationComponent animationComponent = ComponentsMappers.animation.get(entity);
		direction = forceDirectionForAnimationInitialization(direction, spriteType, animationComponent);
		Animation<TextureAtlas.AtlasRegion> oldAnimation = ComponentsMappers.animation.get(entity).getAnimation();
		CharacterAnimation newAnimation = fetchCharacterAnimationByDirectionAndType(entity, direction, spriteType);
		if (newAnimation != null) {
			boolean isIdle = spriteType == IDLE;
			animationComponent.init(isIdle ? 0 : spriteType.getFrameDuration(), newAnimation);
			boolean differentAnimation = oldAnimation != newAnimation;
			if (!sameSpriteType || isIdle) {
				if (spriteType.getPlayMode() != Animation.PlayMode.LOOP) {
					newAnimation.setPlayMode(Animation.PlayMode.NORMAL);
				}
				animationComponent.resetStateTime();
			} else if (differentAnimation) {
				newAnimation.setPlayMode(oldAnimation.getPlayMode());
			}
		}
	}

	private void renderDecals( ) {
		Gdx.gl.glDepthMask(false);
		renderCharacters();
		decalBatch.flush();
		Gdx.gl.glDepthMask(true);
	}

	@Override
	public void dispose( ) {
		axisModelHandler.dispose();
	}
}
