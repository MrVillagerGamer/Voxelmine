package dev.mcc.gui;

public class GUIHudScreen extends GUIScreen{
	public GUIHudScreen() {
		super();
		children = new GUI[3];
		children[0] = new GUIHealthBar(this);
		children[1] = new GUIHotbar(this);
		children[2] = new GUIHand(this);
	}
	public GUIHealthBar getHealthBar() {
		return (GUIHealthBar)children[0];
	}
	public GUIHotbar getHotbar() {
		return (GUIHotbar)children[1];
	}
	public GUIHand getHand() {
		return (GUIHand)children[2];
	}
}
