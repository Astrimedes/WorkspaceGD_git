package hdcheese.world;

import hdcheese.GameSession;
import hdcheese.audio.SoundBank.SoundName;
import hdcheese.utils.Coordinate;
import hdcheese.utils.RectangleInt;

public class Grave {
	
	public AttackEffect lastHitBy;
	public float lastHitTime = 0;
	
	private World world;
	
	public Coordinate tileCoordinate = new Coordinate();
	
	public RectangleInt boundaries = new RectangleInt();
	
	public float maxDug = 10;
	public float currDug = maxDug;
	
	public Grave(World world, int tileCoordinateX, int tileCoordinateY) {
		this.world = world;
		
		this.tileCoordinate.x = tileCoordinateX;
		this.tileCoordinate.y = tileCoordinateY;
		
		boundaries.x = tileCoordinate.x * World.TILE_WIDTH;
		boundaries.y = tileCoordinate.y * World.TILE_WIDTH;
		boundaries.width = World.TILE_WIDTH;
		boundaries.height = World.TILE_WIDTH;
	}
	
	public void tryDig(AttackEffect attack) {
		if (lastHitBy != attack && (world.time - lastHitTime) > attack.lifeTime) {
			lastHitBy = attack;
			lastHitTime = world.time;
			currDug--;
			
			GameSession.getSound().playSound(SoundName.PLAYER_DIG, false);
			
			if (currDug <= 0) {
				GameSession.getSound().playSound(SoundName.PLOP, false);
				// spawn parts!
				for (int i = 0; i < 10; ++i) {
					BodyPart p = new BodyPart(boundaries.x + (boundaries.width/2),
							boundaries.y + (boundaries.width/2));
					world.bodyParts.add(p);
				}
				// switch tile
				world.tiles[tileCoordinate.x][tileCoordinate.y]= TileType.GRAVE_OPEN;
			}
		}
	}
	
}
