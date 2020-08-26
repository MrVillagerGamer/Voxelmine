package dev.mcc.gui;

import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.entity.Entity;
import dev.mcc.render.TextMesh;
import dev.mcc.util.IDisposable;
import dev.mcc.util.Transform;
import dev.mcc.world.block.Block;
import dev.mcc.world.item.ItemStack;

public class GUIHotbar extends GUI{
	private int sel;
	private TextMesh textMesh;
	public GUIHotbar(GUI parent) {
		this.parent = parent;
		this.sel = 0;
		children = new GUI[10];
		for(int i = 0; i < 9; i++) {
			children[i] = new GUIItemSlot(this, (i-4.5f)/7.5f, -0.9f, 0.133f, 0.133f);
		}
		children[9] = new GUIItemSel(this, (sel-4.5f)/7.5f, -0.9f, 0.133f, 0.133f);
		localTransform = new Transform(new Vector3f(0, 0, 0), new Vector3f(), new Vector3f(1, 1, 1f));
	}
	public void setSelection(int sel) {
		sel %= 36;
		if(sel < 0) sel = 36+sel;
		this.sel = sel;
		children[9].onDelete();
		children[9] = new GUIItemSel(this, ((sel%9)-4.5f)/7.5f, -0.9f, 0.133f, 0.133f);
		children[9].onCreate();
		onDelete();
		onCreate();
		updateHand();
	}
	public void updateHand() {
		GUIScreen cscr = Voxelmine.getScreen();
		if(cscr instanceof GUIHudScreen &&
				((GUIItemSlot)children[sel%9]).getItemStack().getId() != ((GUIHudScreen)cscr).getHand().getItemId()) {
			GUIHand hand = ((GUIHudScreen)cscr).getHand();
			hand.setItemId(((GUIItemSlot)children[sel%9]).getItemStack().getId());
			hand.onDelete();
			hand.onCreate();
		}
	}
	public int getSelection() {
		return sel;
	}
	@Override
	public void onCreate() {
		for(GUI gui : children) {
			gui.onCreate();
		}
		Transform t = new Transform(getGlobalTransform());
		t.setScaling(new Vector3f(1));
		t.setPosition(new Vector3f(-0.7f, -0.8f, 0));
		t.getPosition().mul(Voxelmine.getRenderer().getTextScaling());
		textMesh = new TextMesh(Integer.toString(sel/9+1), t);
		Voxelmine.getRenderer().add(textMesh, true);
	}
	@Override
	public void onDelete() {
		for(GUI gui : children) {
			gui.onDelete();
		}
		Voxelmine.getRenderer().remove(textMesh);
		IDisposable.disposeNow(textMesh);
	}
	@Override
	public void onUpdate() {
		updateHand();
		Entity player = Voxelmine.getLocalPlayer();
		for(int i = 0; i < 36; i++) {
			if(sel/9 == i/9) {
				if(!player.getInventory().containsKey(i)) {
					player.getInventory().put(i, new ItemStack(Block.AIR.getId(), 0));
				}
				ItemStack stk = player.getInventory().get(i);
				if(stk.getId() != ((GUIItemSlot)children[i%9]).getItemStack().getId() ||
						stk.getCount() != ((GUIItemSlot)children[i%9]).getItemStack().getCount()) {
					((GUIItemSlot)children[i%9]).setItemStack(new ItemStack(stk));
					children[i%9].onDelete();
					children[i%9].onCreate();
				}
			}
		}
	}
	@Override
	public void onRender() {
		
	}
	@Override
	public void onClick() {
		
	}
	@Override
	public void onFocus() {
		
	}
	@Override
	public void onUnfocus() {
		
	}
	@Override
	public void onKeyPress() {
		
	}
}
