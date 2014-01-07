package hdcheese.world;

import java.util.ArrayList;

import hdcheese.GameSession;
import hdcheese.audio.SoundBank.SoundName;
import hdcheese.graphics.Animation;
import hdcheese.graphics.AnimationState;
import hdcheese.graphics.Animator;
import hdcheese.graphics.GraphicsWarehouse;
import hdcheese.input.AIBrain;
import hdcheese.input.Command;
import hdcheese.utils.Coordinate;
import hdcheese.utils.Direction;
import hdcheese.utils.RectangleInt;
import hdcheese.world.BodyPart.PartNum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	
	private RectangleInt tmpRect = new RectangleInt();
	
	public Color drawColor = Color.WHITE.cpy();
	
	private boolean attackStunned = false;
	public boolean isStunned() {
		return attackStunned;
	}
	public float stunTime = 0;
	public float stunTimeLimit = 0.5f;
	
	public boolean holdingPart = false;
	
	public float maxHealth = 10;
	public float currHealth = maxHealth;
	
	public AttackEffect lastHitBy;
	public float lastHitTime = 0;
	
	public AttackEffect attack;
	
	public AIBrain aiBrain = null;
	
	public Direction direction = Direction.DOWN;

	public float moveRate = World.TILE_WIDTH / 7; // 10
	public int maxVelocity = World.TILE_WIDTH / 14; // 10

	private Vector2 impulse = new Vector2();

	public Vector2 velocity = new Vector2();

	//private Vector2 remainder = new Vector2();
	
	public Animator animator = new Animator();
	
	private ArrayList<Command> newCommands = new ArrayList<Command>();
	public ArrayList<Command> activeCommands = new ArrayList<Command>();
	private ArrayList<Command> deadCommands = new ArrayList<Command>();
	
	private Command lastMoveCommand = Command.MOVE_DOWN;
	
	/**
	 * Lower left corner of object
	 */
	public Vector2 position = new Vector2();

	public RectangleInt boundaries = new RectangleInt();

	public CreatureType type;

	World world;

	public Vector2 nextPosition = new Vector2();
	
	public  boolean dead = false;

	/**
	 * Will apply velocity as an impulse
	 * @param xMove
	 * @param yMove
	 */
	public void moveEntity(float xMove, float yMove) {
		impulse.x += xMove;
		impulse.y += yMove;
	}

	/**
	 * Main setup for creature
	 * @param world
	 * @param width
	 * @param height
	 * @param type
	 */
	public void init(World world, Coordinate startPosition, int width, int height, CreatureType type) {
		// the world will set it's position
		this.boundaries.set(0,0,width, height);

		this.world = world;
		world.addEntity(this, startPosition);

		this.type = type;

		if (type == CreatureType.PLAYER) {
			// set up animation
			animator.animationMap.put(AnimationState.WALK_DOWN, GraphicsWarehouse.playerWalkDownAnim);
			animator.animationMap.put(AnimationState.WALK_UP, GraphicsWarehouse.playerWalkUpAnim);
			animator.animationMap.put(AnimationState.WALK_RIGHT, GraphicsWarehouse.playerWalkRightAnim);
			animator.animationMap.put(AnimationState.WALK_LEFT, GraphicsWarehouse.playerWalkLeftAnim);

			// idle animation
			animator.animationMap.put(AnimationState.IDLE_DOWN, GraphicsWarehouse.playerStandDownAnim);
			animator.animationMap.put(AnimationState.IDLE_UP, GraphicsWarehouse.playerStandUpAnim);
			animator.animationMap.put(AnimationState.IDLE_RIGHT, GraphicsWarehouse.playerStandRightAnim);
			animator.animationMap.put(AnimationState.IDLE_LEFT, GraphicsWarehouse.playerStandLeftAnim);

			// attack animation
			animator.animationMap.put(AnimationState.ATTACK_DOWN, GraphicsWarehouse.playerAttackDownAnim);
			animator.animationMap.put(AnimationState.ATTACK_UP, GraphicsWarehouse.playerAttackUpAnim);
			animator.animationMap.put(AnimationState.ATTACK_RIGHT, GraphicsWarehouse.playerAttackRightAnim);
			animator.animationMap.put(AnimationState.ATTACK_LEFT, GraphicsWarehouse.playerAttackLeftAnim);
		} else if (type == CreatureType.GUARD) {
			// set up animation
			animator.animationMap.put(AnimationState.WALK_DOWN, GraphicsWarehouse.guardWalkDownAnim);
			animator.animationMap.put(AnimationState.WALK_UP, GraphicsWarehouse.guardWalkUpAnim);
			animator.animationMap.put(AnimationState.WALK_RIGHT, GraphicsWarehouse.guardWalkRightAnim);
			animator.animationMap.put(AnimationState.WALK_LEFT, GraphicsWarehouse.guardWalkLeftAnim);

			// idle animation
			animator.animationMap.put(AnimationState.IDLE_DOWN, GraphicsWarehouse.guardStandDownAnim);
			animator.animationMap.put(AnimationState.IDLE_UP, GraphicsWarehouse.guardStandUpAnim);
			animator.animationMap.put(AnimationState.IDLE_RIGHT, GraphicsWarehouse.guardStandRightAnim);
			animator.animationMap.put(AnimationState.IDLE_LEFT, GraphicsWarehouse.guardStandLeftAnim);

			// attack animation
			animator.animationMap.put(AnimationState.ATTACK_DOWN, GraphicsWarehouse.guardAttackDownAnim);
			animator.animationMap.put(AnimationState.ATTACK_UP, GraphicsWarehouse.guardAttackUpAnim);
			animator.animationMap.put(AnimationState.ATTACK_RIGHT, GraphicsWarehouse.guardAttackRightAnim);
			animator.animationMap.put(AnimationState.ATTACK_LEFT, GraphicsWarehouse.guardAttackLeftAnim);
		
		
			// modify guard velocity
			this.maxVelocity /= 2;
			this.moveRate /= 2;
			this.stunTimeLimit *= 2;
		}
	}

	/**
	 * Integrates and zeros out impulse into velocity, separates remainder
	 * @param delta
	 */
	public void figureVelocity(float delta) {
		
		// update attack stun time/status
		if (attackStunned) {
			stunTime += delta;
			if (stunTime >= stunTimeLimit) {
				this.setStun(false);
			}
		}
		
//		if (impulse.x != 0 && impulse.y != 0) {
//			if (Math.abs(impulse.x) > Math.abs(impulse.y)) {
//				velocity.y = 0;
//			} else {
//				velocity.x = 0;
//			}
//		}

		if (impulse.x != 0 || impulse.y != 0) {
//
//			// add new stuff to tmp
////			tmpVector1.set(remainder.x + impulse.x, remainder.y + impulse.y);
//
//			// break out fractional
////			remainder.x = tmpVector1.x % 1;
////			remainder.y = tmpVector1.y % 1;
//
//			// set what's left
////			tmpVector1.set((int)(tmpVector1.x - remainder.x), (int)(tmpVector1.y - remainder.y));
//
//			// add to velocity if necessary
////			if (tmpVector1.x != 0 || tmpVector1.y != 0) {
////				velocity.x += (int)tmpVector1.x;
////				velocity.y += (int)tmpVector1.y;
////			}
			
			velocity.x += impulse.x * delta;
			velocity.y += impulse.y * delta;
			
		}

		else if (impulse.x == 0 && impulse.y == 0){
			//			// no impulse, slow down
			//			float slowRate = delta * World.FRICTION;
			//			
			//			// pre separation
			//			tmpVector1.x = (-Math.signum(velocity.x) * slowRate) + velocity.x;
			//			tmpVector1.y = (-Math.signum(velocity.y) * slowRate) + velocity.y;
			//			
			//			// remainders only
			//			tmpVector2.x = tmpVector1.x % 1;
			//			tmpVector2.y = tmpVector1.y % 1;
			//			
			//			// transform to whole numbers only
			//			tmpVector1.x -= tmpVector2.x;
			//			tmpVector2.y -= tmpVector2.y;
			//			
			//			// add to remainder and velocity (already signed correctly)
			//			// whole number
			//			if (velocity.x > 0) {
			//				velocity.x = (int)Math.max(velocity.x + tmpVector1.x, 0);
			//			} else if (velocity.x < 0) {
			//				velocity.x = (int)Math.min(velocity.x + tmpVector1.x, 0);
			//			}
			//			if (velocity.y > 0) {
			//				velocity.y = (int)Math.max(velocity.y + tmpVector1.y, 0);
			//			} else if (velocity.y < 0) {
			//				velocity.y = (int)Math.min(velocity.y + tmpVector1.y, 0);
			//			}
			//			
			//			// remainder
			//			remainder.x += tmpVector2.x;
			//			remainder.y += tmpVector2.y;
			
			final float FRICTION = World.TILE_WIDTH * 0.65f;
			float frictionNow = FRICTION * delta;
			
			if (Math.abs(velocity.x) > frictionNow) {
				velocity.x -= (Math.signum(velocity.x) * frictionNow);
			} else {
				velocity.x = 0;
			}
			if (Math.abs(velocity.x) > frictionNow) {
				velocity.y -= (Math.signum(velocity.y) * frictionNow);
			} else {
				velocity.y = 0;
			}
		}		

		// zero out impulse
		impulse.x = 0;
		impulse.y = 0;

		// control for diagonal & control for max speed
		if (velocity.x != 0 || velocity.y != 0) {

			if (velocity.x != 0 && velocity.y != 0) {
				//				// the new value
				//				tmpVector1.set((float)(velocity.x) / 2.0f, (float)(velocity.y) / 2.0f);
				//				// fractional portion
				//				tmpVector2.set(tmpVector1.x % 1, tmpVector1.y % 1);
				//				// remove fraction
				//				tmpVector1.x -= tmpVector2.x;
				//				tmpVector1.y -= tmpVector2.y;
				//				// set new values
				//				velocity.set((int)tmpVector1.x, (int)tmpVector1.y);
				//				remainder.x += tmpVector2.x;
				//				remainder.y += tmpVector2.y;
				
				if (Math.abs(velocity.x) < 1) {
					velocity.x = 0;
				}
				if (Math.abs(velocity.x) < 1) {
					velocity.y = 0;
				}
			}
			
			//if (!attackStunned) {
				if (Math.abs(velocity.x)>this.maxVelocity) {
					velocity.x = (Math.signum(velocity.x) * this.maxVelocity);
				}
				if (Math.abs(velocity.y)>this.maxVelocity) {
					velocity.y = (Math.signum(velocity.y) * this.maxVelocity);
				}
			//}
			
		}
	}

//	public void addAnimation(AnimationState animationState, Animation animation) {
//		animator.animationMap.put(animationState, animation);
//	}

	public void addCommand(Command command) {
		//activeCommands.add(command);
		//		switch(command) {
		//		case MOVE_DOWN:
		//			newCommands.remove(Command.MOVE_RIGHT);
		//			newCommands.remove(Command.MOVE_LEFT);
		//			break;
		//		case MOVE_LEFT:
		//			newCommands.remove(Command.MOVE_UP);
		//			newCommands.remove(Command.MOVE_DOWN);
		//			break;
		//		case MOVE_RIGHT:
		//			newCommands.remove(Command.MOVE_UP);
		//			newCommands.remove(Command.MOVE_DOWN);
		//			break;
		//		case MOVE_UP:
		//			newCommands.remove(Command.MOVE_RIGHT);
		//			newCommands.remove(Command.MOVE_LEFT);
		//			break;
		//		default:
		//			break;
		//		}
		if (!attackStunned) {
			newCommands.add(command);
		}
	}

	public void removeCommand(Command command) {
		if (activeCommands.contains(command)) {
			deadCommands.add(command);
		}
		//activeCommands.remove(command);

	}
	
	public void tryPickUp() {
		tmpRect.set(boundaries);
		tmpRect.width *= 2;
		tmpRect.height *= 2;
		tmpRect.x -= boundaries.width;
		tmpRect.y -= boundaries.height;
		
		world.bodyParts.begin();
		for(BodyPart p : world.bodyParts) {
			if (p.partType != PartNum.BONE) {
				if (tmpRect.overlaps(p.boundaries)) {
					// pick it up!!!
					world.partTotal++;
					// remove
					world.bodyParts.removeValue(p, true);
					// play sound
					GameSession.getSound().playSound(SoundName.PLAYER_PICKUP, true);
				}
			}
		}
		world.bodyParts.end();
	}

	public void doCommands() {

		float thisMove = moveRate;

		for(Command n : newCommands) {
			if (n == Command.MOVE_RIGHT) {
				lastMoveCommand = n;
			} else if (n == Command.MOVE_LEFT) {
				lastMoveCommand = n;
			} else if (n == Command.MOVE_UP) {
				lastMoveCommand = n;
			} else if (n == Command.MOVE_DOWN){
				lastMoveCommand = n;
			}
			activeCommands.add(n);
		}
		newCommands.clear();
		
		for(Command d : deadCommands) {
			activeCommands.remove(d);
		}
		deadCommands.clear();
		
		for(Command command : activeCommands) {
			if (command == Command.MOVE_RIGHT) {
				this.moveEntity(thisMove, 0);
			}
			if (command == Command.MOVE_LEFT) {
				this.moveEntity(-thisMove, 0);
			}
			if (command == Command.MOVE_UP) {
				this.moveEntity(0, thisMove);
			}
			if (command == Command.MOVE_DOWN) {
				this.moveEntity(0, -thisMove);
			}
		}

		activeCommands.clear();
	}

	public void setAnimation(float delta) {
		
		// attacking?
		if (this.attack != null) {
			switch(attack.direction) {
			case DOWN:
				animator.setState(AnimationState.ATTACK_DOWN);
				break;
			case UP:
				animator.setState(AnimationState.ATTACK_UP);
				break;
			case RIGHT:
				animator.setState(AnimationState.ATTACK_RIGHT);
				break;
			case LEFT:
				animator.setState(AnimationState.ATTACK_LEFT);
				break;
			}
			return;
		}

		// idle
		if (velocity.x == 0 && velocity.y == 0) {
			switch(direction) {
			case DOWN:
				animator.setState(AnimationState.IDLE_DOWN);
				break;
			case UP:
				animator.setState(AnimationState.IDLE_UP);
				break;
			case RIGHT:
				animator.setState(AnimationState.IDLE_RIGHT);
				break;
			case LEFT:
				animator.setState(AnimationState.IDLE_LEFT);
				break;
			}
		} else {
			// walking
			if (velocity.y == 0) {
				if (velocity.x > 0) {
					direction = Direction.RIGHT;
					animator.setState(AnimationState.WALK_RIGHT);
				} else if (velocity.x < 0) {
					direction = Direction.LEFT;
					animator.setState(AnimationState.WALK_LEFT);
				}
			} else {
				if (velocity.y > 0) {
					direction = Direction.UP;
					animator.setState(AnimationState.WALK_UP);
				} else if (velocity.y < 0) {
					direction = Direction.DOWN;
					animator.setState(AnimationState.WALK_DOWN);
				}
			}
		}

		animator.addStateTime(delta);
	}

	public void rebuildBoundaries() {
		boundaries.setPosition(position.x, position.y);
	}
	
	public void attack() {
		if (!attackStunned && attack == null) {
			
			switch(type) {
			case GUARD:
				GameSession.getSound().playSound(SoundName.GUARD_ATTACK, false);
				break;
			case PLAYER:
				GameSession.getSound().playSound(SoundName.PLAYER_ATTACK, false);
				break;
			}
			
			Gdx.app.log("attack start", "Attack started!");
			
			final int vel = World.TILE_WIDTH / 40;
			
			float targetX = 0;
			float targetY = 0;
			
			Direction attackDirection = Direction.RIGHT;
			if (lastMoveCommand == Command.MOVE_RIGHT) {
				attackDirection = Direction.RIGHT;
			} else if (lastMoveCommand == Command.MOVE_LEFT) {
				attackDirection = Direction.LEFT;
			} else if (lastMoveCommand == Command.MOVE_UP) {
				attackDirection = Direction.UP;
			} else {
				attackDirection = Direction.DOWN;
			}
			
			direction = attackDirection;
			
			attack = new AttackEffect();
			if (this.direction == Direction.RIGHT) {
				targetX = boundaries.right() + (boundaries.width / 2);
				targetY = boundaries.bottom() + (boundaries.height);
			} else if (this.direction == Direction.LEFT) {
				targetX = boundaries.left() - (boundaries.width / 2);
				targetY = boundaries.bottom() + (boundaries.height);
			} else if (this.direction == Direction.UP) {
				targetX = boundaries.x + (boundaries.width/2);
				targetY = boundaries.top() + (boundaries.height / 2);
			} else {
				targetX = boundaries.x + (boundaries.width/2);
				targetY = boundaries.bottom() - (boundaries.height / 2);
			}			
			
			attack.init(this, targetX, targetY, boundaries.width, attackDirection);
			
			setStun(true);
			
		}
	}
	
	public void setStun(boolean stunOrNot) {
		if (stunOrNot) {
			attackStunned = true;
			stunTime = 0;
		} else {
			attackStunned = false;
			stunTime = 0;
		}
		
	}
	
	public void hit(AttackEffect ae) {
		if (lastHitBy != ae && (world.time - lastHitTime) > (ae.lifeTime/2)) {
			
			switch(type) {
			case GUARD:
				GameSession.getSound().playSound(SoundName.GUARD_HURT, false);
				break;
			case PLAYER:
				GameSession.getSound().playSound(SoundName.PLAYER_HURT, false);
				break;
			}
			
			lastHitBy = ae;
			currHealth--;
			lastHitTime = world.time;
			
			if (currHealth <= 0) {
				this.dead = true;
				
				// spawn parts!
				GameSession.getSound().playSound(SoundName.PLOP, false);
				for (int i = 0; i < 10; ++i) {
					BodyPart p = new BodyPart(boundaries.x + (boundaries.width/2),
							boundaries.y + (boundaries.width/2));
					world.bodyParts.add(p);
				}
			}
			// finish the attack
			ae.done = true;
		}
	}

}
