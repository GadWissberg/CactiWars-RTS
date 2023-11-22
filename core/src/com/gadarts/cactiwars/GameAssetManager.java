package com.gadarts.cactiwars;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.Arrays;
import java.util.Optional;

import static com.gadarts.cactiwars.ModelDefinition.FOLDER;

public class GameAssetManager extends AssetManager {
	public com.badlogic.gdx.graphics.g3d.Model getModel(final ModelDefinition model) {
		return get(model.getFilePath(), com.badlogic.gdx.graphics.g3d.Model.class);
	}

	public void loadGameFiles(final AssetsTypes... assetsTypesToExclude) {
		Arrays.stream(AssetsTypes.values())
				.filter(type -> Arrays.stream(assetsTypesToExclude).noneMatch(toExclude -> toExclude == type))
				.filter(type -> !type.isManualLoad())
				.forEach(type -> Arrays.stream(type.getAssetDefinitions()).forEach(def -> {
					String[] filesList = def.getFilesList();
					if (filesList.length == 0) {
						loadFile(def, def.getFilePath(), type.isBlock());
					} else {
						Arrays.stream(filesList).forEach(file -> loadFile(def, file, type.isBlock()));
					}
				}));
		finishLoading();
	}

	private void loadFile(AssetDefinition def, String fileName, boolean block) {
		String path = Gdx.files.getFileHandle(fileName, Files.FileType.Internal).path();
		if (def.getParameters() != null) {
			load(def.getAssetManagerKey() != null ? def.getAssetManagerKey() : path, def.getTypeClass(), def.getParameters());
		} else {
			load(path, def.getTypeClass());
		}
		if (block) {
			finishLoadingAsset(path);
		}
		loadModelExplicitTexture(def);
	}

	private void loadModelExplicitTexture(AssetDefinition def) {
		if (def instanceof ModelDefinition modelDef) {
			Optional.ofNullable(modelDef.getTextureFileName()).ifPresent(t -> {
				String fileName = FOLDER + "/" + t + ".png";
				load(fileName, Texture.class);
			});
		}
	}
}
