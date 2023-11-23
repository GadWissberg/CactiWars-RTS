package com.gadarts.cactiwars.components;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.cactiwars.CharacterAnimations;
import com.gadarts.cactiwars.Direction;
import com.gadarts.cactiwars.SpriteType;
import lombok.Getter;

@Getter
public class CharacterDecalComponent implements GameComponent {
	public static final float BILLBOARD_SCALE = 0.013F;
	private static final Vector3 auxVector3 = new Vector3();
	private static final Vector2 auxVector2 = new Vector2();
	private Decal decal;
	private CharacterAnimations animations;
	private SpriteType spriteType;
	private Direction direction;

	@Override
	public void reset( ) {
	}

	public void init(final CharacterAnimations animations,
					 final SpriteType type,
					 final Vector3 position) {
		this.animations = animations;
		this.direction = Direction.SOUTH;
		this.spriteType = type;
		createCharacterDecal(animations, type, direction, position);
	}

	public void initializeSprite(final SpriteType type, final Direction direction) {
		this.spriteType = type;
		this.direction = direction;
	}

	private void createCharacterDecal(final CharacterAnimations animations,
									  final SpriteType type,
									  final Direction direction,
									  final Vector3 position) {
		decal = Decal.newDecal(animations.get(type, direction).getKeyFrames()[0], true);//Optimize this - it creates an object each time.
		decal.setScale(BILLBOARD_SCALE);
		decal.setPosition(position);
	}

}
