
package dev.mcc.world.chunk.gen;

import java.util.Random;

// This class was written by ThinMatrix.
// Slightly modified for my needs.
public class Perlin {

	public static final float AMPLITUDE = 48f;
	private static final int OCTAVES = 5;

	private Random random = new Random();
	private int seed;
	private int xOffset = 0;
	private int zOffset = 0;

	public Perlin() {
		this.seed = random.nextInt(1000000000);
	}
	
	public Perlin(int seed) {
		this.seed = seed;
	}

	public float noise(float f, float g) {
		float total = 0;
		for(int i=1;i<=OCTAVES;i++){
			float freq = (float) (Math.pow(i, 2));
			float amp = AMPLITUDE / (float) Math.pow(i, 2);
			total += getInterpolatedNoise((f+xOffset)*freq, (g + zOffset)*freq) * amp;
		}
		return total+(int)(AMPLITUDE/1.5f);
	}
	
	private float getInterpolatedNoise(float x, float z){
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = getNoise(intX, intZ);
		float v2 = getNoise(intX + 1, intZ);
		float v3 = getNoise(intX, intZ + 1);
		float v4 = getNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return 1.0f-Math.abs(0.5f-interpolate(i1, i2, fracZ));
	}
	
	private float interpolate(float a, float b, float blend){
		double theta = blend * Math.PI;
		float f = (float)(1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}

	private float getNoise(int x, int z) {
		random.setSeed(x * 49632 + z * 325176 + seed);
		return random.nextFloat() * 2f - 1f;
	}

}

