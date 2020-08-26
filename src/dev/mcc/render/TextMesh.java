package dev.mcc.render;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.Transform;

public class TextMesh extends Mesh{
	private Transform transform;
	private ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> colors = new ArrayList<Vector3f>();
	private ArrayList<Vector2f> texcoords = new ArrayList<Vector2f>();
	private ArrayList<Vector3f> tangents = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> bitangents = new ArrayList<Vector3f>();
	public TextMesh(String text, Transform transform) {
		super();
		this.transform = transform;
		loadString(text);
		super.load(positions.toArray(new Vector3f[positions.size()]), 
				normals.toArray(new Vector3f[normals.size()]), 
				colors.toArray(new Vector3f[colors.size()]), 
				texcoords.toArray(new Vector2f[texcoords.size()]),
				tangents.toArray(new Vector3f[tangents.size()]),
				bitangents.toArray(new Vector3f[bitangents.size()]));
	}
	public void loadString(String text) {
		int sx = 0;
		for(int i = 0; i < text.length(); i++) {
			sx += loadChar(sx, text.charAt(i));
		}
	}
	private boolean[] verts = new boolean[] {
		true, false,
		false, false,
		false, true,
		false, true,
		true, true,
		true, false,
	};
	public int loadChar(int sx, char chr) {
		FontData source = Voxelmine.getFontData();
		FontCharData data = source.get(chr);
		if(data != null) {
			float u1 = data.x;
			float u2 = data.x + data.width;
			float v1 = data.y;
			float v2 = data.y + data.height;
			float x1 = data.xOffset + sx;
			float x2 = data.xOffset + sx + data.width;
			float y1 = data.yOffset;
			float y2 = data.yOffset + data.height;
			u1 /= 512.0f; v1 /= 512.0f;
			u2 /= 512.0f; v2 /= 512.0f;
			for(int i = 0; i < 6; i++) {
				float x = 0, y = 0, u = 0, v = 0;
				x = verts[i*2+0]?x2:x1;
				y = verts[i*2+1]?y2:y1;
				u = verts[i*2+0]?u2:u1;
				v = verts[i*2+1]?v2:v1;
				texcoords.add(new Vector2f(u, v));
				positions.add(new Vector3f(x, y, 0));
				colors.add(new Vector3f(1, 1, 1));
				normals.add(new Vector3f(0, 0, -1));
				tangents.add(new Vector3f(0, 0, 0));
				bitangents.add(new Vector3f(0, 0, 0));
			}
			return data.xAdvance;
		}
		return 0;
	}
	@Override
	public Transform getTransform() {
		return transform;
	}
	@Override
	public void renderText() {
		render();
	}
}
