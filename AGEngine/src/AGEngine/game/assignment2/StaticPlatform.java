package AGEngine.game.assignment2;

import java.awt.Rectangle;
import java.awt.Shape;

import AGEngine.core.Engine;
import AGEngine.objects.GameObject;
import AGEngine.objects.ICollidable;
import AGEngine.objects.IRenderable;
import AGEngine.objects.GameObject.Tag;
import processing.core.PVector;

/* Inspiration and explicit code from following sources:
 * https://processing.org/tutorials/eclipse/
 * https://www.oodesign.com/factory-pattern.html
 * http://learningprocessing.com/examples/chp23/example-23-03-Rectangle
 *  */

public class StaticPlatform extends GameObject implements ICollidable, IRenderable{
	private static final long serialVersionUID = 123456789123456L;
	
	transient protected Engine engine;
	Shape gameObjectShape;

	public StaticPlatform(PVector color, PVector position, PVector size, Engine engine) 
	{
		this.color = new PVector(color.x, color.y, color.z);
		this.position = new PVector(position.x, position.y);
		this.size = new PVector(size.x, size.y);
		this.engine = engine;
		
		gameObjectShape = new Rectangle((int)position.x, (int)position.y, (int)size.x, (int)size.y);
		visible = true;
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		if(isVisible()) {
			engine.pushMatrix();
		    engine.fill(color.x, color.y, color.z);
		    engine.rect(position.x, position.y, size.x, size.y);
		    engine.popMatrix();
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

	@Override
	public Shape getGameObjectShape() {
		// TODO Auto-generated method stub
		return gameObjectShape;
	}
}
