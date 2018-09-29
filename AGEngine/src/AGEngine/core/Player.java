package AGEngine.core;

import java.awt.geom.Area;
import java.util.List;

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
public class Player extends GameObject implements IMovable, ICollidable{
	
	/** Vector that describes the velocity of the player object */
	private PVector _velocity;
	
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
	
	private float _jumpSpeed;

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
		_ground = parent.height - this.height/2;
		_gravity = 0.5f;
	}
	
	@Override
	public void display() {
		parent.rectMode(PConstants.CENTER);
		x = PApplet.constrain(x, width/2, parent.width - width/2);
		y = PApplet.constrain(y, height/2, parent.height - height/2);
		super.display();
	}
	
	@Override
	public void createShape(String shapeName, float x, float y, float width, float height) {
		super.createShape(shapeName, x, y, width, height);
	}
	
	/**This method calls various methods every frame like an update */
	public void run() {
		gravity();
		updatePosition();
		display();
	}

	/**This method makes the gameObject be under a constant influence of gravity */
	private void gravity() {
		// Only apply gravity if above ground (since y positive is down we use < ground)
		if(y < _ground - height/2) {
			y += _gravity;
		}
		else {
			y = _ground - height/2;
		}
		// If on the ground and "jump" key is pressed upward velocity to the jump speed!
		if (y >= _ground && _up)
		{
			y -= get_jumpSpeed();
		}
	}

	@Override
	public void updatePosition() {
		if(!isColliding()) {
			if(_up){  
				y -= _velocity.y;  
			}  
			if(_down){  
				y += _velocity.y;  
			}  
			if(_left){  
				x -= _velocity.x;
			}  
			if(_right){  
				x += _velocity.x;  
			}
		}
		
	}

	/**Check if there has been any collision */
	@Override
	public boolean isColliding() {
		for(GameObject gObject : _gameObjects) {
			/*Area areaA = new Area(gameObjectShape);
			areaA.intersect(new Area(gObject.getGameObjectShape()));
			if(!areaA.isEmpty()) {
				return true;
			}*/
			if(this.gameObjectShape.getBounds2D().intersects(gObject.getGameObjectShape().getBounds2D())) {
				System.out.println("Colliding with " + gObject.toString());
				return true;
			}
		}
		return false;
	}
}
