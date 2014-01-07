package com.hdcheese.gamejam;

import hdcheese.GdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	public static void main(String[] args) {
		
		if (args.length == 0) {
			runGame();
		} else if (args[0].equals("pack")) {
			texturePack();
		}
		
		//runGame();
		
	}
	
	public static void runGame() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "gamejam";
		cfg.useGL20 = false;
		
		cfg.width = 1024;
		cfg.height = 768;
		
		new LwjglApplication(new GdxGame(true), cfg);
	}

	public static void texturePack() {
		// TexturePacker2: Auto-Pack on desktop run
		Settings settings = new Settings();
		settings.filterMag = TextureFilter.Nearest;
		settings.filterMin = TextureFilter.Nearest;
		settings.pot = true; // power of 2
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;
		settings.outputFormat = "png";
		
		settings.edgePadding = false;
		settings.paddingX = 0;
		settings.paddingY = 0;

		// pack up...
		TexturePacker2.process(settings, 
				"unpacked", 
				"packed", 
				"tileset");
	}
}
