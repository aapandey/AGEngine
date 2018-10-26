package AGEngine.game.assignment2;

import java.awt.Rectangle;
import java.awt.Shape;

import AGEngine.core.Engine;
import AGEngine.objects.GameObject;
import AGEngine.objects.ICollidable;
import AGEngine.objects.IMovable;
import AGEngine.objects.IRenderable;
import processing.core.PVector;

public class MovingPlatform extends GameObject implements ICollidable, IRenderable, IMovable{
	
	private static final long serialVersionUID = 12345678L;
	transient protected Engine engine;
	Shape gameObjectShape;
	protected PVector velocity;
	int iter, limit;

	public PVector getVelocity() {
		return velocity;
	}

	public Shape getGameObjectShape() {
		return gameObjectShape;
	}

	public void setVelocity(PVector velocity) {
		this.velocity = velocity;
	}
	
	public void setGameObjectShape(Shape gameObjectShape) {
		this.gameObjectShape = gameObjectShape;
	}

	public MovingPlatform(PVector color, PVector position, PVector size, PVector velocity, Engine engine) 
	{
		this.color = new PVector(color.x, color.y, color.z);
		this.position = new PVector(position.x, position.y);
		this.size = new PVector(size.x, size.y);
		if(velocity != null)
			this.velocity = new PVector(velocity.x, velocity.y);
		else
			this.velocity = null;
		this.engine = engine;
		
		gameObjectShape = new Rectangle((int)position.x, (int)position.y, (int)size.x, (int)size.y);
		iter = 1;
		limit = 100;
		visible = true;
	}
	
	public MovingPlatform(MovingPlatform other, Engine engine) {
		this.color = other.color;
		this.position = other.position;
		this.size = other.size;
		this.velocity = other.velocity;
		this.gameObjectShape = other.gameObjectShape;
		this.visible = other.visible;
		this.objectTag = other.objectTag;
		this.objectID = other.objectID;
		this.engine = engine;
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		if(isVisible()) {
			engine.pushMatrix();
			engine.noStroke();
		    engine.fill(color.x, color.y, color.z);
		    engine.rect(position.x, position.y, size.x, size.y);
		    engine.popMatrix();
		}
	}


	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		position.add(velocity);
		// update shape manually
		((Rectangle)gameObjectShape).x = (int)position.x;
		((Rectangle)gameObjectShape).y = (int)position.y;
		step();
	}
	
	public void step() {
		iter = (iter + 1) % limit;
		if (iter == 0) {
			velocity.x *= -1;
			velocity.y *= -1;
		}
	}

	@Override
	public boolean isColliding(ICollidable gObject) {
		// TODO Auto-generated method stub
		if(this.gameObjectShape.getBounds2D().intersects(gObject.getGameObjectShape().getBounds2D()))
			return true;
		else 
			return false;
	}
}