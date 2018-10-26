package AGEngine.game.assignment2;

import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import AGEngine.core.Engine;
import AGEngine.objects.GameObject;
import AGEngine.objects.ICollidable;
import AGEngine.objects.IMovable;
import AGEngine.objects.IRenderable;
import AGEngine.objects.GameObject.Tag;
import processing.core.PVector;

/* Inspiration and explicit code from following sources:
 * https://forum.processing.org/two/discussion/22935/solved-keyboard-controlled-object-move-and-rotate
 * https://stackoverflow.com/questions/6066860/how-to-call-base-class-method-with-derived-class-object-when-that-method-overrid
 * https://happycoding.io/tutorials/processing/collision-detection
 * https://forum.processing.org/two/discussion/23004/keyboard-input-for-movement-control-gets-stuck
 * https://www.openprocessing.org/sketch/92234
 *  */

public class Player extends GameObject implements ICollidable, IRenderable, IMovable{
	
	private static final long serialVersionUID = 987654321L;
	
	private transient boolean _left, _right, _jump;
	
	transient protected Engine engine;
	Shape gameObjectShape;
	protected PVector velocity;
	protected PVector gravity;
	
	private transient SpawnPoint _spawnPoint;
	protected transient GameObject connectedObject;
	protected transient PlayerStates state;
	
	public enum PlayerStates{
		ON_PLATFORM, IN_AIR
	}
	
	public enum Direction{
		LEFT, RIGHT, UP
	}

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

	public Player(PVector color, PVector position, PVector size, PVector velocity, PVector gravity, Engine engine, SpawnPoint spawnpoint) 
	{
		this.color = new PVector(color.x, color.y, color.z);
		this.position = new PVector(position.x, position.y);
		this.size = new PVector(size.x, size.y);
		if(velocity != null)
			this.velocity = new PVector(velocity.x, velocity.y);
		else
			this.velocity = null;
		if(gravity != null)
			this.gravity = new PVector(gravity.x, gravity.y);
		else
			this.gravity = null;
		this.engine = engine;
		this._spawnPoint = spawnpoint;
		
		gameObjectShape = new Rectangle((int)position.x, (int)position.y, (int)size.x, (int)size.y);
		visible = true;
		state = PlayerStates.IN_AIR;
		_left = _right = _jump = false;
	}
	
	public void copy(Player other) {
		this.color = other.color;
		this.position = other.position;
		this.size = other.size;
		this.velocity = other.velocity;
		this.gravity = other.gravity;
		this.gameObjectShape = other.gameObjectShape;
		this.visible = other.visible;
		this.objectTag = other.objectTag;
		this.objectID = other.objectID;
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
		if(getObjectTag() != Tag.OTHER_PLAYER) {
			PVector vel = new PVector(0, 0);
			if(connectedObject != null)
			{		
				if(connectedObject.getObjectTag() == Tag.DYNAMIC_PLATFORM) {
					vel.add(((MovingPlatform)connectedObject).getVelocity());
				}
			}
			
			switch (state) 
			{
			case IN_AIR:
				vel.add(gravity);
				if(_left) {
					vel.add(-velocity.x, 0);
				}
				if(_right) {
					vel.add(velocity.x, 0);
				}
				break;
			case ON_PLATFORM:
				if(_left) {
					vel.add(-velocity.x, 0);
				}
				if(_right) {
					vel.add(velocity.x, 0);
				}
				if(_jump) {
					vel.add(0, velocity.y);
					state = PlayerStates.IN_AIR;
				}
				break;
			}
			
			position.add(vel);
			position.x = Math.min(Math.max(position.x, 0), engine.get_resolution().x);
			//Check if the player has dropped to death
			if(position.y > engine.get_resolution().y)
				reSpawn();
			// update shape manually
			((Rectangle)gameObjectShape).x = (int)position.x;
			((Rectangle)gameObjectShape).y = (int)position.y;
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
	
	public void handleAllPlayerCollisions(List<GameObject> gameObjects) {
		for(GameObject gameObject : gameObjects) {
			if(gameObject.getObjectTag() == Tag.DYNAMIC_PLATFORM || gameObject.getObjectTag() == Tag.STATIC_PLATFORM) {
				if(isColliding((ICollidable)gameObject)) {
					connectedObject = gameObject;
					state = PlayerStates.ON_PLATFORM;
					return;
				}
			}
			else if(gameObject.getObjectTag() == Tag.DEATH_ZONE ) {
				reSpawn();
				return;
			}
		}
		// We can say that the player is not on any platform thus change its state to in air
		state = PlayerStates.IN_AIR;
	}
	
	public void reSpawn() 
	{
		position = _spawnPoint.getPosition().copy();
		state = PlayerStates.IN_AIR;
	}
	
	public void handelKeyboardInput(Direction d, boolean value) {
		switch(d) {
		case LEFT:
			_left = value;
			break;
		case RIGHT:
			_right = value;
			break;
		case UP:
			_jump = value;
			break;
		}
	}
}