package com.gadarts.cactiwars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class CactiWars extends Game {
	public static final int FULL_SCREEN_RESOLUTION_WIDTH = 1920;
	public static final int FULL_SCREEN_RESOLUTION_HEIGHT = 1080;
	public static final int WINDOWED_RESOLUTION_WIDTH = 1280;
	public static final int WINDOWED_RESOLUTION_HEIGHT = 960;

	@Override
	public void create( ) {
		if (DebugSettings.FULL_SCREEN) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(WINDOWED_RESOLUTION_WIDTH, WINDOWED_RESOLUTION_HEIGHT);
		}
		GameAssetManager assetsManager = new GameAssetManager();
		assetsManager.loadGameFiles();
		setScreen(new BattleScreen(assetsManager));
	}


}
