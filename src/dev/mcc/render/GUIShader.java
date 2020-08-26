package dev.mcc.render;

import static org.lwjgl.opengl.GL20.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.IRenderable;

public class GUIShader extends Shader {
	public GUIShader() {
		super();
	}
	@Override
	protected String getShaderPath(int type) {
		if(type == GL_VERTEX_SHADER) {
			return "/s_ui_vs.txt";
		}
		return "/s_ui_fs.txt";
	}
	private Matrix4f projectionMatrix;
	private float scaling = 1;
	public void setScaling(float scaling) {
		this.scaling = scaling;
	}
	@Override
	public void updateUniforms() {
		projectionMatrix = new Matrix4f();
		projectionMatrix.identity();
		float ar = Voxelmine.getRenderer().getAspectRatio();
		if(scaling >= 0) projectionMatrix.ortho(-scaling*ar, scaling*ar, -scaling, scaling, -scaling, scaling);
		else projectionMatrix.ortho(scaling*ar, -scaling*ar, -scaling, scaling, scaling, -scaling);
		//projectionMatrix.perspective((float)Math.toRadians(70), Voxelmine.getRenderer().getAspectRatio(), 0.1f, 1000.0f);
		int loc1 = glGetUniformLocation(p, "uniform_ProjMatrix");
		float[] m = new float[16];
		projectionMatrix.get(m);
		glUniformMatrix4fv(loc1, false, m);
		updateShadowProjectionMatrix();
		updateShadowTransformationMatrix();
		updateGlobalTransformationUniforms();
		int loc = glGetUniformLocation(p, "uniform_LightDirection");
		Vector3f d = Voxelmine.getRenderer().getLightDirection();
		glUniform3f(loc, d.x, d.y, d.z);
	}
	@Override
	public void updateUniforms(Texture texture) {
		int loc = glGetUniformLocation(p, "uniform_Sampler");
		glUniform1i(loc, 0);
	}
	@Override
	public void updateUniforms(IRenderable renderable) {
		updateRenderableUniforms(renderable);
	}
}
