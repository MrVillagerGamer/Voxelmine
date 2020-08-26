package dev.mcc.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.render.Mesh;
import dev.mcc.util.IDisposable;
import dev.mcc.util.Transform;

public class GUIFullHeart extends GUI {
	private Mesh mesh;
	
	public GUIFullHeart(GUI parent, int index) {
		this.parent = parent;
		float ar = 1;//Voxelmine.getRenderer().getAspectRatio();
		Vector3f trot = new Vector3f();
		Vector3f tpos = new Vector3f(-ar+0.05f+index*0.067f, 0.90f, 0);
		Vector3f tscl = new Vector3f(0.067f, 0.067f, 0.067f);
		this.localTransform = new Transform(tpos, trot, tscl);
	}
	@Override
	public void onCreate() {
		Vector2f uvmin = new Vector2f(30.0f/32.0f, 30.0f/32.0f);
		Vector2f uvmax = new Vector2f(31.0f/32.0f, 31.0f/32.0f);
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
		
	}

	@Override
	public void onRender() {
		
	}

	@Override
	public void onClick() {
		
	}

	@Override
	public void onFocus() {
		
	}

	@Override
	public void onUnfocus() {
		
	}

	@Override
	public void onKeyPress() {
		
	}

}
