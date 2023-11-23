package com.gadarts.cactiwars.components;

import com.badlogic.gdx.math.Vector2;
import com.gadarts.cactiwars.Direction;
import com.gadarts.cactiwars.SpriteType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterComponent implements GameComponent {
	public final static float CHAR_RAD = 0.3f;
	public static final float PASSABLE_MAX_HEIGHT_DIFF = 0.3f;
	private static final Vector2 auxVector = new Vector2();
	private CharacterRotationData rotationData = new CharacterRotationData();
	private SpriteType spriteType;
	private Direction facingDirection;

	@Override
	public void reset( ) {
		rotationData.reset();
	}

	public void init(Direction direction) {
		this.spriteType = SpriteType.RUN;
		this.facingDirection = direction;
	}

}
