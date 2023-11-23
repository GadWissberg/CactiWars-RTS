package com.gadarts.cactiwars;

import com.gadarts.cactiwars.assets.Assets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UnitTypes {
	TROOPER_RIFLE(Assets.Atlases.TROOPER_RIFLE);

	private final Assets.Atlases atlas;

}
