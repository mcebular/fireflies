package net.iamsilver.fireflies.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.iamsilver.fireflies.Fireflies;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		config.width = 1200;
		config.height = 700;

		boolean fullscreen = false;
		if (fullscreen) {
			config.width = 1920;
			config.height = 1080;
			config.fullscreen = true;
		}
		new LwjglApplication(new Fireflies(), config);
	}
}
