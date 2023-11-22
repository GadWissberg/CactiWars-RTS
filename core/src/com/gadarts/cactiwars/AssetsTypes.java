package com.gadarts.cactiwars;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssetsTypes {
	MODEL(Assets.Models.values());

	private final AssetDefinition[] assetDefinitions;
	private final boolean manualLoad;
	private final boolean block;

	AssetsTypes(final AssetDefinition[] assetDefinitions) {
		this(assetDefinitions, false, false);
	}


}
