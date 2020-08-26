package dev.mcc.world.chunk.gen;

import java.util.Random;

import org.joml.Vector3f;

import dev.mcc.util.Transform;
import dev.mcc.world.block.Block;
import dev.mcc.world.chunk.Chunk;
import dev.mcc.world.chunk.gen.settings.TerrainDecorationSettings;

public class FlatChunkGenerator implements IChunkGenerator{
	private static final int SAND_LEVEL = 0;
	private TerrainDecorationSettings decorSettings;
	public FlatChunkGenerator(TerrainDecorationSettings decorSettings) {
		this.decorSettings = decorSettings;
	}
	public void spawnTree(int[][][] blocks, int x, int y, int z) {
		if(x-2 < 0 || z-2 < 0 || x+2 >= blocks.length || z+2 >= blocks[0][0].length || y+7 >= blocks[0].length) {
			return;
		}
		/*for(int i = -2; i <= 2; i++) {
			for(int j = -2; j <= 2; j++) {
				blocks[x+i][y+3][z+j] = Block.LEAVES.getId();
				blocks[x+i][y+4][z+j] = Block.LEAVES.getId();
			}
		}
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				blocks[x+i][y+5][z+j] = Block.LEAVES.getId();
			}
			blocks[x+i][y+6][z] = Block.LEAVES.getId();
			blocks[x][y+6][z+i] = Block.LEAVES.getId();
		}*/
		blocks[x+1][y+2][z+1] = Block.LEAVES.getId();
		blocks[x+1][y+2][z-1] = Block.LEAVES.getId();
		blocks[x-1][y+2][z+1] = Block.LEAVES.getId();
		blocks[x-1][y+2][z-1] = Block.LEAVES.getId();
		blocks[x][y+2][z+2] = Block.LEAVES.getId();
		blocks[x][y+2][z-2] = Block.LEAVES.getId();
		blocks[x+2][y+2][z] = Block.LEAVES.getId();
		blocks[x-2][y+2][z] = Block.LEAVES.getId();
		blocks[x][y+3][z+1] = Block.LEAVES.getId();
		blocks[x][y+3][z-1] = Block.LEAVES.getId();
		blocks[x+1][y+3][z] = Block.LEAVES.getId();
		blocks[x-1][y+3][z] = Block.LEAVES.getId();
		blocks[x][y+4][z] = Block.LEAVES.getId();
		
		
		for(int i = 0; i <= 3; i++) {
			blocks[x][y+i][z] = Block.WOOD.getId();
		}
	}
	@Override
	public Chunk generate(int chunkX, int chunkY, int chunkZ) {
		int[][][] blocks = new int[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		Perlin perlin = new Perlin(1973);
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					blocks[x][y][z] = Block.AIR.getId();
				}
			}
		}
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				int h = (int)Perlin.AMPLITUDE/2;
				boolean halfGrass = false;
				/*if((int) perlin.noise((x+chunkX*Chunk.SIZE-1)/256.0f, (z+chunkZ*Chunk.SIZE)/256.0f)+(int)Perlin.AMPLITUDE/2 > h) {
					halfGrass = true;
				}else if((int) perlin.noise((x+chunkX*Chunk.SIZE+1)/256.0f, (z+chunkZ*Chunk.SIZE)/256.0f)+(int)Perlin.AMPLITUDE/2 > h) {
					halfGrass = true;
				}else if((int) perlin.noise((x+chunkX*Chunk.SIZE)/256.0f, (z+chunkZ*Chunk.SIZE-1)/256.0f)+(int)Perlin.AMPLITUDE/2 > h) {
					halfGrass = true;
				}else if((int) perlin.noise((x+chunkX*Chunk.SIZE)/256.0f, (z+chunkZ*Chunk.SIZE+1)/256.0f)+(int)Perlin.AMPLITUDE/2 > h) {
					halfGrass = true;
				}*/
				boolean stoneSurface = false;
				int min = h+10; int max = h-10;
				for(int i = -1; i <= 1; i++) {
					for(int j = -1; j <= 1; j++) {
						int h2 = h;
						if(min > h2) min = h2;
						if(max < h2) max = h2;
						if((i == 0 || j == 0) && h2 > h) halfGrass = true;
					}
				}
				if(Math.abs(min-max) >= 2) stoneSurface = true;
				boolean tallGrass = (new Random(x*1213+z*2425).nextInt()%decorSettings.getGrassRarity())==0;
				boolean tree = (new Random(x*1213+z*2425).nextInt()%decorSettings.getTreeRarity())==0;
				
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					if(y + chunkY * Chunk.HEIGHT == h+1) {
						if(!stoneSurface && halfGrass && h + chunkY * Chunk.HEIGHT < SAND_LEVEL) blocks[x][y][z] = Block.HALF_SAND.getId();
						else if(stoneSurface && halfGrass) blocks[x][y][z] = Block.HALF_STONE.getId();
						else if(halfGrass) blocks[x][y][z] = Block.HALF_GRASS.getId();
						else if(!stoneSurface && tree && h + chunkY * Chunk.HEIGHT > SAND_LEVEL) spawnTree(blocks, x, y, z);
						else if(!stoneSurface && tallGrass && h + chunkY * Chunk.HEIGHT > SAND_LEVEL) blocks[x][y][z] = Block.TALLGRASS.getId();
					}
					if(y + chunkY * Chunk.HEIGHT == h) {
						if(stoneSurface) blocks[x][y][z] = Block.STONE.getId();
						else if(halfGrass) blocks[x][y][z] = Block.DIRT.getId();
						else blocks[x][y][z] = Block.GRASS.getId();
					}else if(y + chunkY * Chunk.HEIGHT < h) {
						blocks[x][y][z] = Block.STONE.getId();
					}
				}
				if(h + chunkY * Chunk.HEIGHT < SAND_LEVEL) {
					blocks[x][h<0?0:h][z] = Block.SAND.getId();
				}
			}
		}
		Chunk chunk = new Chunk(new Transform(new Vector3f(chunkX*Chunk.SIZE, chunkY*Chunk.HEIGHT, chunkZ*Chunk.SIZE), new Vector3f(), new Vector3f(1, 1, 1)), blocks);
		return chunk;
	}
}
