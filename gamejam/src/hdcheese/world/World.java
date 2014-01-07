package hdcheese.world;

import java.util.ArrayList;

import hdcheese.graphics.GraphicsWarehouse;
import hdcheese.graphics.Renderer2D;
import hdcheese.input.AIState;
import hdcheese.input.Command;
import hdcheese.utils.Coordinate;
import hdcheese.utils.RectangleInt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.DelayedRemovalArray;

public class World {

	public TileType tiles[][];

	public static int TILE_WIDTH = 60;
	public static float TIME_STEP = (float)(1.0/30.0);

	public DelayedRemovalArray<BodyPart> bodyParts = new DelayedRemovalArray<BodyPart>();

	Color bgColor = new Color(Color.rgba8888(82, 152, 97, 1));
	public RectangleInt bounds = new RectangleInt();

	public int partTotal = 0;

	/**
	 * Units slow down this much per second
	 */
	public static float HIT_VELOCITY = World.TILE_WIDTH;

	public DelayedRemovalArray<Grave> graves = new DelayedRemovalArray<Grave>();

	public float time = 0;

	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> deadEntities = new ArrayList<Entity>();

	public DelayedRemovalArray<AttackEffect> attacks = new DelayedRemovalArray<AttackEffect>();

	public int tilesWide;
	public int tilesTall;

	public int unitsWide;
	public int unitsTall;

	// temporary variables!
	private RectangleInt moveRect = new RectangleInt();
	private RectangleInt destRect = new RectangleInt();

	public World(int tilesWide, int tilesTall) {		
		this.tilesWide = tilesWide;
		this.tilesTall = tilesTall;

		tiles = new TileType[tilesWide][tilesTall];
	}

	public void initRandomWorld() {

		ArrayList<Coordinate> graveCoords = new ArrayList<Coordinate>();
		graveCoords.add(new Coordinate(3,9));
		graveCoords.add(new Coordinate(3,13));
		graveCoords.add(new Coordinate(5,5));
		graveCoords.add(new Coordinate(5,17));
		graveCoords.add(new Coordinate(8,8));
		graveCoords.add(new Coordinate(8,11));
		graveCoords.add(new Coordinate(8,18));
		graveCoords.add(new Coordinate(9,2));
		graveCoords.add(new Coordinate(9,20));
		graveCoords.add(new Coordinate(10,5));
		graveCoords.add(new Coordinate(10,17));
		graveCoords.add(new Coordinate(15,6));
		graveCoords.add(new Coordinate(15,11));
		graveCoords.add(new Coordinate(15,16));
		graveCoords.add(new Coordinate(18,11));

		for(int x = 0; x < tilesWide; ++x) {
			for(int y = 0; y < tilesTall; ++y) {
				boolean done = false;
				// check for grave
				for (Coordinate c : graveCoords) {
					if (x == c.x && y == c.y) {
						tiles[x][y] = TileType.GRAVE;
						graves.add(new Grave(this,x,y));
						done = true;
					}
				}

				if (!done) {
					if (x == 0) {
						done = true;
						if (y == 0) {
							tiles[x][y] =TileType.FENCE_LL;
						} else if (y == tilesTall-1) {
							tiles[x][y] = TileType.FENCE_UL;
						} else {
							tiles[x][y] = TileType.FENCE_VERTICAL;
						}
					} else if (y == 0) {
						done = true;
						if (x == tilesWide -1) {
							tiles[x][y] = TileType.FENCE_LR;
						} else {
							tiles[x][y] = TileType.FENCE_HORIZONTAL;
						}
					} else if (y == tilesTall - 1) {
						done = true;
						if (x == tilesWide -1) {
							tiles[x][y] = TileType.FENCE_UR;
						} else {
							tiles[x][y] = TileType.FENCE_HORIZONTAL;
						}
					} else if (x == tilesWide - 1) {
						done = true;
						tiles[x][y] = TileType.FENCE_VERTICAL;
					}
					if (!done) {
						done = true;
						tiles[x][y] = TileType.GRASS;
					}
				}
			}
		}

		this.unitsWide = tilesWide * TILE_WIDTH;
		this.unitsTall = tilesTall * TILE_WIDTH;

		bounds.set(0, 0, unitsWide, unitsTall);
	}

	//	public void drawWorld(ShapeRenderer shapeRender, OrthographicCamera camera) {
	//		shapeRender.setProjectionMatrix(camera.combined);
	//		
	//		shapeRender.begin(ShapeType.Filled);
	//		
	//		for(int x = 0; x < tilesWide; ++x) {
	//			for(int y = 0; y < tilesTall; ++y) {
	//				if (tiles[x][y] == TileType.GRASS) {
	//					shapeRender.setColor(Color.GREEN);
	//				} else if (tiles[x][y] == TileType.GRAVE) {
	//					shapeRender.setColor(Color.GRAY);
	//				}
	//				shapeRender.rect(x * TILE_WIDTH, y * TILE_WIDTH, 
	//						TILE_WIDTH, TILE_WIDTH);
	//			}
	//		}
	//		
	//		shapeRender.end();
	//	}

	public void drawWorld(SpriteBatch spriteBatch, Renderer2D renderer) {
		spriteBatch.setProjectionMatrix(renderer.getWorldCamera().combined);

		spriteBatch.begin();
		spriteBatch.setColor(Color.WHITE);

		for(int x = 0; x < tilesWide; ++x) {
			for(int y = 0; y < tilesTall; ++y) {
				//				if (tiles[x][y] == TileType.GRASS) {
				//					//spriteBatch.setColor(Color.GREEN);
				//					spriteBatch.draw(GraphicsWarehouse.grassTile, x * TILE_WIDTH, y * TILE_WIDTH,
				//							TILE_WIDTH, TILE_WIDTH);
				//				} else if (tiles[x][y] == TileType.GRAVE) {
				//					//spriteBatch.setColor(Color.GRAY);
				//					spriteBatch.draw(GraphicsWarehouse.graveTile, x * TILE_WIDTH, y * TILE_WIDTH,
				//							TILE_WIDTH, TILE_WIDTH);
				//				}
				spriteBatch.draw(GraphicsWarehouse.getTileTexture(tiles[x][y]), 
						x * TILE_WIDTH, y * TILE_WIDTH,
						TILE_WIDTH, TILE_WIDTH);
			}
		}

		spriteBatch.end();

		// draw grave "health bars"
		renderer.shapeRenderer.setProjectionMatrix(renderer.getWorldCamera().combined);
		renderer.shapeRenderer.begin(ShapeType.Filled);
		if (graves.size > 0) {
			graves.begin();
			for(Grave g : graves) {
				if (g.currDug > 0) {
					// draw health bar
					renderer.shapeRenderer.setColor(Color.BLACK);
					float vericalOffset = (World.TILE_WIDTH/8);
					renderer.shapeRenderer.rect(g.boundaries.x, g.boundaries.y - vericalOffset, 
							g.boundaries.width, vericalOffset);
					renderer.shapeRenderer.setColor(Color.GRAY);
					renderer.shapeRenderer.rect(g.boundaries.x, g.boundaries.y - vericalOffset, 
							g.boundaries.width * (g.currDug/g.maxDug), vericalOffset);
				} else {
					graves.removeValue(g, true);
				}
			}
			graves.end();
		}
		renderer.shapeRenderer.end();
	}

	/**
	 * Main update for collision etc
	 * @param delta
	 */
	public void update(float delta) {

		time += delta;

		updateParts(delta);

		processAttacks(delta);

		moveEntities(delta);

	}

	private void updateParts(float delta) {
		for (BodyPart p : bodyParts) {
			p.update(delta);
		}
	}

	private void processAttacks(float delta) {
		AttackEffect a;
		attacks.begin();
		for (int i = 0; i < attacks.size; ++i) {
			a = attacks.get(i);
			a.update(delta);
			if (a.done) {
				a.owner.attack = null;
				attacks.removeIndex(i);
			} else {
				for(Entity e : entities) {
					if (e != a.owner) {
						if (e.boundaries.overlaps(a.bounds)) {
							// hit them!
							Gdx.app.log("hit!", "Attack from " + a.owner.type + " hit " + e.type);
							e.hit(a);
							switch(a.direction) {
							case DOWN:
								e.moveEntity(0, -World.HIT_VELOCITY);
								break;
							case LEFT:
								e.moveEntity(-World.HIT_VELOCITY, 0);
								break;
							case RIGHT:
								e.moveEntity(World.HIT_VELOCITY, 0);
								break;
							case UP:
								e.moveEntity(0, World.HIT_VELOCITY);
								break;
							}
						}
					}
				}
				if (a.owner.type == CreatureType.PLAYER) {
					for(Grave g : graves) {
						if (a.bounds.overlaps(g.boundaries)) {
							// dig!
							g.tryDig(a);
							// destroy the grave
							if (g.currDug <= 0) {
								//tiles[g.tileCoordinate.x][g.tileCoordinate.y] = TileType.GRAVE_OPEN;
							}
						}
					}
				}
			}
		}
		attacks.end();
	}


	private void moveEntities(float delta) {

		// process commands, move them around, figure next position
		for (Entity e : entities) {
			updateEntity(e, delta);
			if (e.dead) {
				deadEntities.add(e);
			}
		}
		for(Entity e : entities) {
			resolveX(e, delta);
		}
		for (Entity e : entities) {
			resolveY(e, delta);
		}
		for (Entity e : entities) {
			resolveDynamic(e, delta);
		}

		// set final positions
		for(Entity e : entities) {
			e.position.x = e.nextPosition.x;
			e.position.y = e.nextPosition.y;
			e.rebuildBoundaries();
		}

		// remove dead guys
		for (Entity d : deadEntities) {
			if (entities.contains(d)) {
				entities.remove(d);
			}
		}
	}

	private void resolveY(Entity e, float delta) {

		e.nextPosition.y = e.position.y + e.velocity.y;

		// Y check world tiles
		Coordinate currCoord = new Coordinate();
		// check appropriate corners of new position for collisions
		Coordinate nextCoord;
		boolean hitY = false;

		// set next move position
		moveRect.set(e.nextPosition.x, 
				e.nextPosition.y, e.boundaries.width, e.boundaries.height);

		if (e.velocity.y > 0) {
			// right top
			nextCoord = this.getTileCoordinate(moveRect.right(), moveRect.top());
			currCoord = this.getTileCoordinate(e.boundaries.right(), e.boundaries.top());
			if (currCoord.y != nextCoord.y) {
				// tile collision going up
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitY = true;
					e.nextPosition.y = Math.min(e.nextPosition.y, (TILE_WIDTH * nextCoord.y) - e.boundaries.height -1);
				}
			}
			// left top
			nextCoord = this.getTileCoordinate(moveRect.left(), moveRect.top());
			currCoord = this.getTileCoordinate(e.boundaries.left(), e.boundaries.top());
			if (currCoord.y != nextCoord.y) {
				// tile collision going up
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitY = true;
					e.nextPosition.y = Math.min(e.nextPosition.y, (TILE_WIDTH * nextCoord.y) - e.boundaries.height -1);
				}
			}
		} else if (e.velocity.y < 0) {
			// right bottom
			nextCoord = this.getTileCoordinate(moveRect.right(), moveRect.bottom()-1);
			currCoord = this.getTileCoordinate(e.boundaries.right(), e.boundaries.bottom()+1);
			if (currCoord.y != nextCoord.y) {
				// tile collision going up
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitY = true;
					e.nextPosition.y = Math.max(e.nextPosition.y, (TILE_WIDTH * nextCoord.y) + e.boundaries.height +1);
				}
			}
			// left bottom
			nextCoord = this.getTileCoordinate(moveRect.left(), moveRect.bottom()-1);
			currCoord = this.getTileCoordinate(e.boundaries.left(), e.boundaries.bottom()+1);
			if (currCoord.y != nextCoord.y) {
				// tile collision going up
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitY = true;
					e.nextPosition.y = Math.max(e.nextPosition.y, (TILE_WIDTH * nextCoord.y) + e.boundaries.height +1);
				}
			}
		}

		if (hitY) {
			if (e.aiBrain != null) {
				// try a different valid direction...
				if (e.velocity.y > 0) {
					if (MathUtils.randomBoolean(0.6f)) {
						e.aiBrain.currCommand = Command.MOVE_DOWN;
					} else {
						e.aiBrain.currCommand = MathUtils.randomBoolean() ? Command.MOVE_RIGHT : Command.MOVE_LEFT;
					}
				} else {
					if (MathUtils.randomBoolean(0.6f)) {
						e.aiBrain.currCommand = Command.MOVE_UP;
					} else {
						e.aiBrain.currCommand = MathUtils.randomBoolean() ? Command.MOVE_RIGHT : Command.MOVE_LEFT;
					}
				}
				// now wander if a map tile was hit
				//				if (!hitEntity) {
				//					e.aiBrain.setState(AIState.WANDER);
				//				}
				e.aiBrain.setState(AIState.WANDER);
			}
			e.velocity.y = 0;
		}
	}

	private void resolveX(Entity e, float delta) {
		// X Movement vs world map tiles
		e.nextPosition.x = e.velocity.x + e.position.x;
		// check world tiles for collision
		moveRect.set(e.nextPosition.x, 
				e.position.y + e.velocity.y, e.boundaries.width, e.boundaries.height);
		Coordinate currCoord = new Coordinate();
		// check appropriate corners of new position for collisions
		Coordinate nextCoord;
		boolean hitX = false;
		if (e.velocity.x > 0) {
			// right bottom
			nextCoord = this.getTileCoordinate(moveRect.right(), moveRect.bottom());
			currCoord = this.getTileCoordinate(e.boundaries.right(), e.boundaries.bottom());
			if (currCoord.x != nextCoord.x) {
				// tile collision going right
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitX = true;
					e.nextPosition.x = Math.min(e.nextPosition.x, (TILE_WIDTH * nextCoord.x) - e.boundaries.width -1);
				}
			}
			// right top
			nextCoord = this.getTileCoordinate(moveRect.right(), moveRect.top());
			currCoord = this.getTileCoordinate(e.boundaries.right(), e.boundaries.top());
			if (currCoord.x != nextCoord.x) {
				// tile collision going left
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitX = true;
					e.nextPosition.x = Math.min(e.nextPosition.x, (TILE_WIDTH * nextCoord.x) - e.boundaries.width -1);
				}
			}
		} else if (e.velocity.x < 0) {
			// left bottom
			nextCoord = this.getTileCoordinate(moveRect.left()-1, moveRect.bottom());
			currCoord = this.getTileCoordinate(e.boundaries.left()+1, e.boundaries.bottom());
			if (currCoord.x != nextCoord.x) {
				// tile collision going right
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitX = true;
					e.nextPosition.x = Math.max(e.nextPosition.x, (TILE_WIDTH * nextCoord.x) + e.boundaries.width +1);
				}
			}
			// left top
			nextCoord = this.getTileCoordinate(moveRect.left()-1, moveRect.top());
			currCoord = this.getTileCoordinate(e.boundaries.left()+1, e.boundaries.top());
			if (currCoord.x != nextCoord.x) {
				// tile collision going right
				if (getTileAtTilePosition(nextCoord.x, nextCoord.y).obstacle) {
					hitX = true;
					e.nextPosition.x = Math.max(e.nextPosition.x, (TILE_WIDTH * nextCoord.x) + e.boundaries.width +1);
				}
			}
		}
		// resolve hits
		if (hitX) {
			if (e.aiBrain != null) {
				// try a different valid direction...
				if (e.velocity.x > 0) {
					if (MathUtils.randomBoolean(0.6f)) {
						e.aiBrain.currCommand = Command.MOVE_LEFT;
					} else {
						e.aiBrain.currCommand = MathUtils.randomBoolean() ? Command.MOVE_UP : Command.MOVE_DOWN;
					}
				} else {
					if (MathUtils.randomBoolean(0.6f)) {
						e.aiBrain.currCommand = Command.MOVE_RIGHT;
					} else {
						e.aiBrain.currCommand = MathUtils.randomBoolean() ? Command.MOVE_UP : Command.MOVE_DOWN;
					}
				}
				// now wander if a map tile was hit
				//								if (!hitEntity) {
				//									e.aiBrain.setState(AIState.WANDER);
				//								}

				e.aiBrain.setState(AIState.WANDER);
			}
			e.velocity.x = 0;
		}
	}

	private void resolveDynamic(Entity e, float delta) {
		// check against other entities
		boolean hitEntity = false;
		boolean hitX = false;
		boolean hitY = false;
		if (Math.abs(e.velocity.x) > 0 || Math.abs(e.velocity.y) > 0) {
			for(Entity o : entities) {
				if (o != e) {
					// MY next position prediction
					moveRect.set(e.nextPosition.x, e.nextPosition.y, e.boundaries.width, e.boundaries.height);

					// intersection shape - used to find amount to push back

					// do they overlap?
					if (moveRect.overlaps(o.boundaries)) {
						hitEntity = true;

						// moveRect now holds merged rectangles
						destRect.set(o.boundaries);
						moveRect.merge(destRect);
						if (Math.abs(e.velocity.x) > 0) {
							if (e.velocity.x > 0) {
								hitX = true;
								e.nextPosition.x -= (e.boundaries.width + o.boundaries.width) - moveRect.width;
							} else if (e.velocity.x < 0) {
								hitX = true;
								e.nextPosition.x += (e.boundaries.width + o.boundaries.width) - moveRect.width;
							}
						}

						if (Math.abs(e.velocity.y) > 0) {
							if (e.velocity.y > 0) {
								hitY = true;
								e.nextPosition.y -= (e.boundaries.height + o.boundaries.height) - moveRect.height;
							} else if (e.velocity.y < 0) {
								hitY = true;
								e.nextPosition.y += (e.boundaries.height + o.boundaries.height) - moveRect.height;
							}
						}

						if (hitX) {
							e.velocity.x = 0;
						}
						if (hitY) {
							e.velocity.y = 0;
						}
					}
				}
			}
		}
	}



	private void updateEntity(Entity e, float delta) {

		// process commands
		e.doCommands();

		// update velocity values
		e.figureVelocity(delta);

	}

	/**
	 * Add entity to world at given start position (in world units)
	 * @param e
	 * @param startPosition
	 */
	public void addEntity(Entity e, Coordinate startPosition) {
		entities.add(e);
		e.position.x = startPosition.x;
		e.position.y = startPosition.y;
		e.nextPosition.x = startPosition.x;
		e.nextPosition.y = startPosition.y;
		e.boundaries.setPosition(startPosition.x, startPosition.y);

		// find possible problem tiles to replace with non-colliding tiles
		Coordinate coord;
		// lower left
		coord = this.getTileCoordinate(e.boundaries.x, e.boundaries.y);
		if (this.getTileAtTilePosition(coord.x, coord.y).obstacle) {
			// replace with grass
			tiles[coord.x][coord.y] = TileType.GRASS;
		}
		// lower right
		coord = this.getTileCoordinate(e.boundaries.x+e.boundaries.width, e.boundaries.y);
		if (this.getTileAtTilePosition(coord.x, coord.y).obstacle) {
			// replace with grass
			tiles[coord.x][coord.y] = TileType.GRASS;
		}
		// upper right
		coord = this.getTileCoordinate(e.boundaries.x+e.boundaries.width, e.boundaries.y+e.boundaries.height);
		if (this.getTileAtTilePosition(coord.x, coord.y).obstacle) {
			// replace with grass
			tiles[coord.x][coord.y] = TileType.GRASS;
		}
		// upper left
		coord = this.getTileCoordinate(e.boundaries.x, e.boundaries.y+e.boundaries.height);
		if (this.getTileAtTilePosition(coord.x, coord.y).obstacle) {
			// replace with grass
			tiles[coord.x][coord.y] = TileType.GRASS;
		}
	}

	public Coordinate getTileCoordinate(float worldUnitX, float worldUnitY) {
		Coordinate coord = new Coordinate();

		coord.x = (int)Math.floor(worldUnitX / World.TILE_WIDTH);
		coord.y = (int)Math.floor(worldUnitY / World.TILE_WIDTH);

		coord.x = MathUtils.clamp(coord.x, 0, tilesWide-1);
		coord.y = MathUtils.clamp(coord.y, 0, tilesTall-1);

		return coord;
	}

	public TileType getTileAtTilePosition(int xIndex, int yIndex) {
		return tiles[xIndex][yIndex];
	}

	public TileType getTileAtWorldPosition(int unitsX, int unitsY) {
		Coordinate coord = getTileCoordinate(unitsX, unitsY);
		return getTileAtTilePosition(coord.x, coord.y);
	}

	public void drawEntities(ShapeRenderer shapeRenderer, OrthographicCamera camera) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		for(Entity e : entities) {
			if (e.type == CreatureType.PLAYER) {
				shapeRenderer.setColor(Color.BLUE);
			} else {
				shapeRenderer.setColor(Color.RED);
			}
			shapeRenderer.rect(e.boundaries.x, e.boundaries.y, e.boundaries.width, e.boundaries.height);
		}
		shapeRenderer.end();
	}
	
	public void drawBodyParts(SpriteBatch spriteBatch, Renderer2D renderer) {
		// draw body parts underneath
				spriteBatch.setProjectionMatrix(renderer.getWorldCamera().combined);
				spriteBatch.begin();
				for (BodyPart p : bodyParts) {
					switch(p.partType) {
					case BONE:
						spriteBatch.draw(GraphicsWarehouse.bone, p.boundaries.x,
								p.boundaries.y, World.TILE_WIDTH, World.TILE_WIDTH * 1.5f);
						break;
					case ONE:
						spriteBatch.draw(GraphicsWarehouse.part01, p.boundaries.x,
								p.boundaries.y, World.TILE_WIDTH, World.TILE_WIDTH * 1.5f);
						break;
					case THREE:
						spriteBatch.draw(GraphicsWarehouse.part03, p.boundaries.x,
								p.boundaries.y, World.TILE_WIDTH, World.TILE_WIDTH * 1.5f);
						break;
					case TWO:
						spriteBatch.draw(GraphicsWarehouse.part02, p.boundaries.x,
								p.boundaries.y, World.TILE_WIDTH, World.TILE_WIDTH * 1.5f);
						break;
					}
				}
				spriteBatch.end();
	}

	public void drawEntities(SpriteBatch spriteBatch, Renderer2D renderer, float delta) {

		renderer.shapeRenderer.setProjectionMatrix(renderer.getWorldCamera().combined);
		renderer.shapeRenderer.begin(ShapeType.Filled);

		// draw sprites
		spriteBatch.setProjectionMatrix(renderer.getWorldCamera().combined);
		spriteBatch.begin();
		for(Entity e : entities) {
			spriteBatch.setColor(e.drawColor);
			e.setAnimation(delta);
			//			if (e.type == CreatureType.PLAYER) {
			//				spriteBatch.setColor(Color.BLUE);
			//			} else {
			//				spriteBatch.setColor(Color.RED);
			//			}
			spriteBatch.draw(e.animator.getCurrentFrame(1), e.boundaries.x, e.boundaries.y, TILE_WIDTH, TILE_WIDTH * 1.5f);

			// draw health bar
			renderer.shapeRenderer.setColor(Color.RED);
			float vericalOffset = (World.TILE_WIDTH/8);
			renderer.shapeRenderer.rect(e.boundaries.x, e.boundaries.y - vericalOffset, 
					e.boundaries.width, vericalOffset);
			renderer.shapeRenderer.setColor(Color.GREEN);
			renderer.shapeRenderer.rect(e.boundaries.x, e.boundaries.y - vericalOffset, 
					e.boundaries.width * (e.currHealth/e.maxHealth), vericalOffset);

		}
		spriteBatch.end();
		renderer.shapeRenderer.end();

	}


}
