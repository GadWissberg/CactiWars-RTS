package com.gadarts.cactiwars.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import lombok.Getter;

@Getter
public class ModelInstanceComponent extends GameComponent {
	private ModelInstance modelInstance;

	@Override
	public void reset( ) {

	}

	public void init(ModelInstance modelInstance) {
		this.modelInstance = modelInstance;
	}
}
