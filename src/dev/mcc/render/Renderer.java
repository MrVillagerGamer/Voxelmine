package dev.mcc.render;

import static org.lwjgl.glfw.GLFW.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import dev.mcc.Voxelmine;
import dev.mcc.gui.GUI;
import dev.mcc.gui.GUIScreen;
import dev.mcc.util.IDisposable;
import dev.mcc.util.IRenderable;
import dev.mcc.util.MeshUtil;
import dev.mcc.util.Transform;
import dev.mcc.world.World;
import dev.mcc.world.chunk.Chunk;

public class Renderer {
	//public static final int WIDTH = 800, HEIGHT = 600;
	public static final int SHADOWMAP_SIZE = 2048;
	private long handle = 0;
	private int w = 800, h = 600;
	private ArrayList<IRenderable> worldObjects = new ArrayList<IRenderable>();
	private ArrayList<IRenderable> guiObjects = new ArrayList<IRenderable>();
	private WorldShader worldShader;
	private ShadowShader shadowShader;
	private WorldDepthShader depthShader;
	private SkyShader atmosphereShader;
	private GUIShader guiShader;
	private Texture fontTexture, blockTextureAtlas, blockNormalMapAtlas, whiteTexture;
	private Transform cameraTransform;
	private Camera camera;
	private Framebuffer shadowFramebuffer;
	private Framebuffer depthFramebuffer;
	private TextMesh overlay;
	private Mesh skyboxMesh;
	public Renderer() {
		glfwInit();
		//glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_FALSE);
		glfwWindowHint(GLFW_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_CORE_PROFILE, GLFW_TRUE);
		//glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		handle = glfwCreateWindow(w, h, "Voxelmine", 0, 0);
		glfwSetWindowSizeCallback(handle, (win,w,h) -> {
			IDisposable.disposeNow(depthFramebuffer);
			depthFramebuffer = new Framebuffer(w, h);
			this.w = w;
			this.h = h;
			Voxelmine.getScreen().onDelete();
			GUIScreen oldScr = Voxelmine.getScreen();
			try {
				Class<?> clazz = oldScr.getClass();
				Constructor<?> cons = clazz.getConstructor();
				Voxelmine.setScreen((GUIScreen)cons.newInstance());
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			Voxelmine.getScreen().onCreate();
		});
		glfwMakeContextCurrent(handle);
		glfwSwapInterval(0);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		skyboxMesh = MeshUtil.loadSphere(new Transform(new Vector3f(), new Vector3f(), new Vector3f(1)));
		IDisposable.disposeOnExit(skyboxMesh);
		glCullFace(GL_CW);
		worldShader = new WorldShader();
		shadowShader = new ShadowShader();
		depthShader = new WorldDepthShader();
		atmosphereShader = new SkyShader();
		guiShader = new GUIShader();
		fontTexture = new Texture("res/default.png");
		blockTextureAtlas = new Texture("res/atlas.png");
		blockNormalMapAtlas = new Texture("res/t_ba_n.png");
		whiteTexture = new Texture("res/t_wt.png");
		cameraTransform = new Transform(new Vector3f(2*Chunk.SIZE, 100, 2*Chunk.SIZE), new Vector3f(), new Vector3f(1, 1, 1));
		Voxelmine.getLocalPlayer().getTransform().setPosition(new Vector3f(World.MAX_SIZE*Chunk.SIZE/2, 200, World.MAX_SIZE*Chunk.SIZE/2));
		camera = new Camera(cameraTransform);
		shadowFramebuffer = new Framebuffer(SHADOWMAP_SIZE, SHADOWMAP_SIZE);
		depthFramebuffer = new Framebuffer(w, h); // Not a bug
		overlay = new TextMesh("Voxelmine Engine v0.0.1a", new Transform(new Vector3f(0, -getFontScaling(), 0), new Vector3f(), new Vector3f(1, 1, 1)));
		glClearColor(0.4f, 0.7f, 1.0f, 1.0f);
	}
	public void setWidth(int w) {
		this.w = w;
	}
	public void setHeight(int h) {
		this.h = h;
	}
	public long getWindowHandle() {
		return handle;
	}
	public Vector3f getTextScaling() {
		return new Vector3f(getFontScaling(), -getFontScaling(), 0);
	}
	public float getAspectRatio() {
		return w/(float)h;
	}
	public void add(IRenderable renderable, boolean worldOrScreen) {
		if(!worldOrScreen) worldObjects.add(renderable);
		else guiObjects.add(renderable);
	}
	public void remove(IRenderable renderable) {
		if(worldObjects.contains(renderable)) {
			worldObjects.remove(renderable);
		}
		if(guiObjects.contains(renderable)) {
			guiObjects.remove(renderable);
		}
	}
	public Transform getCameraTransform() {
		return cameraTransform;
	}
	public boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}
	public void render() {
		camera.move();
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		
		glActiveTexture(GL_TEXTURE0);
		blockTextureAtlas.bind();
		glActiveTexture(GL_TEXTURE1);
		blockNormalMapAtlas.bind();
		glActiveTexture(GL_TEXTURE2);
		shadowFramebuffer.bindTexture();
		glActiveTexture(GL_TEXTURE3);
		depthFramebuffer.bindTexture();
		shadowFramebuffer.bind();
		glViewport(0, 0, (int)shadowFramebuffer.getTextureSize().x, (int)shadowFramebuffer.getTextureSize().y);
		glClear(GL_DEPTH_BUFFER_BIT);
		shadowShader.bind();
		shadowShader.updateUniforms();
		shadowShader.updateUniforms(blockTextureAtlas);
		for(IRenderable renderable : worldObjects) {
			shadowShader.updateUniforms(renderable);
			renderable.render();
		}
		shadowShader.unbind();
		shadowFramebuffer.unbind();
		glViewport(0, 0, w, h);
		depthFramebuffer.bind();
		glClear(GL_DEPTH_BUFFER_BIT);
		depthShader.bind();
		depthShader.updateUniforms();
		depthShader.updateUniforms(blockTextureAtlas);
		for(IRenderable renderable : worldObjects) {
			depthShader.updateUniforms(renderable);
			renderable.render();
		}
		depthShader.unbind();
		depthFramebuffer.unbind();
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		atmosphereShader.bind();
		atmosphereShader.updateUniforms();
		atmosphereShader.updateUniforms(skyboxMesh);
		glDisable(GL_CULL_FACE);
		skyboxMesh.render();
		glEnable(GL_CULL_FACE);
		glCullFace(GL_CW);
		atmosphereShader.unbind();
		glClear(GL_DEPTH_BUFFER_BIT);
		worldShader.bind();
		worldShader.updateUniforms();
		worldShader.updateUniforms(blockTextureAtlas);
		for(IRenderable renderable : worldObjects) {
			worldShader.updateUniforms(renderable);
			renderable.render();
		}
		worldShader.unbind();
		glActiveTexture(GL_TEXTURE0);
		blockTextureAtlas.unbind();
		glActiveTexture(GL_TEXTURE1);
		blockNormalMapAtlas.unbind();
		glActiveTexture(GL_TEXTURE2);
		shadowFramebuffer.unbindTexture();
		glActiveTexture(GL_TEXTURE3);
		depthFramebuffer.unbindTexture();
		glClear(GL_DEPTH_BUFFER_BIT);
		guiShader.bind();
		glActiveTexture(GL_TEXTURE0);
		blockTextureAtlas.bind();
		guiShader.setScaling(1);
		guiShader.updateUniforms();
		for(IRenderable gui : guiObjects) {
			guiShader.updateUniforms(gui);
			gui.render();
		}
		blockTextureAtlas.unbind();
		guiShader.setScaling(-getFontScaling());
		guiShader.updateUniforms();
		fontTexture.bind();
		guiShader.updateUniforms(overlay);
		overlay.render();
		glDisable(GL_DEPTH_TEST);
		for(IRenderable gui : guiObjects) {
			guiShader.updateUniforms(gui);
			gui.renderText();
		}
		glEnable(GL_DEPTH_TEST);
		fontTexture.unbind();
		guiShader.unbind();
		glfwSwapBuffers(handle);
		glfwPollEvents();
	}
	public float getFontScaling() {
		return 1200;
	}
	public void disposeContextAndWindow() {
		IDisposable.disposeAll();
		glfwTerminate();
	}
	public Vector3f getLightDirection() {
		LocalDateTime ldt = LocalDateTime.now();
		float celestialAngle = ldt.getHour()/24.0f+ldt.getMinute()/24.0f/60.0f;
		return new Vector3f(0.67f, 1.0f, 0.33f).normalize().rotateZ(celestialAngle*6.28f);
	}
}
