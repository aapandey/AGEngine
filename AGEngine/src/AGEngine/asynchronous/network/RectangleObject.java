package AGEngine.asynchronous.network;

import java.io.Serializable;

/** Class to be passed over the network */
public class RectangleObject implements Serializable{
	
	private static final long serialVersionUID = 12345678L;
	
	private float x;
	private float y;
	private float height;
	private float width;
	private int id;
	private float r, g, b;
	private boolean isAlive;
	
	public synchronized float getR() {
		return r;
	}

	public synchronized float getG() {
		return g;
	}

	public synchronized float getB() {
		return b;
	}
	
	public synchronized boolean isAlive() {
		return isAlive;
	}

	public synchronized void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public RectangleObject(int id, float x, float y, float height, float width, boolean alive) {
		super();
		setX(x);
		setY(y);
		setHeight(height);
		setWidth(width);
		setId(id);
		setAlive(alive);
	}
	
	public RectangleObject(RectangleObject other) {
		super();
		setX(other.getX());
		setY(other.getY());
		setHeight(other.getHeight());
		setWidth(other.getWidth());
		setId(other.getId());
		setAlive(other.isAlive());
	}
	
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
		this.id = id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}
