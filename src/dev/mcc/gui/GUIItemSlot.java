package dev.mcc.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.render.Mesh;
import dev.mcc.render.TextMesh;
import dev.mcc.util.IDisposable;
import dev.mcc.util.MeshUtil;
import dev.mcc.util.Transform;
import dev.mcc.world.block.Block;
import dev.mcc.world.item.ItemStack;

public class GUIItemSlot extends GUI {
	private Mesh mesh, itemMesh, textMesh;
	private ItemStack stk;
	public GUIItemSlot(GUI parent, float x, float y, float w, float h) {
		this.parent = parent;
		this.stk = new ItemStack(0, 0);
		//float ar = 1;//Voxelmine.getRenderer().getAspectRatio();
		Vector3f trot = new Vector3f();
		Vector3f tpos = new Vector3f(x, y, 0);
		Vector3f tscl = new Vector3f(w, h, (w+h)/2);
		this.localTransform = new Transform(tpos, trot, tscl);
	}
	private void loadItem() {
		Transform t = new Transform(getGlobalTransform());
		if(Block.REGISTRY.get(stk.getId()) != null) { // If the item is a block
			t.getRotation().x = (float) Math.toRadians(22.5f);
			t.getRotation().z = (float) Math.toRadians(0.0f);
			t.getRotation().y = (float) Math.toRadians(45.0f);
		}
		t.setScaling(new Vector3f(0.075f));
		itemMesh = MeshUtil.loadItem(t, stk.getId());
		Voxelmine.getRenderer().add(itemMesh, true);
	}
	@Override
	public void onCreate() {
		Vector2f uvmin = new Vector2f(15.0f/16.0f, 15.0f/16.0f);
		Vector2f uvmax = new Vector2f(14.0f/16.0f, 16.0f/16.0f);
		mesh = new GUIMesh(this, new Vector3f(1, 1, 1), uvmin, uvmax);
		Voxelmine.getRenderer().add(mesh, true);
		Transform t = new Transform(getGlobalTransform());
		t.setScaling(new Vector3f(1));
		t.getPosition().mul(Voxelmine.getRenderer().getFontScaling());
		t.getPosition().y = -t.getPosition().y-0.067f*Voxelmine.getRenderer().getFontScaling();
		t.getPosition().x = t.getPosition().x+0.067f*Voxelmine.getRenderer().getFontScaling();
		if(stk.getCount() != 0) textMesh = new TextMesh(Integer.toString(stk.getCount()), t);
		else textMesh = new TextMesh("", t);
		Voxelmine.getRenderer().add(textMesh, true);
		loadItem();
	}
	@Override
	public void onDelete() {
		Voxelmine.getRenderer().remove(mesh);
		IDisposable.disposeNow(mesh);
		Voxelmine.getRenderer().remove(itemMesh);
		IDisposable.disposeNow(itemMesh);
		Voxelmine.getRenderer().remove(textMesh);
		IDisposable.disposeNow(textMesh);
	}
	public ItemStack getItemStack() {
		return stk;
	}
	public void setItemStack(ItemStack stk) {
		this.stk = stk;
	}
	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnfocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPress() {
		// TODO Auto-generated method stub

	}

}
