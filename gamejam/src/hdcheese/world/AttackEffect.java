package hdcheese.world;

import hdcheese.utils.Direction;
import hdcheese.utils.RectangleInt;

public class AttackEffect {
	
	public Direction direction = Direction.RIGHT;
	
	private World world;
	
	public int damage = 1;
	
	public RectangleInt bounds = new RectangleInt();
	
	public boolean done = false;
	
	float tmpX = 0;
	float tmpY = 0;
	
	// time it will live
	public float lifeTime = 0.3f;
	public float time = 0;
	
	public Entity owner;
	
	public void init(Entity owner, float targetX, float targetY, float width, Direction direction) {
		this.direction = direction;
		this.owner = owner;
		this.world = owner.world;
		
		this.world.attacks.add(this);
		
		bounds.set(targetX - (width/2), targetY - (width/2), width, width);
	}
	
	public void update(float delta) {
		
		if (time > lifeTime) {
			done = true;
			owner.setStun(true);
			return;
		}
		
		time += delta;
		
		// update bounds according to player bounds
		switch(direction) {
		case DOWN:
			bounds.x = owner.boundaries.x;
			bounds.y = owner.boundaries.bottom() - bounds.height;
			break;
		case LEFT:
			bounds.setCenter(owner.boundaries.left() - (bounds.width/2), 
					owner.boundaries.bottom() + (owner.boundaries.height/2));
			break;
		case RIGHT:
			bounds.setCenter(owner.boundaries.right() + (bounds.width/2), 
					owner.boundaries.bottom() + (owner.boundaries.height/2));
			break;
		case UP:
			bounds.x = owner.boundaries.x;
			bounds.y = owner.boundaries.top();
			break;
		}
	}
	
}
