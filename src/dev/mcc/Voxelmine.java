package dev.mcc;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import dev.mcc.audio.SoundManager;
import dev.mcc.entity.Entity;
import dev.mcc.entity.EntityType;
import dev.mcc.gui.GUIHealthBar;
import dev.mcc.gui.GUIHotbar;
import dev.mcc.gui.GUIHudScreen;
import dev.mcc.gui.GUIScreen;
import dev.mcc.render.FontData;
import dev.mcc.render.Renderer;
import dev.mcc.render.TestMesh;
import dev.mcc.render.TextMesh;
import dev.mcc.util.Transform;
import dev.mcc.world.World;
import dev.mcc.world.WorldType;
import dev.mcc.world.block.Block;
import dev.mcc.world.chunk.gen.DefaultChunkGenerator;
import dev.mcc.world.chunk.gen.IChunkGenerator;
import dev.mcc.world.item.Item;

public class Voxelmine {
	private static World world;
	private static Renderer renderer;
	private static SoundManager soundManager;
	private static Entity localPlayer;
	private static GUIScreen screen;
	private static FontData fontData;
	private static WorldType worldType;
	public static FontData getFontData() {
		if(fontData == null) fontData = new FontData("res/default.fnt");
		return fontData;
	}
	public static Renderer getRenderer() {
		if(renderer == null) renderer = new Renderer();
		return renderer;
	}
	public static World getWorld() {
		if(world == null) world = worldType.newWorld();
		return world;
	}
	public static SoundManager getSoundManager() {
		if(soundManager == null) soundManager = new SoundManager();
		return soundManager;
	}
	public static Entity getLocalPlayer() {
		if(localPlayer == null) localPlayer = getWorld().spawnEntity(new Vector3f(0, 0, 0), EntityType.PLAYER.getId());
		return localPlayer;
	}
	public static GUIScreen getScreen() {
		if(screen == null) screen = new GUIHudScreen();
		return screen;
	}
	public static void setScreen(GUIScreen screen) {
		Voxelmine.screen.onDelete();
		Voxelmine.screen = screen;
		Voxelmine.screen.onCreate();
	}
	public static GUIHealthBar getHealthBar() {
		if(screen instanceof GUIHudScreen) {
			return ((GUIHudScreen)screen).getHealthBar();
		}
		return null;
	}
	private static float elapsedTime = 0, deltaTime = 0;
	public static float getElapsedTime() {
		return elapsedTime;
	}
	public static float getDeltaTime() {
		return deltaTime;
	}
	public static void main(String[] args) {
		String reqLevelType = "";
		String reqLevelSeed = ""; // Currently ignored
		for(String str : args) {
			String[] toks = str.split("\\:");
			if(toks.length == 2) {
				if(toks[0].trim().equals("level_type")) {
					reqLevelType = toks[1].trim();
				}
				if(toks[0].trim().equals("level_seed")) {
					reqLevelSeed = toks[1].trim();
				}
			}
		}
		try {
			Block.init();
			Item.init();
			EntityType.init();
			WorldType.init();
			worldType = WorldType.FLAT;
			for(int i = 0; i < 100; i++) {
				WorldType type = WorldType.WORLD_TYPES.get(i);
				if(type != null) {
					if(type.getName().equals(reqLevelType)) {
						worldType = type;
					}
				}
			}
			getSoundManager().init();
			getRenderer();
			getScreen().onCreate();
			getScreen().onUpdate();
			
			//IChunkGenerator gen = new DefaultChunkGenerator();
			glfwSetInputMode(renderer.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			renderer.add(new TestMesh(), false);
			long time, lastTime = System.nanoTime();
			float frameTime = 0; int frames = 0;
			while(!renderer.shouldClose()) {
				time = System.nanoTime();
				deltaTime = (float) ((time - lastTime) / 1000000000D);
				lastTime = time;
				frameTime += deltaTime;
				frames++;
				if(frameTime >= 1) {
					System.out.println("FPS: " + frames);
					frameTime = 0;
					frames = 0;
				}
				getScreen().onUpdate();
				getWorld().update();
				elapsedTime += deltaTime;
				renderer.render();
				if(glfwGetKey(renderer.getWindowHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
					break;
				}
			}
			getScreen().onDelete();
			renderer.disposeContextAndWindow();
			getSoundManager().dispose();
		}catch(Exception e) {
			e.printStackTrace();
			renderer.disposeContextAndWindow();
			getSoundManager().dispose();
		}
	}
}
