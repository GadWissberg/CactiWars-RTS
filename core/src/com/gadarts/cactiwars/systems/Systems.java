package com.gadarts.cactiwars.systems;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Systems {
	CAMERA(new CameraSystem()),
	RENDER(new RenderSystem()),
	ENVIRONMENT(new EnvironmentSystem());

	private final GameEntitySystem instance;

}
