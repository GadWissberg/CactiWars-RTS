package com.gadarts.cactiwars;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

public enum Direction {
	SOUTH(0, 1, 0B00000010),
	SOUTH_EAST(1, 1, 0B00000001),
	EAST(1, 0, 0B00001000),
	NORTH_EAST(1, -1, 0B001000000),
	NORTH(0, -1, 0B010000000),
	NORTH_WEST(-1, -1, 0B100000000),
	WEST(-1, 0, 0B000100000),
	SOUTH_WEST(-1, 1, 0B00000100);

	private final Vector2 directionVector;

	@Getter
	private final int mask;

	Direction(final int x, final int z, int mask) {
		directionVector = new Vector2(x, z);
		this.mask = mask;
	}

	public Vector2 getDirection(final Vector2 output) {
		return output.set(directionVector);
	}
}
