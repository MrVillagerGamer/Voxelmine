package dev.mcc.entity;

import dev.mcc.util.Registry;

public abstract class EntityType {
	public static final Registry<EntityType> REGISTRY = new Registry<EntityType>();
	public static final EntityType PLAYER = new EntityType() {
		@Override
		public Entity newEntity() {
			return new Entity();
		}
	};
	private static void register(int id, EntityType type) {
		type.setId(id);
		REGISTRY.register(id, type);
	}
	public static void init() {
		register(0, PLAYER);
	}
	private int id;
	public EntityType() {
		this.id = -1;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public abstract Entity newEntity();
}
