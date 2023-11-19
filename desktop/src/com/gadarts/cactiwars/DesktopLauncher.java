package com.gadarts.cactiwars;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
		config.setResizable(false);
		config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL20, 4, 2);
		config.setForegroundFPS(60);
		config.setTitle("CactiWars");
		new Lwjgl3Application(new CactiWars(), config);
	}
}
