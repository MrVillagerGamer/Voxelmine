package dev.mcc.gui;

import org.joml.Vector3f;

import dev.mcc.util.Transform;

public abstract class GUI {
	protected GUI parent;
	protected GUI[] children;
	protected Transform localTransform;
	public GUI getParent() {
		return parent;
	}
	public GUI[] getChildren() {
		return children;
	}
	public Transform getLocalTransform() {
		return localTransform;
	}
	public Transform getGlobalTransform() {
		GUI current = this;
		Transform t = new Transform(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1));
		while(current != null) {
			Vector3f tpos = new Vector3f(current.localTransform.getPosition());
			Vector3f trot = new Vector3f(current.localTransform.getRotation());
			Vector3f tscl = new Vector3f(current.localTransform.getScaling());
			t.setPosition(t.getPosition().add(tpos));
			t.setRotation(t.getRotation().add(trot));
			t.setScaling(t.getScaling().mul(tscl));
			current = current.parent;
		}
		return t;
	}
	public abstract void onCreate();
	public abstract void onDelete();
	public abstract void onUpdate();
	public abstract void onRender();
	public abstract void onClick();
	public abstract void onFocus();
	public abstract void onUnfocus();
	public abstract void onKeyPress();
	
}
