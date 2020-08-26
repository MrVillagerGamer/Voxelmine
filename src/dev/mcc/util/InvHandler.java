package dev.mcc.util;

import java.util.Map.Entry;

import dev.mcc.entity.Entity;
import dev.mcc.world.item.ItemStack;

public class InvHandler {
	public static boolean give(Entity e, int id) {
		for(Entry<Integer, ItemStack> ent : e.getInventory().entrySet()) {
			ItemStack stk = ent.getValue();
			if(stk.getId() == id && stk.getCount() < 64) {
				stk.setCount(stk.getCount()+1);
				return true;
			}
		}
		for(Entry<Integer, ItemStack> ent : e.getInventory().entrySet()) {
			ItemStack stk = ent.getValue();
			if(stk.getId() == 0) {
				stk.setCount(1);
				stk.setId(id);
				return true;
			}
		}
		return false;
	}
	public static boolean take(Entity e, int id, int count) {
		for(Entry<Integer, ItemStack> ent : e.getInventory().entrySet()) {
			ItemStack stk = ent.getValue();
			if(stk.getId() == id && stk.getCount() > 0) {
				stk.setCount(stk.getCount()-1);
				if(stk.getCount() == 0) stk.setId(0);
				return true;
			}
		}
		return false;
	}
}
