package dev.mcc.util;

public class Registry<T extends Object> {
	private Object[] arr = new Object[65536];
	public void register(int id, T o) {
		arr[id] = o;
	}
	@SuppressWarnings("unchecked")
	public T get(int id) {
		return (T)arr[id];
	}
}
