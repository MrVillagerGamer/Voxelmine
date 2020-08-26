package dev.mcc.render;

import static org.lwjgl.opengl.GL20.*;

import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.IRenderable;

public class SkyShader extends Shader{
	@Override
	public void updateUniforms(IRenderable renderable) {
		
	}
	@Override
	public void updateUniforms() {
		updateCameraProjectionMatrix();
		updateGlobalTransformationUniformsRotationOnly();
		int loc = glGetUniformLocation(p, "uniform_ViewDir");
		Vector3f rot = Voxelmine.getRenderer().getCameraTransform().getRotation();
		Vector3f d = new Vector3f();
		float horiz = (float)Math.cos(rot.x);
		d.x = -(float)Math.sin(rot.y) * horiz;
		d.z = -(float)Math.cos(rot.y) * horiz;
		d.y = (float)Math.sin(rot.x);
		glUniform3f(loc, d.x, d.y, d.z);
		loc = glGetUniformLocation(p, "uniform_LightDirection");
		d = Voxelmine.getRenderer().getLightDirection();
		glUniform3f(loc, d.x, d.y, d.z);
		loc = glGetUniformLocation(p, "uniform_ViewPosition");
		Vector3f pv = Voxelmine.getRenderer().getCameraTransform().getPosition();
		glUniform3f(loc, pv.x, pv.y, pv.z);
	}
	@Override
	protected String getShaderPath(int type) {
		if(type == GL_VERTEX_SHADER) {
			return "/s_at_vs.txt";
		}
		return "/s_at_fs.txt";
	}
}
