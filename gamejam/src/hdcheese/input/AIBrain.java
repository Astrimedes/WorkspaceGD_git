package hdcheese.input;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import hdcheese.GameSession;
import hdcheese.audio.SoundBank.SoundName;
import hdcheese.utils.Coordinate;
import hdcheese.world.CreatureType;
import hdcheese.world.Entity;
import hdcheese.world.World;

public class AIBrain {

	Vector2 tmpCoord = new Vector2();
	Vector2 myPos = new Vector2();
	Vector2 otherPos = new Vector2();

	public World world;

	public Entity entity;

	public Entity target;

	public Command lastCommand = null;
	public Command currCommand = null;

	public AIState state = AIState.SEEK_PLAYER;

	public float stateTime = 0;

	public float directionChangeFrequency = 0.1f;
	public float lastDirectionChange = 0;

	public void setState(AIState nextState) {
		if (nextState != state) {
			state = nextState;
		}
	}

	public void init(World world, Entity e) {
		this.world = world;
		setEntity(e);
	}

	public void setEntity(Entity e) {
		this.entity = e;
		e.aiBrain = this;
	}

	public void update(float delta) {
		stateTime += delta;

		Command intent = currCommand;
		
		if (stateTime - lastDirectionChange > directionChangeFrequency) {
			lastDirectionChange = stateTime;
			switch(state) {
			case SEEK_PLAYER:
				float minDistance = Float.MAX_VALUE;
				float dist = Float.MAX_VALUE;

				entity.boundaries.getCenter(tmpCoord);
				myPos.set(tmpCoord.x, tmpCoord.y);

				// find closest player to target
				for (Entity e : world.entities) {
					if (e.type == CreatureType.PLAYER && !e.dead) {
						e.boundaries.getCenter(tmpCoord);
						otherPos.set(tmpCoord.x, tmpCoord.y);

						dist = myPos.dst2(otherPos);
						
						if (dist < (World.TILE_WIDTH * World.TILE_WIDTH * 40) && (dist > e.boundaries.width * e.boundaries.width) && dist < minDistance) {
							if (target == null) {
								GameSession.getSound().playSound(SoundName.GUARD_VOICE, false);
							}
							target = e;
							minDistance = dist;
						}
					}
				}
				
				if (target == null) {
					setState(AIState.WANDER);
					return;
				}
				
				if (minDistance < (entity.boundaries.width * entity.boundaries.width * 2)) {
					entity.attack();
				}

				// find direction of furthest distance, go in that direction
				minDistance = target.position.x - entity.position.x; // X
				dist = target.position.y - entity.position.y; // Y
				if (Math.abs(minDistance) > Math.abs(dist)) { // X
					intent = minDistance > 0 ? Command.MOVE_RIGHT : Command.MOVE_LEFT;
				} else { // Y
					intent = dist > 0 ? Command.MOVE_UP : Command.MOVE_DOWN;
				}
				break;
			case WANDER:
				if (MathUtils.randomBoolean(0.05f)) {
					target = null;
				}
				
				int rand = MathUtils.random(3);
				if (rand == 0) {
					intent = Command.MOVE_UP;
				} else if (rand == 1) {
					intent = Command.MOVE_DOWN;
				} else if (rand == 2) {
					intent = Command.MOVE_LEFT;
				} else {
					intent = Command.MOVE_RIGHT;
				}
				// after 10 seconds of wander, switch to seeking
				if (stateTime > directionChangeFrequency) {
					setState(AIState.SEEK_PLAYER);
				}
				break;
			}
		}

		// Send the command!
		if (intent != currCommand) {
			lastCommand = currCommand;
			currCommand = intent;
		}

		if (currCommand != null) {
			entity.addCommand(currCommand);
		}
		
	}

}
