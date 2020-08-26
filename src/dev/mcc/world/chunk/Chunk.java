package dev.mcc.world.chunk;

import dev.mcc.util.Transform;
import dev.mcc.world.block.Block;

public class Chunk {
	public static final int SIZE = 16;
	public static final int HEIGHT = 128;
	private int[][][] blocks;
	private int[] hmap;
	private Transform transform;
	public Chunk(Transform transform, int[][][] blocks) {
		this.transform = transform;
		this.blocks = blocks;
		this.hmap = new int[SIZE*SIZE];
		for(int x = 0; x < SIZE; x++) {
			for(int z = 0; z < SIZE; z++) {
				updateHeightMap(x, z);
			}
		}
	}
	public void setBlocks(int[][][] blocks) {
		this.blocks = blocks;
	}
	public int[][][] getBlocks() {
		return blocks;
	}
	public void updateHeightMap(int x, int z) {
		for(int i = HEIGHT-1; i >= 0; i--) {
			if(Block.REGISTRY.get(blocks[x][i][z]).isSolid()) {
				hmap[x+SIZE*z] = i;
				break;
			}
		}
	}
	public int[] getHeightMap() {
		return hmap;
	}
	public Transform getTransform() {
		return transform;
	}
}
