package com.gadarts.cactiwars.systems.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.cactiwars.EntityBuilder;
import lombok.Getter;

@Getter
public class AxisModelHandler implements Disposable {
	private final static Vector3 auxVector3_1 = new Vector3();
	private final static Vector3 auxVector3_2 = new Vector3();

	private Model axisModelX;
	private Model axisModelY;
	private Model axisModelZ;

	public void addAxis(Engine engine) {
		ModelBuilder modelBuilder = new ModelBuilder();
		axisModelX = createAndAddAxisModel((PooledEngine) engine, modelBuilder, auxVector3_2.set(1, 0, 0));
		axisModelY = createAndAddAxisModel((PooledEngine) engine, modelBuilder, auxVector3_2.set(0, 1, 0));
		axisModelZ = createAndAddAxisModel((PooledEngine) engine, modelBuilder, auxVector3_2.set(0, 0, 1));
	}

	private Model createAndAddAxisModel(PooledEngine engine, ModelBuilder modelBuilder, Vector3 vector) {
		Model axisModel = createAxisModel(modelBuilder, vector, new Color(vector.x, vector.y, vector.z, 1F));
		ModelInstance axisModelInstanceX = new ModelInstance(axisModel);
		EntityBuilder.beginBuildingEntity(engine)
				.addModelInstanceComponent(axisModelInstanceX)
				.finishAndAddToEngine();
		return axisModel;
	}

	private Model createAxisModel(final ModelBuilder modelBuilder, final Vector3 dir, final Color color) {
		return modelBuilder.createArrow(
				auxVector3_1.setZero(),
				dir,
				new Material(ColorAttribute.createDiffuse(color)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
		);
	}

	@Override
	public void dispose( ) {
		axisModelX.dispose();
		axisModelY.dispose();
		axisModelZ.dispose();
	}
}
