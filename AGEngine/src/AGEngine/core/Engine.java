package AGEngine.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import AGEngine.network.AsyncClient;
import AGEngine.network.AsyncServer;
import AGEngine.objects.*;
import AGEngine.objects.GameObject.Tag;
import processing.core.PApplet;
import processing.core.PVector;

/* Inspiration and explicit code from following sources:
 * https://processing.org/tutorials/eclipse/
 * http://learningprocessing.com/examples/chp23/example-23-03-Rectangle
 * https://processing.org/reference/keyCode.html
 * https://www.journaldev.com/1289/copyonwritearraylist-java
 *  */

public class Engine extends PApplet {
	// List of gameObjects currently in the scene to be handled by the engine
	private List<GameObject> _gameObjects;
	// Manager as reference
	private static Manager _manager;
	private AsyncClient _client;
	private AsyncServer _server;
	private static boolean _isServer;
	private AGEngine.objects.GameObject _player;
	
	private static PVector _resolution;
	
	public AsyncClient get_client() {
		return _client;
	}

	public AsyncServer get_server() {
		return _server;
	}

	private static PVector _background;
	
	public static boolean is_isServer() {
		return _isServer;
	}
	
	public PVector get_resolution() {
		return _resolution;
	}
	
	public void set_isServer(boolean _isServer) {
		this._isServer = _isServer;
	}
	
	public Engine() {
		_gameObjects = new CopyOnWriteArrayList<GameObject>();
		_manager.setEngine(this);
	}
	
	public static void setupEngine(Manager manager, boolean isServer, PVector resolution, PVector background) {
		if(_manager != null)
			return;
		else {
			_manager = manager;
			_isServer = isServer;
			_resolution = resolution;
			_background = background;/*
			if(is_isServer()) {
				setupClient(address, port);
			}
			else {
				setupServer(port);
			}*/
			PApplet.main("AGEngine.core.Engine");
		}
	}
	
	public void addPlayer(GameObject player) {
		_player = player;
	}
	
	
	public void startServer() {
		if(is_isServer()) {
			(new Thread(_server)).start();
		}
		else {
			(new Thread(_client)).start();
		}
	}
	
	public void setupClient(String address, int port) {
		_client = new AsyncClient(address, port, _manager);
	}
	
	public void setupServer(int port) {
		_server = new AsyncServer(port, _manager);
	}
	
	public void setup() {
		startServer();
	}
	
	public void settings() {
		size((int)_resolution.x, (int)_resolution.y);
	}
	
	public void draw() {
		background(_background.x, _background.y, _background.z);
		updateObjectPositions();
		if(!is_isServer()) {
			handleCollisions();
		}
		drawObjects();
	}
	
	public void handleCollisions() {
		// TODO Auto-generated method stub
		synchronized (_gameObjects) {
			((AGEngine.game.assignment2.Player)_player).handleAllPlayerCollisions(_gameObjects);
		}
	}
	
	public void drawObjects() {
		// TODO Auto-generated method stub
		synchronized (_gameObjects) {
			for(GameObject gameObject : _gameObjects) {
				if(gameObject instanceof IRenderable) {
					((IRenderable)gameObject).display();
				}
			}
		}
	}
	
	public void updateObjectPositions() {
		// TODO Auto-generated method stub
		synchronized (_gameObjects) {
			for(GameObject gameObject : _gameObjects) {
				if(gameObject instanceof IMovable) {
					((IMovable)gameObject).updatePosition();
				}
			}	
		}
	}
	
	public void keyPressed() {
		if(!is_isServer()) {
			if(key == 'A' || key == 'a' || key == LEFT)
				((AGEngine.game.assignment2.Player)_player).handelKeyboardInput(AGEngine.game.assignment2.Player.Direction.LEFT, true);
			if(key == 'D' || key == 'd' || key == RIGHT)
				((AGEngine.game.assignment2.Player)_player).handelKeyboardInput(AGEngine.game.assignment2.Player.Direction.RIGHT, true);
			if(key == ' ' || key == UP)
				((AGEngine.game.assignment2.Player)_player).handelKeyboardInput(AGEngine.game.assignment2.Player.Direction.UP, true);
		}
	}
	
	public void keyReleased() {
		if(!is_isServer()) {
			if(key == 'A' || key == 'a' || key == LEFT)
				((AGEngine.game.assignment2.Player)_player).handelKeyboardInput(AGEngine.game.assignment2.Player.Direction.LEFT, false);
			if(key == 'D' || key == 'd' || key == RIGHT)
				((AGEngine.game.assignment2.Player)_player).handelKeyboardInput(AGEngine.game.assignment2.Player.Direction.RIGHT, false);
			if(key == ' ' || key == UP)
				((AGEngine.game.assignment2.Player)_player).handelKeyboardInput(AGEngine.game.assignment2.Player.Direction.UP, false);
		}
	}
	
	public void addGameObject(GameObject object) {
		synchronized (_gameObjects) {
			_gameObjects.add(object);
		}
	}

}
