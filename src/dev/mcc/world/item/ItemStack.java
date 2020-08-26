package dev.mcc.world.item;

public class ItemStack {
	private int id;
	private int count;
	public ItemStack(int id, int count) {
		this.id = id;
		this.count = count;
	}
	public ItemStack(ItemStack stk) {
		this(stk.getId(), stk.getCount());
	}
	public int getId() {
		return id;
	}
	public int getCount() {
		return count;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCount(int count) {
		this.count = count;
		if(count == 0) id = 0;
	}
}
