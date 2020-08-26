package dev.mcc.world.chunk;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class VoxelData {
	public static final Vector3f[] POSITIONS = new Vector3f[] {
		
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
}
