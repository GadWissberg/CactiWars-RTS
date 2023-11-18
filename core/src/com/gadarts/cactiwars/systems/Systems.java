package com.gadarts.cactiwars.systems;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Systems {
	CAMERA(new CameraSystem()),
	RENDER(new RenderSystem());

	private final GameEntitySystem instance;

}
