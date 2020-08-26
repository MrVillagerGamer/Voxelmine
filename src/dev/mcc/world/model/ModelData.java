package dev.mcc.world.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector3f;

import dev.mcc.util.AxisAlignedBB;
import dev.mcc.util.ResourceLocation;

public class ModelData {
	public static final ModelData CUBE = new ModelData(false, new ModelPiece(new Vector3f(0), new Vector3f(1)));
	public static final ModelData TRANSP = new ModelData(true, new ModelPiece(new Vector3f(0), new Vector3f(1)));
	public static final ModelData SLAB = new ModelData(true, new ModelPiece(new Vector3f(0), new Vector3f(1, 0.5f, 1)));
	public static final ModelData PLANT = new ModelData(true, 
			new ModelPiece(new Vector3f(0.5f, 0, 0), new Vector3f(0.5f, 1, 1)),
			new ModelPiece(new Vector3f(0, 0, 0.5f), new Vector3f(1, 1, 0.5f)))
			.setFakeLighting(true);
	public static final ModelData LEAVES = new ModelData(true, new ModelPiece(new Vector3f(0), new Vector3f(1)),
			// Y fuzzies
			new ModelPiece(new Vector3f(-0.167f, 0.33f, -0.167f), new Vector3f(1.167f, 0.33f, 1.167f)), 
			new ModelPiece(new Vector3f(-0.167f, 0.67f, -0.167f), new Vector3f(1.167f, 0.67f, 1.167f)),
			// X fuzzies
			new ModelPiece(new Vector3f(0.33f, -0.167f, -0.167f), new Vector3f(0.33f, 1.167f, 1.167f)), 
			new ModelPiece(new Vector3f(0.67f, -0.167f, -0.167f), new Vector3f(0.67f, 1.167f, 1.167f)),
			// Z fuzzies
			new ModelPiece(new Vector3f(-0.167f, -0.167f, 0.33f), new Vector3f(1.167f, 1.167f, 0.33f)), 
			new ModelPiece(new Vector3f(-0.167f, -0.167f, 0.67f), new Vector3f(1.167f, 1.167f, 0.67f)))
			.setBounds(new AxisAlignedBB(new Vector3f(-0.167f), new Vector3f(1.167f)));
	private ArrayList<ModelPiece> pieces = new ArrayList<ModelPiece>();
	public static final ModelData EMPTY = new ModelData(true).setRendered(false);
	private boolean transparent;
	private boolean rendered;
	private boolean fakeLight;
	private AxisAlignedBB bounds;
	public ModelData(boolean trans, ModelPiece... pieces) {
		this.transparent = trans;
		this.rendered = true;
		this.fakeLight = false;
		for(ModelPiece piece : pieces) {
			this.pieces.add(piece);
		}
		this.bounds = new AxisAlignedBB(new Vector3f(), new Vector3f(1));
	}
	public ModelData setBounds(AxisAlignedBB bounds) {
		this.bounds = bounds;
		return this;
	}
	public AxisAlignedBB getBounds() {
		return bounds;
	}
	public ModelData(ResourceLocation location) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(location.getPath() + ".ini")));
			// TODO: Read, parse, load.
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ModelData setFakeLighting(boolean fakeLight) {
		this.fakeLight = fakeLight;
		return this;
	}
	public boolean useFakeLighting() {
		return fakeLight;
	}
	public boolean isRendered() {
		return rendered;
	}
	public ModelData setRendered(boolean render) {
		this.rendered = render;
		return this;
	}
	public boolean isTransparent() {
		return transparent;
	}
	public ArrayList<ModelPiece> getPieces() {
		return pieces;
	}
}
