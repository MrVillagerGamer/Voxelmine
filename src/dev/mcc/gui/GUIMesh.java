package dev.mcc.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.render.Mesh;
import dev.mcc.util.Transform;

public class GUIMesh extends Mesh {
	private GUI gui;
	public GUIMesh(GUI gui, Vector3f color, Vector2f uvmin, Vector2f uvmax) {
		this.gui = gui;
		Vector3f[] v = new Vector3f[] {
			new Vector3f(1.0f, 0.0f, 0.0f),
			new Vector3f(1.0f, 1.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 0.0f),
			new Vector3f(1.0f, 1.0f, 0.0f),
			new Vector3f(0.0f, 1.0f, 0.0f),
		};
		Vector3f[] n = new Vector3f[] {
			new Vector3f(0, 0, -1),
			new Vector3f(0, 0, -1),
			new Vector3f(0, 0, -1),
			new Vector3f(0, 0, -1),
			new Vector3f(0, 0, -1),
			new Vector3f(0, 0, -1),
		};
		Vector3f[] u = new Vector3f[] {
			new Vector3f(0, -1, 0),
			new Vector3f(0, -1, 0),
			new Vector3f(0, -1, 0),
			new Vector3f(0, -1, 0),
			new Vector3f(0, -1, 0),
			new Vector3f(0, -1, 0),
		};
		Vector3f[] b = new Vector3f[] {
			new Vector3f(-1, 0, 0),
			new Vector3f(-1, 0, 0),
			new Vector3f(-1, 0, 0),
			new Vector3f(-1, 0, 0),
			new Vector3f(-1, 0, 0),
			new Vector3f(-1, 0, 0),
		};
		Vector3f[] c = new Vector3f[] {
			color,
			color,
			color,
			color,
			color,
			color,
		};
		Vector2f[] t = new Vector2f[] {
			new Vector2f(0.0f, 1.0f),
			new Vector2f(0.0f, 0.0f),
			new Vector2f(1.0f, 1.0f),
			new Vector2f(1.0f, 1.0f),
			new Vector2f(0.0f, 0.0f),
			new Vector2f(1.0f, 0.0f)
		};
		for(int i = 0; i < t.length; i++) {
			if(t[i].x >= 0.5f) t[i].x = uvmax.x;
			else t[i].x = uvmin.x;
			if(t[i].y >= 0.5f) t[i].y = uvmax.y;
			else t[i].y = uvmin.y;
		}
		load(v, n, c, t, u, b);
	}
	@Override
	public Transform getTransform() {
		return gui.getGlobalTransform();
	}
	@Override
	public void renderText() {
		// TODO Auto-generated method stub
		
	}
}
