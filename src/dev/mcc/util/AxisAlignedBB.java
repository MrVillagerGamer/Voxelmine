package dev.mcc.util;

import org.joml.Vector3f;

public class AxisAlignedBB {
	private Vector3f min, max;
	public AxisAlignedBB(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}
	public Vector3f getMin() {
		return min;
	}
	public Vector3f getMax() {
		return max;
	}
	public Vector3f getSize() {
		return new Vector3f(max).sub(min).sub(new Vector3f(0.001f));
	}
}
