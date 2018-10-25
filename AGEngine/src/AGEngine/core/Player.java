package AGEngine.core;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.List;

import AGEngine.objects.ICollidable;
import AGEngine.objects.IMovable;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/* Inspiration and explicit code from following sources:
 * https://forum.processing.org/two/discussion/22935/solved-keyboard-controlled-object-move-and-rotate
 * https://stackoverflow.com/questions/6066860/how-to-call-base-class-method-with-derived-class-object-when-that-method-overrid
 * https://happycoding.io/tutorials/processing/collision-detection
 * https://forum.processing.org/two/discussion/23004/keyboard-input-for-movement-control-gets-stuck
 * https://www.openprocessing.org/sketch/92234
 *  */
public class Player extends GameObject implements IMovable{
	
	/** Vector that describes the velocity of the player object */
	private PVector _velocity;
	
	// Reference list for all GameObjects in the scene for collision detection
	private List<GameObject> _gameObjects;
	public void set_gameObjects(List<GameObject> gameObjects) {
		this._gameObjects = gameObjects;
	}

	// Booleans to control movement
	private boolean _up;  
	private boolean _down;  
	private boolean _left;  
	private boolean _right;
	private float _ground;
	
	// Controls jump speed
	private float _jumpSpeed;
	
	// Controls gravity
	private float _gravity;
	
	public float get_jumpSpeed() {
		return _jumpSpeed;
	}

	public void set_jumpSpeed(float _jumpSpeed) {
		this._jumpSpeed = _jumpSpeed;
	}

	public boolean is_up() {
		return _up;
	}

	public void set_up(boolean _up) {
		this._up = _up;
	}

	public boolean is_down() {
		return _down;
	}

	public void set_down(boolean _down) {
		this._down = _down;
	}

	public boolean is_left() {
		return _left;
	}

	public void set_left(boolean _left) {
		this._left = _left;
	}

	public boolean is_right() {
		return _right;
	}

	public void set_right(boolean _right) {
		this._right = _right;
	} 
	
	/** Constructor for Player class, accepts the PApplet, velocity vector and float speed */
	public Player(PApplet p, PVector vel, float speed) {
		super(p);
		
		_velocity = vel.copy();
		_jumpSpeed = speed;
		_up = false;
		_down = false;
		_left = false;
		_right = false;
		
		// Set ground to align with center of rectangle
		_ground = parent.height - this.height;
		_gravity = 1;
	}
	
	@Override
	public void display() {
		//parent.rectMode(PConstants.CENTER);
		if(shapeName == "Rectangle") {
			((Rectangle)gameObjectShape).x = (int)PApplet.constrain(((Rectangle)gameObjectShape).x,
					0, parent.width - width);
			((Rectangle)gameObjectShape).y = (int)PApplet.constrain(((Rectangle)gameObjectShape).y,
					0, parent.height - height);
		}
		super.display();
	}
	
	/**This method calls various methods every frame like an update */
	public void run() {
		gravity();
		updatePosition();
		display();
	}

	/**This method makes the gameObject be under a constant influence of gravity */
	private void gravity() {
		if(shapeName == "Rectangle") {
			// Only apply gravity if above ground (since y positive is down we use < ground)
			if(((Rectangle)gameObjectShape).y < _ground - height) {
				((Rectangle)gameObjectShape).y += _gravity;
			}
			// If on the ground and "jump" key is pressed upward velocity to the jump speed!
			if (((Rectangle)gameObjectShape).y >= _ground && _up)
			{
				((Rectangle)gameObjectShape).y -= get_jumpSpeed();
			}
		}
	}

	@Override
	public void updatePosition() {
		// Checks if there hasn't been a collision then accept user input
		if(!isColliding()) {
			if(shapeName == "Rectangle") {
				if(_up){  
					((Rectangle)gameObjectShape).y -= _velocity.y;  
				}  
				if(_down){  
					((Rectangle)gameObjectShape).y += _velocity.y;  
				}  
				if(_left){  
					((Rectangle)gameObjectShape).x -= _velocity.x;
				}  
				if(_right){  
					((Rectangle)gameObjectShape).x += _velocity.x;  
				}
			}
		}
		
	}

	/**Check if there has been any collision */
	public boolean isColliding() {
		for(GameObject gObject : _gameObjects) {
			if(this.gameObjectShape.getBounds2D().intersects(gObject.getGameObjectShape().getBounds2D())) {
				return true;
			}
		}
		return false;
	}
}
