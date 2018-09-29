package AGEngine.core;

interface IMovable {
	abstract void updatePosition();
}

interface IRenderable {
	void display();
}

interface ICollidable {
	public boolean isColliding();
}
