package hdcheese.world;

public enum TileType {
	GRASS(false),
	GRAVE(true),
	GRAVE_OPEN(true),
	FENCE_HORIZONTAL(true),
	FENCE_VERTICAL(true),
	FENCE_LL(true),
	FENCE_LR(true),
	FENCE_UL(true),
	FENCE_UR(true);
	
	private TileType(boolean obstacle) {
		this.obstacle = obstacle;
	}
	
	public boolean obstacle;
	
}
