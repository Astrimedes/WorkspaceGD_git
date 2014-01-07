package hdcheese.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class RectangleInt implements Poolable{
	public float x, y;
	public float width, height;

	/** Constructs a new rectangle with all values set to zero */
	public RectangleInt () {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}

	/** Constructs a new rectangle with the given corner pofloat in the bottom left and dimensions.
	 * @param x The corner pofloat x-coordinate
	 * @param y The corner pofloat y-coordinate
	 * @param width The width
	 * @param height The height */
	public RectangleInt (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** Constructs a rectangle based on the given rectangle
	 * @param rect The rectangle */
	public RectangleInt (RectangleInt rect) {
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}

	/** @param x bottom-left x coordinate
	 * @param y bottom-left y coordinate
	 * @param width width
	 * @param height height
	 * @return this rectangle for chaining */
	public void set (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** @return the x-coordinate of the bottom left corner */
	public float getX () {
		return x;
	}

	/** Sets the x-coordinate of the bottom left corner
	 * @param x The x-coordinate
	 * @return this rectangle for chaining */
	public RectangleInt setX (float x) {
		this.x = x;
		
		return this;
	}

	/** @return the y-coordinate of the bottom left corner */
	public float getY () {
		return y;
	}

	/** Sets the y-coordinate of the bottom left corner
	 * @param y The y-coordinate 
	 * @return this rectangle for chaining */
	public RectangleInt setY (float y) {
		this.y = y;
		
		return this;
	}

	/** @return the width */
	public float getWidth () {
		return width;
	}

	/** Sets the width of this rectangle
	 * @param width The width 
	 * @return this rectangle for chaining */
	public RectangleInt setWidth (float width) {
		this.width = width;
		
		return this;
	}

	/** @return the height */
	public float getHeight () {
		return height;
	}

	/** Sets the height of this rectangle
	 * @param height The height 
	 * @return this rectangle for chaining */
	public RectangleInt setHeight (float height) {
		this.height = height;
		
		return this;
	}

//	/** return the Coordinate with coordinates of this rectangle
//	 * @param position The Coordinate */
//	public Coordinate getPosition (Coordinate position) {
//		return position.set(x, y);
//	}

	/** Sets the x and y-coordinates of the bottom left corner
	 * @param x The x-coordinate
	 * @param y The y-coordinate 
	 * @return this rectangle for chaining */
	public RectangleInt setPosition (float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}

	/** Sets the width and height of this rectangle
	 * @param width The width
	 * @param height The height 
	 * @return this rectangle for chaining */
	public RectangleInt setSize (float width, float height) {
		this.width = width;
		this.height = height;
		
		return this;
	}

	/** Sets the squared size of this rectangle
	 * @param sizeXY The size 
	 * @return this rectangle for chaining */
	public RectangleInt setSize (float sizeXY) {
		this.width = sizeXY;
		this.height = sizeXY;
		
		return this;
	}

	/** @return the Coordinate with size of this rectangle
	 * @param size The Coordinate */
//	public Coordinate getSize (Coordinate size) {
//		return size.set(width, height);
//	}

	/** @param x pofloat x coordinate
	 * @param y pofloat y coordinate
	 * @return whether the pofloat is contained in the rectangle */
	public boolean contains (float x, float y) {
		return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
	}

	/** @param rectangle the other {@link RectangleInt}.
	 * @return whether the other rectangle is contained in this rectangle. */
	public boolean contains (RectangleInt rectangle) {
		float xmin = rectangle.x;
		float xmax = xmin + rectangle.width;

		float ymin = rectangle.y;
		float ymax = ymin + rectangle.height;

		return ((xmin > x && xmin < x + width) && (xmax > x && xmax < x + width))
			&& ((ymin > y && ymin < y + height) && (ymax > y && ymax < y + height));
	}

	/** @param r the other {@link RectangleInt}
	 * @return whether this rectangle overlaps the other rectangle. */
	public boolean overlaps (RectangleInt r) {
		return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
	}

	/** Sets the values of the given rectangle to this rectangle.
	 * @param rect the other rectangle
	 * @return this rectangle for chaining */
	public RectangleInt set (RectangleInt rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
		
		return this;
	}

	/** Merges this rectangle with the other rectangle.
	 * @param rect the other rectangle
	 * @return this rectangle for chaining */
	public RectangleInt merge (RectangleInt rect) {
		float minX = Math.min(x, rect.x);
		float maxX = Math.max(x + width, rect.x + rect.width);
		x = minX;
		width = maxX - minX;

		float minY = Math.min(y, rect.y);
		float maxY = Math.max(y + height, rect.y + rect.height);
		y = minY;
		height = maxY - minY;
		
		return this;
	}
	
	/** Calculates the center of the rectangle. Results are located in the given Coordinate
	 * @param coord the Coordinate to use 
	 * @return the given coord with results stored inside */
	public Vector2 getCenter (Vector2 coord) {
		coord.x = x + width / 2;
		coord.y = y + height / 2;
		return coord;
	}

	/** Moves this rectangle so that its center pofloat is located at a given position
	 * @param x the position's x
	 * @param y the position's y
	 * @return this for chaining */
	public RectangleInt setCenter (float x, float y) {
		setPosition(x - width / 2, y - height / 2);
		return this;
	}
	
	/**
	 * Return right side X position
	 * @return
	 */
	public float right() {
		return x + width;
	}
	
	/**
	 * Return left side X position
	 * @return
	 */
	public float left() {
		return x;
	}
	
	/**
	 * Return top side Y position
	 * @return
	 */
	public float top() {
		return y + height;
	}
	
	/**
	 * Return bottom side Y position
	 * @return
	 */
	public float bottom() {
		return y;
	}

	public String toString () {
		return x + "," + y + "," + width + "," + height;
	}

	@Override
	public void reset() {
		x=0;
		y=0;
		width=0;
		height=0;
	}
}
