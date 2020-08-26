package dev.mcc.world.item;

import dev.mcc.util.Registry;
import dev.mcc.world.block.Block;
import dev.mcc.world.model.ModelData;

public class Item {
	public static final Registry<Item> REGISTRY = new Registry<Item>();
	private static void register(Block b) {
		Item item = new Item(b);
		item.setId(b.getId());
		item.setTexIndex(b.getTexIndex());
		REGISTRY.register(b.getId(), item);
	}
	public static void init() {
		register(Block.AIR);
		register(Block.GRASS);
		register(Block.DIRT);
		register(Block.STONE);
		register(Block.SAND);
		register(Block.HALF_GRASS);
		register(Block.HALF_SAND);
		register(Block.HALF_STONE);
		register(Block.WOOD);
		register(Block.LEAVES);
		register(Block.TALLGRASS);
	}
	private ModelData model;
	private int[] texIndex;
	private int id;
	public Item(Block block) {
		this.model = block.getModel();
	}
	public ModelData getModel() {
		return model;
	}
	private void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public Item setTexIndex(int[] texIndex) {
		this.texIndex = texIndex;
		return this;
	}
	public int[] getTexIndex() {
		return texIndex;
	}
	public int getTexIndex(int f) {
		return texIndex[f];
	}
}
