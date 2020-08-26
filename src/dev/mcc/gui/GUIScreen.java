package dev.mcc.gui;

import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.Transform;

public class GUIScreen extends GUI{
	public GUIScreen() {
		parent = null;
		children = new GUI[0];
		localTransform = new Transform(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f(1, 1, 1f));
	}
	@Override
	public void onCreate() {
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
		for(GUI gui : children) {
			gui.onUpdate();
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
