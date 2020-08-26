package dev.mcc.world.block;

import dev.mcc.audio.Sound;
import dev.mcc.util.Registry;
import dev.mcc.world.model.ModelData;

public class Block {
	public static final Registry<Block> REGISTRY = new Registry<Block>();
	public static final Block AIR = new Block().setSolid(false).setModel(ModelData.EMPTY);
	public static final Block GRASS = new Block().setSolid(true).setTexIndex(1, 2, 3);
	public static final Block DIRT = new Block().setSolid(true).setTexIndex(3, 3, 3);
	public static final Block STONE = new Block().setSolid(true).setTexIndex(0, 0, 0);
	public static final Block SAND = new Block().setSolid(true).setTexIndex(4, 4, 4);
	public static final Block HALF_GRASS = new Block().setSolid(true).setTexIndex(1, 2, 3).setModel(ModelData.SLAB);
	public static final Block HALF_SAND = new Block().setSolid(true).setTexIndex(4, 4, 4).setModel(ModelData.SLAB);
	public static final Block HALF_STONE = new Block().setSolid(true).setTexIndex(0, 0, 0).setModel(ModelData.SLAB);
	public static final Block WOOD = new Block().setSolid(true).setTexIndex(5, 6, 5);
	public static final Block LEAVES = new Block().setSolid(true).setTexIndex(7, 7, 7).setModel(ModelData.TRANSP);
	public static final Block TALLGRASS = new Block().setSolid(false).setTexIndex(8, 8, 8).setModel(ModelData.PLANT);
	public static final Block WHITE = new Block().setTexIndex(10, 10, 10);
	private static void register(int id, Block block) {
		block.setId(id);
		REGISTRY.register(id, block);
	}
	public static void init() {
		register(0, AIR);
		register(1, GRASS);
		register(2, DIRT);
		register(3, STONE);
		register(4, SAND);
		register(5, HALF_GRASS);
		register(6, HALF_SAND);
		register(7, HALF_STONE);
		register(8, WOOD);
		register(9, LEAVES);
		register(10, TALLGRASS);
		register(11, WHITE);
	}
	private int id;
	private boolean solid;
	private int[] texIndex;
	private Sound sound;
	private ModelData model;
	public Block() {
		this.id = -1;
		this.solid = true;
		this.texIndex = new int[] {0, 0, 0, 0, 0, 0};
		this.sound = Sound.DIRT;
		this.model = ModelData.CUBE;
	}
	public ModelData getModel() {
		return model;
	}
	public Block setModel(ModelData model) {
		this.model = model;
		return this;
	}
	private void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	private Block setSolid(boolean solid) {
		this.solid = solid;
		return this;
	}
	public boolean isSolid() {
		return solid;
	}
	private Block setTexIndex(int top, int side, int bottom) {
		this.texIndex = new int[] {side, side, top, bottom, side, side};
		return this;
	}
	public int[] getTexIndex() {
		return texIndex;
	}
	public int getTexIndex(int f) {
		return texIndex[f];
	}
	public Block setSound(Sound sound) {
		this.sound = sound;
		return this;
	}
	public Sound getSound() {
		return sound;
	}
}
