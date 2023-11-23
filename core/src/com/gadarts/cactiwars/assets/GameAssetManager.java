package com.gadarts.cactiwars.assets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.gadarts.cactiwars.CharacterAnimation;
import com.gadarts.cactiwars.CharacterAnimations;
import com.gadarts.cactiwars.Direction;
import com.gadarts.cactiwars.SpriteType;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.gadarts.cactiwars.assets.ModelDefinition.FOLDER;

public class GameAssetManager extends AssetManager {
	public GameAssetManager( ) {
		setLoader(String.class, new ShaderLoader(getFileHandleResolver()));
	}

	public com.badlogic.gdx.graphics.g3d.Model getModel(final ModelDefinition model) {
		return get(model.getFilePath(), com.badlogic.gdx.graphics.g3d.Model.class);
	}

	public String getShader(final Assets.Shaders shader) {
		return get(shader.getFilePath(), String.class);
	}

	public void generateCharactersAnimations( ) {
		Arrays.stream(Assets.Atlases.values())
				.forEach(atlas -> addAsset(
						atlas.name(),
						CharacterAnimations.class,
						createCharacterAnimations(atlas)));
	}

	@SuppressWarnings("GDXJavaUnsafeIterator")
	private boolean checkIfAtlasContainsSpriteType(SpriteType spriteType, TextureAtlas atlas) {
		boolean result = false;
		for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
			if (region.name.startsWith(spriteType.name().toLowerCase())) {
				result = true;
				break;
			}
		}
		return result;
	}

	private CharacterAnimations createCharacterAnimations(final Assets.Atlases character) {
		CharacterAnimations animations = new CharacterAnimations();
		TextureAtlas atlas = getAtlas(character);
		Arrays.stream(SpriteType.values())
				.filter(spriteType -> checkIfAtlasContainsSpriteType(spriteType, atlas))
				.forEach(spriteType -> {
					if (spriteType.isSingleDirection()) {
						inflateCharacterAnimation(animations, atlas, spriteType, Direction.SOUTH);
					} else {
						Direction[] directions = Direction.values();
						Arrays.stream(directions).forEach(dir -> inflateCharacterAnimation(animations, atlas, spriteType, dir));
					}
				});
		return animations;
	}

	private void inflateCharacterAnimation(final CharacterAnimations animations,
										   final TextureAtlas atlas,
										   final SpriteType spriteType,
										   final Direction dir) {
		String sprTypeName = spriteType.name().toLowerCase();
		int vars = spriteType.getVariations();
		IntStream.range(0, vars).forEach(variationIndex -> {
			String name = formatNameForVariation(dir, sprTypeName, vars, variationIndex, spriteType.isSingleDirection());
			CharacterAnimation a = createAnimation(atlas, spriteType, name, dir);
			if (a == null && variationIndex == 0) {
				name = formatNameForVariation(dir, sprTypeName, 1, 0, spriteType.isSingleDirection());
				a = createAnimation(atlas, spriteType, name, dir);
				animations.put(spriteType, 0, dir, a);
			} else if (a != null && a.getKeyFrames().length > 0) {
				animations.put(spriteType, variationIndex, dir, a);
			}
		});
	}

	private CharacterAnimation createAnimation(final TextureAtlas atlas,
											   final SpriteType spriteType,
											   final String name,
											   final Direction dir) {
		Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);
		CharacterAnimation animation = null;
		if (!regions.isEmpty()) {
			animation = new CharacterAnimation(
					spriteType.getFrameDuration(),
					regions,
					spriteType.getPlayMode(),
					dir);
		}
		return animation;
	}

	private String formatNameForVariation(Direction dir,
										  String sprTypeName,
										  int vars,
										  int variationIndex,
										  boolean singleDirection) {
		return String.format("%s%s%s",
				sprTypeName,
				vars > 1 ? "_" + variationIndex : "",
				singleDirection ? "" : "_" + dir.name().toLowerCase());
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
		generateCharactersAnimations();
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

	public TextureAtlas getAtlas(final AtlasDefinition atlas) {
		return get(atlas.getFilePath(), TextureAtlas.class);
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
