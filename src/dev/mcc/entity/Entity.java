package dev.mcc.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector3f;

import dev.mcc.Voxelmine;
import dev.mcc.util.AxisAlignedBB;
import dev.mcc.util.Transform;
import dev.mcc.world.World;
import dev.mcc.world.block.Block;
import dev.mcc.world.item.ItemStack;
import dev.mcc.world.model.ModelPiece;

/**
 * Represents an entity. An entity is anything in the game
 * that is not a UI element or a block.
 * @author braiden
 *
 */
public class Entity {
	protected Vector3f velocity = new Vector3f();
	protected Transform transform = new Transform(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1));
	protected AxisAlignedBB aabb = new AxisAlignedBB(new Vector3f(-0.3f, -0.9f, -0.3f), new Vector3f(0.3f, 0.9f, 0.3f));
	private Vector3f dirMul = new Vector3f();
	protected int maxHealth = 20;
	protected int health = maxHealth;
	protected HashMap<Integer, ItemStack> inventory = new HashMap<Integer, ItemStack>();
	/**
	 * Returns whether or not a given point
	 * is in the geometry of a world block.
	 * @param pos - The position to check for
	 * @return - If it is inside a block
	 */
	private boolean isInsideWorldBlockModel(Vector3f pos) {
		World world = Voxelmine.getWorld();
		Block b = Block.REGISTRY.get(world.getBlock((int)pos.x, (int)pos.y, (int)pos.z));
		ArrayList<ModelPiece> pieces = b.getModel().getPieces();
		for(int i = 0; i < pieces.size(); i++) {
			ModelPiece piece = pieces.get(i);
			Vector3f min = piece.getMinimum();
			Vector3f max = piece.getMaximum();
			Vector3f off = new Vector3f(pos.x%1, pos.y%1, pos.z%1);
			if(off.x > min.x && off.y > min.y && off.z > min.z) {
				if(off.x < max.x && off.y < max.y && off.z < max.z) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Gets this entity's inventory. For a player the inventory must never be null,
	 * but for anything else it can be.
	 * @return - The inventory
	 */
	public HashMap<Integer, ItemStack> getInventory() {
		return inventory;
	}
	/**
	 * Sets the entity's health. If the supplied
	 * health is greater than the maximum health
	 * then the entity's health is just set to
	 * the maximum health 
	 * @param health - The requested health
	 */
	public void setHealth(int health) {
		this.health = health;
		if(health > maxHealth) {
			this.health = maxHealth;
		}
		if(this.health < 0) {
			Voxelmine.getWorld().killEntity(this);
		}
	}
	/**
	 * Retrieves the health of this entity.
	 * @return - The entity's health
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Retrieves the maximum health for this entity.
	 * For those modding this class, the health MUST
	 * NOT exceed the maximum health value. This is 
	 * because it might break other things.
	 * @return - This entity's maximum health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Attacks the player from the specified damage source.
	 * If no damage source applies,  use DamageSource.GENERIC.
	 * @param source
	 * @param damage
	 */
	public void attack(DamageSource source, int damage) {
		if(damage != 0) {
			setHealth(getHealth()-damage);
		}
	}
	
	/**
	 * Checks for a collision in the specified direction,
	 * returns a new Vector3f containing the possible move
	 * direction.
	 * @param dir - The direction to check for collisions.
	 * @return - The returned possible move direction.
	 */
	public Vector3f checkCollide(Vector3f dir) {
		Vector3f p = new Vector3f(transform.getPosition());
		Vector3f min = aabb.getMin();
		Vector3f max = aabb.getMax();
		Vector3f size = aabb.getSize();
		Vector3f resultDir = new Vector3f(dir);
		World world = Voxelmine.getWorld();
		// Check on front and back faces of the bounding box
		for(float x = min.x; x <= max.x; x += size.x) {
			for(float y = min.y; y <= max.y; y += size.y) {
				Vector3f pos = new Vector3f(x+p.x, y+p.y, p.z+min.z+dir.z);
				if(isInsideWorldBlockModel(pos)) {
					resultDir.z = 0;
				}
				pos = new Vector3f(x+p.x, y+p.y, p.z+max.z+dir.z);
				if(isInsideWorldBlockModel(pos)) {
					resultDir.z = 0;
				}
			}
		}
		// Check on top and bottom faces of the bounding box
		for(float x = min.x; x <= max.x; x += size.x) {
			for(float z = min.z; z <= max.z; z += size.z) {
				Vector3f pos = new Vector3f(x+p.x, p.y+min.y+dir.y, z+p.z);
				if(isInsideWorldBlockModel(pos)) {
					resultDir.y = 0;
				}
				pos = new Vector3f(x+p.x, p.y+max.y+dir.y, z+p.z);
				if(isInsideWorldBlockModel(pos)) {
					resultDir.y = 0;
				}
			}
		}
		// Check on left and right faces of the bounding box
		for(float y = min.y; y <= max.y; y += size.y) {
			for(float z = min.z; z <= max.z; z += size.z) {
				Vector3f pos = new Vector3f(p.x+min.x+dir.x, y+p.y, z+p.z);
				if(isInsideWorldBlockModel(pos)) {
					resultDir.x = 0;
				}
				pos = new Vector3f(p.x+max.x+dir.x, y+p.y, z+p.z);
				if(isInsideWorldBlockModel(pos)) {
					resultDir.x = 0;
				}
			}
		}
		return resultDir;
	}
	
	/**
	 * Applies a force in the specified direction. Has the option
	 * to accelerate the current force or to add the current force.
	 * Note: Every frame, the applied force vectors are subtracted
	 * from velocity. Therefore, to keep a non-accelerated force going
	 * you have to call this every frame!
	 * @param dir - The direction in which to move / accelerate
	 * @param accelerate - Whether the entity should be moved or accelerated
	 */
	public void applyForce(Vector3f dir, boolean accelerate) {
		Vector3f dirAdd = checkCollide(new Vector3f(dir).normalize());
		if(accelerate) {
			dirMul = dirMul.add(new Vector3f(dirAdd).mul(Voxelmine.getDeltaTime()));
			if(dirAdd.x == 0) dirMul.x = 0;
			if(dirAdd.y == 0) dirMul.y = 0;
			if(dirAdd.z == 0) dirMul.z = 0;
		}
		if(accelerate) velocity = velocity.add(new Vector3f(dir).add(dirMul).mul(Voxelmine.getDeltaTime()));
		else velocity = velocity.add(new Vector3f(dir));
		Vector3f newDir = checkCollide(new Vector3f(velocity).normalize());
		if(newDir.x == 0) velocity.x = 0;
		if(newDir.y == 0) velocity.y = 0;
		if(newDir.z == 0) velocity.z = 0;
	}
	/**
	 * Updates the entity's position based on the velocity vector
	 * Called once every frame.
	 */
	public void updatePosition() {
		transform.setPosition(transform.getPosition().add(new Vector3f(velocity).mul(Voxelmine.getDeltaTime())));
	}
	/**
	 * This should be self-explanatory.
	 * @return - The entity's velocity.
	 */
	public Vector3f getVelocity() {
		return velocity;
	}
	
	/**
	 * Retrieves position, rotation and scale information for the entity.
	 * @return - The entity's transform.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public void zeroVelocityXZ() {
		velocity.x = 0;
		velocity.z = 0;
		
	}
	
	public AxisAlignedBB getBoundingBox() {
		return aabb;
	}
	
	/**
	 * Called once per frame after the world 
	 * is generated. Used for physics and movement.
	 * It is NOT called twenty times per second
	 * that would be tick().
	 */
	public void update() {
		float velY = getVelocity().y;
		if(getVelocity().y == 0) applyForce(new Vector3f(0, -20f, 0), false);
		else applyForce(new Vector3f(0, -20f, 0), true);
		if(getVelocity().y == 0 && velY < -20.0f) {
			attack(DamageSource.FALL, -(int)(20.0f+velY)/5);
		}
	}
}
