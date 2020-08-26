package dev.mcc.gui;

import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.render.Mesh;
import dev.mcc.util.IDisposable;
import dev.mcc.util.MeshUtil;
import dev.mcc.util.Transform;

public class GUIHand extends GUI{
	private Mesh handheld;
	private int id;
	public GUIHand(GUI parent) {
		this.parent = parent;
		children = new GUI[0];
		float ar = Voxelmine.getRenderer().getAspectRatio();
		this.localTransform = new Transform(new Vector3f(0.5f, -1.33f, -0.67f), new Vector3f((float)Math.toRadians(22.5f), (float)Math.toRadians(12.5f), 0), new Vector3f(0.67f));
	}
	public void setItemId(int id) {
		this.id = id;
	}
	public int getItemId() {
		return id;
	}
	@Override
	public void onCreate() {
		Transform t = getGlobalTransform();
		handheld = MeshUtil.loadItem(t, id);
		Voxelmine.getRenderer().add(handheld, true);
	}
	@Override
	public void onDelete() {
		Voxelmine.getRenderer().remove(handheld);
		IDisposable.disposeNow(handheld);
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
