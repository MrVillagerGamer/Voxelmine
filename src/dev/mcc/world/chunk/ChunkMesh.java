package dev.mcc.world.chunk;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.render.Mesh;
import dev.mcc.util.Transform;
import dev.mcc.world.block.Block;

public class ChunkMesh extends Mesh{
	private Chunk chunk;
	private static final Vector3f[] POSITIONS = new Vector3f[] {
	
		new Vector3f(0.0f, 0.0f, 0.0f),
		new Vector3f(1.0f, 0.0f, 0.0f),
		new Vector3f(1.0f, 1.0f, 0.0f),
		new Vector3f(0.0f, 1.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(1.0f, 0.0f, 1.0f),
		new Vector3f(1.0f, 1.0f, 1.0f),
		new Vector3f(0.0f, 1.0f, 1.0f),
	
	};
	public static final Vector3f[] NORMALS = new Vector3f[] {

		new Vector3f(0.0f, 0.0f, -1.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(0.0f, 1.0f, 0.0f),
		new Vector3f(0.0f, -1.0f, 0.0f),
		new Vector3f(-1.0f, 0.0f, 0.0f),
		new Vector3f(1.0f, 0.0f, 0.0f)

	};
	public static final Vector3f[] TANGENTS = new Vector3f[] {

		new Vector3f(0.0f, -1.0f, 0.0f),
		new Vector3f(0.0f, 1.0f, 0.0f),
		new Vector3f(1.0f, 0.0f, 0.0f),
		new Vector3f(-1.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, -1.0f),
		new Vector3f(0.0f, 0.0f, 1.0f)

	};
	public static final Vector3f[] BITANGENTS = new Vector3f[] {

		new Vector3f(-1.0f, 0.0f, 0.0f),
		new Vector3f(1.0f, 0.0f, 0.0f),
		new Vector3f(0.0f, 0.0f, 1.0f),
		new Vector3f(0.0f, 0.0f, -1.0f),
		new Vector3f(0.0f, -1.0f, 0.0f),
		new Vector3f(0.0f, 1.0f, 0.0f)

	};
	public static final int[][] INDICES = new int[][] {

		// 0 1 2 2 1 3
		{0, 3, 1, 1, 3, 2}, // Back Face
		{5, 6, 4, 4, 6, 7}, // Front Face
		{3, 7, 2, 2, 7, 6}, // Top Face
		{1, 5, 0, 0, 5, 4}, // Bottom Face
		{4, 7, 0, 0, 7, 3}, // Left Face
		{1, 2, 5, 5, 2, 6} // Right Face

	};
	public static final Vector2f[] TEXCOORDS = new Vector2f[] {

		new Vector2f(0.0f, 1.0f),
		new Vector2f(0.0f, 0.0f),
		new Vector2f(1.0f, 1.0f),
		new Vector2f(1.0f, 1.0f),
		new Vector2f(0.0f, 0.0f),
		new Vector2f(1.0f, 0.0f)

	};
	public ChunkMesh(Chunk chunk) {
		super();
		this.chunk = chunk;
		load();
	}
	public int getBlock(int x, int y, int z) {
		if(x < 0 || y < 0 || z < 0 || x >= Chunk.SIZE || y >= Chunk.HEIGHT || z >= Chunk.SIZE) {
			return 0;
		}
		return chunk.getBlocks()[x][y][z];
	}
	public void load() {
		int[][][] blocks = chunk.getBlocks();
		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texcoords = new ArrayList<Vector2f>();
		ArrayList<Vector3f> tangents = new ArrayList<Vector3f>();
		ArrayList<Vector3f> bitangents = new ArrayList<Vector3f>();
		boolean[][][] finished = new boolean[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int y = 0; y < Chunk.HEIGHT; y++) {
				for(int z = 0; z < Chunk.SIZE; z++) {
					Block b = Block.REGISTRY.get(blocks[x][y][z]);
					boolean faceDone = false;
					Vector3f faceDims = new Vector3f(1, 1, 1);
					if(b.getModel().isRendered() && !finished[x][y][z]) {
						if(b.getModel().getPieces().size() == 1 && b.getModel().getPieces().get(0).getSize().y > 0.99f && !b.getModel().isTransparent()) {
							for(int i = y+1; i < Chunk.HEIGHT; i++) {
								if(blocks[x][i][z] != b.getId() || finished[x][i][z]) {
									break;
								}
								faceDims.y++;
								finished[x][i][z] = true;
							}
						}
						if(b.getModel().getPieces().size() == 1 && b.getModel().getPieces().get(0).getSize().x > 0.99f && !b.getModel().isTransparent()) {
							for(int i = x+1; i < Chunk.SIZE; i++) {
								boolean finishX = false;
								for(int j = y; j < y+(int)(faceDims.y+0.1f); j++) {
									if(blocks[i][j][z] != b.getId() || finished[i][j][z]) {
										finishX = true;
										break;
									}
								}
								if(finishX) {
									break;
								}
								faceDims.x++;
								for(int j = y; j < y+(int)(faceDims.y+0.1f); j++) {
									finished[i][j][z] = true;
								}
							}
						}
						if(b.getModel().getPieces().size() == 1 && b.getModel().getPieces().get(0).getSize().z > 0.99f && !b.getModel().isTransparent()) {
							for(int k = z+1; k < Chunk.SIZE; k++) {
								boolean finishZ = false;
								for(int i = x; i < x+(int)(faceDims.x+0.1f); i++) {
									for(int j = y; j < y+(int)(faceDims.y+0.1f); j++) {
										if(blocks[i][j][k] != b.getId() || finished[i][j][k]) {
											finishZ = true;
											break;
										}
									}
								}
								if(finishZ) {
									break;
								}
								faceDims.z++;
								for(int i = x; i < x+(int)(faceDims.x+0.1f); i++) {
									for(int j = y; j < y+(int)(faceDims.y+0.1f); j++) {
										finished[i][j][k] = true;
									}
								}
							}
						}
						boolean[] solids = new boolean[6];
						for(int f = 0; f < 6; f++) {
							solids[f] = true;
							int sx = (int)(NORMALS[f].x+0.0f)!=0?1:(int)(faceDims.x+0.0f);
							int sy = (int)(NORMALS[f].y+0.0f)!=0?1:(int)(faceDims.y+0.0f);
							int sz = (int)(NORMALS[f].z+0.0f)!=0?1:(int)(faceDims.z+0.0f);
							int dx = (int)(NORMALS[f].x+0.0f)>0?(int)(faceDims.x+0.0f)-1:0;
							int dy = (int)(NORMALS[f].y+0.0f)>0?(int)(faceDims.y+0.0f)-1:0;
							int dz = (int)(NORMALS[f].z+0.0f)>0?(int)(faceDims.z+0.0f)-1:0;
							for(int i = 0; i < sx; i++) {
								for(int j = 0; j < sy; j++) {
									for(int k = 0; k < sz; k++) {
										Vector3f v = new Vector3f(i+x+dx, j+y+dy, k+z+dz);
										//v = v.mul(0.5f).add(new Vector3f(0.5f));
										//v = v.add(new Vector3f(i+x+0.1f, j+y+0.1f, k+z+0.1f));
										v = v.add(new Vector3f(NORMALS[f]));
										//v = v.add(new Vector3f(NORMALS[f]).mul(1.5f).add(new Vector3f(-0.5f)));
										Block b2 = Block.REGISTRY.get(getBlock((int)v.x, (int)v.y, (int)v.z));
										if(!b2.getModel().isRendered() || b2.getModel().isTransparent()) solids[f] = false;
									}
								}
							}
						}
						for(int i = 0; i < b.getModel().getPieces().size(); i++) {
							for(int f = 0; f < 6; f++) {
								//Block b2 = Block.REGISTRY.get(getBlock(x+(int)NORMALS[f].x,y+(int)NORMALS[f].y,z+(int)NORMALS[f].z));
								if(!solids[f]) {
									for(int p = 0; p < 6; p++) {
										Vector3f v = new Vector3f(POSITIONS[INDICES[f][p]]);
										v = v.mul(b.getModel().getPieces().get(i).getSize());
										v = v.add(b.getModel().getPieces().get(i).getMinimum());
										v = v.mul(faceDims);
										v = v.add(new Vector3f(x, y, z));
										positions.add(v);
										Vector3f n = new Vector3f(NORMALS[f]);
										if(b.getModel().useFakeLighting()) {
											n = new Vector3f(0, 1, 0);
										}
										normals.add(n);
										Vector3f c = new Vector3f(1, 1, 1);
										colors.add(c);
										float tx = b.getTexIndex(f);
										float ty = 0;
										Vector2f tuv = new Vector2f(TEXCOORDS[p]);
										tuv.x = tuv.x>0.5f?tuv.x-0.0002f:tuv.x+0.0002f;
										tuv.y = tuv.y>0.5f?tuv.y-0.0002f:tuv.y+0.0002f;
										int fx = 1, fy = 1;
										if((int)NORMALS[f].x != 0) {
											fx = (int) faceDims.z;
											fy = (int) faceDims.y;
											tuv.x *= Math.min(b.getModel().getPieces().get(i).getSize().z, 1);
											tuv.y *= Math.min(b.getModel().getPieces().get(i).getSize().y, 1);
											
										}else if((int)NORMALS[f].y != 0) {
											fx = (int) faceDims.x;
											fy = (int) faceDims.z;
											tuv.x *= Math.min(b.getModel().getPieces().get(i).getSize().x, 1);
											tuv.y *= Math.min(b.getModel().getPieces().get(i).getSize().z, 1);
											
										}else if((int)NORMALS[f].z != 0) {
											fx = (int) faceDims.x;
											fy = (int) faceDims.y;
											tuv.x *= Math.min(b.getModel().getPieces().get(i).getSize().x, 1);
											tuv.y *= Math.min(b.getModel().getPieces().get(i).getSize().y, 1);
											
										}
										Vector2f t = new Vector2f(tuv).div(16).add(new Vector2f(tx, ty).div(16f));
										t = t.add(new Vector2f(fx, fy));
										texcoords.add(t);
										if((p % 3) == 2) {
											Vector3f p2 = positions.get(positions.size()-2);
											Vector3f p1 = positions.get(positions.size()-3);
											Vector3f tangent = new Vector3f(p2).sub(p1).normalize();
											tangents.add(tangent);
											tangents.add(tangent);
											tangents.add(tangent);
											Vector3f bitangent = new Vector3f(tangent).cross(normals.get(normals.size()-1));
											bitangents.add(bitangent);
											bitangents.add(bitangent);
											bitangents.add(bitangent);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		super.load(positions.toArray(new Vector3f[positions.size()]), 
				normals.toArray(new Vector3f[normals.size()]), 
				colors.toArray(new Vector3f[colors.size()]), 
				texcoords.toArray(new Vector2f[texcoords.size()]),
				tangents.toArray(new Vector3f[tangents.size()]),
				bitangents.toArray(new Vector3f[bitangents.size()]));
	}
	@Override
	public Transform getTransform() {
		return chunk.getTransform();
	}
	public Chunk getChunk() {
		return chunk;
	}
	@Override
	public void renderText() {
		// TODO Auto-generated method stub
		
	}
}
