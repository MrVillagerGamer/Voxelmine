package dev.mcc.gui;

import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.render.Mesh;
import dev.mcc.util.Transform;

public class GUIHealthBar extends GUI{
	private int health = 0;
	private boolean healthSet = false;
	public GUIHealthBar(GUI parent) {
		this.parent = parent;
		this.children = new GUI[0];
		updateTransform();
	}
	public void setHealth(int health) {
		this.health = health;
		healthSet = true;
	}
	private void updateTransform() {
		this.localTransform = new Transform(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1));
	}
	@Override
	public void onCreate() {
		updateTransform();
		children = new GUI[10];
		for(int i = 0; i < this.children.length; i++) {
			int health = (this.health - i * 2);
			if(health <= 0) {
				children[i] = new GUINone(this);
			}else if(health == 1) {
				children[i] = new GUIHalfHeart(this, i);
			}else {
				children[i] = new GUIFullHeart(this, i);
			}
		}
		for(GUI gui : children) {
			gui.onCreate();
		}
	}
	@Override
	public void onDelete() {
		for(GUI gui : children) {
			gui.onDelete();
		}
	}
	@Override
	public void onUpdate() {
		if(healthSet) {
			onDelete();
			onCreate();
			healthSet = false;
		}
	}
	@Override
	public void onRender() {
		for(GUI gui : children) {
			gui.onRender();
		}
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
