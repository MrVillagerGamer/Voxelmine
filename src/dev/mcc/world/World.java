package dev.mcc.world;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.entity.DamageSource;
import dev.mcc.entity.Entity;
import dev.mcc.entity.EntityType;
import dev.mcc.util.AxisAlignedBB;
import dev.mcc.util.IDisposable;
import dev.mcc.world.chunk.Chunk;
import dev.mcc.world.chunk.ChunkMesh;
import dev.mcc.world.chunk.gen.IChunkGenerator;

public class World {
	private static final int SIZE = 12;
	public static final int MAX_SIZE = 1024;
	private static final int MAX_HEIGHT = 1;
	private ChunkMesh[][][] meshes = new ChunkMesh[MAX_SIZE][MAX_HEIGHT][MAX_SIZE];
	private Chunk[][][] chunks = new Chunk[MAX_SIZE][MAX_HEIGHT][MAX_SIZE];
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<ChunkMesh> loadedChunks = new ArrayList<ChunkMesh>();
	private IChunkGenerator gen;
	public World(IChunkGenerator gen) {
		this.gen = gen;
	}
	private boolean generated = false;
	public Entity spawnEntity(Vector3f pos, int id) {
		Entity e = EntityType.REGISTRY.get(id).newEntity();
		entities.add(e);
		return e;
	}
	public void killEntity(Entity e) {
		entities.remove(e);
	}
	public List<Entity> getEntities() {
		List<Entity> result = new ArrayList<Entity>();
		for(Entity e : entities) {
			result.add(e);
		}
		return result;
	}
	public List<Entity> getEntities(float x1, float y1, float z1, float x2, float y2, float z2) {
		Vector3f min = new Vector3f(x1, y1, z1);
		Vector3f max = new Vector3f(x2, y2, z2);
		return getEntities(min, max);
	}
	public List<Entity> getEntities(AxisAlignedBB area) {
		Vector3f min = area.getMin();
		Vector3f max = area.getMax();
		return getEntities(min, max);
	}
	public List<Entity> getEntities(Vector3f min, Vector3f max) {
		List<Entity> result = new ArrayList<Entity>();
		
		for(Entity e : entities) {
			Vector3f p = e.getTransform().getPosition();
			if(p.x >= min.x && p.y >= min.y && p.z >= min.z) {
				if(p.x < max.x && p.y < max.y && p.z < max.z) {
					result.add(e);
				}
			}
		}
		return result;
	}
	public boolean wasGenerated() {
		return generated;
	}
	public int getBlockRel(Chunk chunk, int x, int y, int z) {
		int cx = (int) chunk.getTransform().getPosition().x;
		int cy = (int) chunk.getTransform().getPosition().y;
		int cz = (int) chunk.getTransform().getPosition().z;
		if((x+cx)/Chunk.SIZE >= MAX_SIZE || (y+cy)/Chunk.HEIGHT >= MAX_HEIGHT || (z+cz)/Chunk.SIZE >= MAX_SIZE
				|| (x+cx)/Chunk.SIZE < 0 || (y+cy)/Chunk.HEIGHT < 0 || (z+cz)/Chunk.SIZE < 0) {
			return 0;
		}
		int bx = (x+cx)%Chunk.SIZE;
		int by = (y+cy)%Chunk.HEIGHT;
		int bz = (z+cz)%Chunk.SIZE;
		if(bx < 0) bx = Chunk.SIZE+bx;
		if(by < 0) by = Chunk.HEIGHT+by;
		if(bz < 0) bz = Chunk.SIZE+bz;
		
		int cx2 = (x+cx)/Chunk.SIZE;
		int cy2 = (y+cy)/Chunk.HEIGHT;
		int cz2 = (z+cz)/Chunk.SIZE;
		Chunk chunk2 = chunks[cx2][cy2][cz2];
		if(chunk2 == null) return 0;
		return chunk2.getBlocks()[bx][by][bz];
	}
	public void update() {
		Vector3f pos = new Vector3f(Voxelmine.getRenderer().getCameraTransform().getPosition()).div(Chunk.SIZE);
		generated = chunks[(int)pos.x][0][(int)pos.z]!=null;
		boolean done = false;
		ArrayList<ChunkMesh> toDestroy = new ArrayList<ChunkMesh>();
		for(ChunkMesh chunk : loadedChunks) {
			toDestroy.add(chunk);
		}
		for(int cx = (int)pos.x - SIZE - 2; cx <= (int)pos.x + SIZE + 2; cx++) {
			for(int cz = (int)pos.z - SIZE - 2; cz <= (int)pos.z + SIZE + 2; cz++) {
				for(int cy = 0; cy < MAX_HEIGHT; cy++) {
					if(cx < 0 || cy < 0 || cz < 0 || cx >= MAX_SIZE || cy >= MAX_HEIGHT || cz >= MAX_SIZE) {
						continue;
					}
					if(meshes[cx][cy][cz] != null
					&& toDestroy.contains(meshes[cx][cy][cz])) {
						toDestroy.remove(meshes[cx][cy][cz]);
					}
				}
			}
		}
		for(int cx = (int)pos.x - SIZE; cx <= (int)pos.x + SIZE; cx++) {
			for(int cz = (int)pos.z - SIZE; cz <= (int)pos.z + SIZE; cz++) {
				for(int cy = 0; cy < MAX_HEIGHT; cy++) {
					if(cx < 0 || cy < 0 || cz < 0 || cx >= MAX_SIZE || cy >= MAX_HEIGHT || cz >= MAX_SIZE) {
						continue;
					}
					if(meshes[cx][cy][cz] != null) {
						continue;
					}
					if(chunks[cx][cy][cz] == null) chunks[cx][cy][cz] = gen.generate(cx, cy, cz);
					meshes[cx][cy][cz] = new ChunkMesh(chunks[cx][cy][cz]);
					Voxelmine.getRenderer().add(meshes[cx][cy][cz], false);
					loadedChunks.add(meshes[cx][cy][cz]);
					if(!generated) {
						if(cx == (int)pos.x) {
							if(cz == (int)pos.z) {
								//generated = true;
							}
						}
					}
					done = true;
					if(done) {
						break;
					}
				}
				if(done) {
					break;
				}
			}
			if(done) {
				break;
			}
		}
		for(ChunkMesh mesh : toDestroy) {
			int cx = (int)mesh.getTransform().getPosition().x / Chunk.SIZE;
			int cz = (int)mesh.getTransform().getPosition().z / Chunk.SIZE;
			int cy = (int)mesh.getTransform().getPosition().y / Chunk.HEIGHT;
			if(meshes[cx][cy][cz] == null) continue;
			//Chunk chunk = mesh.getChunk();
			//chunks[cx][cy][cz] = null;
			Voxelmine.getRenderer().remove(mesh);
			IDisposable.disposeNow(mesh);
			if(loadedChunks.contains(meshes[cx][cy][cz])) {
				loadedChunks.remove(meshes[cx][cy][cz]);
			}
			meshes[cx][cy][cz] = null;
		}
		toDestroy.clear();
		if(wasGenerated()) {
			for(Entity e : entities) {
				e.update();
			}
		}
	}
	/*
	public void generate(IChunkGenerator gen) {
		for(int x = 0; x < SIZE; x++) {
			for(int z = 0; z < SIZE; z++) {
				for(int y = 0; y < HEIGHT; y++) {
					chunks[x][y][z] = gen.generate(x, y, z);
				}
			}
		}
		for(int x = 0; x < SIZE; x++) {
			for(int z = 0; z < SIZE; z++) {
				for(int y = 0; y < HEIGHT; y++) {
					meshes[x][y][z] = new ChunkMesh(chunks[x][y][z]);
					Voxelmine.getRenderer().add(meshes[x][y][z], false);
				}
			}
		}
	}
	*/
	public int getBlock(int x, int y, int z) {
		if(x/Chunk.SIZE >= MAX_SIZE || y/Chunk.HEIGHT >= MAX_HEIGHT || z/Chunk.SIZE >= MAX_SIZE
				|| x/Chunk.SIZE < 0 || y/Chunk.HEIGHT < 0 || z/Chunk.SIZE < 0) {
			return 0;
		}
		Chunk chunk = chunks[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE];
		if(chunk != null) {
			return chunk.getBlocks()[x%Chunk.SIZE][y%Chunk.HEIGHT][z%Chunk.SIZE];
		}
		return 0;
	}
	public void setBlock(int x, int y, int z, int i) {
		Chunk chunk = chunks[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE];
		if(chunk != null) {
			chunk.getBlocks()[x%Chunk.SIZE][y%Chunk.HEIGHT][z%Chunk.SIZE] = i;
		}
		Voxelmine.getRenderer().remove(meshes[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE]);
		IDisposable.disposeNow(meshes[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE]);
		loadedChunks.remove(meshes[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE]);
		meshes[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE] = new ChunkMesh(chunks[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE]);
		Voxelmine.getRenderer().add(meshes[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE], false);
		loadedChunks.add(meshes[x/Chunk.SIZE][y/Chunk.HEIGHT][z/Chunk.SIZE]);
	}
}
