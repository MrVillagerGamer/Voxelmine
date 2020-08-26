package dev.mcc.world.chunk.gen;

import dev.mcc.world.chunk.Chunk;

public interface IChunkGenerator {
	public Chunk generate(int chunkX, int chunkY, int chunkZ);
}
