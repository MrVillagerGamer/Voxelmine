package dev.mcc.render;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.IDisposable;
import dev.mcc.util.IRenderable;
import dev.mcc.util.Transform;
import dev.mcc.world.chunk.Chunk;

public abstract class Shader implements IDisposable {
	protected int p, vs, fs;
	public Shader() {
		load();
	}
	public void updateCameraProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.identity();
		projectionMatrix.perspective((float)Math.toRadians(70), Voxelmine.getRenderer().getAspectRatio(), 0.1f, 1000.0f);
		int loc = glGetUniformLocation(p, "uniform_ProjMatrix");
		float[] m = new float[16];
		projectionMatrix.get(m);
		glUniformMatrix4fv(loc, false, m);
	}
	public void updateChunkUniforms(Chunk chunk) {
		int loc = glGetUniformLocation(p, "uniform_ChunkLightCount");
		glUniform1i(loc, 0);
		loc = glGetUniformLocation(p, "uniform_HeightMap");
		glUniform1iv(loc, chunk.getHeightMap());
	}
	public void updateShadowProjectionMatrix() {
		Matrix4f projMatrix = new Matrix4f();
		projMatrix.identity();
		projMatrix.ortho(-80, 80, -80, 80, -80, 80);
		Vector3f d = Voxelmine.getRenderer().getLightDirection();
		projMatrix.lookAt(-d.x, -d.y, -d.z, d.x, d.y, d.z, 0, 1, 0);
		int loc = glGetUniformLocation(p, "uniform_DepthProjMatrix");
		float[] m = new float[16];
		projMatrix.get(m);
		glUniformMatrix4fv(loc, false, m);
	}
	public void updateShadowTransformationMatrix() {
		Vector3f pv = Voxelmine.getRenderer().getCameraTransform().getPosition();
		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.identity();
		transformationMatrix.translate(-pv.x, -pv.y, -pv.z);
		
		int loc = glGetUniformLocation(p, "uniform_DepthViewMatrix");
		float[] m = new float[16];
		transformationMatrix.get(m);
		glUniformMatrix4fv(loc, false, m);
	}
	public void updateGlobalTransformationUniformsRotationOnly() {
		Transform transform = Voxelmine.getRenderer().getCameraTransform();
		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.rotate(-transform.getRotation().x, new Vector3f(1, 0, 0));
		transformationMatrix.rotate(-transform.getRotation().y, new Vector3f(0, 1, 0));
		transformationMatrix.rotate(-transform.getRotation().z, new Vector3f(0, 0, 1));
		//transformationMatrix.translate(new Vector3f(transform.getPosition()).negate());
		int loc = glGetUniformLocation(p, "uniform_ViewMatrix");
		float[] m = new float[16];
		transformationMatrix.get(m);
		glUniformMatrix4fv(loc, false, m);
	}
	public void updateGlobalTransformationUniforms() {
		Transform transform = Voxelmine.getRenderer().getCameraTransform();
		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.rotate(-transform.getRotation().x, new Vector3f(1, 0, 0));
		transformationMatrix.rotate(-transform.getRotation().y, new Vector3f(0, 1, 0));
		transformationMatrix.rotate(-transform.getRotation().z, new Vector3f(0, 0, 1));
		transformationMatrix.translate(new Vector3f(transform.getPosition()).negate());
		int loc = glGetUniformLocation(p, "uniform_ViewMatrix");
		float[] m = new float[16];
		transformationMatrix.get(m);
		glUniformMatrix4fv(loc, false, m);
		loc = glGetUniformLocation(p, "uniform_ViewPosition");
		Vector3f pv = Voxelmine.getRenderer().getCameraTransform().getPosition();
		glUniform3f(loc, pv.x, pv.y, pv.z);
	}
	public void updateRenderableUniforms(IRenderable renderable) {
		Transform transform = renderable.getTransform();
		Matrix4f transformationMatrix = new Matrix4f();
		transformationMatrix.identity();
		transformationMatrix.translate(transform.getPosition());
		transformationMatrix.rotate(transform.getRotation().x, new Vector3f(1, 0, 0));
		transformationMatrix.rotate(transform.getRotation().y, new Vector3f(0, 1, 0));
		transformationMatrix.rotate(transform.getRotation().z, new Vector3f(0, 0, 1));
		transformationMatrix.scale(transform.getScaling());
		int loc = glGetUniformLocation(p, "uniform_ModelMatrix");
		float[] m = new float[16];
		transformationMatrix.get(m);
		glUniformMatrix4fv(loc, false, m);
		loc = glGetUniformLocation(p, "uniform_AtlasSize");
		glUniform1i(loc, 16);
	}
	@Override
	public void dispose() {
		glDetachShader(p, vs);
		glDetachShader(p, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);
		glDeleteProgram(p);
	}
	// Called once per frame per rendered object
	public abstract void updateUniforms(IRenderable renderable);
	// Called once per frame
	public abstract void updateUniforms();
	protected abstract String getShaderPath(int type);
	public void bind() {
		glUseProgram(p);
	}
	public void unbind() {
		glUseProgram(0);
	}
	private int loadShader(int type) {
		String source = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(getShaderPath(type))));
			String line;
			while((line = br.readLine()) != null) {
				source += line + "\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);
		System.err.println(glGetShaderInfoLog(shader, 10243));
		return shader;
	}
	public void load() {
		vs = loadShader(GL_VERTEX_SHADER);
		fs = loadShader(GL_FRAGMENT_SHADER);
		p = glCreateProgram();
		glAttachShader(p, vs);
		glAttachShader(p, fs);
		glLinkProgram(p);
		System.err.print(glGetProgramInfoLog(p, 1024));
		glValidateProgram(p);
		IDisposable.disposeOnExit(this);
	}
	public void updateUniforms(Texture texture) {
		// TODO Auto-generated method stub
		
	}
}











