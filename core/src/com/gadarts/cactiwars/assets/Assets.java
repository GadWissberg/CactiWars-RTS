package com.gadarts.cactiwars.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import lombok.Getter;

public final class Assets {
	public static final String PATH_SEPARATOR = "/";

	@Getter
	public enum Shaders implements ShaderDefinition {
		DECAL_VERTEX,
		DECAL_FRAGMENT;

		private final String filePath;

		Shaders( ) {
			this.filePath = FOLDER + PATH_SEPARATOR + name().toLowerCase() + "." + FORMAT;
		}

		@Override
		public AssetLoaderParameters<String> getParameters( ) {
			return null;
		}

		@Override
		public Class<String> getTypeClass( ) {
			return String.class;
		}
	}

	@Getter
	public enum Models implements ModelDefinition {
		ROCK();

		private final String filePath;
		private final float alpha;
		private final String textureFileName;

		Models( ) {
			this(1.0f, null, null);
		}

		Models(final float alpha, String fileName, String textureFileName) {
			String name = fileName != null ? fileName : name().toLowerCase();
			this.filePath = FOLDER + PATH_SEPARATOR + name + "." + FORMAT;
			this.textureFileName = textureFileName;
			this.alpha = alpha;
		}


		@Override
		public AssetLoaderParameters<Model> getParameters( ) {
			return null;
		}

		@Override
		public Class<Model> getTypeClass( ) {
			return Model.class;
		}
	}

	@Getter
	public enum Atlases implements AtlasDefinition {
		TROOPER_RIFLE();

		private final String filePath;
		private final float alpha;
		private final String textureFileName;

		Atlases( ) {
			this(1.0f, null, null);
		}

		Atlases(final float alpha, String fileName, String textureFileName) {
			String name = fileName != null ? fileName : name().toLowerCase();
			this.filePath = FOLDER + PATH_SEPARATOR + name + "." + FORMAT;
			this.textureFileName = textureFileName;
			this.alpha = alpha;
		}


		@Override
		public AssetLoaderParameters<TextureAtlas> getParameters( ) {
			return null;
		}

		@Override
		public Class<TextureAtlas> getTypeClass( ) {
			return TextureAtlas.class;
		}

	}
}
