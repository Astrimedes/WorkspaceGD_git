package hdcheese.utils;

import com.badlogic.gdx.utils.Pool.Poolable;

public class Coordinate implements Poolable {
	public int x;
	public int y;
	
	public Coordinate() {
		x = 0;
		y = 0;
	}
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public void reset() {
		x=0;
		y=0;
	}
}
