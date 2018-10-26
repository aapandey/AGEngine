package AGEngine.game.assignment2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import AGEngine.core.Engine;
import AGEngine.core.Manager;
import AGEngine.network.AsyncClient;
import AGEngine.network.AsyncServer;
import AGEngine.objects.GameObject;
import AGEngine.objects.GameObject.Tag;
import processing.core.PVector;

public class SceneManager extends Manager{
	
	protected GameObject player;
	protected GameObject spawnPoint;
	protected GameObject staticPlatform;
	protected GameObject movingPlatform1, movingPlatform2;
	protected GameObject deathObject;
	protected List<GameObject> gameObjectsInScene;
	
	protected boolean isServer;
	protected static boolean isObjectTransfer;
	public AsyncClient client;
	public AsyncServer server;
	protected int playerObjectID;
	
	
	public SceneManager(boolean b) {
		// TODO Auto-generated constructor stub
		this.isServer = b;
		gameObjectsInScene = new CopyOnWriteArrayList<GameObject>();
		player = deathObject = null;
		spawnPoint = null;
		staticPlatform = movingPlatform1 = movingPlatform2 = null;
		client = null;
		server = null;
	}

	public boolean isObjectTransfer() {
		return isObjectTransfer;
	}

	public int getPlayerObjectID() {
		return playerObjectID;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SceneManager scene = null;
		if (args[0].toLowerCase().equals("server")) {
			System.out.println("Initializing server");
			if(args[1] != null && args[1].toLowerCase().equals("string")) {
				isObjectTransfer = false;
			}
			else {
				isObjectTransfer = true;
			}
			scene = new SceneManager(true);
		}
		else if(args[0].toLowerCase().equals("client")){
			System.out.println("Initializing client.");
			scene = new SceneManager(false);
			if(args[1] != null) {
				scene.playerObjectID = Integer.parseInt(args[1]);
			}
			else {
				scene.playerObjectID = (int)(Math.random() * 100);
			}
		}
		if(scene != null)
			Engine.setupEngine(scene, scene.isServer, new PVector(800, 800), new PVector(255, 255, 255));
			scene.initialize();
	}

	public void initialize() {
		// TODO Auto-generated method stub
		// Add a spawn point
		try {
			spawnPoint = new SpawnPoint(new PVector(300, 300));
			spawnPoint.setVisible(false);
			spawnPoint.setObjectTag(Tag.OTHER);
			spawnPoint.setObjectID(500);
			addObjectInList(spawnPoint);
			
			if(isServer) {
				setUpGame();
				engine.setupServer(5500);
				server = engine.get_server();
			}
			else {
				engine.setupClient("127.0.0.1", 5500);
				client = engine.get_client();
				player = new Player(new PVector((float)Math.random()*255, (float)Math.random()*255, (float)Math.random()*255),
						new PVector(300, 300), new PVector(50, 50), new PVector(5, 5), new PVector(0, 2), engine, (SpawnPoint) spawnPoint);
				player.setVisible(true);
				player.setObjectID(playerObjectID);
				player.setObjectTag(Tag.PLAYER);
				engine.addPlayer(player);
				addObjectInList(player);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setUpGame() {
		// TODO Auto-generated method stub
		// setup the platform
		staticPlatform = new StaticPlatform(new PVector(0, 0, 0), new PVector(200, 350), new PVector(400, 50), engine);
		staticPlatform.setVisible(true);
		staticPlatform.setObjectTag(Tag.STATIC_PLATFORM);
		staticPlatform.setObjectID(100);
		addObjectInList(staticPlatform);
		
		// setup the moving platforms
		movingPlatform1 = new MovingPlatform(new PVector(255, 0, 255), new PVector(50, 600), new PVector(75, 75), new PVector(0, 3), engine);
		movingPlatform1.setVisible(true);
		movingPlatform1.setObjectTag(Tag.DYNAMIC_PLATFORM);
		movingPlatform1.setObjectID(200);
		addObjectInList(movingPlatform1);
		
		movingPlatform2 = new MovingPlatform(new PVector(0, 255, 0), new PVector(500, 200), new PVector(100, 25), new PVector(-1, 0), engine);
		movingPlatform2.setVisible(true);
		movingPlatform2.setObjectTag(Tag.DYNAMIC_PLATFORM);
		movingPlatform2.setObjectID(201);
		addObjectInList(movingPlatform2);
		
		// setup death object
		deathObject = new StaticPlatform(new PVector(255, 0, 0), new PVector(400, 300), new PVector(50, 50), engine);
		deathObject.setVisible(true);
		deathObject.setObjectTag(Tag.DEATH_ZONE);
		deathObject.setObjectID(300);
		addObjectInList(deathObject);
	}
	
	public void addObjectInList(GameObject object) {
		synchronized (gameObjectsInScene) {
			for(GameObject gameObject : gameObjectsInScene) {
				if(object.getObjectID() == gameObject.getObjectID())
					return;
			}
			gameObjectsInScene.add(object);
			engine.addGameObject(object);
		}
	}
	
	public int getObjectPositionInList(GameObject object) {
		synchronized (gameObjectsInScene) {
			for(int i = 0; i < gameObjectsInScene.size(); i++) {
				if(gameObjectsInScene.get(i).getObjectID() == object.getObjectID())
					return i;
			}
			return -1;
		}
	}

	@Override
	public List<GameObject> sendDataToClient(int connection_id) {
		// TODO Auto-generated method stub
		List<GameObject> list = new CopyOnWriteArrayList<>();
		synchronized (gameObjectsInScene) {
			for(GameObject gameObject : gameObjectsInScene) {
				list.add(gameObject);
			}
			return list;
		}
	}

	@Override
	public GameObject sendDataToServer() {
		// TODO Auto-generated method stub
		return (GameObject)player;
	}

	@Override
	public void getDataFromServer(List<GameObject> dataFromServer, int connection_id) {
		// TODO Auto-generated method stub
		for(GameObject gameObject: dataFromServer) {
			int position = getObjectPositionInList(gameObject);
			if(position != -1) {
				if(gameObject.getObjectTag() == Tag.OTHER_PLAYER) {
					((Player)gameObjectsInScene.get(position)).copy((Player)gameObject);
				}
			}
			else {
				addObjectInList(gameObject);
			}
		}
	}

	@Override
	public void getDataFromClient(GameObject dataFromClient, int connection_id) {
		// TODO Auto-generated method stub
		if(dataFromClient != null) {
			int position = getObjectPositionInList(dataFromClient);
			if(position != -1) {
				// copy values from client update
				synchronized (gameObjectsInScene) {
					((Player)gameObjectsInScene.get(position)).copy((Player)dataFromClient);
				}
			}
			else {
				// Create a new player and add it to scene
				Player newPlayer = new Player(dataFromClient.getColor(), dataFromClient.getPosition(), dataFromClient.getSize(),
						((Player)dataFromClient).getVelocity(), new PVector(0, 2), engine, (SpawnPoint) spawnPoint);
				newPlayer.setVisible(true);
				newPlayer.setObjectID(dataFromClient.getObjectID());
				newPlayer.setObjectTag(Tag.OTHER_PLAYER);
				addObjectInList(newPlayer);
			}
		}
	}

}
