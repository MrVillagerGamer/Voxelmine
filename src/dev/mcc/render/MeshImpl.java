package dev.mcc.render;

import dev.mcc.util.Transform;

public class MeshImpl extends Mesh {
	private Transform transform;
	public MeshImpl(Transform transform) {
		this.transform = transform;
	}
	@Override
	public Transform getTransform() {
		return transform;
	}
	@Override
	public void renderText() {
		
	}

}
