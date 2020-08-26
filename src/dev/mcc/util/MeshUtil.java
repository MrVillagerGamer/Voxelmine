package dev.mcc.util;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.render.Mesh;
import dev.mcc.render.MeshImpl;
import dev.mcc.world.block.Block;
import dev.mcc.world.chunk.VoxelData;
import dev.mcc.world.item.Item;
import dev.mcc.world.model.ModelData;

public class MeshUtil {
	public static Mesh loadSphere(Transform transform) {
		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texcoords = new ArrayList<Vector2f>();
		ArrayList<Vector3f> tangents = new ArrayList<Vector3f>();
		ArrayList<Vector3f> bitangents = new ArrayList<Vector3f>();
		float x, y, z, xy;
		int res = 30;
		float sectorStep = 2 * (float)Math.PI / res;
		float stackStep = (float)Math.PI / res;
		float sectorAngle, stackAngle;
		for(int i = 0; i <= res; i++) {
			stackAngle = (float)Math.PI / 2 - i * stackStep;
			xy = 1.0f * (float)Math.cos(stackAngle);
		    z = 1.0f * (float)Math.sin(stackAngle);
			for(int j = 0; j <= res; j++) {
				sectorAngle = j * sectorStep;
				x = xy * (float)Math.cos(sectorAngle);
		        y = xy * (float)Math.sin(sectorAngle);
				positions.add(new Vector3f(x, y, z));
			}
		}
		Vector3f[] newPositions = new Vector3f[res*res*6];
		int k1, k2, idx = 0;
		for(int i = 0; i < res; i++) {
		    k1 = i * (res + 1);
		    k2 = k1 + res + 1;
		    for(int j = 0; j < res; j++, k1++, k2++) {
		    	if(i != 0) {
		    		newPositions[idx] = positions.get(k1);
			    	newPositions[idx+1] = positions.get(k2);
			    	newPositions[idx+2] = positions.get(k1 + 1);
		    	}else {
		    		newPositions[idx] = new Vector3f();
			    	newPositions[idx+1] = new Vector3f();
			    	newPositions[idx+2] = new Vector3f();
		    	}
		    	if(i != res-1) {
		    		newPositions[idx+3] = positions.get(k1 + 1);
			    	newPositions[idx+4] = positions.get(k2);
			    	newPositions[idx+5] = positions.get(k2 + 1);
		    	}else {
		    		newPositions[idx+3] = new Vector3f();
			    	newPositions[idx+4] = new Vector3f();
			    	newPositions[idx+5] = new Vector3f();
		    	}
		    	idx += 6;
		        for(int k = 0; k < 6; k++) {
			        normals.add(new Vector3f());
			        texcoords.add(new Vector2f());
			        tangents.add(new Vector3f());
			        bitangents.add(new Vector3f());
			        colors.add(new Vector3f(1));
		        }
		    }
		}
		Mesh itemMesh = new MeshImpl(transform);
		itemMesh.load(newPositions,
				normals.toArray(new Vector3f[normals.size()]), 
				colors.toArray(new Vector3f[colors.size()]), 
				texcoords.toArray(new Vector2f[texcoords.size()]),
				tangents.toArray(new Vector3f[tangents.size()]),
				bitangents.toArray(new Vector3f[bitangents.size()]));
		return itemMesh;
	}
	public static Mesh loadCube(Transform transform) {

		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texcoords = new ArrayList<Vector2f>();
		ArrayList<Vector3f> tangents = new ArrayList<Vector3f>();
		ArrayList<Vector3f> bitangents = new ArrayList<Vector3f>();
		for(int i = 0; i < ModelData.CUBE.getPieces().size(); i++) {
			for(int f = 0; f < 6; f++) {
				for(int p = 0; p < 6; p++) {
					Vector3f v = new Vector3f(VoxelData.POSITIONS[VoxelData.INDICES[f][p]]);
					v = v.mul(ModelData.CUBE.getPieces().get(i).getSize());
					v = v.add(ModelData.CUBE.getPieces().get(i).getMinimum());
					//v = v.mul(1);
					//v = v.add(new Vector3f(0.25f, 0.33f, -0.0f));
					positions.add(v);
					Vector3f n = new Vector3f(VoxelData.NORMALS[f]);
					normals.add(n);
					Vector3f c = new Vector3f(1, 1, 1);
					colors.add(c);
					float tx = 0;
					float ty = 0;
					Vector2f tuv = new Vector2f(VoxelData.TEXCOORDS[p]);
					int fx = 1, fy = 1;
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
		Mesh itemMesh = new MeshImpl(transform);
		itemMesh.load(positions.toArray(new Vector3f[positions.size()]), 
				normals.toArray(new Vector3f[normals.size()]), 
				colors.toArray(new Vector3f[colors.size()]), 
				texcoords.toArray(new Vector2f[texcoords.size()]),
				tangents.toArray(new Vector3f[tangents.size()]),
				bitangents.toArray(new Vector3f[bitangents.size()]));
		return itemMesh;
	}
	public static Mesh loadItem(Transform transform, int id) {
		
		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texcoords = new ArrayList<Vector2f>();
		ArrayList<Vector3f> tangents = new ArrayList<Vector3f>();
		ArrayList<Vector3f> bitangents = new ArrayList<Vector3f>();
		Item b = Item.REGISTRY.get(id);
		for(int i = 0; i < b.getModel().getPieces().size(); i++) {
			for(int f = 0; f < 6; f++) {
				for(int p = 0; p < 6; p++) {
					Vector3f v = new Vector3f(VoxelData.POSITIONS[VoxelData.INDICES[f][p]]);
					v = v.mul(b.getModel().getPieces().get(i).getSize());
					v = v.add(b.getModel().getPieces().get(i).getMinimum());
					v = v.mul(1);
					v = v.add(new Vector3f(0.25f, 0.33f, -0.0f));
					positions.add(v);
					Vector3f n = new Vector3f(VoxelData.NORMALS[f]);
					if(b.getModel().useFakeLighting()) {
						n = new Vector3f(0, 1, 0);
					}
					normals.add(n);
					Vector3f c = new Vector3f(1, 1, 1);
					colors.add(c);
					float tx = b.getTexIndex(f);
					float ty = 0;
					Vector2f tuv = new Vector2f(VoxelData.TEXCOORDS[p]);
					int fx = 1, fy = 1;
					if((int)VoxelData.NORMALS[f].x != 0) {
						fx = (int) 1;
						fy = (int) 1;
						tuv.x *= b.getModel().getPieces().get(i).getSize().z;
						tuv.y *= b.getModel().getPieces().get(i).getSize().y;
					}else if((int)VoxelData.NORMALS[f].y != 0) {
						fx = (int) 1;
						fy = (int) 1;
						tuv.x *= b.getModel().getPieces().get(i).getSize().x;
						tuv.y *= b.getModel().getPieces().get(i).getSize().z;
					}else if((int)VoxelData.NORMALS[f].z != 0) {
						fx = (int) 1;
						fy = (int) 1;
						tuv.x *= b.getModel().getPieces().get(i).getSize().x;
						tuv.y *= b.getModel().getPieces().get(i).getSize().y;
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
		Mesh itemMesh = new MeshImpl(transform);
		itemMesh.load(positions.toArray(new Vector3f[positions.size()]), 
				normals.toArray(new Vector3f[normals.size()]), 
				colors.toArray(new Vector3f[colors.size()]), 
				texcoords.toArray(new Vector2f[texcoords.size()]),
				tangents.toArray(new Vector3f[tangents.size()]),
				bitangents.toArray(new Vector3f[bitangents.size()]));
		return itemMesh;
	}
}
