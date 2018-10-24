package AGEngine.core;

import java.awt.Rectangle;
import AGEngine.objects.*;
import java.awt.Shape;

import AGEngine.objects.IRenderable;
import processing.core.PApplet;

/* Inspiration and explicit code from following sources:
 * https://processing.org/tutorials/eclipse/
 * https://www.oodesign.com/factory-pattern.html
 * http://learningprocessing.com/examples/chp23/example-23-03-Rectangle
 *  */
public class GameObject implements IRenderable{
	
	/** Parent PApplet reference holder */
	PApplet parent;
	
	/** Reference holder for type of shape */
	Shape gameObjectShape;
	
	
	public Shape getGameObjectShape() {
		return gameObjectShape;
	}

	//protected float x, y;
	protected float width, height;
	protected float r, g, b, alpha;
	protected String shapeName;
	
	public GameObject(PApplet p) {
		parent = p;
	}
	
	/** Method to create various different shapes
	 * shapeName can be Rectangle
	 * x = x position
	 * y = y position
	 * width = width of shape
	 * height = height of shape */
	public void createShape(String shapeName, float x, float y, float width, float height) {
		this.shapeName = shapeName;
		if(shapeName == "Rectangle")
		{
			this.width = width;
			this.height = height;
			gameObjectShape = new Rectangle((int)x, (int)y, (int)width, (int)height);
		}
	}
	
	/** Method to set colors */
	public void setColor(float r, float g, float b, float alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}
	
	/** Method to call on every draw to display GameObject shape */
	public void display() {
		parent.fill(r, g, b);
		if(shapeName == "Rectangle") {
			parent.rect(((Rectangle)gameObjectShape).x, ((Rectangle)gameObjectShape).y, 
					((Rectangle)gameObjectShape).width, ((Rectangle)gameObjectShape).height);
		}
	}
	
}
