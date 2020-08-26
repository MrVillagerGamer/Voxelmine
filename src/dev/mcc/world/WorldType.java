package dev.mcc.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import dev.mcc.util.Registry;
import dev.mcc.world.chunk.gen.DefaultChunkGenerator;
import dev.mcc.world.chunk.gen.FlatChunkGenerator;
import dev.mcc.world.chunk.gen.IChunkGenerator;
import dev.mcc.world.chunk.gen.settings.TerrainDecorationSettings;


public class WorldType {
	public static final Registry<WorldType> WORLD_TYPES = new Registry<WorldType>();
	public static final WorldType DEFAULT = new WorldType("Default", DefaultChunkGenerator.class, TerrainDecorationSettings.DEFAULT);
	public static final WorldType FLAT = new WorldType("Default", FlatChunkGenerator.class, TerrainDecorationSettings.FLAT);
	public static void init() {
		WORLD_TYPES.register(0, DEFAULT);
		WORLD_TYPES.register(1, FLAT);
	}
	private IChunkGenerator generator;
	private String name;
	public WorldType(String displayName, Class<?> generator, TerrainDecorationSettings decorSettings) {
		this.name = displayName;
		try {
			Constructor<?> cons = generator.getDeclaredConstructor(TerrainDecorationSettings.class);
			this.generator = (IChunkGenerator)cons.newInstance(decorSettings);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public String getName() {
		return name;
	}
	public World newWorld() {
		return new World(generator);
	}
}
