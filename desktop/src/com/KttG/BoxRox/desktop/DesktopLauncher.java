package com.KttG.BoxRox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.KttG.BoxRox.BoxRox;
import com.KttG.BoxRox.CameraControl;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.width = (int) (LwjglApplicationConfiguration.getDesktopDisplayMode().width/2.3f); //2.5f
		config.height = (int) (LwjglApplicationConfiguration.getDesktopDisplayMode().height/1.10f); //1.3f
//		config.width = CameraControl.width;
//		config.height = CameraControl.height;
//		config.width = (int) (LwjglApplicationConfiguration.getDesktopDisplayMode().width);
//		config.height = (int) (LwjglApplicationConfiguration.getDesktopDisplayMode().height);
//		config.fullscreen = true;
		
		new LwjglApplication(new BoxRox(), config);
	}
}
