package hdcheese.world;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import hdcheese.GameScreen;
import hdcheese.GameSession;
import hdcheese.MainMenuScreen;
import hdcheese.audio.MusicBank.MusicName;
import hdcheese.audio.SoundBank.SoundName;
import hdcheese.graphics.GraphicsWarehouse;
import hdcheese.graphics.Renderer2D;
import hdcheese.input.AIBrain;
import hdcheese.input.Command;
import hdcheese.input.Player;
import hdcheese.utils.Coordinate;
import hdcheese.utils.Direction;

public class GameplayScreen extends GameScreen{
	
	boolean loaded = false;
	
	BitmapFont scoreFont;
	
	public float lastSpawnTime = 0;

	public AssetManager assets = new AssetManager();

	private Renderer2D renderer;

	private SpriteBatch spriteBatch = new SpriteBatch();

	public ArrayList<AIBrain> aiBrains = new ArrayList<AIBrain>();

	public World world;

	public ArrayList<Entity> playerEntities = new ArrayList<Entity>();
	public ArrayList<Entity> enemyEntities = new ArrayList<Entity>();
	
	private Vector2 vec1 = new Vector2();
	private Vector2 vec2 = new Vector2();

	public GameplayScreen(boolean useTransitions) {
		super(useTransitions);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean loadAssets() {
		
		if (loaded) {
			return true;
		}
		
		GameSession.getMenuTool().initializeSkin(false);
		Skin skin= GameSession.getMenuTool().getSkin();
		scoreFont = skin.getFont("small");
		
		// load graphics
		assets.load("sprites//packedfile.atlas", TextureAtlas.class);
		assets.finishLoading();
		GraphicsWarehouse.loadTextures((TextureAtlas)assets.get("sprites//packedfile.atlas"));

		world = new World(22, 22);
		world.initRandomWorld();

		for (int i = 0; i < GameSession.players.length; ++i) {
			playerEntities.add(i, new Entity());
			playerEntities.get(i).init(world, new Coordinate((13+i) * World.TILE_WIDTH, (2+i)*World.TILE_WIDTH), 
					World.TILE_WIDTH - 2, World.TILE_WIDTH - 2, 
					CreatureType.PLAYER);
			GameSession.players[i].addEntity(playerEntities.get(i));
			// colorize!
			if (i > 0) {
				switch(i) {
				case 1:
					playerEntities.get(i).drawColor.set(Color.GRAY);
					break;
				case 2:
					playerEntities.get(i).drawColor.set(Color.YELLOW);
					break;
				case 3:
					playerEntities.get(i).drawColor.set(Color.GREEN);
					break;	
				}
			}
		}

		// Enemies and AI
//		for (int e = 0; e < 2; ++e) {
//			enemyEntities.add(e, new Entity());
//			enemyEntities.get(e).init(world, new Coordinate(
//					(int)(world.unitsWide/(e+3)), (int)(world.unitsTall/(e+3))), 
//					World.TILE_WIDTH - 2, World.TILE_WIDTH - 2, 
//					CreatureType.GUARD);
//			// set up ai!
//			AIBrain ai = new AIBrain();
//			ai.init(world, enemyEntities.get(e));
//			aiBrains.add(ai);
//		}

		renderer = new Renderer2D();
		renderer.setScale((float)(1.0 / 16.0), 18 * World.TILE_WIDTH,  18 * World.TILE_WIDTH);
		renderer.correct();

		renderer.getWorldCamera().position.x = world.unitsWide / 2;
		renderer.getWorldCamera().position.y = world.unitsTall / 2;

		loadAudio();
		
		GameSession.getMusic().playMusic(MusicName.GAMEPLAY);
		
		loaded = true;

		return true;
	}

	private void loadAudio() {
		// music
		game.getMusic().loadMusic(MusicName.GAMEPLAY, "audio/game jam 3.mp3");

		// sounds
		game.getSound().loadSound(SoundName.GUARD_ATTACK, "audio/club01.mp3", "audio/club02.mp3",
				"audio/club03.mp3");
		game.getSound().loadSound(SoundName.PLAYER_ATTACK, "audio/shovelhit01.mp3", "audio/shovelhit02.mp3",
				"audio/shovelhit03.mp3");
		game.getSound().loadSound(SoundName.GUARD_HURT, "audio/guardow01.mp3", "audio/guardow02.mp3");
		game.getSound().loadSound(SoundName.PLAYER_HURT, "audio/igorow01.mp3", "audio/igorow02.mp3");
		game.getSound().loadSound(SoundName.GUARD_VOICE, "audio/oya.mp3");
		game.getSound().loadSound(SoundName.PLAYER_DIG, "audio/dig01.mp3", "audio/dig02.mp3");
		game.getSound().loadSound(SoundName.PLAYER_PICKUP, "audio/pickup_1.wav");
		game.getSound().loadSound(SoundName.PLOP, "audio/plop01.mp3", "audio/plop02.mp3", "audio/plop03.mp3");
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		
	}

	@Override
	protected void unloadAssets() {
		assets.clear();
		GameSession.getMusic().stopMusic();
		GameSession.getMusic().unloadAssets();
		GameSession.getSound().unloadAssets();
	}

	@Override
	public boolean isPaused() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.correct();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// draw background color
		renderer.shapeRenderer.setProjectionMatrix(renderer.getWorldCamera().combined);
		renderer.shapeRenderer.begin(ShapeType.Filled);
		renderer.shapeRenderer.setColor(world.bgColor);
		renderer.shapeRenderer.rect(world.bounds.x, world.bounds.y, world.bounds.width, world.bounds.height);
		renderer.shapeRenderer.end();

		world.drawWorld(spriteBatch, renderer);
		world.drawBodyParts(spriteBatch, renderer);
		//world.drawEntities(renderer.shapeRenderer, renderer.getWorldCamera()); // shapes
		world.drawEntities(spriteBatch, renderer, delta); // sprites

		// draw attacks
//		renderer.shapeRenderer.setProjectionMatrix(renderer.getWorldCamera().combined);
//		renderer.shapeRenderer.begin(ShapeType.Filled);
//		renderer.shapeRenderer.setColor(Color.WHITE);
//		for(AttackEffect a : world.attacks) {
//			renderer.shapeRenderer.rect(a.bounds.x, a.bounds.y, a.bounds.width, a.bounds.height);
//		}
//		renderer.shapeRenderer.end();
		
		
		// draw parts collected
		spriteBatch.setProjectionMatrix(renderer.getScreenCamera().combined);
		spriteBatch.begin();
		scoreFont.setColor(Color.RED);
		scoreFont.draw(spriteBatch, "Parts: " + world.partTotal, Gdx.graphics.getWidth() * 0.75f,
				Gdx.graphics.getHeight() * 0.9f);
		spriteBatch.end();
		
		// update camera
		for (int i = 0; i < GameSession.players.length; ++i) {
			if (!GameSession.players[i].entity.dead) {
				renderer.getWorldCamera().position.x = GameSession.players[i].entity.position.x;
				renderer.getWorldCamera().position.y = GameSession.players[i].entity.position.y;
				renderer.getWorldCamera().update();
			}
		}
		

		handleInput(delta);
		aiThink(delta);

		world.update(delta);
		
		checkForSpawn();
	}

	private void aiThink(float delta) {
		for(AIBrain ai : aiBrains) {
			ai.update(delta);
		}

	}

	public void handleInput(float delta) {

		float zoomRate = 0.5f;

		// control camera with keyboard
//		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
//			renderer.getWorldCamera().zoom += delta * zoomRate;
//			renderer.getWorldCamera().update();
//		} else if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
//			renderer.getWorldCamera().zoom -= delta * zoomRate;
//			renderer.getWorldCamera().update();
//		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			game.setScreen(new MainMenuScreen(), false);
		}
		
		float maxPlayerDistance = Float.MAX_VALUE;
		float dist = 0;
		
		if (GameSession.players.length == 1) {
			maxPlayerDistance = 1;
		}

		// player control
		for (Player p : GameSession.players) {
			
			vec1.set(p.entity.position.x, p.entity.position.y);
			
			for(Player o : GameSession.players) {
				if (o != p && !o.entity.dead) {
					vec2.set(o.entity.position.x, o.entity.position.y);
					dist = Math.abs(vec2.dst2(vec1));
					if (dist < maxPlayerDistance) {
						maxPlayerDistance = dist;
					}
				}
			}
			
			if (p.controller != null) {
				
				// attacks
				if (p.controller.getButton(0)) {
					p.entity.attack();
				} else if (p.controller.getButton(1)) {
					p.entity.tryPickUp();
				}
				
				// movement
				PovDirection direction = p.controller.getPov(0);
				switch(direction) {
				case center:
					break;
				case east:
					p.entity.addCommand(Command.MOVE_RIGHT);
					break;
				case north:
					p.entity.addCommand(Command.MOVE_UP);
					break;
				case northEast:
					if (p.entity.direction == Direction.UP) {
						p.entity.addCommand(Command.MOVE_UP);
					} else {
						p.entity.addCommand(Command.MOVE_RIGHT);
					}
					break;
				case northWest:
					if (p.entity.direction == Direction.UP) {
						p.entity.addCommand(Command.MOVE_UP);
					} else {
						p.entity.addCommand(Command.MOVE_LEFT);
					}
					break;
				case south:
					p.entity.addCommand(Command.MOVE_DOWN);
					break;
				case southEast:
					if (p.entity.direction == Direction.DOWN) {
						p.entity.addCommand(Command.MOVE_DOWN);
					} else {
						p.entity.addCommand(Command.MOVE_RIGHT);
					}
					break;
				case southWest:
					if (p.entity.direction == Direction.DOWN) {
						p.entity.addCommand(Command.MOVE_DOWN);
					} else {
						p.entity.addCommand(Command.MOVE_LEFT);
					}
					break;
				case west:
					p.entity.addCommand(Command.MOVE_LEFT);
					break;
				default:
					break;
				}
			}
		}
		
		// keyboard controls first player
		Entity p1 = GameSession.players[0].entity;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			p1.addCommand(Command.MOVE_LEFT);
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			p1.addCommand(Command.MOVE_RIGHT);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			p1.addCommand(Command.MOVE_DOWN);
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			p1.addCommand(Command.MOVE_UP);
		}
		//  attack / pick up
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			p1.attack();
		} else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			p1.tryPickUp();
		}
		
		
		// zoom based on distance between players
		if (maxPlayerDistance > (World.TILE_WIDTH * World.TILE_WIDTH * (World.TILE_WIDTH/4))) {
			renderer.getWorldCamera().zoom = Interpolation.linear.apply(renderer.getWorldCamera().zoom,
					MathUtils.clamp(maxPlayerDistance / 100000,	1, 2.5f), 0.3f);
		} else {
			renderer.getWorldCamera().zoom = Interpolation.linear.apply(renderer.getWorldCamera().zoom,
					1, 0.3f);
		}

	}
	
	public void checkForSpawn() {
		if (world.time - lastSpawnTime > 10 && MathUtils.randomBoolean(0.1f)) {
			
			lastSpawnTime = world.time;
			
			int tries = 0;
			
			int x = 0;
			int y = 0;
			boolean tileOK = false;
			while(!tileOK) {
				x = MathUtils.random(1,world.tilesWide-2);
				y = MathUtils.random(1,world.tilesTall-2);
				tileOK = !world.getTileAtTilePosition(x, y).obstacle;
				
				tries++;
				
				// give up for now
				if (tries > 100) {
					return;
				}
			}
			
			
			Entity enemy = new Entity();
			enemyEntities.add(enemy);
			enemy.init(world, new Coordinate(x * World.TILE_WIDTH, y * World.TILE_WIDTH),
					World.TILE_WIDTH - 2, World.TILE_WIDTH - 2, 
					CreatureType.GUARD);
			// set up ai!
			AIBrain ai = new AIBrain();
			ai.init(world, enemy);
			aiBrains.add(ai);
		}
	}

}
