package dev.mcc.render;

import static org.lwjgl.opengl.GL20.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.IRenderable;

public class WorldDepthShader extends Shader {
	public WorldDepthShader() {
		super();
	}
	@Override
	protected String getShaderPath(int type) {
		if(type == GL_VERTEX_SHADER) {
			return "/s_dp_vs.txt";
		}
		return "/s_dp_fs.txt";
	}
	@Override
	public void updateUniforms() {
		updateCameraProjectionMatrix();
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
