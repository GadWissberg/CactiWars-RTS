package com.gadarts.cactiwars.assets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssetsTypes {
	MODEL(Assets.Models.values()),
	SHADER(Assets.Shaders.values()),
	ATLAS(Assets.Atlases.values());

	private final AssetDefinition[] assetDefinitions;
	private final boolean manualLoad;
	private final boolean block;

	AssetsTypes(final AssetDefinition[] assetDefinitions) {
		this(assetDefinitions, false, false);
	}


}
