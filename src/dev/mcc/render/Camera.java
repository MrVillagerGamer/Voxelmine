package dev.mcc.render;

import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import java.util.HashMap;

import dev.mcc.Voxelmine;
import dev.mcc.entity.DamageSource;
import dev.mcc.entity.Entity;
import dev.mcc.gui.GUIHotbar;
import dev.mcc.gui.GUIHudScreen;
import dev.mcc.gui.GUIScreen;
import dev.mcc.util.InvHandler;
import dev.mcc.util.Transform;
import dev.mcc.world.World;
import dev.mcc.world.block.Block;
import dev.mcc.world.item.ItemStack;

public class Camera {
	private Transform transform;
	private boolean[] mouseInputLock = new boolean[2];
	private boolean spaceLock = false, eLock = false;
	public Camera(Transform transform) {
		this.transform = transform;
	}
	private boolean inited = false;
	public void move() {
		if(!inited) {
			glfwSetScrollCallback(Voxelmine.getRenderer().getWindowHandle(), (win, dx, dy) -> {
				int val = (int)dy;
				if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) val *= 9;
				GUIScreen scr = Voxelmine.getScreen();
				if(scr instanceof GUIHudScreen) {
					GUIHotbar hotbar = ((GUIHudScreen)scr).getHotbar();
					hotbar.setSelection(hotbar.getSelection()-val);
				}
			});
			inited = true;
		}
		if(!Voxelmine.getWorld().wasGenerated()) {
			return;
		}
		Entity player = Voxelmine.getLocalPlayer();
		float moveSpeed = 5f;
		float turnSpeed = 0.005f;
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		glfwGetCursorPos(Voxelmine.getRenderer().getWindowHandle(), xpos, ypos);
		Vector3f rot = new Vector3f((float)ypos[0], (float)xpos[0], 0).mul(turnSpeed);
		
		player.zeroVelocityXZ();
		Vector3f dir = new Vector3f();
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_W) == GLFW_PRESS) {
			float horiz = 1;
			dir.x = -(float)Math.sin(rot.y) * horiz;
			dir.z = -(float)Math.cos(rot.y) * horiz;
		}else if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_S) == GLFW_PRESS) {
			float horiz = 1;
			dir.x = (float)Math.sin(rot.y) * horiz;
			dir.z = (float)Math.cos(rot.y) * horiz;
		}else if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_D) == GLFW_PRESS) {
			float horiz = 1;
			dir.x = (float)Math.cos(rot.y) * horiz;
			dir.z = -(float)Math.sin(rot.y) * horiz;
		}else if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_A) == GLFW_PRESS) {
			float horiz = 1;
			dir.x = -(float)Math.cos(rot.y) * horiz;
			dir.z = (float)Math.sin(rot.y) * horiz;
		}
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_SPACE) == GLFW_RELEASE) {
			spaceLock = false;
		}
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_SPACE) == GLFW_PRESS && !spaceLock) {
			if(player.getVelocity().y == 0) {
				player.applyForce(new Vector3f(0, 10f, 0), false);
			}
			spaceLock = true;
		}
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_E) == GLFW_RELEASE) {
			eLock = false;
		}
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_E) == GLFW_PRESS && !eLock) {
			boolean visible = glfwGetInputMode(Voxelmine.getRenderer().getWindowHandle(), GLFW_CURSOR)==GLFW_CURSOR_NORMAL;
			glfwSetInputMode(Voxelmine.getRenderer().getWindowHandle(), GLFW_CURSOR, visible?GLFW_CURSOR_DISABLED:GLFW_CURSOR_NORMAL);
			eLock = true;
		}
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_E) == GLFW_RELEASE) {
			eLock = false;
		}
		if(glfwGetKey(Voxelmine.getRenderer().getWindowHandle(), GLFW_KEY_E) == GLFW_PRESS && !eLock) {
			boolean visible = glfwGetInputMode(Voxelmine.getRenderer().getWindowHandle(), GLFW_CURSOR)==GLFW_CURSOR_NORMAL;
			glfwSetInputMode(Voxelmine.getRenderer().getWindowHandle(), GLFW_CURSOR, visible?GLFW_CURSOR_DISABLED:GLFW_CURSOR_NORMAL);
			eLock = true;
		}
		// Left mouse button
		if(glfwGetMouseButton(Voxelmine.getRenderer().getWindowHandle(), GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) {
			mouseInputLock[0] = true;
		}
		if(glfwGetMouseButton(Voxelmine.getRenderer().getWindowHandle(), GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS && mouseInputLock[0]) {
			raycastBreak();
			mouseInputLock[0] = false;
		}
		// Right mouse button
		if(glfwGetMouseButton(Voxelmine.getRenderer().getWindowHandle(), GLFW_MOUSE_BUTTON_RIGHT) == GLFW_RELEASE) {
			mouseInputLock[1] = true;
		}
		if(glfwGetMouseButton(Voxelmine.getRenderer().getWindowHandle(), GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS && mouseInputLock[1]) {
			raycastPlace();
			mouseInputLock[1] = false;
		}
		
		dir = dir.mul(moveSpeed);
		player.applyForce(dir, false);
		player.updatePosition();
		//transform.getPosition().add(dir.mul(moveSpeed * Voxelmine.getDeltaTime()));
		player.getTransform().setRotation(rot);
		transform.setPosition(player.getTransform().getPosition());
		transform.setRotation(player.getTransform().getRotation());
		Voxelmine.getHealthBar().setHealth(player.getHealth());
	}
	public void raycastPlace() {
		Entity player = Voxelmine.getLocalPlayer();
		float reach = 10, step = 0.05f, dist = 0;
		World world = Voxelmine.getWorld();
		Vector3f rot = transform.getRotation();
		Vector3f o = new Vector3f(transform.getPosition());
		//o.y -= player.getBoundingBox().getSize().y;
		Vector3f d = new Vector3f();
		float horiz = (float)Math.cos(rot.x);
		d.x = -(float)Math.sin(rot.y) * step * horiz;
		d.z = -(float)Math.cos(rot.y) * step * horiz;
		d.y = (float)Math.sin(rot.x) * step;
		boolean found = false;
		while(!found && dist < reach) {
			o.x += d.x;
			o.y += d.y;
			o.z += d.z;
			dist += step;
			if(Block.REGISTRY.get(world.getBlock((int)o.x, (int)o.y, (int)o.z)).getModel().isRendered()) {
				found = true;
				break;
			}
		}
		if(found) {
			o.x -= d.x;
			o.y -= d.y;
			o.z -= d.z;
			int sel = ((GUIHudScreen)Voxelmine.getScreen()).getHotbar().getSelection();
			HashMap<Integer, ItemStack> inv = Voxelmine.getLocalPlayer().getInventory();
			if(inv.get(sel).getId() == 0 || inv.get(sel).getCount() == 0
			|| Block.REGISTRY.get(inv.get(sel).getId()) == null) {
				return;
			}
			world.setBlock((int)o.x, (int)o.y, (int)o.z, inv.get(sel).getId());
			inv.get(sel).setCount(inv.get(sel).getCount()-1);
			Block.REGISTRY.get(world.getBlock((int)o.x, (int)o.y, (int)o.z)).getSound().play();
		}
	}
	public void raycastBreak() {
		Entity player = Voxelmine.getLocalPlayer();
		float reach = 10, step = 0.05f, dist = 0;
		World world = Voxelmine.getWorld();
		Vector3f rot = transform.getRotation();
		Vector3f o = new Vector3f(transform.getPosition());
		//o.y -= player.getBoundingBox().getSize().y;
		Vector3f d = new Vector3f();
		float horiz = (float)Math.cos(rot.x);
		d.x = -(float)Math.sin(rot.y) * step * horiz;
		d.z = -(float)Math.cos(rot.y) * step * horiz;
		d.y = (float)Math.sin(rot.x) * step;
		boolean found = false;
		while(!found && dist < reach) {
			o.x += d.x;
			o.y += d.y;
			o.z += d.z;
			dist += step;
			if(Block.REGISTRY.get(world.getBlock((int)o.x, (int)o.y, (int)o.z)).getModel().isRendered()) {
				found = true;
				break;
			}
		}
		if(found) {
			int id = world.getBlock((int)o.x, (int)o.y, (int)o.z);
			InvHandler.give(Voxelmine.getLocalPlayer(), id);
			Block.REGISTRY.get(id).getSound().play();
			world.setBlock((int)o.x, (int)o.y, (int)o.z, Block.AIR.getId());
		}
	}
}
