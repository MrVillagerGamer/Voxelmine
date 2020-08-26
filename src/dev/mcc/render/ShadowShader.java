package dev.mcc.render;

import static org.lwjgl.opengl.GL20.*;

import dev.mcc.util.IRenderable;

public class ShadowShader extends Shader {
	public ShadowShader() {
		super();
	}
	@Override
	public void updateUniforms(IRenderable renderable) {
		updateRenderableUniforms(renderable);
	}
	@Override
	public void updateUniforms() {
		updateShadowProjectionMatrix();
		updateShadowTransformationMatrix();
		updateGlobalTransformationUniforms();
	}
	@Override
	protected String getShaderPath(int type) {
		if(type == GL_VERTEX_SHADER) {
			return "/s_sd_vs.txt";
		}
		return "/s_sd_fs.txt";
	}
}
