package dev.mcc.util;

public class ResourceLocation {
	private String path, namespace, name;
	public ResourceLocation(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
		this.path = "res/" + namespace + "/" + name;
	}
	public String getPath() {
		return path;
	}
	public String getName() {
		return name;
	}
	public String getNamespace() {
		return namespace;
	}
}
