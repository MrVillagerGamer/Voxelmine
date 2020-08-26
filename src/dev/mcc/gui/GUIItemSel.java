package dev.mcc.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.render.Mesh;
import dev.mcc.util.IDisposable;
import dev.mcc.util.Transform;

public class GUIItemSel extends GUI {
	private Mesh mesh;
	public GUIItemSel(GUI parent, float x, float y, float w, float h) {
		this.parent = parent;
		children = new GUI[0];
		//float ar = 1;//Voxelmine.getRenderer().getAspectRatio();
		Vector3f trot = new Vector3f();
		Vector3f tpos = new Vector3f(x, y, 1f);
		Vector3f tscl = new Vector3f(w, h, (w+h)/2);
		this.localTransform = new Transform(tpos, trot, tscl);
	}
	@Override
	public void onCreate() {
		Vector2f uvmin = new Vector2f(14.0f/16.0f, 15.0f/16.0f);
		Vector2f uvmax = new Vector2f(13.0f/16.0f, 16.0f/16.0f);
		mesh = new GUIMesh(this, new Vector3f(1, 1, 1), uvmin, uvmax);
		Voxelmine.getRenderer().add(mesh, true);
	}
	@Override
	public void onDelete() {
		Voxelmine.getRenderer().remove(mesh);
		IDisposable.disposeNow(mesh);
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnfocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPress() {
		// TODO Auto-generated method stub

	}

}
