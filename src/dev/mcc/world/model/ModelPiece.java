package dev.mcc.world.model;

import org.joml.Vector3f;

public class ModelPiece {
	private Vector3f min, max;
	public ModelPiece(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}
	public Vector3f getMinimum() {
		return min;
	}
	public Vector3f getMaximum() {
		return max;
	}
	public Vector3f getSize() {
		return new Vector3f(max).sub(min);
	}
}
