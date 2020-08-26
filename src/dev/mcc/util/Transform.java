package dev.mcc.util;

import org.joml.Vector3f;

public class Transform {
	private Vector3f position, rotation, scaling;
	public Transform(Vector3f position, Vector3f rotation, Vector3f scaling) {
		this.position = position;
		this.rotation = rotation;
		this.scaling = scaling;
	}
	public Transform(Transform t) {
		this(new Vector3f(t.getPosition()), new Vector3f(t.getRotation()), new Vector3f(t.getScaling()));
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getRotation() {
		return rotation;
	}
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	public Vector3f getScaling() {
		return scaling;
	}
	public void setScaling(Vector3f scaling) {
		this.scaling = scaling;
	}
}
