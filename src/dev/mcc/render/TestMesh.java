package dev.mcc.render;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.util.Transform;

public class TestMesh extends Mesh{
	static Vector3f[] v = new Vector3f[] {
		new Vector3f(-0.5f, -0.5f, -2.0f),
		new Vector3f(0.5f, -0.5f, -2.0f),
		new Vector3f(0.5f, 0.5f, -2.0f),
		new Vector3f(-0.5f, 0.5f, -2.0f),
	};
	static Vector3f[] n = new Vector3f[] {
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
	};
	static Vector3f[] c = new Vector3f[] {
		new Vector3f(1.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 1.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(1.0f, 1.0f, 1.0f),
	};
	static Vector2f[] t = new Vector2f[] {
			new Vector2f(4.0f/16.0f, 1.0f/16.0f),
			new Vector2f(3.0f/16.0f, 1.0f/16.0f),
			new Vector2f(3.0f/16.0f, 0.0f/16.0f),
			new Vector2f(4.0f/16.0f, 0.0f/16.0f),
	};
	public TestMesh() {
		//super(v, n, c, t);
	}
	@Override
	public Transform getTransform() {
		return new Transform(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f(1, 1, 1));
	}
	@Override
	public void renderText() {
		// TODO Auto-generated method stub
		
	}
}
