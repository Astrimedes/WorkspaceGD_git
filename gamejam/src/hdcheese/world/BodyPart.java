package hdcheese.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import hdcheese.utils.RectangleInt;

public class BodyPart {
	
	public enum PartNum {
		ONE,
		TWO,
		THREE,
		BONE
	}
	
	public PartNum partType;
	
	public float moveTime = 0;
	public float maxMoveTime = MathUtils.random(1);
	public float moveRate = World.TILE_WIDTH;
	
	public RectangleInt boundaries = new RectangleInt();
	public Vector2 velocity = new Vector2();
	
	public BodyPart(float xPosSource, float yPosSource) {
		boundaries.x = xPosSource;
		boundaries.y = yPosSource;
		boundaries.width = World.TILE_WIDTH;
		boundaries.width = World.TILE_WIDTH;
		
		int rand = MathUtils.random(0,3);
		if (rand == 0) {partType = PartNum.BONE;}
		if (rand == 1) {partType = PartNum.ONE;}
		if (rand == 2) {partType = PartNum.TWO;}
		if (rand == 3) {partType = PartNum.THREE;}
		
		velocity.x = MathUtils.random(-moveRate,moveRate);
		velocity.y = MathUtils.random(-moveRate,moveRate);
	}
	
	public void update(float delta) {
		if (moveTime > maxMoveTime) {
			velocity.x = 0;
			velocity.y = 0;
			return;
		}
		
		moveTime += delta;
		
		boundaries.x += velocity.x * delta;
		boundaries.y += velocity.y * delta;
	}
}
