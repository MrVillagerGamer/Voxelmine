package dev.mcc.world.chunk.gen.settings;

// TODO: Ore generation settings.
public class TerrainDecorationSettings {
	public static final TerrainDecorationSettings DEFAULT = new TerrainDecorationSettings();
	public static final TerrainDecorationSettings FLAT = new TerrainDecorationSettings();
	
	private int treeRarity;
	private int grassRarity;
	public TerrainDecorationSettings() {
		this.treeRarity = 100;
		this.grassRarity = 4;
	}
	public int getTreeRarity() {
		return treeRarity;
	}
	public int getGrassRarity() {
		return grassRarity;
	}
}
